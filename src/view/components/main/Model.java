package view.components.main;

import be.Event;
import bll.EventManagementLogic;
import bll.EventManager;
import bll.ILogicManager;
import exceptions.EventException;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import view.components.listeners.Displayable;

import java.sql.SQLException;

public class Model {
//TODO
// when an event is deleted if succesful from the db remove it from the list
// maybe we use a map instead of a list where the eventId will be the key;

    private Displayable eventsDisplayer;
    private EventManager manager;
    private ILogicManager evmLogic;
    /**
     * Holds the events for a given user
     */
    private ObservableList<Event> events;
    private final static Model instance;  //ensures that by using Singelton all controllers use the same model
    static {
        try {
            instance = new Model();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (EventException e) {
            throw new RuntimeException(e);
        }
    }

    public static Model getInstance(){
        return instance;
    }

    private Model() throws SQLException, EventException {
        manager = new EventManager();
        events = FXCollections.observableArrayList();
        evmLogic = new EventManagementLogic();
        initializeEventsList();
    }

    public void addEvent(Event event) throws SQLException, EventException {
        boolean inserted = manager.addEvent(event);
        if (inserted) {
            events.add(event);
        }
    }

    private void initializeEventsList() {
        this.events.setAll(evmLogic.getEvents());
        addUpdateEventListener();
    }

    /**listener for changes in the  events list, calls the EventDisplayer method to display the events */
    private void addUpdateEventListener() {
        this.events.addListener((ListChangeListener<Event>) change -> {
            while (change.next()) {
                if (change.wasAdded() || change.wasUpdated() || change.wasRemoved()) {
                    eventsDisplayer.displayEvents();
                }
            }
        });
    }
    public ObservableList<Event> getEvents() {
        return this.events;
    }


    /**
     * Sets the Event Displayer responsible for displaying the events*/
    public void setEventsDisplayer(Displayable eventsDisplayer) {
        this.eventsDisplayer = eventsDisplayer;
    }

}
