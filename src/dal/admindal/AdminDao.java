package dal.admindal;
import be.Event;
import be.EventStatus;
import be.User;
import dal.ConnectionManager;
import exceptions.ErrorCode;
import exceptions.EventException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

public class AdminDao implements IAdminDao {

    private ConnectionManager connectionManager;

    public AdminDao() throws exceptions.EventException {
        this.connectionManager = new ConnectionManager();
    }

    @Override
    public ObservableMap<Integer, EventStatus> getAllEvents() throws EventException {
        return retrieveEvents();
    }


    /**
     * Retrieves all the events related to an eventCoordinator
     */
    private ObservableMap<Integer, EventStatus> retrieveEvents() throws EventException {
        ObservableMap<Integer, EventStatus> events = FXCollections.observableHashMap();
        String sql = "SELECT E.*,(SELECT COUNT(U.EventId) FROM UsersEvents U WHERE U.EventId=E.EventId) AS Count FROM Event E";

        try (Connection conn = connectionManager.getConnection()) {
            try (PreparedStatement psmt = conn.prepareStatement(sql)) {
                ResultSet res = psmt.executeQuery();
                while (res.next()) {
                    int eventId = res.getInt("EventId");
                    LocalDate startDate = res.getDate("Start_Date").toLocalDate();
                    String name = res.getString("Name");
                    String description = res.getString("Description");
                    LocalDate endDate = null;
                    if (res.getDate("End_Date") != null) {
                        endDate = res.getDate("End_Date").toLocalDate();
                    }
                    LocalTime startTime = res.getTime("Start_Time").toLocalTime();
                    LocalTime endTime = null;
                    if (res.getTime("End_Time") != null) {
                        endTime = res.getTime("End_Time").toLocalTime();
                    }
                    String location = res.getString("Location");
                    Event event = new Event(name, description, startDate, endDate, startTime, endTime, location);
                    event.setId(eventId);
                    int totalNormalTickets = getNormalTicketsNumberForEvent(conn,eventId);
                    int totalSpecialTickets = getSpecialTicketsNumberForEvent(conn,eventId);
                    int totalTickets=totalNormalTickets+totalSpecialTickets;
                    event.setAvailableTickets(totalTickets);
                    EventStatus eventStatus = new EventStatus(event);
                    eventStatus.setCoordinatorCount(res.getInt("Count"));
                    events.put(event.getId(), eventStatus);
                }
            }
        } catch (exceptions.EventException | SQLException e) {
            throw new EventException(e.getMessage(), e.getCause(), ErrorCode.OPERATION_DB_FAILED);
        }
        return events;
    }



    /**
     * get the sum off normal tickets for an event, if the result from db is null, than method returns zero
     */
    private int getNormalTicketsNumberForEvent(Connection conn, int eventId) throws SQLException {
        int totalNormalTicketsNumber = 0;
        String sql = "SELECT SUM(T.Quantity) AS TotalQuantity " +
                "FROM Ticket T " +
                "JOIN EventTickets ET ON T.ID = ET.TicketID " +
                "JOIN Event E ON E.EventId = ET.EventId " +
                "WHERE ET.EventId =?";
        try (PreparedStatement psmt = conn.prepareStatement(sql)) {
            psmt.setInt(1, eventId);
            psmt.execute();
            ResultSet rs = psmt.getResultSet();
            if (rs.next()) {
                totalNormalTicketsNumber = rs.getInt("TotalQuantity");
            }
        }
        return totalNormalTicketsNumber;
    }

    /**
     * get the sum of special tickets for an event, if the result from db is null, than method returns zero
     */
    private int getSpecialTicketsNumberForEvent(Connection conn, int eventId) throws SQLException {
        int totalSpecialTicketsNumber = 0;
        String sql = "SELECT SUM(T.Quantity) AS TotalQuantity " +
                "FROM SpecialTickets T " +
                "JOIN EventSpecialTickets ST ON T.ID = ST.SpecialTicketID " +
                "JOIN Event E ON E.EventId = ST.EventId " +
                "WHERE ST.EventId = ?";
        try (PreparedStatement psmt = conn.prepareStatement(sql)) {
            psmt.setInt(1, eventId);
            psmt.execute();
            ResultSet rs = psmt.getResultSet();
            if (rs.next()) {
                totalSpecialTicketsNumber = rs.getInt("TotalQuantity");
            }
        }
        return totalSpecialTicketsNumber;
    }


}


