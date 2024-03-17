package dal;

import be.Event;
import be.Location;
import exceptions.EventException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class eventDAO {
   private final ConnectionManager connectionManager;

   public eventDAO() throws SQLException, EventException {
        this.connectionManager = new ConnectionManager();
       // Location loc = event.getLocation();

    }

    public void insertEvent(Event event) throws SQLException {
        int locationID = insertLocation(event.getLocation());
        try (Connection connection = connectionManager.getConnection()) {
            String sql = "INSERT INTO Event (Start_date, Name, LocationId, Description, AvTickets, End_Date, Start_Time, End_Time) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setDate(1, java.sql.Date.valueOf(event.getStartDate()));
                statement.setString(2, event.getName());
                statement.setInt(3, locationID);
                statement.setString(4, event.getDescription());
                statement.setInt(5, 0);
                statement.setDate(6, java.sql.Date.valueOf(event.getEndDate()));
                statement.setTime(7, java.sql.Time.valueOf(event.getStartTime()));
                statement.setTime(8, java.sql.Time.valueOf(event.getEndTime()));
                statement.executeUpdate();


            }
        } catch (EventException e) {
            throw new RuntimeException(e);
        }

    }


    public int insertLocation(Location location) throws SQLException {
        try (Connection connection = connectionManager.getConnection()) {
            String sql = "INSERT INTO Location (street, additional, country, City, Postal_Code) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, location.getStreet());
                statement.setString(2, location.getAdditional());
                statement.setString(3, location.getCountry());
                statement.setString(4, location.getCity());
                statement.setString(5, location.getPostalCode());
                statement.executeUpdate();

                // Get the auto-generated key (location_id)
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    } else {
                        throw new SQLException("Failed to insert location, no keys generated.");
                    }
                }
            }
        } catch (EventException e) {
            throw new RuntimeException(e);
        }

    }}
