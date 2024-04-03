package bll.admin;

import be.EventStatus;
import be.Status;
import be.User;
import exceptions.EventException;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

import java.util.Collection;
import java.util.List;

public interface IAdminLogic {
    public List<EventStatus> getSortedEventsByStatus(Collection<EventStatus> events, Status status);
    ObservableMap<Integer, EventStatus> getEventsWithStatus() throws EventException;

    List<User> getEventCoordinators(int eventId) throws EventException;

    List<EventStatus> getAllSortedEventsByStatus(Collection<EventStatus> values);
    boolean unassignUser(int entityId, int eventId) throws EventException;

    List<User> getAllCoordinators(int eventId) throws EventException;

    boolean assignCoordinatorsToEvent(ObservableList<Integer> selectedUsers, int id) throws  EventException;

    boolean deleteEvent(int eventId) throws EventException;
}
