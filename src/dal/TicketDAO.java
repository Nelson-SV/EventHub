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

    //Todo hope my comments are not upsetting you , have a nice day !!!
    //ticket insertion needs to be in the same transaction with the insert event, not in his own transaction,
    //all the data regarding to an event needs to be inserted at the same time, if one fails everrything fails.
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


    //this method is in the EventDao, you can reuse
    private ObservableMap<Integer, Event> retrieveEvents() throws EventException {
        ObservableMap<Integer, Event> events = FXCollections.observableHashMap();
        String sql = "SELECT * FROM Event";

        try (Connection conn = connectionManager.getConnection()) {
            try (PreparedStatement psmt = conn.prepareStatement(sql)) {
                ResultSet res = psmt.executeQuery();
                while (res.next()) {
                    int id = res.getInt(1);
                    LocalDate startDate = res.getDate(2).toLocalDate();
                    String name = res.getString(3);
                    String description = res.getString(4);
                    int avTickets = res.getInt(5);
                    LocalDate endDate = null;
                    if (res.getDate(6) != null) {
                        endDate = res.getDate(6).toLocalDate();
                    }
                    LocalTime startTime = res.getTime(7).toLocalTime();
                    LocalTime endTime = null;
                    if (res.getTime(8) != null) {
                        endTime = res.getTime(8).toLocalTime();
                    }
                    String location = res.getString(9);
                    Event event = new Event(name, description, startDate, endDate, startTime, endTime, location);
                    event.setId(id);
                    event.setAvailableTickets(avTickets);
                    events.put(event.getId(), event);
                }
            }
        } catch (EventException | SQLException e) {
            throw new EventException(e.getMessage(), e.getCause(), ErrorCode.OPERATION_DB_FAILED);
        }
        return events;
    }

}
