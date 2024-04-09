package view.components.main;

import be.*;
import bll.*;
import exceptions.EventException;
import javafx.application.Platform;
import javafx.collections.*;
import javafx.concurrent.Task;
import javafx.util.StringConverter;
import view.components.eventsObservers.DateObservable;
import view.components.eventsObservers.DateObserver;
import view.components.eventsObservers.EventsObservable;
import view.components.listeners.CoordinatorsDisplayer;
import view.components.listeners.Displayable;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

public class Model implements CommonModel {
//TODO
// 1.when an event is deleted if succesful from the db remove it from the list
// 2.maybe we use a map instead of a list where the eventId will be the key;
// 3.add an observable object that will hold the current selected event to be managed


    /**
     * holds the response off edit validity , in order to display information on the screen
     */
    private EventInvalidResponse eventEditResponse;
    private Displayable eventsDisplayer;
    private DateObserver eventsObservable;
    private DateObserver dateObservable;

    private CoordinatorsDisplayer coordinatorsDisplayer;

    /**
     * Holds the events for a given user
     */
    private ObservableMap<Integer, Event> coordinatorEvents;

    private ObservableMap<Integer, Event> allEvents;

    private ObservableMap<Integer, Ticket> eventTickets;
    private ObservableMap<Integer, Ticket> specialTickets;
    /**
     * holds all the event coordinators available
     */
    private ObservableMap<Integer, User> allEventCoordinators;
    private HashMap<Integer, List<Integer>> assignedoordinators;

    private EventManager eventManager;
    private ILogicManager evmLogic;
    private TicketManager ticketManager;

    /**
     * holds the current opened event for managing
     */
    private Event selectedEvent;

    private List<Ticket> addedTickets, ticketToEdit, ticketsToDelete;

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
        coordinatorEvents = FXCollections.observableHashMap();
        allEvents = FXCollections.observableHashMap();
        eventTickets = FXCollections.observableHashMap();
        evmLogic = new EventManagementLogic();
        addedTickets = new ArrayList<>();
        ticketToEdit = new ArrayList<>();
        ticketsToDelete = new ArrayList<>();
        initializeEventsMap();
        returnAllEvents();
    }

    private void initializeEventsObservable() throws EventException {
        eventsObservable = new EventsObservable(this);
        eventsObservable.startService();
    }

    private void initializeEventDateObservable() {
        dateObservable = new DateObservable(this);
        dateObservable.startService();
    }

    /**
     * creates a new event
     *
     * @param event the new event created
     */
    public void addEvent(Event event) throws EventException {
        Integer inserted = eventManager.addEvent(event, addedTickets);
        addedTickets.clear();
        if (inserted != null) {
            event.setId(inserted);
            coordinatorEvents.put(inserted, event);

        }
    }

    public List<Ticket> getNewAddedTicket(Ticket ticket) {
        addedTickets.add(ticket);
        return addedTickets;
    }

    public List<Ticket> getTicketsToEdit(Ticket ticket, int id) {
        ticket.setId(id);
        ticketToEdit.add(ticket);
        return ticketToEdit;
    }

    public List<Ticket> getTicketsToDelete(Ticket ticket, int id) {
        ticket.setId(id);
        ticketsToDelete.add(ticket);
        return ticketsToDelete;
    }

    public void removeAddedTicket(Ticket ticket) {
        addedTickets.remove(ticket);
    }


    /**
     * initialize the events map
     */
    public void initializeEventsMap() throws EventException {
        coordinatorEvents = evmLogic.getEvents();
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
    public List<Event> sortedEventsList() {
        return evmLogic.getSortedEventsByStatus(coordinatorEvents.values());
    }

    /**
     * updates the view that is displaying the coordinators
     */
    public void setCoordinatorsDisplayer(CoordinatorsDisplayer displayer) {
        this.coordinatorsDisplayer = displayer;
    }

    /**
     * set the event that has been selected to be managed
     * it is a clone, if user wants to cancel , than the original event will not be affected
     */
    public void setSelectedEvent(int id) {
        this.selectedEvent = new Event(coordinatorEvents.get(id));
    }

    /**
     * returns the event that have been selected for editing
     */
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
        Event originalEvent = allEvents.get(selectedEvent.getId());
        if (selectedEvent.equals(allEvents.get(selectedEvent.getId()))) {
            return true;
        }
        boolean areDatesModified = evmLogic.areDatesModified(selectedEvent, allEvents.get(selectedEvent.getId()));
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
     * sets the response off edit event validation operation
     */
    public void setEventEditResponse(EventInvalidResponse eventEditResponse) {
        this.eventEditResponse = eventEditResponse;
    }

    /**
     * delete operation to be performed
     */
    @Override
    public void performDeleteOperation(int entityId, DeleteOperation deleteOperation) throws EventException {
        switch (deleteOperation) {
            case DELETE_EVENT -> this.deleteEvent(entityId);
            case DELETE_USER -> this.deleteUser(entityId);
            //case DELETE_TICKET -> this.deleteTicket(entityId);
        }
    }


    private void deleteUser(int entityId) {
        //To be implemented
    }

    /**
     * returns the event with the corresponding id , from the coordinatorEvents map
     *
     * @param eventId the id of the required event
     */
    public Event getEventById(int eventId) {
        return coordinatorEvents.get(eventId);
    }

    /**
     * delete an event from the database
     *
     * @param eventId the id of the event that will be deleted
     */
    private void deleteEvent(int eventId) throws EventException {
        boolean deleted = evmLogic.deleteEvent(eventId, ticketsToDelete);
        if (deleted) {
            this.coordinatorEvents.remove(eventId);
            ticketsToDelete.clear();
            Platform.runLater(() -> getEventsDisplayer().displayEvents());
        }
    }

    /**
     * compares the dates of the events with the current date,
     * in order to rerender the view
     */
    public boolean compareEventDatesWithCurrentDate() {
        return EventStatusCalculator.isStatusChanged(coordinatorEvents.values());
    }


    public boolean isModified(Map<Integer, List<Integer>> assignedCoordinators) {
        return evmLogic.isModifyed(assignedCoordinators, selectedEvent, coordinatorEvents.get(selectedEvent.getId()));
    }


    /**
     * save the edit operation performed on the current selected event
     */
    public void saveEditEventOperation(List<User> assignedCoordinators) throws EventException {
        HashMap<Integer, List<Integer>> assignedCoordinatorsMap = new HashMap<>();

        assignedCoordinatorsMap.put(selectedEvent.getId(), assignedCoordinators.stream().map(User::getUserId).collect(Collectors.toList()));
        boolean isModified = evmLogic.isModifyed(assignedCoordinatorsMap, selectedEvent, coordinatorEvents.get(selectedEvent.getId()));
        if (!isModified && ticketToEdit.isEmpty() && addedTickets.isEmpty() && ticketsToDelete.isEmpty()) {
            return;
        }
        boolean editSucceded = evmLogic.saveEditOperation(selectedEvent, assignedCoordinatorsMap, ticketToEdit, addedTickets, ticketsToDelete);
        addedTickets.clear();
        ticketToEdit.clear();
        ticketsToDelete.clear();
        if (editSucceded) {
            coordinatorEvents.put(selectedEvent.getId(), selectedEvent);
            eventEditResponse = null;
        }
    }

    public ObservableMap<Integer, Event> getCoordinatorEvents() {
        return coordinatorEvents;
    }

    public void setCoordinatorEvents(ObservableMap<Integer, Event> coordinatorEvents) {
        this.coordinatorEvents = coordinatorEvents;
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