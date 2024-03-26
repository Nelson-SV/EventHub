package dal;

import be.Event;
import be.Ticket;
import exceptions.ErrorCode;
import exceptions.EventException;
import exceptions.TicketException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class TicketDAO {

    private final ConnectionManager connectionManager;
    public TicketDAO() throws TicketException, EventException {
        this.connectionManager = new ConnectionManager();
    }

    /*
    public Integer insertTicket(Ticket ticket) throws TicketException {
        Integer ticketId = null;
        Connection conn = null;
        try {
            conn = connectionManager.getConnection();
            conn.setAutoCommit(false);
            String ticketSql = "INSERT INTO Ticket (Type, Quantity, Price) VALUES (?, ?, ?)";
            PreparedStatement ticketStatement = conn.prepareStatement(ticketSql, Statement.RETURN_GENERATED_KEYS);
            ticketStatement.setString(1, ticket.getTicketType());
            ticketStatement.setInt(2, ticket.getQuantity());
            ticketStatement.setFloat(3, ticket.getTicketPrice());

            ticketStatement.executeUpdate();
            try (ResultSet generatedKeys = ticketStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    ticketId = generatedKeys.getInt(1);
                } else {
                    throw new TicketException(ErrorCode.OPERATION_DB_FAILED);
                }
            }
            conn.commit();
        } catch (TicketException | SQLException | EventException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    throw new TicketException(ex.getMessage(), ex.getCause(), ErrorCode.CONNECTION_FAILED);
                }
            }
            throw new TicketException(e.getMessage(), e.getCause(), ErrorCode.CONNECTION_FAILED);
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                throw new TicketException(e.getMessage(), e.getCause(), ErrorCode.CONNECTION_FAILED);
            }
        }
        return ticketId;
    }
     */


    /*private ObservableMap<Integer, Ticket> retrieveTickets() throws TicketException, EventException {
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
            throw new TicketException(e.getMessage(), e.getCause(), ErrorCode.OPERATION_DB_FAILED);
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
                    float price = res.getFloat("Price");

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
                    float price = res.getFloat("Price");

                    Ticket ticket = new Ticket(id, type, quantity, price);
                    tickets.put(ticket.getId(), ticket);
                }
            }
        } catch (SQLException | EventException e) {
            throw new EventException(e.getMessage(), e.getCause(), ErrorCode.OPERATION_DB_FAILED);        }

        return tickets;
    }



}
