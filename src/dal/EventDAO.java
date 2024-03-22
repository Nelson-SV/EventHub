package dal;

import be.Event;
import be.Role;
import be.User;
import exceptions.ErrorCode;
import exceptions.EventException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class EventDAO {
    private final ConnectionManager connectionManager;
    public EventDAO() throws EventException {
        this.connectionManager = new ConnectionManager();
    }

    public Integer insertEvent(Event event) throws EventException {
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
        return eventId;
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
        ObservableMap<Integer,User> evCoordinators = FXCollections.observableHashMap();
        String sql = "SELECT U.UserId,U.FirstName,U.LastName,U.Role FROM USERS AS U "+
        "Where U.Role Like ? "+
        "AND U.UserId NOT IN (SELECT us.UserId FROM Users us join UsersEvents ue ON us.UserId=ue.UserId  join Event e ON e.EventId=ue.EventId WHERE e.EventId=?)";


        try(Connection conn = connectionManager.getConnection()){
            try(PreparedStatement psmt = conn.prepareStatement(sql)){
                psmt.setString(1, Role.EVENT_COORDINATOR.getValue());
                psmt.setInt(2,eventId);
                ResultSet rs =psmt.executeQuery();
                while(rs.next()){
                int userId = rs.getInt(1);
                String firstName = rs.getString(2);
                String lastName = rs.getString(3);
                String role =  rs.getString(4);
                User user = new User(firstName,lastName,role);
                user.setUserId(userId);
                evCoordinators.put(userId,user);
                }
            }
        } catch (SQLException |EventException e) {
            throw new EventException(e.getMessage(),e.getCause(),ErrorCode.OPERATION_DB_FAILED);
        }
        return evCoordinators;
    }


    public List<User> getEventCoordinatorsList(int eventId) throws EventException {
        List<User> evCoordinators = new ArrayList<>();
        String sql = "SELECT U.UserId,U.FirstName,U.LastName,U.Role FROM USERS AS U "+
                "Where U.Role Like ? "+
                "AND U.UserId NOT IN (SELECT us.UserId FROM Users us join UsersEvents ue ON us.UserId=ue.UserId  join Event e ON e.EventId=ue.EventId WHERE e.EventId=?)";
        try(Connection conn = connectionManager.getConnection()){
            try(PreparedStatement psmt = conn.prepareStatement(sql)){
                psmt.setString(1, Role.EVENT_COORDINATOR.getValue());
                psmt.setInt(2,eventId);
                ResultSet rs =psmt.executeQuery();
                while(rs.next()){
                    int userId = rs.getInt(1);
                    String firstName = rs.getString(2);
                    String lastName = rs.getString(3);
                    String role =  rs.getString(4);
                    User user = new User(firstName,lastName,role);
                    user.setUserId(userId);
                    evCoordinators.add(user);
                }
            }
        } catch (SQLException |EventException e) {
            throw new EventException(e.getMessage(),e.getCause(),ErrorCode.OPERATION_DB_FAILED);
        }
        return evCoordinators;
    }
}
