package view.components.main;

import be.Event;
import be.Ticket;
import be.User;
import bll.EventManagementLogic;
import bll.EventManager;
import bll.ILogicManager;
import bll.TicketManager;
import exceptions.EventException;
import exceptions.TicketException;
import javafx.collections.*;
import javafx.concurrent.Task;
import view.components.listeners.CoordinatorsDisplayer;
import view.components.listeners.Displayable;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class Model {
//TODO
// 1.when an event is deleted if succesful from the db remove it from the list
// 2.maybe we use a map instead of a list where the eventId will be the key;
// 3.add an observable object that will hold the current selected event to be managed

    private Displayable eventsDisplayer;
    private CoordinatorsDisplayer coordinatorsDisplayer;
    /**
     * Holds the events for a given user
     */
    private ObservableMap<Integer, Event> coordinatorEvents;
    private ObservableMap<Integer, Ticket> eventTickets;
    /**
     * holds all the event coordinators available
     */
    private ObservableMap<Integer, User> allEventCoordinators;
    private HashMap<Integer,List<Integer>> assignedoordinators;

    private EventManager eventManager;
    private ILogicManager evmLogic;
    private TicketManager ticketManager;
    /**
     * holds the current opened event for managing
     */
    private Event selectedEvent;
    private List<Ticket> addedTickets;

    private static Model instance;
    public static Model getInstance() throws EventException, TicketException {
        if (instance == null) {
            instance = new Model();
        }
        return instance;
    }

    private Model() throws EventException, TicketException {
        eventManager = new EventManager();
        ticketManager = new TicketManager();
        coordinatorEvents = FXCollections.observableHashMap();
        eventTickets = FXCollections.observableHashMap();
        evmLogic = new EventManagementLogic();
        allEventCoordinators = FXCollections.observableHashMap();
        addedTickets = new ArrayList<>();
    //    addEventListenerCoordinators();
        initializeEventsMap();
    }


    /**
     * creates a new event
     *
     * @param event the new event created
     */
    public void addEvent(Event event) throws EventException {
        Integer inserted = eventManager.addEvent(event, addedTickets);
        if (inserted != null) {
            event.setId(inserted);
            coordinatorEvents.put(inserted, event);
        }
    }

    public List<Ticket> getNewTicket(Ticket ticket){
        addedTickets.add(ticket);
        return addedTickets;
    }

    /*
    public List<Ticket> addTicket(Ticket ticket) throws TicketException {
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



    /**
     * initialize the events map
     */
    private void initializeEventsMap() throws EventException {
        coordinatorEvents = evmLogic.getEvents();
        addUpdateEventListener();
    }

    /**
     * listener for changes in the  events list, calls the EventDisplayer method to display the events
     */
    private void addUpdateEventListener() {
        this.coordinatorEvents.addListener((MapChangeListener<? super Integer, ? super Event>) change -> {
            if (change.wasAdded() || change.wasRemoved()) {
                eventsDisplayer.displayEvents();
            }
        });
    }

    /**
     * Sets the Event Displayer responsible for displaying the events
     */
    public void setEventsDisplayer(Displayable eventsDisplayer) {
        this.eventsDisplayer = eventsDisplayer;
    }

    /**
     * sorts the events with the least amount pff time remaining until it starts first
     */
    public List<Event> sortedEventsList() {
        Collection<Event> events = coordinatorEvents.values();
        return events.stream()
                .sorted(Comparator.comparing(event -> Math.abs(ChronoUnit.DAYS.between(LocalDate.now(), event.getStartDate()))))
                .collect(Collectors.toList());
    }

    /**updates the view that is displaying the coordinators*/
    public void setCoordinatorsDisplayer(CoordinatorsDisplayer displayer){
        this.coordinatorsDisplayer=displayer;
    }

    /**
     * set the event that has been selected to be managed
     * it is a clone, if user wants to cancel , than the original event will not be affected
     */
    public void setSelectedEvent(int id)
    {
        this.selectedEvent = new Event(coordinatorEvents.get(id));
    }

    /**
     * returns the event that have been selected for editing
     */
    public Event getSelectedEvent() {
        return this.selectedEvent;
    }

    /**retrieve the eventCoordinators that are not assigned to the selected event */
    public Task<List<User>> executeData(int eventId) {
       return this.evmLogic.getevCoord(eventId);
    }

    public CoordinatorsDisplayer getCoordinatorsDisplayer() {
        return coordinatorsDisplayer;
    }


public boolean isEditValid(){
     return  evmLogic.isEditValid(selectedEvent);
}

public boolean isModified(Map<Integer,List<Integer>> assignedCoordinators){
       return evmLogic.isModifyed(assignedCoordinators,selectedEvent,coordinatorEvents.get(selectedEvent.getId()));
}

    /**save the edit operation performed on the current selected event*/
    public void saveEditEventOperation(Map<Integer,List<Integer>> assignedCoordinators){
       boolean isModified=evmLogic.isModifyed(assignedCoordinators,selectedEvent,coordinatorEvents.get(selectedEvent.getId()));

       if(!isModified){
            return;
        }
        evmLogic.saveEditOperation(selectedEvent,assignedCoordinators);



    }


}
