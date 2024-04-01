package dal;

import be.Event;
import be.Role;
import be.Ticket;
import be.User;
import exceptions.ErrorCode;
import exceptions.EventException;
import exceptions.ExceptionLogger;
import exceptions.TicketException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class EventDAO {
    private final ConnectionManager connectionManager;

    public EventDAO() throws EventException {
        this.connectionManager = new ConnectionManager();
    }

    public Integer insertEvent(Event event, List<Ticket> tickets) throws EventException {
        Integer eventId = null;
        Connection conn = null;
        try {
            conn = connectionManager.getConnection();
            conn.setAutoCommit(false);
            String sql = "INSERT INTO Event (Start_date, Name, Description, AvTickets, End_Date, Start_Time, End_Time, Location) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setDate(1, java.sql.Date.valueOf(event.getStartDate()));
            statement.setString(2, event.getName());
            statement.setString(3, event.getDescription());
            statement.setInt(4, 0);
            if (event.getEndDate() != null) {
                statement.setDate(5, java.sql.Date.valueOf(event.getEndDate()));
            } else {
                statement.setDate(5, null);
            }
            if (event.getStartTime() != null) {
                statement.setTime(6, java.sql.Time.valueOf(event.getStartTime()));
            } else {
                statement.setTime(6, null);
            }

            if (event.getEndTime() != null) {
                statement.setTime(7, java.sql.Time.valueOf(event.getEndTime()));
            } else {
                statement.setTime(7, null);
            }
            statement.setString(8, event.getLocation());
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    eventId = generatedKeys.getInt(1);
                } else {
                    throw new EventException(ErrorCode.OPERATION_DB_FAILED);
                }
            }

            if (!tickets.isEmpty()) {
                List<Integer> ticketIds = insertTicket(tickets, conn);
                addTicketToEvent(ticketIds, eventId, conn);
            }
            conn.commit();

        } catch (EventException | SQLException | TicketException e) {
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
        return eventId;
    }

    public void addTicketToEvent(List<Integer> ticketIds, int eventID, Connection conn) throws EventException, SQLException {
        String sql = "INSERT INTO EventTickets (TicketID, EventID) VALUES (?, ?)";
        try (PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            for (Integer ticketId : ticketIds) {
                statement.setInt(1, ticketId);
                statement.setInt(2, eventID);
                statement.addBatch();
            }
            statement.executeBatch();
        }
    }

    public List<Integer> insertTicket(List<Ticket> tickets, Connection conn) throws TicketException, SQLException {
        List<Integer> ticketIds = new ArrayList<>();
        String ticketSql = "INSERT INTO Ticket (Type, Quantity, Price) VALUES (?, ?, ?)";
        try (PreparedStatement ticketStatement = conn.prepareStatement(ticketSql, Statement.RETURN_GENERATED_KEYS)) {
            for (Ticket ticket : tickets) {
                ticketStatement.setString(1, ticket.getTicketType());
                ticketStatement.setInt(2, ticket.getQuantity());
                ticketStatement.setBigDecimal(3, ticket.getTicketPrice());

                ticketStatement.executeUpdate();

                try (ResultSet generatedKeys = ticketStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        ticketIds.add(generatedKeys.getInt(1));
                    } else {
                        throw new TicketException(ErrorCode.OPERATION_DB_FAILED);
                    }
                }
            }
        }
        return ticketIds;
    }


    public ObservableMap<Integer, Event> getEvents() throws EventException {
        return retrieveEvents();
    }


    //TODO
    //needs to be modified to accept an user
    //needs to be modified to handle the errors


    /**
     * Retrieves all the events related to an eventCoordinator
     */
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

    public ObservableMap<Integer, User> getEventCoordinators(int eventId) throws EventException {
        ObservableMap<Integer, User> evCoordinators = FXCollections.observableHashMap();
        String sql = "SELECT U.UserId,U.FirstName,U.LastName,U.Role FROM USERS AS U " +
                "Where U.Role Like ? " +
                "AND U.UserId NOT IN (SELECT us.UserId FROM Users us join UsersEvents ue ON us.UserId=ue.UserId  join Event e ON e.EventId=ue.EventId WHERE e.EventId=?)";

        try (Connection conn = connectionManager.getConnection()) {
            try (PreparedStatement psmt = conn.prepareStatement(sql)) {
                psmt.setString(1, Role.EVENT_COORDINATOR.getValue());
                psmt.setInt(2, eventId);
                ResultSet rs = psmt.executeQuery();
                while (rs.next()) {
                    int userId = rs.getInt(1);
                    String firstName = rs.getString(2);
                    String lastName = rs.getString(3);
                    String role = rs.getString(4);
                    User user = new User(firstName, lastName, role);
                    user.setUserId(userId);
                    evCoordinators.put(userId, user);
                }
            }
        } catch (SQLException | EventException e) {
            throw new EventException(e.getMessage(), e.getCause(), ErrorCode.OPERATION_DB_FAILED);
        }
        return evCoordinators;
    }


//    public List<User> getEventCoordinatorsList(int eventId) throws EventException {
//        List<User> evCoordinators = new ArrayList<>();
//        String sql = "SELECT U.UserId,U.FirstName,U.LastName,U.Role FROM USERS AS U "+
//                "Where U.Role Like ? "+
//                "AND U.UserId NOT IN (SELECT us.UserId FROM Users us join UsersEvents ue ON us.UserId=ue.UserId  join Event e ON e.EventId=ue.EventId WHERE e.EventId=?)";
//        try(Connection conn = connectionManager.getConnection()){
//            try(PreparedStatement psmt = conn.prepareStatement(sql)){
//                psmt.setString(1, Role.EVENT_COORDINATOR.getValue());
//                psmt.setInt(2,eventId);
//                ResultSet rs =psmt.executeQuery();
//                while(rs.next()){
//                    int userId = rs.getInt(1);
//                    String firstName = rs.getString(2);
//                    String lastName = rs.getString(3);
//                    String role =  rs.getString(4);
//                    User user = new User(firstName,lastName,role);
//                    user.setUserId(userId);
//                    evCoordinators.add(user);
//                }
//            }
//        } catch (SQLException |EventException e) {
//            throw new EventException(e.getMessage(),e.getCause(),ErrorCode.OPERATION_DB_FAILED);
//        }
//        return evCoordinators;
//    }

    public boolean saveEditOperation(Event selectedEvent, Map<Integer, List<Integer>> assignedCoordinators) throws EventException {
        boolean succeded = false;
        String updateEvent = "UPDATE Event SET Start_date=?,Name=?,Description=?,End_Date=?,Start_Time=?,End_Time=?,Location=? WHERE EventId=?";
        Connection conn = null;
        try {
            conn = connectionManager.getConnection();
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
            try (PreparedStatement psmt = conn.prepareStatement(updateEvent)) {
                if (selectedEvent.getStartDate() != null) {
                    psmt.setDate(1, Date.valueOf(selectedEvent.getStartDate()));
                } else {
                    psmt.setDate(1, null);
                }
                psmt.setString(2, selectedEvent.getName());
                psmt.setString(3, selectedEvent.getDescription());
                if (selectedEvent.getEndDate() != null) {
                    psmt.setDate(4, Date.valueOf(selectedEvent.getEndDate()));
                } else {
                    psmt.setDate(4, null);
                }
                if (selectedEvent.getStartTime() != null) {
                    psmt.setTime(5, Time.valueOf(selectedEvent.getStartTime()));
                } else {
                    psmt.setTime(5, null);
                }
                if (selectedEvent.getEndTime() != null) {
                    psmt.setTime(6, Time.valueOf(selectedEvent.getEndTime()));
                } else {
                    psmt.setTime(6, null);
                }
                psmt.setString(7, selectedEvent.getLocation());
                psmt.setInt(8, selectedEvent.getId());
                psmt.executeUpdate();
            }
            insertCoordinators(selectedEvent.getId(), assignedCoordinators, conn);
            conn.commit();
            succeded = true;
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
        return succeded;
    }

    private void insertCoordinators(int eventId, Map<Integer, List<Integer>> assignedCoordinators, Connection conn) throws SQLException {
        if (assignedCoordinators.get(eventId).isEmpty()) {
            return;
        }
        String insertCoordinators = "INSERT INTO UsersEvents(UserId,EventId) values (?,?)";
        try (PreparedStatement psmt = conn.prepareStatement(insertCoordinators)) {
            for (Integer userId : assignedCoordinators.get(eventId)) {
                psmt.setInt(1, userId);
                psmt.setInt(2, eventId);
                psmt.addBatch();
            }
            psmt.executeBatch();
        }
    }

    public boolean deleteEvent(int eventId) throws EventException {
        boolean succeeded = false;
        String sql = "DELETE FROM Event WHERE EventId=?";
        try (Connection conn = connectionManager.getConnection()) {
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
            try (PreparedStatement psmt = conn.prepareStatement(sql)) {
                psmt.setInt(1, eventId);
                psmt.executeUpdate();
                conn.commit();
                succeeded = true;
            } catch (SQLException e) {
                conn.rollback();
                throw new EventException(e.getMessage(), e.getCause(), ErrorCode.OPERATION_DB_FAILED);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            ExceptionLogger.getInstance().getLogger().log(Level.SEVERE, e.getMessage(), e);
        }
        return succeeded;
    }

}


