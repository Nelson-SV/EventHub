package bll;

import be.Event;
import be.EventStatus;
import be.Status;
import be.User;
import exceptions.EventException;
import javafx.collections.ObservableMap;
import javafx.concurrent.Task;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public interface ILogicManager {
    ObservableMap<Integer,Event> getEvents() throws EventException;
    Task<List<User>> getevCoord(int eventId);

    ObservableMap<Integer, User> getEventCoordinators(int eventId) throws EventException;

    boolean isModifyed(Map<Integer, List<Integer>> assignedCoordinators, Event selectedEvent, Event original);

    boolean saveEditOperation(Event selectedEvent, Map<Integer, List<Integer>> assignedCoordinators) throws EventException;
    boolean isEditValid(Event selectedEvent);

    Status computeEventStatus(EventStatus event);

    ObservableMap<Integer, EventStatus> getEventsWithStatus(Map<Integer, Event> coordinatorEvents);

    List<Event> getSortedEventsByStatus(Collection<Event> events);
}
