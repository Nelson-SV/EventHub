package bll.admin;

import be.*;
import exceptions.EventException;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

import java.io.File;
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

    boolean deleteEvent(int eventId, List<Ticket> ticketToDelete) throws EventException;

    boolean deleteEvent(int eventId) throws EventException;

    List<EventStatus> getSearchedEvents(String eventName,List<EventStatus> events);

    List<String> getRoles();
    boolean fileExists(File file);

    User saveUserWithImage(User user,File uploadedImage) throws EventException;

    User saveUserWithDefaultImage(User user) throws  EventException;

    ObservableMap<Integer,User> getAllUsersWithFullData() throws EventException;

    ObservableList<User> sortUserByRole(Collection<User> values);
    List<User> sortedUsersByLastName(Collection<User> values);

    User editUserOperation(User selectedUserToEdit, File uploadedImage,User unEditedUser) throws EventException;
    boolean deleteUserFromSystem(int userId)throws EventException;
}
