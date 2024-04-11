package dal;

import be.Customer;
import be.Event;
import be.Ticket;
import be.TicketType;
import exceptions.ErrorCode;
import exceptions.EventException;
import exceptions.ExceptionLogger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

import java.math.BigDecimal;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;

public class TicketDAO {

    private final ConnectionManager connectionManager;
    private CustomerDAO customerDAO = new CustomerDAO();

    public TicketDAO() throws EventException {
        this.connectionManager = new ConnectionManager();
    }


    /*private ObservableMap<Integer, Ticket> retrieveTickets() throws EventException {
        ObservableMap<Integer, Ticket> tickets = FXCollections.observableHashMap();
        String sql = "SELECT * FROM Ticket";

        try (Connection conn = connectionManager.getConnection()) {
            try (PreparedStatement psmt = conn.prepareStatement(sql)) {
                ResultSet res = psmt.executeQuery();
                while (res.next()) {
                    int id = res.getInt(1);
                    String type = res.getString(2);
                    int quantity = res.getInt(3);
                    float price = res.getFloat(4);

                    Ticket ticket = new Ticket(id, type, quantity, price);
                    tickets.put(ticket.getId(), ticket);
                }
            }
        } catch (SQLException e) {
            throw new EventException(e.getMessage(), e.getCause(), ErrorCode.OPERATION_DB_FAILED);
        }
        return tickets;
    }*/
    public ObservableMap<Integer, Ticket> retrieveTicketsForEvent(int eventId) throws EventException {
        ObservableMap<Integer, Ticket> tickets = FXCollections.observableHashMap();
        String sql = "SELECT t.* FROM Ticket t JOIN EventTickets et ON t.ID = et.TicketID WHERE et.EventID = ?";

        try (Connection conn = connectionManager.getConnection();
             PreparedStatement psmt = conn.prepareStatement(sql)) {
            psmt.setInt(1, eventId);
            try (ResultSet res = psmt.executeQuery()) {
                while (res.next()) {
                    int id = res.getInt("ID");
                    String type = res.getString("Type");
                    int quantity = res.getInt("Quantity");
                    BigDecimal price = res.getBigDecimal("Price");
                    String color = res.getString("Colour");

                    Ticket ticket = new Ticket(id, type, quantity, price, color);
                    tickets.put(ticket.getId(), ticket);
                }
            }
        } catch (SQLException | EventException e) {
            throw new EventException(e.getMessage(), e.getCause(), ErrorCode.OPERATION_DB_FAILED);
        }

        return tickets;
    }

    public ObservableMap<Integer, Ticket> retrieveSpecialTicketsForEventOrNot(int eventId) throws EventException {
        ObservableMap<Integer, Ticket> tickets = FXCollections.observableHashMap();
        String sql = "SELECT t.* FROM SpecialTickets t LEFT JOIN EventSpecialTickets et ON t.ID = et.SpecialTicketID WHERE et.EventID = ? OR et.EventID IS NULL";

        try (Connection conn = connectionManager.getConnection();
             PreparedStatement psmt = conn.prepareStatement(sql)) {
            psmt.setInt(1, eventId);
            try (ResultSet res = psmt.executeQuery()) {
                while (res.next()) {
                    int id = res.getInt("ID");
                    String type = res.getString("Type");
                    int quantity = res.getInt("Quantity");
                    BigDecimal price = res.getBigDecimal("Price");
                    String color = res.getString("Colour");

                    Ticket ticket = new Ticket(id, type, quantity, price, color);
                    tickets.put(ticket.getId(), ticket);
                }
            }
        } catch (SQLException | EventException e) {
            throw new EventException(e.getMessage(), e.getCause(), ErrorCode.OPERATION_DB_FAILED);
        }

        return tickets;
    }

    public void deductQuantity(List<Ticket> eventTickets, Connection conn) throws EventException {

        try {
            String sql = "UPDATE Ticket SET Quantity = Quantity - ? WHERE ID = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            for (Ticket ticket : eventTickets) {
                statement.setInt(1, ticket.getQuantity());
                statement.setInt(2, ticket.getId());
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (SQLException e) {
            throw new EventException(e.getMessage(), e.getCause(), ErrorCode.OPERATION_DB_FAILED);
        }
    }

    public void deductSpecialQuantity(List<Ticket> specialTickets, Connection conn) throws EventException {

        try {
            String sql = "UPDATE SpecialTickets SET Quantity = Quantity - ? WHERE ID = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            for (Ticket ticket : specialTickets) {
                statement.setInt(1, ticket.getQuantity());
                statement.setInt(2, ticket.getId());
                statement.addBatch();
            }

            statement.executeBatch();
        } catch (SQLException e) {
            throw new EventException(e.getMessage(), e.getCause(), ErrorCode.OPERATION_DB_FAILED);
        }
    }

    public void insertSoldTicket(List<Ticket> eventTickets, int customerID, Connection conn) throws EventException {
        try {
            String sql = "INSERT INTO SoldTickets (TicketID, CustomerID) VALUES (?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            for (Ticket ticket : eventTickets) {
                for (int i = 0; i < ticket.getQuantity(); i++) {
                    statement.setInt(1, ticket.getId());
                    statement.setInt(2, customerID);
                    statement.addBatch();
                }
            }
            statement.executeBatch();
        } catch (SQLException e) {
            throw new EventException(e.getMessage(), e.getCause(), ErrorCode.OPERATION_DB_FAILED);
        }
    }

    public void insertSoldSpecialTicket(List<Ticket> specialTickets, int customerID, Connection conn) throws EventException { //addConn

        try {
            String sql = "INSERT INTO SoldTickets (SpecialTicketId, CustomerID) VALUES (?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            for (Ticket ticket : specialTickets) {
                for (int i = 0; i < ticket.getQuantity(); i++) {
                    statement.setInt(1, ticket.getId());
                    statement.setInt(2, customerID);
                    statement.addBatch();
                }
            }
            statement.executeBatch();
        } catch (SQLException e) {
            throw new EventException(e.getMessage(), e.getCause(), ErrorCode.OPERATION_DB_FAILED);
        }
    }

    public boolean insertSoldTickets(List<Ticket> allSelectedTickets, Customer customer) throws EventException {
        boolean sellOperationPerformed = false;
        Connection conn = null;
        try {
            Integer customerId = null;
            conn = connectionManager.getConnection();
            conn.setAutoCommit(false);
            if (customer != null) {
                customerId = customerDAO.addCustomer(customer, conn);
            }
            if (!allSelectedTickets.isEmpty()) {
                List<Ticket> specialTickets = new ArrayList<>();
                List<Ticket> eventTickets = new ArrayList<>();

                for (Ticket item : allSelectedTickets) {
                    boolean isSpecial = item.getSpecial();
                    if (isSpecial) {
                        specialTickets.add(item);
                    } else {
                        eventTickets.add(item);
                    }
                }
                insertSoldSpecialTicket(specialTickets, customerId, conn);
                deductSpecialQuantity(specialTickets, conn);

                insertSoldTicket(eventTickets, customerId, conn);
                deductQuantity(eventTickets, conn);
            }
            conn.commit();
            sellOperationPerformed = true;
        } catch (EventException | SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    throw new EventException(ex.getMessage(), ex.getCause(), ErrorCode.CONNECTION_FAILED);
                }
            }
            throw new EventException(e.getMessage(), e.getCause(), ErrorCode.CONNECTION_FAILED);
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                throw new EventException(e.getMessage(), e.getCause(), ErrorCode.CONNECTION_FAILED);
            }
        }
        return sellOperationPerformed;
    }

    /**
     * get the uuid for the sold tickets  from the database
     */
    public Map<TicketType, List<Ticket>> retrieveTicketsUUID(Map<TicketType, List<Ticket>> ticketTypeListMap) throws EventException {
        Map<TicketType, List<Ticket>> ticketTypeMap = new HashMap<>();
        String sqlNormal = "SELECT UUID FROM SoldTickets WHERE  TicketId= ?";
        String sqlSpecial = "SELECT UUID FROM SoldTickets WHERE SpecialTicketId= ?";
        try {
            Connection conn = connectionManager.getConnection();
            List<Ticket> specialTicketsUUID = getTicketsUUID(sqlSpecial, conn, ticketTypeListMap.get(TicketType.SPECIAL));
            List<Ticket> normalTicketsUUId = getTicketsUUID(sqlNormal, conn, ticketTypeListMap.get(TicketType.NORMAL));
            ticketTypeMap.put(TicketType.NORMAL, normalTicketsUUId);
            ticketTypeMap.put(TicketType.SPECIAL, specialTicketsUUID);
            System.out.println(ticketTypeMap +"ticket type");
        } catch (EventException | SQLException e) {
            throw new EventException(e.getMessage(), e, ErrorCode.OPERATION_DB_FAILED);
        }
        return ticketTypeMap;
    }


    /**
     * get the uuid for a specific list off tickets
     *
     * @param sql     the operation that needs to be performed
     * @param conn    the database connection
     * @param tickets the list off tickets that needs to have the uuid retrieved from database
     */
    private List<Ticket> getTicketsUUID(String sql, Connection conn, List<Ticket> tickets) throws SQLException {
        try (PreparedStatement psmt = conn.prepareStatement(sql)) {
            for (Ticket special : tickets) {
                psmt.setInt(1, special.getId());
                try (ResultSet rs = psmt.executeQuery()) {
                    if (rs.next()) {
                        String uuid = rs.getString("UUID");
                        special.setUUID(uuid);
                    }
                }
            }
        }
        return tickets;
    }

    public Integer addSpecialTicket(Ticket ticket, Event event) throws EventException {
        Integer ticketId = null;
        Connection conn = null;
        try {
            conn = connectionManager.getConnection();
            conn.setAutoCommit(false);
            conn.commit();
            String sql = "INSERT INTO SpecialTickets (Type, Quantity, Price, Colour) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, ticket.getTicketType());
            statement.setInt(2, ticket.getQuantity());
            statement.setBigDecimal(3, ticket.getTicketPrice());
            statement.setString(4, ticket.getColor());

            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    ticketId = generatedKeys.getInt(1);
                } else {
                    throw new EventException(ErrorCode.OPERATION_DB_FAILED);
                }
            }

            if(event != null && !Objects.equals(event.getName(), "Not Related to Events")) {
                addSpecialTicketToEvent(ticketId, event, conn);
            }

            conn.commit();

        } catch (EventException | SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    throw new EventException(ex.getMessage(), ex.getCause(), ErrorCode.CONNECTION_FAILED);
                }
            }
            throw new EventException(e.getMessage(), e.getCause(), ErrorCode.CONNECTION_FAILED);
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                throw new EventException(e.getMessage(), e.getCause(), ErrorCode.CONNECTION_FAILED);
            }
        }
        return ticketId;
    }

    public void addSpecialTicketToEvent(int ticketID, Event event, Connection conn) throws EventException, SQLException{
        String sql = "INSERT INTO EventSpecialTickets (SpecialTicketID, EventID) VALUES (?, ?)";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, ticketID);
            statement.setInt(2, event.getId());
            statement.executeUpdate();
        }
    }

    public void updateSpecialTicket(Ticket specialTicket) throws EventException {

        String updateTicket = "UPDATE SpecialTickets SET Type=?, Quantity=?, Price=?, Colour=? WHERE ID=?";
        Connection conn = null;
        try {
            conn = connectionManager.getConnection();
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
            try (PreparedStatement psmt = conn.prepareStatement(updateTicket)) {
                psmt.setString(1, specialTicket.getTicketType());
                psmt.setInt(2, specialTicket.getQuantity());
                psmt.setBigDecimal(3, specialTicket.getTicketPrice());
                psmt.setString(4, specialTicket.getColor());
                psmt.setInt(5, specialTicket.getId());
                psmt.executeUpdate();
            }

            conn.commit();
        } catch (SQLException | EventException e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                ExceptionLogger.getInstance().getLogger().log(Level.SEVERE, ex.getMessage(), ex);
                throw new EventException(ex.getMessage(), ex.getCause(), ErrorCode.OPERATION_DB_FAILED);
            }
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    ExceptionLogger.getInstance().getLogger().log(Level.SEVERE, e.getMessage(), e);
                }
            }
        }
    }

    public void deleteSpecialTicket(Ticket specialTicket) throws EventException {

        String sql = "DELETE FROM SpecialTickets WHERE ID=?";
        try (Connection conn = connectionManager.getConnection()) {
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
            try (PreparedStatement psmt = conn.prepareStatement(sql)) {
                psmt.setInt(1, specialTicket.getId());
                psmt.executeUpdate();
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw new EventException(e.getMessage(), e.getCause(), ErrorCode.OPERATION_DB_FAILED);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            ExceptionLogger.getInstance().getLogger().log(Level.SEVERE, e.getMessage(), e);
        }
    }

}
