package dal;

import be.Event;
import exceptions.ErrorCode;
import exceptions.EventException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EventDAO {
    private final ConnectionManager connectionManager;

    public EventDAO() throws SQLException, EventException {
        this.connectionManager = new ConnectionManager();

    }
    //TODO
// Exception to be handled
    public boolean insertEvent(Event event) {
        boolean success = false;
        Connection conn = null;
        try {
            conn = connectionManager.getConnection();
            conn.setAutoCommit(false);
            String sql = "INSERT INTO Event (Start_date, Name, Description, AvTickets, End_Date, Start_Time, End_Time, Location) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setDate(1, java.sql.Date.valueOf(event.getStartDate()));
                statement.setString(2, event.getName());
                statement.setString(3, event.getDescription());
                statement.setInt(4, 0);
                if (event.getEndDate() != null) {
                    statement.setDate(5, java.sql.Date.valueOf(event.getEndDate()));
                } else {
                    statement.setDate(5, null);
                }
                statement.setTime(6, java.sql.Time.valueOf(event.getStartTime()));
                if (event.getEndTime() != null) {
                    statement.setTime(7, java.sql.Time.valueOf(event.getEndTime()));
                } else {
                    statement.setTime(7, null);
                }
                statement.setString(8, event.getLocation());
                statement.executeUpdate();
            }
            conn.commit();
            success = true;
        } catch (EventException | SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
            throw new RuntimeException(e);
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return success;
    }

    /*public int insertLocation(Location location, Connection connection) throws SQLException {
        String sql = "INSERT INTO Location (street, additional, country, City, Postal_Code) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, location.getStreet());
            statement.setString(2, location.getAdditional());
            statement.setString(3, location.getCountry());
            statement.setString(4, location.getCity());
            statement.setString(5, location.getPostalCode());
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1); // Return generated location ID
                } else {
                    throw new SQLException("Failed to insert location, no keys generated.");
                }
            }
        }
    }*/


    public ObservableMap<Integer,Event> getEvents() throws EventException {
        return retrieveEvents();
    }

    //TODO
    //needs to be modified to accept an user
    //needs to be modified to handle the errors


    /**Retrieves all the events related to an eventCoordinator*/
    private ObservableMap<Integer,Event> retrieveEvents() throws EventException {
        ObservableMap<Integer,Event> events = FXCollections.observableHashMap();
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
                    events.put(event.getId(),event);
                }
            }
        } catch (EventException | SQLException e) {
           throw new EventException(e.getMessage(),e.getCause(), ErrorCode.OPERATION_DB_FAILED);
        }
        return events;
    }


}
