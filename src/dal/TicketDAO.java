package dal;

import be.Ticket;
import exceptions.ErrorCode;
import exceptions.EventException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

import java.math.BigDecimal;
import java.sql.*;

public class TicketDAO {

    private final ConnectionManager connectionManager;
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

                    Ticket ticket = new Ticket(id, type, quantity, price);
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

                    Ticket ticket = new Ticket(id, type, quantity, price);
                    tickets.put(ticket.getId(), ticket);
                }
            }
        } catch (SQLException | EventException e) {
            throw new EventException(e.getMessage(), e.getCause(), ErrorCode.OPERATION_DB_FAILED);        }

        return tickets;
    }

    public void deductQuantity(int id, int quantity) throws EventException {
        Connection conn = null;
        try {
            conn = connectionManager.getConnection();
            String sql = "UPDATE Ticket SET Quantity = Quantity - ? WHERE ID = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, quantity);
            statement.setInt(2, id);
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated == 0) {
                throw new SQLException("No ticket found with ID: " + id);
            }
        } catch (SQLException | EventException e) {
            throw new EventException(e.getMessage(), e.getCause(), ErrorCode.OPERATION_DB_FAILED);        }
    }

    public void deductSpecialQuantity(int id, int quantity) throws EventException {
        Connection conn = null;
        try {
            conn = connectionManager.getConnection();
            String sql = "UPDATE SpecialTickets SET Quantity = Quantity - ? WHERE ID = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, quantity);
            statement.setInt(2, id);
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated == 0) {
                throw new SQLException("No ticket found with ID: " + id);
            }
        } catch (SQLException | EventException e) {
            throw new EventException(e.getMessage(), e.getCause(), ErrorCode.OPERATION_DB_FAILED);        }
    }

    public void insertSoldTicket(int ticketID, int customerID) throws EventException {
        Connection conn = null;
        try {
            conn = connectionManager.getConnection();
            String sql = "INSERT INTO SoldTickets (TicketID, CustomerID) VALUES (?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, ticketID);
            statement.setInt(2, customerID);
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted == 0) {
                throw new SQLException("Failed to insert ticket: " + ticketID + " for customer: " + customerID);
            }
        } catch (SQLException e) {
            throw new EventException(e.getMessage(), e.getCause(), ErrorCode.OPERATION_DB_FAILED);
        }
    }

    public void insertSoldSpecialTicket(int ticketID, int customerID) throws EventException {
        Connection conn = null;
        try {
            conn = connectionManager.getConnection();
            String sql = "INSERT INTO SoldTickets (SpecialTicketId, CustomerID) VALUES (?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, ticketID);
            statement.setInt(2, customerID);
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted == 0) {
                throw new SQLException("Failed to insert ticket: " + ticketID + " for customer: " + customerID);
            }
        } catch (SQLException e) {
            throw new EventException(e.getMessage(), e.getCause(), ErrorCode.OPERATION_DB_FAILED);
        }
    }



}
