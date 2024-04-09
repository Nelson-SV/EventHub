package view.components.main;

import be.*;
import bll.*;
import exceptions.EventException;
import javafx.application.Platform;
import javafx.collections.*;
import javafx.concurrent.Task;
import view.components.eventsObservers.DateObservable;
import view.components.eventsObservers.DateObserver;
import view.components.eventsObservers.EventsObservable;
import view.components.listeners.CoordinatorsDisplayer;
import view.components.listeners.Displayable;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;
public class Model implements CommonModel {


    //TODO  logged user to be initialized with the actual logged user
//12,Andrei Ionut,test,Grosu,http://res.cloudinary.com/deipyfz99/image/upload/v1712563835/mbwqk1wpanct5bdtdmgw.png,event_coordinator
    private User loggedUser ;
    {
   loggedUser = new User("Andrei Ionut", "Grosu", "event_coordinator", "","http://res.cloudinary.com/deipyfz99/image/upload/v1712563835/mbwqk1wpanct5bdtdmgw.png");
   loggedUser.setUserId(12);
    }

    /**
     * holds the response of edit validity , in order to display information on the screen
     */
    private EventInvalidResponse eventEditResponse;
    /**
     * holds the component that displays the events
     */
    private Displayable eventsDisplayer;
    /**
     * the thread that is checking if the  events table is changing into the database, addition, delete
     */
    private DateObserver eventsObservable;
    /**
     * the thread that updates the UI event status based on the event start date and event end date
     */
    private DateObserver dateObservable;

    /**
     * holds the component that is displaying coordinators
     */
    private CoordinatorsDisplayer coordinatorsDisplayer;

    /**
     * Holds the events for a given user
     */
    //private ObservableMap<Integer, Event> coordinatorEvents;

    /**
     * holds the events with the status computed of the current logged in coordinator, in order to be displayed on the screen
     */
    private ObservableMap<Integer, EventStatus> loggedCoordinatorEvents;


    /**holds all the evnts in the sytem, is used for the selling tickets management*/
    private ObservableMap<Integer, Event> allEvents;

    private ObservableMap<Integer, Ticket> eventTickets;
    private ObservableMap<Integer, Ticket> specialTickets;


    // Todo the following to collections are not used, we remove them
    /*** holds all the event coordinators available*/
    private ObservableMap<Integer, User> allEventCoordinators;
    private HashMap<Integer, List<Integer>> assignedoordinators;

    private EventManager eventManager;
    private ILogicManager evmLogic;
    private TicketManager ticketManager;

    /**
     * holds the current opened event for managing
     */
    private Event selectedEvent;

    private List<Ticket> addedTickets;

    private static Model instance;

    public static Model getInstance() throws EventException {
        if (instance == null) {
            instance = new Model();
        }
        return instance;
    }


    private Model() throws EventException {
        initializeEventsObservable();
        initializeEventDateObservable();
        eventManager = new EventManager();
        ticketManager = new TicketManager();
       // coordinatorEvents = FXCollections.observableHashMap();
        allEvents = FXCollections.observableHashMap();
        eventTickets = FXCollections.observableHashMap();
        evmLogic = new EventManagementLogic();
        addedTickets = new ArrayList<>();
        initializeEventsMap();
        returnAllEvents();
        loggedCoordinatorEvents = FXCollections.observableHashMap();
    }

    /**
     * initialize the thread that is checking the database for modifications
     */
    private void initializeEventsObservable() throws EventException {
        eventsObservable = new EventsObservable(this);
        eventsObservable.startService();
    }

    /**
     * initialize the thread that check if event status has changed
     */
    private void initializeEventDateObservable() {
        dateObservable = new DateObservable(this);
        dateObservable.startService();
    }



    public User getLoggedUser() {
        return loggedUser;
    }

    public void setLoggedUser(User loggedUser) {
        this.loggedUser = loggedUser;
    }


    /*** creates a new event
     @param event the new event created*/
    public void addEvent(Event event,int userId) throws EventException {
        Integer inserted = eventManager.addEvent(event, addedTickets,userId);
        addedTickets.clear();
        if (inserted != null) {
            event.setId(inserted);
            EventStatus createdEvent = new EventStatus(event);
            createdEvent.setStatus(EventStatusCalculator.calculateStatus(event));
            loggedCoordinatorEvents.put(inserted,createdEvent);
        }
        Platform.runLater(()->{
            this.eventsDisplayer.displayEvents();
        });
    }

    public List<Ticket> getNewAddedTicket(Ticket ticket) {
        addedTickets.add(ticket);
        return addedTickets;
    }

    public void removeAddedTicket(Ticket ticket) {
        addedTickets.remove(ticket);
    }


    /**
     * initialize the events map
     */
    public void initializeEventsMap() throws EventException {
        loggedCoordinatorEvents = evmLogic.getEventsWithStatus(loggedUser.getUserId());
    }

    /**
     * Sets the Event Displayer responsible for displaying the events
     */
    public void setEventsDisplayer(Displayable eventsDisplayer) {
        this.eventsDisplayer = eventsDisplayer;
        this.eventsObservable.addDisplayable(this.eventsDisplayer);
        this.dateObservable.addDisplayable(this.eventsDisplayer);
    }

    public Displayable getEventsDisplayer() {
        return eventsDisplayer;
    }

    /**
     * sorts the events with the least amount of time remaining until it starts first
     */
    public List<EventStatus> sortedEventsList() {
        return evmLogic.getAllSortedEventsByStatus(loggedCoordinatorEvents.values());
        //return evmLogic.getSortedEventsByStatus(loggedCoordinatorEvents.values());
    }

    /**
     * sets the view that is displaying the coordinators
     */
    public void setCoordinatorsDisplayer(CoordinatorsDisplayer displayer) {
        this.coordinatorsDisplayer = displayer;
    }

    /**
     * set the event that has been selected to be managed
     * it is a clone, if user wants to cancel , than the original event will not be affected
     */
    public void setSelectedEvent(int id) {
        this.selectedEvent = new Event(loggedCoordinatorEvents.get(id).getEventDTO());
    }

    /*** returns the event that have been selected for editing*/
    public Event getSelectedEvent() {
        return this.selectedEvent;
    }

    /**
     * retrieve the eventCoordinators that are not assigned to the selected event
     */
    public Task<List<User>> executeData(int eventId) {
        return this.evmLogic.getevCoord(eventId);
    }

    public CoordinatorsDisplayer getCoordinatorsDisplayer() {
        return coordinatorsDisplayer;
    }


    public boolean isEditValid() {
        Event originalEvent = loggedCoordinatorEvents.get(selectedEvent.getId()).getEventDTO();
        if (selectedEvent.equals(originalEvent)) {
            return true;
        }
        boolean areDatesModified = evmLogic.areDatesModified(selectedEvent,originalEvent);
        if (areDatesModified) {
            EventInvalidResponse eventInvalidResponse = evmLogic.areEditedDatesValid(selectedEvent, originalEvent);
            if (eventInvalidResponse == null) {
                return true;
            } else {
                this.eventEditResponse = eventInvalidResponse;
                return false;
            }
        }
        return true;
    }

    /**
     * retrieves the response off edit event validation operation
     */
    public EventInvalidResponse getEventEditResponse() {
        return eventEditResponse;
    }

    /**
     * delete operation , that needs to be performed
     *
     * @param entityId        the entity that needs to be deleted , event, user , ticket
     * @param deleteOperation the type of the delete operation
     */
    @Override
    public void performDeleteOperation(int entityId, DeleteOperation deleteOperation) throws EventException {
        switch (deleteOperation) {
            case DELETE_EVENT -> this.deleteEvent(entityId);
            case DELETE_USER -> this.deleteUser(entityId);
            case DELETE_TICKET -> this.deleteTicket(entityId);
        }
    }

    private void deleteTicket(int entityId) {

    }


    private void deleteUser(int entityId) {
        //To be implemented
    }

    /**
     * returns the event with the corresponding id , from the coordinatorEvents map
     *
     * @param eventId the id of the required event
     */
    public Event getEventById(int eventId)
    {
        return loggedCoordinatorEvents.get(eventId).getEventDTO();
    }

    /**
     * delete an event from the database
     *
     * @param eventId the id of the event that will be deleted
     */
    private void deleteEvent(int eventId) throws EventException {
        boolean deleted = evmLogic.deleteEvent(eventId);
        if (deleted) {
            this.loggedCoordinatorEvents.remove(eventId);
            Platform.runLater(() -> getEventsDisplayer().displayEvents());
        }
    }

    /**
     * compares the dates of the events with the current date,
     * in order to rerender the view
     */
    public boolean compareEventDatesWithCurrentDate() {
        return EventStatusCalculator.isStatusChanged(loggedCoordinatorEvents.values());
    }


    public boolean isModified(Map<Integer, List<Integer>> assignedCoordinators) {
        return evmLogic.isModifyed(assignedCoordinators, selectedEvent, loggedCoordinatorEvents.get(selectedEvent.getId()).getEventDTO());
    }

    /**
     * save the edit operation performed on the current selected event
     */
    public void saveEditEventOperation(List<User> assignedCoordinators) throws EventException {
        HashMap<Integer, List<Integer>> assignedCoordinatorsMap = new HashMap<>();
        assignedCoordinatorsMap.put(selectedEvent.getId(), assignedCoordinators.stream().map(User::getUserId).collect(Collectors.toList()));
        boolean isModified = evmLogic.isModifyed(assignedCoordinatorsMap, selectedEvent, loggedCoordinatorEvents.get(selectedEvent.getId()).getEventDTO());
        if (!isModified) {
            return;
        }
        boolean editSucceded = evmLogic.saveEditOperation(selectedEvent, assignedCoordinatorsMap, addedTickets);
        addedTickets.clear();
        if (editSucceded) {
            EventStatus editedEvent = new EventStatus(selectedEvent);
            editedEvent.setStatus(EventStatusCalculator.calculateStatus(editedEvent.getEventDTO()));
            loggedCoordinatorEvents.put(selectedEvent.getId(), editedEvent);
            eventEditResponse = null;
        }
    }

    public ObservableMap<Integer, EventStatus> getCoordinatorEvents() {
        return loggedCoordinatorEvents;
    }

    public void setCoordinatorEvents(ObservableMap<Integer, EventStatus> coordinatorEvents) {
        this.loggedCoordinatorEvents = coordinatorEvents;
    }

    public ObservableMap<Integer, Event> getAllEvents() {
        return allEvents;
    }

    public void setAllEvents(ObservableMap<Integer, Event> allEvents) {
        this.allEvents = allEvents;
    }


    public List<String> getAllEventNames() {
        Collection<Event> events = allEvents.values();

        List<String> eventNames = events.stream()
                .map(Event::getName) // Extract the name of each event
                .collect(Collectors.toList()); // Collect names into a list

        return eventNames;
    }

    public ObservableMap<Integer, Ticket> getTicketsForEvent(int eventId) throws EventException {
        eventTickets = ticketManager.getTicketsForEvent(eventId); // Store tickets for the specified event
        return eventTickets; // Return the tickets for the specified event
    }

    public ObservableMap<Integer, Ticket> getSpecialTicketsForEventOrNot(int eventId) throws EventException {
        specialTickets = ticketManager.getSpecialTicketsRelatedOrNot(eventId); // Store tickets for the specified event
        return specialTickets; // Return the tickets for the specified event
    }


    public Integer getEventIdByName(String eventName) {
        for (Event event : allEvents.values()) {
            if (event.getName().equals(eventName)) {
                return event.getId();
            }
        }
        return -1;
    }

    public void returnAllEvents() throws EventException {
        allEvents = eventManager.getAllEvents();
    }


    public void sellTicket(List<Ticket> allSelectedTickets, Customer customer) throws EventException {
        ticketManager.soldTickets(allSelectedTickets, customer);
    }

    public LocalTime convertStringToTime(String value) {
        return evmLogic.convertStringToLocalTime(value);
    }



        /*
    public List<Ticket> addTicket(Ticket ticket) {
        Integer inserted = ticketManager.addTicket(ticket);
        if (inserted != null) {
            ticket.setId(inserted);
            addedTicket = new Ticket(inserted, ticket.getTicketType(), ticket.getQuantity(), ticket.getTicketPrice());
            eventTickets.put(inserted, ticket);
        }
        return null;
    }

     */

//    /**initialize the event coordinators list*/
//public void initializeEventCoordinators(int eventId) throws EventException {
//    evmLogic.getEventCoordinators(eventId).values().forEach((user)->allEventCoordinators.put(user.getUserId(),user));
//}
}