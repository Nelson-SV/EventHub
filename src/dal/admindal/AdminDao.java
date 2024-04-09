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
        String sql = "SELECT E.*,(SELECT COUNT(U.EventId) FROM UsersEvents U WHERE U.EventId=E.EventId) FROM Event E";

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
                    EventStatus eventStatus = new EventStatus(event);
                    eventStatus.setCoordinatorCount(res.getInt(10));
                    events.put(event.getId(), eventStatus);
                }
            }
        } catch (exceptions.EventException | SQLException e) {
            throw new EventException(e.getMessage(), e.getCause(), ErrorCode.OPERATION_DB_FAILED);
        }
        return events;
    }
}
