package bll;

import be.*;
import dal.EventDAO;
import dal.UsersDAO;
import exceptions.EventException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.concurrent.Task;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
public class EventManagementLogic implements ILogicManager {
    private EventDAO eventData;
    private UsersDAO usersDao;

    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

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

    /**
     Convert from event objects  to event status objects
     */
    @Override
    public ObservableMap<Integer, EventStatus> getEventsWithStatus(int userId) throws EventException {
        ObservableMap<Integer, EventStatus> eventsWithStatus = eventData.retrieveEventsForUser(userId);
        eventsWithStatus.values().forEach(item -> item.setStatus(computeEventStatus(item)));
        return eventsWithStatus;
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


    /**
     * checks if the start date is valid compared with the local Date
     */
    private boolean isStartDateValid(LocalDate startDate) {
        return startDate != null && !startDate.isBefore(LocalDate.now());
    }

    private boolean isEndDateValid(LocalDate startDate, LocalDate endDate) {
        return !startDate.isAfter(endDate);
    }

    private boolean isStartTimeValid(LocalTime startTime, LocalTime endTime) {
        return startTime.isBefore(endTime);
    }

    /**
     * persist the edit event operation
     *
     * @param selectedEvent        the current edited event
     * @param assignedCoordinators the coordinates assigned to this event
     */
    @Override
    public boolean saveEditOperation(Event selectedEvent, Map<Integer, List<Integer>> assignedCoordinators,List<Ticket> editTickets, List<Ticket> newTickets, List<Ticket> deleteTickets) throws EventException {
        return eventData.saveEditOperation(selectedEvent, assignedCoordinators, editTickets, newTickets, deleteTickets);
    }

    /**
     * compute the status off the current based on the start date, time, end date, time
     *
     * @param event the event that needs to be calculated for
     */
    public Status computeEventStatus(EventStatus event) {
        return EventStatusCalculator.calculateStatus(event);
    }

    //sorting the events with status

    public List<EventStatus> getAllSortedEventsByStatus(Collection<EventStatus> events) {
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

    private List<EventStatus> sortOngoing(Collection<EventStatus> events) {
        List<EventStatus> ongoing = events.stream().filter((item) -> item.getStatus().getValue().equals(Status.ONGOING.getValue())).toList();
        return sortByStartingDateAndTime(ongoing);
    }

    private List<EventStatus> sortUpcoming(Collection<EventStatus> events) {
        List<EventStatus> upcoming = events.stream().filter((item) -> item.getStatus().getValue().equals(Status.UPCOMING.getValue())).toList();
        return sortByStartingDateAndTime(upcoming);
    }

    private List<EventStatus> sortFinalized(Collection<EventStatus> events) {
        List<EventStatus> finalized = events.stream().filter((item) -> item.getStatus().getValue().equals(Status.FINALIZED.getValue())).toList();
        return sortByStartingDateAndTime(finalized);
    }
    private List<EventStatus> sortByStartingDateAndTime(List<EventStatus> events) {
        return events.stream()
                .sorted(Comparator.comparing((EventStatus event) -> event.getEventDTO().getStartDate())
                        .thenComparing(event -> event.getEventDTO().getStartTime()))
                .collect(Collectors.toList());
    }

    /**
     * delete an event from the database
     *
     * @param eventId the id of the event
     */
    @Override
    public boolean deleteEvent(int eventId, List<Ticket> ticketsToDelete) throws EventException {
        return eventData.deleteEvent(eventId, ticketsToDelete);
    }

    public LocalTime convertStringToLocalTime(String value) {
        if (value != null && !value.isEmpty()) {
            return LocalTime.parse(value, timeFormatter);
        } else {
            return null;
        }
    }

    @Override
    public boolean areDatesModified(Event editedEvent, Event originalEvent) {
        if (!Objects.equals(editedEvent.getStartDate(), originalEvent.getStartDate())) {
            return true;
        }
        if (!Objects.equals(editedEvent.getStartTime(), originalEvent.getStartTime())) {

            return true;
        }
        if (!Objects.equals(editedEvent.getEndDate(), originalEvent.getEndDate())) {
            return true;
        }
        if (!Objects.equals(editedEvent.getEndTime(), originalEvent.getEndTime())) {
            return true;
        }
        return false;
    }

    /**
     * return true is event is null
     */
    private boolean isEndDateNull(LocalDate endDate) {
        return endDate == null;
    }


    public EventInvalidResponse areEditedDatesValid(Event editedEvent, Event originalEvent) {
        boolean isEditValid = true;
        EventInvalidResponse eventInvalid = new EventInvalidResponse();
        if (!Objects.equals(editedEvent.getStartDate(), originalEvent.getStartDate())) {
            if (!isStartDateValidCompleteCheck(editedEvent.getStartDate(), originalEvent.getEndDate())) {
                isEditValid = false;
                eventInvalid.setStartDateInvalid(editedEvent.getStartDate().toString() + ": Start date is not valid!");
            }
        }
        if (!Objects.equals(editedEvent.getStartTime(), originalEvent.getStartTime())) {
            boolean isStartTimeValid = isStartTimeValid(editedEvent.getStartTime(), editedEvent.getEndTime(), editedEvent.getStartDate(), editedEvent.getEndDate());
            System.out.println(isStartTimeValid + "start time validity");
            if (!isStartTimeValid) {
                isEditValid = false;
                eventInvalid.setStartTimeInvalid(editedEvent.getStartTime().toString() + isStartTimeValid + ": Start time is not valid!");
            }
        }
        if (!Objects.equals(editedEvent.getEndDate(), originalEvent.getEndDate())) {
            boolean endDateValid = isEndDateValid(editedEvent.getStartDate(), editedEvent.getEndDate());
            if (!endDateValid) {
                isEditValid = false;
                eventInvalid.setEndDateInvalid(editedEvent.getEndDate().toString() + ": End date is not valid!");
            }
        }
        if (!Objects.equals(editedEvent.getEndTime(), originalEvent.getEndTime())) {
            boolean endTimeValid = isEndTimeValidCompleteCheck(editedEvent.getStartTime(), editedEvent.getEndTime(), editedEvent.getStartDate(), editedEvent.getEndDate());
            if (!endTimeValid){
                isEditValid = false;
                eventInvalid.setEndTimeInvalid(editedEvent.getEndTime() + " : End time is not valid!");
            }
        }
        if (isEditValid) {
            return null;
        }
        return eventInvalid;
    }



    /**
     * check if start date is valid, before current date or after the end date
     */
    private boolean isStartDateValidCompleteCheck(LocalDate startDate, LocalDate endDate) {
        if (!isStartDateValid(startDate)) {
            return false;
        }

        if (!isEndDateNull(endDate)) {
            return isEndDateValid(startDate, endDate);
        }
        return true;
    }

    /**
     * check is startTime is valid, before the endTime, if the end date is null
     */
    private boolean isStartTimeValid(LocalTime startTime, LocalTime endTime, LocalDate startDate, LocalDate endDate) {
        if (endDate == null) {
            if (endTime == null) {
                return true;
            }
            return isStartTimeValid(startTime, endTime);
        } else {
            if (startDate.isEqual(endDate)) {
                if (endTime == null) {
                    return true;
                }
                return startTime.isBefore(endTime);
            }
            return true;
        }
    }


    /**
     * check is end time is valid, if is not before the start time
     */
    private boolean isEndTimeValidCompleteCheck(LocalTime startTime, LocalTime endTime, LocalDate startDate, LocalDate endDate) {
        if (endDate == null) {
            return isStartTimeValid(startTime, endTime);
        } else {
            if (startDate.isEqual(endDate)) {
                if (endTime == null) {
                    return true;
                }
                return startTime.isBefore(endTime);
            }
            return true;
        }
    }


/**applies the filter and returns the resulted collection
 * @param events events that needs to be sorted
 * @param filter the filter that needs to be applied*/
    @Override
    public List<EventStatus> performSearchFilterOperation(Collection<EventStatus> events, String filter) {
        String filterToApply  = filter.toLowerCase();

        return events.stream().filter((eventStatus -> eventStatus.getEventDTO().getName().toLowerCase().contains(filterToApply))).toList() ;
    }

}
