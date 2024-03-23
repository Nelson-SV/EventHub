package bll;
import be.Event;
import be.User;
import dal.EventDAO;
import dal.UsersDAO;
import exceptions.EventException;
import javafx.collections.ObservableMap;
import javafx.concurrent.Task;

import java.util.List;

public class EventManagementLogic implements ILogicManager{
    private EventDAO  eventData;
    private UsersDAO usersDao ;
    public EventManagementLogic() throws  EventException {
        this.eventData = new EventDAO();
        this.usersDao=  new UsersDAO();
    }

    @Override
    public ObservableMap<Integer,Event> getEvents() throws EventException {
        return eventData.getEvents();
    }

    @Override
    public Task<List<User>> getevCoord(int eventId){
        return  usersDao.getEventUsers(eventId);
    }

    @Override
    public ObservableMap<Integer, User> getEventCoordinators(int eventId) throws EventException {
        return eventData.getEventCoordinators(eventId);
    }
}
