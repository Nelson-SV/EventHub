package bll.admin;

import be.EventStatus;
import be.Status;
import be.User;
import bll.EventStatusCalculator;
import dal.EventDAO;
import dal.UsersDAO;
import dal.admindal.AdminDao;
import dal.admindal.IAdminDao;
import exceptions.EventException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.concurrent.Task;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class AdminManagementLogic implements IAdminLogic {
    private EventDAO eventDAO;
    private UsersDAO usersDAO;
    private IAdminDao adminDao;

    public AdminManagementLogic() throws EventException {
        this.eventDAO = new EventDAO();
        this.usersDAO = new UsersDAO();
        this.adminDao = new AdminDao();
    }

    /**
     * To not be used, not sure if we need it
     * Convert from event objects  to event status objects
     */
    @Override
    public ObservableMap<Integer, EventStatus> getEventsWithStatus() throws EventException {
        ObservableMap<Integer, EventStatus> eventsWithStatus = adminDao.getAllEvents();
        eventsWithStatus.values().forEach(item -> item.setStatus(computeEventStatus(item)));
        return eventsWithStatus;
    }


    @Override
    public List<User> getEventCoordinators(int eventId) throws EventException {
        return usersDAO.getEventCoordinators(eventId);
    }


    /**
     * compute the status off the current based on the start date, time, end date, time
     *
     * @param event the event that needs to be calculated for
     */
    public Status computeEventStatus(EventStatus event) {
        return EventStatusCalculator.calculateStatus(event);
    }
    //TODO to be deleted if not needed anymore

    /**
     * sort the events by the status and startDate
     */
    public List<EventStatus> getSortedEventsByStatus(Collection<EventStatus> events) {
        List<EventStatus> sortedEvents = new ArrayList<>();
        //sort ongoing events
        List<EventStatus> ongoingEvents = sortOngoing(events);
        //sort upcoming events
        List<EventStatus> upcomingEvents = sortUpcoming(events);
        //sort finalized events
        List<EventStatus> finalizedEvents = sortFinalized(events);

        sortedEvents.addAll(ongoingEvents);
        sortedEvents.addAll(upcomingEvents);
        sortedEvents.addAll(finalizedEvents);
        return sortedEvents;
    }

    /**
     * sorts the events by status ahead, and store them in a map ,by status
     */
    public ObservableMap<Status, List<EventStatus>> setSortedEventsByStatus(Collection<EventStatus> events) {
        ObservableMap<Status, List<EventStatus>> eventsByStatus = FXCollections.observableHashMap();
        List<EventStatus> sortedEvents = new ArrayList<>();
        //sort ongoing events
        List<EventStatus> ongoingEvents = sortOngoing(events);
        //sort upcoming events
        List<EventStatus> upcomingEvents = sortUpcoming(events);
        //sort finalized events
        List<EventStatus> finalizedEvents = sortFinalized(events);

        sortedEvents.addAll(ongoingEvents);
        sortedEvents.addAll(upcomingEvents);
        sortedEvents.addAll(finalizedEvents);

        eventsByStatus.put(Status.ONGOING, ongoingEvents);
        eventsByStatus.put(Status.UPCOMING, upcomingEvents);
        eventsByStatus.put(Status.FINALIZED, finalizedEvents);
        eventsByStatus.put(Status.ALL, sortedEvents);
        return eventsByStatus;
    }

    @Override
    public boolean unassignUser(int entityId, int eventId) throws EventException {
        return usersDAO.unassignUser(entityId, eventId);
    }



    @Override
    public List<User> getAllCoordinators(int eventId) throws EventException {
        return usersDAO.getAllEvents(eventId);
    }

    @Override
    public boolean assignCoordinatorsToEvent(ObservableList<Integer> selectedUsers, int eventId) throws EventException {
        return usersDAO.assignCoordinatorsToEvent(selectedUsers,eventId);
    }

    private List<EventStatus> sortOngoing(Collection<EventStatus> events) {
        List<EventStatus> ongoing = events.stream().filter((item) -> item.getStatus().getValue().equals(Status.ONGOING.getValue())).toList();
        return sortByStartingDate(ongoing);
    }

    private List<EventStatus> sortUpcoming(Collection<EventStatus> events) {
        List<EventStatus> upcoming = events.stream().filter((item) -> item.getStatus().getValue().equals(Status.UPCOMING.getValue())).toList();
        return sortByStartingDate(upcoming);
    }

    private List<EventStatus> sortFinalized(Collection<EventStatus> events) {
        List<EventStatus> finalized = events.stream().filter((item) -> item.getStatus().getValue().equals(Status.FINALIZED.getValue())).toList();
        return sortByStartingDate(finalized);
    }

    private List<EventStatus> sortByStartingDate(List<EventStatus> events) {
        return events.stream()
                .sorted(Comparator.comparing(event -> Math.abs(ChronoUnit.DAYS.between(LocalDate.now(), event.getEventDTO().getStartDate()))))
                .collect(Collectors.toList());
    }

}
