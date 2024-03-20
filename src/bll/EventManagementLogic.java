package bll;
import be.Event;
import dal.EventDAO;
import exceptions.EventException;
import javafx.collections.ObservableMap;

public class EventManagementLogic implements ILogicManager{
    private EventDAO  eventData;
    public EventManagementLogic() throws  EventException {
        this.eventData = new EventDAO();
    }

    @Override
    public ObservableMap<Integer,Event> getEvents() throws EventException {
        return eventData.getEvents();
    }
}
