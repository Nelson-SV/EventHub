package view.components.main;

import be.Customer;
import be.Event;
import be.User;
import bll.CustomerManager;
import bll.EventManagementLogic;
import bll.EventManager;
import bll.ILogicManager;
import exceptions.EventException;
import javafx.collections.*;
import view.components.eventManagement.EventManagementController;
import view.components.listeners.CoordinatorsDisplayer;
import view.components.listeners.Displayable;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
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
    /**
     * holds all the event coordinators available
     */
    private ObservableMap<Integer, User> allEventCoordinators;


    private EventManager manager;
    private CustomerManager customerManager;
    private ILogicManager evmLogic;
    /**
     * holds the current opened event for managing
     */
    private Event selectedEvent;

    private static Model instance;
    //ensures that by using Singelton all controllers use the same model


    public static Model getInstance() throws EventException {
        if (instance == null) {
            instance = new Model();
        }
        return instance;
    }

    private Model() throws EventException {
        customerManager = new CustomerManager();
        manager = new EventManager();
        coordinatorEvents = FXCollections.observableHashMap();
        evmLogic = new EventManagementLogic();
        allEventCoordinators = FXCollections.observableHashMap();
        addEventListenerCoordinators();
        initializeEventsMap();
    }


    /**
     * creates a new event
     *
     * @param event the new event created
     */
    public void addEvent(Event event) throws EventException {
        Integer inserted = manager.addEvent(event);
        if (inserted != null) {
            event.setId(inserted);
            coordinatorEvents.put(inserted, event);
        }
    }

    /**initialize the event coordinators list*/
public void initializeEventCoordinators(int eventId) throws EventException {
    evmLogic.getEventCoordinators(eventId).values().forEach((user)->allEventCoordinators.put(user.getUserId(),user));
}



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



    /**get all event Coordinators off the app that are not assigned to this event*/
    public ObservableList<User> getAllEventCoordinators() {
        return FXCollections.observableArrayList(allEventCoordinators.values());
    }

    /**
     * listen for the eventCoordinators changes*/
    private void addEventListenerCoordinators(){
        this.allEventCoordinators.addListener((MapChangeListener<? super Integer, ? super User>) change  ->{
           if(change.wasAdded()|| change.wasRemoved()){
               coordinatorsDisplayer.setCoordinators();
           }
        });
    }
    /**updates the view that is displaying the coordinators*/
    public void setCoordinatorsDisplayer(CoordinatorsDisplayer displayer){
        this.coordinatorsDisplayer=displayer;
    }



    /**
     * set the event that has been selected to be managed
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

    public void addCustomer(Customer customer) throws EventException {
        customerManager.addCustomer(customer);

    }
    public List<String> getAllEventNames() {
        Collection<Event> events = coordinatorEvents.values();

        List<String> eventNames = events.stream()
                .map(Event::getName) // Extract the name of each event
                .collect(Collectors.toList()); // Collect names into a list

        return eventNames;
    }

}
