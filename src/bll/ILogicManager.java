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

    boolean isModifyed(Map<Integer, List<Integer>> assignedCoordinators, Event selectedEvent, Event original);

    boolean saveEditOperation(Event selectedEvent, Map<Integer, List<Integer>> assignedCoordinators, List<Ticket> editTickets, List<Ticket> newTickets, List<Ticket> deleteTickets) throws EventException;

    //EventInvalidResponse isInputValidTest(Event selectedEvent);

    Status computeEventStatus(EventStatus event);

    ObservableMap<Integer, EventStatus> getEventsWithStatus(int userId) throws EventException;

    List<EventStatus> getAllSortedEventsByStatus(Collection<EventStatus> events);

    boolean deleteEvent(int eventId, List<Ticket> ticketsToDelete) throws EventException;

    LocalTime convertStringToLocalTime(String value);

    boolean areDatesModified(Event editedEvent, Event originalEvent);

    EventInvalidResponse areEditedDatesValid(Event editedEvent,Event originalEvent);


}
