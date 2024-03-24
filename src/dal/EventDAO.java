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
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class EventDAO {
    private final ConnectionManager connectionManager;
    public EventDAO() throws EventException {
        this.connectionManager = new ConnectionManager();
    }

    public Integer insertEvent(Event event, Ticket addedTicket) throws EventException {
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

            //this is outside off the transaction
            if(addedTicket != null) {
                addTicketToEvent(eventId, addedTicket, conn);
            }
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


    //Todo hope my comments are not upsetting you, have a nice day!!!
    //the ticket needs to be added at the same time with the event, in the same tranasaction,
    // to avoid having tickets without events, not in his own transaction.
    public void addTicketToEvent(int eventID, Ticket ticket, Connection conn) throws EventException {
        System.out.println(ticket);
        System.out.println(eventID);
        try {
            conn.setAutoCommit(false);
            String sql = "INSERT INTO EventTickets (Event_ID, Ticket_ID) VALUES (?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, eventID);
            statement.setInt(2, ticket.getId());
            statement.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

        public ObservableMap<Integer, Event> getEvents () throws EventException {
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

    public void  saveEditOperation(Event selectedEvent, Map<Integer, List<Integer>> assignedCoordinators) {
        System.out.println(selectedEvent);
        String updateEvent = "UPDATE Event SET Start_date=?,Name=?,Description=?,End_Date=?,Start_Time=?,End_Time=?,Location=? WHERE EventId=?";
             Connection conn = null;
        try{
            conn= connectionManager.getConnection();
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
          try(PreparedStatement psmt = conn.prepareStatement(updateEvent)){
                if(selectedEvent.getStartDate()!=null){
                    psmt.setDate(1,Date.valueOf(selectedEvent.getStartDate()));
                }else{
                    psmt.setDate(1,null);
                }
                psmt.setString(2,selectedEvent.getName());
                psmt.setString(3,selectedEvent.getDescription());
                if(selectedEvent.getEndDate()!=null){
                    psmt.setDate(4,Date.valueOf(selectedEvent.getEndDate()));
                }else{
                    psmt.setDate(4,null);
                }
                if(selectedEvent.getStartTime()!=null){
                    psmt.setTime(5,Time.valueOf(selectedEvent.getStartTime()));
                }else{
                    psmt.setTime(5,null);
                }
                if(selectedEvent.getEndTime()!=null){
                    psmt.setTime(6,Time.valueOf(selectedEvent.getEndTime()));
                }else{
                    psmt.setTime(6,null);
                }
                psmt.setString(7,selectedEvent.getLocation());
                psmt.setInt(8,selectedEvent.getId());
              System.out.println(     psmt.executeUpdate());
              System.out.println(conn);
          }
              insertCoordinators(selectedEvent.getId(),assignedCoordinators,conn);
          conn.commit();
        } catch (SQLException | EventException e) {
           try{
               if(conn!=null){
                   conn.rollback();
               }
           } catch (SQLException ex) {
               ex.printStackTrace();
               ExceptionLogger.getInstance().getLogger().log(Level.SEVERE,ex.getMessage(),ex);
           }
        }finally {
            if(conn!=null){
                try {
                    conn.close();
                } catch (SQLException e) {
                   e.printStackTrace();
                    ExceptionLogger.getInstance().getLogger().log(Level.SEVERE,e.getMessage(),e);

                }
            }
        }
    }

    private  void insertCoordinators(int eventId, Map<Integer, List<Integer>> assignedCoordinators,Connection conn) throws SQLException {
        System.out.println(eventId);
        System.out.println(assignedCoordinators.get(eventId));
        String insertCoordinators="INSERT INTO UsersEvents(UserId,EventId) values (?,?)";
        try(PreparedStatement psmt = conn.prepareStatement(insertCoordinators)){
            for(Integer userId : assignedCoordinators.get(eventId) ){
                psmt.setInt(1,userId);
                psmt.setInt(2,eventId);
                psmt.addBatch();
            }
            psmt.executeBatch();
        }
    }
}


