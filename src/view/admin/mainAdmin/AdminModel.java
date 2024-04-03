package view.admin.mainAdmin;

import be.*;
import bll.admin.AdminManagementLogic;
import bll.admin.IAdminLogic;
import exceptions.EventException;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import org.controlsfx.control.tableview2.filter.filtereditor.SouthFilter;
import view.admin.listeners.AdminCoordinatorsDisplayer;
import view.admin.listeners.SortCommander;
import view.admin.listeners.SortObserver;
import view.admin.listeners.SortSubject;
import view.components.listeners.Displayable;
import view.components.main.CommonModel;

import java.util.ArrayList;
import java.util.List;

/**
 * We will use dependency injection instead off the singleton design pattern,
 * this will allow us to  test the controllers in isolation
 */
public class AdminModel implements CommonModel, SortCommander, SortObserver {
    private IAdminLogic adminLogic;
    private Displayable eventsDisplayer;
    private List<SortSubject> observers;
    /**
     * hold the id of the latest shortcut button pressed
     */
    private String latestPressed;
    private AdminCoordinatorsDisplayer coordinatorsDisplayer;
    private EventStatus selectedEvent;

    /**
     * holds all the events in the system
     */
    private ObservableMap<Integer, EventStatus> allEvents;

    /**
     * holds the events sorted by status
     */
    private List<EventStatus> sortedEventsByStatus;


    /**
     * the collection that is displayed on the screen
     */
    private List<EventStatus> currentDisplayedEvents;

    /**
     * holds the coordinators of the current selected event
     */
    private ObservableList<User> eventAssignedCoordinators;

    /**
     * holds all the coordinators in the system ,without the current selected event ones
     */
    private final ObservableList<User> allCoordinators;
    /**
     * holds all the selected coordinator that will be assigned to the selected event
     */
    private ObservableList<Integer> selectedUsers;
    //TODO delete if not used
    /*** holds the coordinators for all the events*/
    private ObservableMap<Integer, List<User>> eventCoordinators;

    public AdminModel() throws EventException {
        this.adminLogic = new AdminManagementLogic();
        this.allEvents = FXCollections.observableHashMap();
        this.eventCoordinators = FXCollections.observableHashMap();
        this.eventAssignedCoordinators = FXCollections.observableArrayList();
        this.allCoordinators = FXCollections.observableArrayList();
        this.currentDisplayedEvents = new ArrayList<>();
        this.observers = new ArrayList<>();
    }

    public ObservableList<User> getAllCoordinators() {
        return allCoordinators;
    }

    /**
     * retrieves all the events from the database
     */
    public void initializeEvents() throws EventException {
        allEvents = adminLogic.getEventsWithStatus();
        initializeSortedEventsToBeDisplayed();
    }

    /**
     * retrieves the coordinators for an event  from the database
     */
    public void initializeEventCoordinators(int eventId) throws EventException {
        eventAssignedCoordinators.setAll(adminLogic.getEventCoordinators(eventId));
        System.out.println(eventAssignedCoordinators.size() + " executed");
    }

    /**
     * retrieves all the coordinators in the system except the ones that are already assigned to this event
     */
    public void initialiazeAllCoordinators(int entityId) throws EventException {
        allCoordinators.setAll(adminLogic.getAllCoordinators(entityId));
    }

    /**
     * sort the events by status in order relative to the current LocalDateTime
     */

    private void initializeSortedEventsToBeDisplayed() {
        this.currentDisplayedEvents = adminLogic.getAllSortedEventsByStatus(this.allEvents.values());
    }

//THE FOLLOWING METHODS ARE RELATED TO THE SORTING OPERATIONS

    /**
     * sorts the events with the least amount of time remaining until it starts first
     */
    public List<EventStatus> sortedEventsList() {
        return this.currentDisplayedEvents;
    }

    /**
     * returns the selected Events
     */
    private void sortEventsByStatus(Status status) {
        currentDisplayedEvents = adminLogic.getSortedEventsByStatus(allEvents.values(), status);
    }

    /**
     * Sets the Event Displayer responsible for displaying the events
     */
    public void setEventsDisplayer(Displayable eventsDisplayer) {
        this.eventsDisplayer = eventsDisplayer;
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
            Platform.runLater(() -> this.selectedEvent.setCoordinatorCount(selectedEvent.getCoordinatorCount() - 1));
            List<User> removedUser = eventAssignedCoordinators.stream().filter(e -> e.getUserId() != entityId).toList();
            this.eventAssignedCoordinators.setAll(removedUser);
            refreshEventCoordinators();
        } else {
            System.out.println("delete operation unsuccessfully");
        }
    }

    /**
     * decrease the number off the coordinators in order to reflect the remove operation in the event view
     */
    private void decreaseEventCoordinators() {
        Platform.runLater(() -> this.selectedEvent.setCoordinatorCount(selectedEvent.getCoordinatorCount() - 1));
    }

    private void increaseEventCoordinators(int amount) {
        Platform.runLater(() -> this.selectedEvent.setCoordinatorCount(selectedEvent.getCoordinatorCount() + amount));
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

    public void addSelectedUser(int entityId) {
        if (selectedUsers == null) {
            this.selectedUsers = FXCollections.observableArrayList();
        }
        this.selectedUsers.add(entityId);
    }

    //TODO change the name to reflect the operations
    public void saveSelectedCoordinatorsAndClearSelectedUsersList() throws EventException {
        if (selectedUsers.isEmpty()) {
            return;
        }
        boolean saved = adminLogic.assignCoordinatorsToEvent(selectedUsers, selectedEvent.getEventDTO().getId());
        if (saved) {
            increaseEventCoordinators(selectedUsers.size());
            selectedUsers = null;
        }
    }


    //TODO
    @Override
    public void performSortOperation(Status status) {
        sortEventsByStatus(status);
        notifySubjects();
        eventsDisplayer.displayEvents();
    }


    @Override
    public void addSubject(SortSubject subject) {
        this.observers.add(subject);
    }

    @Override
    public void removeSubject(SortSubject subject) {
        this.observers.remove(subject);
    }


    // TODO
    @Override
    public void notifySubjects() {
        for (SortSubject sortSubject : observers) {
            if (sortSubject.isSelected() && !sortSubject.getIdentificationId().equals(latestPressed)) {
                sortSubject.changeToSort();
                sortSubject.changePerformedOperationToSort();
            } else if (sortSubject.isSelected() && sortSubject.getIdentificationId().equals(latestPressed)) {
                sortSubject.changeToAll();
                sortSubject.changePerformedOperationToDefault();
            } else {
                sortSubject.changeToSort();
                sortSubject.changePerformedOperationToSort();
            }
        }
    }

    @Override
    public void setLatestSelected(String latestPressedId) {
        this.latestPressed = latestPressedId;
    }
}
