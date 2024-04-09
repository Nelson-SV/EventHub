package bll;

import be.*;
import exceptions.EventException;
import javafx.collections.ObservableMap;
import javafx.concurrent.Task;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface ILogicManager {
    ObservableMap<Integer, Event> getEvents() throws EventException;

    Task<List<User>> getevCoord(int eventId);

    ObservableMap<Integer, User> getEventCoordinators(int eventId) throws EventException;

    boolean isModifyed(Map<Integer, List<Integer>> assignedCoordinators, Event selectedEvent, Event original);

    boolean saveEditOperation(Event selectedEvent, Map<Integer, List<Integer>> assignedCoordinators, List<Ticket> editTickets, List<Ticket> newTickets) throws EventException;

    //EventInvalidResponse isInputValidTest(Event selectedEvent);

    Status computeEventStatus(EventStatus event);

    ObservableMap<Integer, EventStatus> getEventsWithStatus(Map<Integer, Event> coordinatorEvents);

    List<Event> getSortedEventsByStatus(Collection<Event> events);

    boolean deleteEvent(int eventId) throws EventException;

    LocalTime convertStringToLocalTime(String value);

    boolean areDatesModified(Event editedEvent, Event originalEvent);

    EventInvalidResponse areEditedDatesValid(Event editedEvent,Event originalEvent);


}
