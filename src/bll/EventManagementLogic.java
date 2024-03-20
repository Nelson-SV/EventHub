package bll;
import be.Event;
import dal.EventDAO;
import exceptions.EventException;
import javafx.collections.ObservableMap;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public class EventManagementLogic implements ILogicManager{
    private EventDAO  eventData;
    public EventManagementLogic() throws SQLException, EventException {
        this.eventData = new EventDAO();
    }

    @Override
    public ObservableMap<Integer,Event> getEvents() throws EventException {
        return eventData.getEvents();
    }
}
