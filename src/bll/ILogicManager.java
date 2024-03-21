package bll;

import be.Event;
import be.User;
import exceptions.EventException;
import javafx.collections.ObservableMap;
public interface ILogicManager {
    ObservableMap<Integer,Event> getEvents() throws EventException;

    ObservableMap<Integer, User> getEventCoordinators(int eventId) throws EventException;
}
