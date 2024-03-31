package view.admin.mainAdmin;
import be.Event;
import be.EventStatus;
import be.Status;
import be.User;
import bll.admin.AdminManagementLogic;
import bll.admin.IAdminLogic;
import exceptions.EventException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.concurrent.Task;
import view.components.listeners.Displayable;
import view.components.main.CommonModel;
import java.util.List;

/**
 * We will use dependency injection instead off the singleton design pattern,
 * this will allow us to  test the controllers in isolation*/
public class AdminModel implements CommonModel {
    private IAdminLogic adminLogic;
    private Displayable eventsDisplayer;

    /**holds all the events in the system*/
    private ObservableMap<Integer, EventStatus> allEvents;

    private ObservableMap<Status,List<EventStatus>> sortedEventsByStatus;

    /*** holds the coordinators for all the events*/
    private ObservableMap<Integer, List<User>> eventCoordinators;

    public AdminModel() throws EventException {
        this.adminLogic = new AdminManagementLogic();
        this.allEvents= FXCollections.observableHashMap();
        this.eventCoordinators=FXCollections.observableHashMap();
    }
    /**retrieves all the events from the database*/
    public void initializeEvents() throws EventException {
       allEvents=adminLogic.getEventsWithStatus();
    }
    /**retrieves the coordinators from the database*/
    public Task<List<User>> initializeEventCoordinators(int eventId) throws EventException {
       return adminLogic.getEventCoordinators(eventId);
    }
    /**
     * sorts the events with the least amount of time remaining until it starts first
     */
    public List<EventStatus> sortedEventsList() {
        return adminLogic.getSortedEventsByStatus(this.allEvents.values());
    }


    /**sort the events based on their status*/
    public void setSortedEventsByStatus(){
        sortedEventsByStatus = adminLogic.setSortedEventsByStatus(allEvents.values());
    }

    /**
     * Sets the Event Displayer responsible for displaying the events
     */
    public void setEventsDisplayer(Displayable eventsDisplayer) {
        this.eventsDisplayer = eventsDisplayer;
//        this.eventsObservable.addDisplayable(this.eventsDisplayer);
//        this.dateObservable.addDisplayable(this.eventsDisplayer);
    }

    public Displayable getEventsDisplayer() {
        return eventsDisplayer;
    }

    @Override
    public void deleteEvent(int eventId) throws EventException {
        //To be implemented
    }

    @Override
    public Event getEventById(int eventId) {
        //To be implemented
        return null;
    }
}
