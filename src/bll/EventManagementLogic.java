package bll;
import be.Event;
import dal.EventDAO;
import exceptions.EventException;
import java.sql.SQLException;
import java.util.List;

public class EventManagementLogic implements ILogicManager{
    private EventDAO  eventData;
    public EventManagementLogic() throws SQLException, EventException {
        this.eventData = new EventDAO();
    }

    @Override
    public List<Event> getEvents() {
        return eventData.getEvents();
    }
}
