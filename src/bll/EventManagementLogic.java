package bll;
import be.Event;
import be.EventStatus;
import be.Status;
import be.User;
import dal.EventDAO;
import dal.UsersDAO;
import exceptions.EventException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.concurrent.Task;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class EventManagementLogic implements ILogicManager {
    private EventDAO eventData;
    private UsersDAO usersDao;

    public EventManagementLogic() throws EventException {
        this.eventData = new EventDAO();
        this.usersDao = new UsersDAO();
    }

    @Override
    public ObservableMap<Integer, Event> getEvents() throws EventException {
        return eventData.getEvents();
    }


    @Override
    public Task<List<User>> getevCoord(int eventId) {
        return usersDao.getEventUsers(eventId);
    }

    @Override
    public ObservableMap<Integer, User> getEventCoordinators(int eventId) throws EventException {
        return eventData.getEventCoordinators(eventId);
    }

    /**
     * check if edit operations was performed
     *
     * @param selectedEvent        the current edited event
     * @param assignedCoordinators the coordinates assigned to this event
     * @param original             the original event before edit operation;
     */
    @Override
    public boolean isModifyed(Map<Integer, List<Integer>> assignedCoordinators, Event selectedEvent, Event original) {
        return !assignedCoordinators.get(selectedEvent.getId()).isEmpty() || !selectedEvent.equals(original);
    }

    public boolean isEditValid(Event selectedEvent) {
        boolean endDateValid = true;
        boolean endTimeValid = true;
        LocalDate startDate = selectedEvent.startDateProperty().get();
        LocalDate endDate = selectedEvent.getEndDate();
        LocalTime startTime = selectedEvent.startTimeProperty().get();
        LocalTime endTime = selectedEvent.getEndTime();
        if (endDate != null) {
            endDateValid = isEndDateValid(startDate, endDate);
        }
        if (endTime != null) {
            endTimeValid = isEndTimeValid(startTime, endTime, startDate, endDate);
        }

        return isNameValid(selectedEvent.getName()) &&
                isStartDateValid(startDate) &&
                !isStartTimeNull(startTime) &&
                !isLocationEmpty(selectedEvent.getLocation()) &&
                endDateValid &&
                endTimeValid;
    }

    private boolean isNameValid(String name) {
        return !name.isEmpty();
    }

    private boolean isStartDateValid(LocalDate startDate) {
        return startDate != null && !startDate.isBefore(LocalDate.now());
    }

    private boolean isStartTimeNull(LocalTime startTime) {
        return startTime == null;
    }

    private boolean isEndDateValid(LocalDate startDate, LocalDate endDate) {
        return !startDate.isAfter(endDate);
    }

    private boolean isEndTimeValid(LocalTime startTime, LocalTime endTime, LocalDate startDate, LocalDate endDate) {
        if (endDate != null) {
            if (startDate.isEqual(endDate)) {
                return startTime.isBefore(endTime);
            } else {
                return startTime.isBefore(endTime);
            }
        } else {
            return true;
        }
    }

    private boolean isLocationEmpty(String location) {
        return location.isEmpty();
    }


    /**
     * persist the edit event operation
     *
     * @param selectedEvent        the current edited event
     * @param assignedCoordinators the coordinates assigned to this event
     */
    @Override
    public boolean saveEditOperation(Event selectedEvent, Map<Integer, List<Integer>> assignedCoordinators) throws EventException {
        return eventData.saveEditOperation(selectedEvent, assignedCoordinators);
    }


    /**
     * compute the status off the current based on the start date, time, end date, time
     *
     * @param event the event that needs to be calculated for
     */
    public Status computeEventStatus(EventStatus event) {
        return EventStatusCalculator.calculateStatus(event);

    }


    /**
     * To not be used, not sure if we need it
     * Convert from event objects  to event status objects
     */
    @Override
    public ObservableMap<Integer, EventStatus> getEventsWithStatus(Map<Integer, Event> coordinatorEvents) {
        ObservableMap<Integer, EventStatus> eventsWithStatus = FXCollections.observableHashMap();
        coordinatorEvents.values().stream().map((EventStatus::new))
                .forEach((item) -> {
                    item.setStatus(computeEventStatus(item));
                    eventsWithStatus.put(item.getEventDTO().getId(), item);
                });
        return eventsWithStatus;
    }

    /**
     *sort the events by the status and startDate  */
    public List<Event> getSortedEventsByStatus(Collection<Event> events){
        List<EventStatus> eventsWithStatus = convertToEventsWithStatus(events);
        List<EventStatus> sortedEvents = new ArrayList<>();
        //sort ongoing events
        List<EventStatus> ongoingEvents = sortOngoing(eventsWithStatus);
        //sort upcoming events
        List<EventStatus> upcomingEvents= sortUpcoming(eventsWithStatus);
        //sort finalized events
        List<EventStatus> finalizedEvents=sortFinalized(eventsWithStatus);

        sortedEvents.addAll(ongoingEvents);
        sortedEvents.addAll(upcomingEvents);
        sortedEvents.addAll(finalizedEvents);

        return convertToEvent(sortedEvents);
    }

    private List<EventStatus> sortOngoing(List<EventStatus> events){
        List<EventStatus> ongoing = events.stream().filter((item)->item.getStatus().getValue().equals(Status.ONGOING.getValue())).toList();
    return sortByStartingDate(ongoing);
    }

    private List<EventStatus> sortUpcoming(List<EventStatus> events){
     List<EventStatus> upcoming = events.stream().filter((item)->item.getStatus().getValue().equals(Status.UPCOMING.getValue())).toList();
     return sortByStartingDate(upcoming);
    }

    private List<EventStatus> sortFinalized(List<EventStatus> events){
        List<EventStatus> finalized = events.stream().filter((item)->item.getStatus().getValue().equals(Status.FINALIZED.getValue())).toList();
    return sortByStartingDate(finalized);
    }



    private List<EventStatus> sortByStartingDate(List<EventStatus> events){
        return events.stream()
                .sorted(Comparator.comparing(event -> Math.abs(ChronoUnit.DAYS.between(LocalDate.now(), event.getEventDTO().getStartDate()))))
                .collect(Collectors.toList());
    }

    private List<EventStatus> convertToEventsWithStatus(Collection<Event> events) {
        return events.stream()
                .map((item) -> {
                    Status status = EventStatusCalculator.calculateStatus(item);
                    EventStatus eventStatus = new EventStatus(item);
                    eventStatus.setStatus(status);
                    return eventStatus;
                })
                .toList();
    }
    private List<Event> convertToEvent(List<EventStatus> events){
        return events.stream().map(EventStatus::getEventDTO).toList();
    }

    /**delete an event from the database
     * @param eventId the id of the event*/
    @Override
    public boolean deleteEvent(int eventId) throws EventException {
        return eventData.deleteEvent(eventId);
    }

}
