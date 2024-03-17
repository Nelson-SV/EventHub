package dal;

import be.Event;
import be.Location;
import exceptions.EventException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class EventDAO {
   private final ConnectionManager connectionManager;

   public EventDAO() throws SQLException, EventException {
        this.connectionManager = new ConnectionManager();
       // Location loc = event.getLocation();

    }

//TODO
    // change to use transaction when  new event is inserted into the database in order to avoid data loss
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

    }

public List<Event> getEvents(){
       return retrieveEvents();
}

//TODO
    //needs to be modified to accept an user
    //needs to be modified to handle the errors
private List<Event> retrieveEvents() {
       List<Event> events = new ArrayList<>();
       String sql ="SELECT * FROM Event AS e JOIN Location AS l ON e.LocationId=l.LocationId";
       try(Connection conn= connectionManager.getConnection()) {
           try(PreparedStatement psmt =conn.prepareStatement(sql)){
               ResultSet res = psmt.executeQuery();
               while(res.next()){
               int id =res.getInt(1);
               LocalDate startDate= res.getDate(2).toLocalDate();
               String name = res.getString(3);
               String description = res.getString(5);
               int avTickets = res.getInt(6);
               LocalDate endDate = res.getDate(7).toLocalDate();
               LocalTime startTime = res.getTime(8).toLocalTime();
               LocalTime endTime =  res.getTime(9).toLocalTime();
               int locId= res.getInt(10);
               String street = res.getString(11);
               String additional = res.getString(12);
               String country = res.getString(13);
               String city =  res.getString(14);
               String postalCode= res.getString(15);
               Location location = new Location(street,additional,postalCode,country,city);
               location.setId(locId);
                   //String name, String description, LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime, Location location
                Event event = new Event(name,description,startDate,endDate,startTime,endTime,location);
                event.setId(id);
                event.setAvailableTickets(avTickets);
                events.add(event);
                   System.out.println(event);
               }
           }

       }catch (EventException | SQLException e){
           System.out.println(e.getCause().getMessage());
       }
return events;
}


}
