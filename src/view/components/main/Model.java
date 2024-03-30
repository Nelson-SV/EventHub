package view.components.main;
import be.*;
import bll.*;
import exceptions.EventException;
import exceptions.TicketException;
import javafx.collections.*;
import javafx.concurrent.Task;
import view.components.listeners.CoordinatorsDisplayer;
import view.components.listeners.Displayable;
import java.util.*;
import java.util.stream.Collectors;

public class Model {
//TODO
// 1.when an event is deleted if succesful from the db remove it from the list
// 2.maybe we use a map instead of a list where the eventId will be the key;
// 3.add an observable object that will hold the current selected event to be managed

    private Displayable eventsDisplayer;
    private EventsObservable eventsObservable;
    private DateObservable dateObservable;
    private CustomerManager customerManager;
    private CoordinatorsDisplayer coordinatorsDisplayer;

    /**
     * Holds the events for a given user
     */
    private ObservableMap<Integer, Event> coordinatorEvents;

    private ObservableMap<Integer, Ticket> eventTickets;
    private ObservableMap<Integer, Ticket> specialTickets;
    /**
     * holds all the event coordinators available
     */
    private ObservableMap<Integer, User> allEventCoordinators;
    private HashMap<Integer,List<Integer>> assignedoordinators;


//TODO not sure if we will need it , not use @Grosu
    private ObservableMap<Integer, EventStatus> coordinatorEventsWithStatus;
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
        initializeEventsObservable();
        initializeEventDateObservable();
        eventManager = new EventManager();
        ticketManager = new TicketManager();
        coordinatorEvents = FXCollections.observableHashMap();
        coordinatorEventsWithStatus= FXCollections.observableHashMap();
        eventTickets = FXCollections.observableHashMap();
        evmLogic = new EventManagementLogic();
        addedTickets = new ArrayList<>();
        initializeEventsMap();
    }

    private void initializeEventsObservable() throws EventException {
        eventsObservable= new EventsObservable(this);
        eventsObservable.startService();
    }
    private void initializeEventDateObservable(){
        dateObservable= new DateObservable(this);
        dateObservable.startDateService();
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




    /**
     * initialize the events map
     */
    public void initializeEventsMap() throws EventException {
        coordinatorEvents = evmLogic.getEvents();
    }


    //TODO change from using events to events with status , after discussing with the team
    // do not use for now @Grosu
    /**
     * initialize the events with status map */
    private void initializeEventsWithStatusMap(Map<Integer,Event> coordinatorEvents){
      coordinatorEventsWithStatus = evmLogic.getEventsWithStatus(coordinatorEvents);
    }


//Todo needs to be deleted if not used anymore Grosu

//    /**
//     * listener for changes in the  events list, calls the EventDisplayer method to display the events
//     */
//    private void addUpdateEventListener() {
//        this.coordinatorEvents.addListener((MapChangeListener<? super Integer, ? super Event>) change -> {
//            if (change.wasAdded() || change.wasRemoved()) {
//                eventsDisplayer.displayEvents();
//            }
//        });
//    }


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
     * sorts the events with the least amount pff time remaining until it starts first
     */
    public List<Event> sortedEventsList() {
        return evmLogic.getSortedEventsByStatus(coordinatorEvents.values());
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
        System.out.println("In edit");
        return  evmLogic.isEditValid(selectedEvent);
    }
    /**returns the event with the corresponding id , from the coordinatorEvents map
     * @param eventId the id of the required event */
    public Event getEventById(int eventId){
        return coordinatorEvents.get(eventId);
    }

    /**
     * delete an event from the database
     *
     * @param eventId the id of the event that will be deleted
     */
    public void deleteEvent(int eventId) throws EventException {
        boolean deleted =  evmLogic.deleteEvent(eventId);
        if(deleted){
            this.coordinatorEvents.remove(eventId);
        }
    }
    public boolean isModified(Map<Integer,List<Integer>> assignedCoordinators){
        return evmLogic.isModifyed(assignedCoordinators,selectedEvent,coordinatorEvents.get(selectedEvent.getId()));
    }



    /**save the edit operation performed on the current selected event*/
    public void saveEditEventOperation(List<User> assignedCoordinators) throws EventException {
        HashMap<Integer,List<Integer>> assignedCoordinatorsMap = new HashMap<>();
        assignedCoordinatorsMap.put(selectedEvent.getId(),assignedCoordinators.stream().map(User::getUserId).collect(Collectors.toList()));
        boolean isModified=evmLogic.isModifyed(assignedCoordinatorsMap,selectedEvent,coordinatorEvents.get(selectedEvent.getId()));
        if(!isModified){
            return;
        }
        boolean editSucceded =evmLogic.saveEditOperation(selectedEvent,assignedCoordinatorsMap);
        if(editSucceded){
            coordinatorEvents.put(selectedEvent.getId(),selectedEvent);
        }
    }

    public ObservableMap<Integer, Event> getCoordinatorEvents() {
        return coordinatorEvents;
    }

    public void setCoordinatorEvents(ObservableMap<Integer, Event> coordinatorEvents) {
        this.coordinatorEvents = coordinatorEvents;
    }



    public void addCustomer (Customer customer) throws EventException {
        customerManager.addCustomer(customer);
    }
    public List<String> getAllEventNames () {
        Collection<Event> events = coordinatorEvents.values();

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
        for (Event event : coordinatorEvents.values()) {
            if (event.getName().equals(eventName)) {
                return event.getId();
            }
        }
        return -1;
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
}