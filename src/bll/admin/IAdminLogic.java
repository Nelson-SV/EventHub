package bll.admin;

import be.Event;
import be.EventStatus;
import be.Status;
import be.User;
import exceptions.EventException;
import javafx.collections.ObservableMap;
import javafx.concurrent.Task;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface IAdminLogic {
    ObservableMap<Integer, EventStatus> getEventsWithStatus() throws EventException;

    List<User> getEventCoordinators(int eventId) throws EventException;

    List<EventStatus> getSortedEventsByStatus(Collection<EventStatus> values);

    ObservableMap<Status,List<EventStatus>> setSortedEventsByStatus(Collection<EventStatus> events);

    boolean unassignUser(int entityId, int eventId) throws EventException;
}
