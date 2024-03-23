package bll;

import be.Event;
import be.User;
import exceptions.EventException;
import javafx.collections.ObservableMap;
import javafx.concurrent.Task;

import java.util.List;

public interface ILogicManager {
    ObservableMap<Integer,Event> getEvents() throws EventException;
    Task<List<User>> getevCoord(int eventId);

    ObservableMap<Integer, User> getEventCoordinators(int eventId) throws EventException;
}
