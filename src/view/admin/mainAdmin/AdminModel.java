package view.admin.mainAdmin;

import be.*;
import bll.admin.AdminManagementLogic;
import bll.admin.IAdminLogic;
import exceptions.EventException;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import view.admin.listeners.AdminCoordinatorsDisplayer;
import view.components.listeners.Displayable;
import view.components.main.CommonModel;

import java.util.List;

/**
 * We will use dependency injection instead off the singleton design pattern,
 * this will allow us to  test the controllers in isolation
 */
public class AdminModel implements CommonModel {
    private IAdminLogic adminLogic;
    private Displayable eventsDisplayer;
    private AdminCoordinatorsDisplayer coordinatorsDisplayer;
    private EventStatus selectedEvent;

    /**
     * holds all the events in the system
     */
    private ObservableMap<Integer, EventStatus> allEvents;
    private ObservableMap<Status, List<EventStatus>> sortedEventsByStatus;

    /**
     * holds the coordinators of the current selected event
     */
    private ObservableList<User> eventAssignedCoordinators;

    /***/
    private ObservableList<User> allCoordinators;

    //TODO delete if not used
    /*** holds the coordinators for all the events*/
    private ObservableMap<Integer, List<User>> eventCoordinators;

    public AdminModel() throws EventException {
        this.adminLogic = new AdminManagementLogic();
        this.allEvents = FXCollections.observableHashMap();
        this.eventCoordinators = FXCollections.observableHashMap();
        this.eventAssignedCoordinators = FXCollections.observableArrayList();
        this.allCoordinators=FXCollections.observableArrayList();
    }

    public ObservableList<User> getAllCoordinators() {
        return allCoordinators;
    }

    /**
     * retrieves all the events from the database
     */
    public void initializeEvents() throws EventException {
        allEvents = adminLogic.getEventsWithStatus();
    }

    /**
     * retrieves the coordinators for an event  from the database
     */
    public void initializeEventCoordinators(int eventId) throws EventException {
        eventAssignedCoordinators.setAll(adminLogic.getEventCoordinators(eventId));
        System.out.println(eventAssignedCoordinators.size() + " executed");
    }

    /**retrieves all the coordinators in the system except the ones that are already assigned to this event*/
    public void initialiazeAllCoordinators(int entityId) throws EventException{
        allCoordinators.setAll(adminLogic.getAllCoordinators(entityId));
    }




    /**
     * sorts the events with the least amount of time remaining until it starts first
     */
    public List<EventStatus> sortedEventsList() {
        return adminLogic.getSortedEventsByStatus(this.allEvents.values());
    }


    /**
     * sort the events based on their status
     */
    public void setSortedEventsByStatus() {
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

    private void deleteEvent(int eventId) throws EventException {
        //To be implemented;
    }

    private void deleteUser(int entityId) throws EventException {
        if (adminLogic.unassignUser(entityId, selectedEvent.getEventDTO().getId())) {
            System.out.println("delete operation successfully");
            Platform.runLater(()->  this.selectedEvent.setCoordinatorCount(selectedEvent.getCoordinatorCount() - 1));
            List<User> removedUser = eventAssignedCoordinators.stream().filter(e->e.getUserId()!=entityId).toList();
            this.eventAssignedCoordinators.setAll(removedUser);
            refreshEventCoordinators();
        } else {
            System.out.println("delete operation unsuccessfully");
        }
    }

    public AdminCoordinatorsDisplayer getCoordinatorsDisplayer() {
        return coordinatorsDisplayer;
    }

    public void setCoordinatorsDisplayer(AdminCoordinatorsDisplayer coordinatorsDisplayer) {
        this.coordinatorsDisplayer = coordinatorsDisplayer;
    }

    @Override
    public void performDeleteOperation(int entityId, DeleteOperation deleteOperation) throws EventException {
        switch (deleteOperation) {
            case DELETE_EVENT -> this.deleteEvent(entityId);
            case DELETE_USER -> this.deleteUser(entityId);
        }
    }

    @Override
    public Event getEventById(int eventId) {
        return allEvents.get(eventId).getEventDTO();
    }

    public void setSelectedEvent(int eventId) {
        this.selectedEvent = allEvents.get(eventId);
    }

    /**
     * returns the eventAssignedCoordinators
     */
    public ObservableList<User> getEventAssignedCoordinators() {
        return eventAssignedCoordinators;
    }

    private void refreshEventCoordinators() {
        Platform.runLater(() -> this.coordinatorsDisplayer.displayEventCoordinators());
    }


}
