package view.admin.mainAdmin;

import be.*;
import bll.admin.AdminManagementLogic;
import bll.admin.IAdminLogic;
import exceptions.EventException;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import view.admin.listeners.*;
import view.components.listeners.Displayable;
import view.components.main.CommonModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * We will use dependency injection instead off the singleton design pattern,
 * this will allow us to  test the controllers in isolation
 */
public class AdminModel implements CommonModel, SortCommander, SortObserver {


    private final static ObservableList<String> roles = FXCollections.observableArrayList();
    private IAdminLogic adminLogic;
    private Displayable eventsDisplayer;


    //User management  setup
    /**
     * the actual controller that is displaying the users
     */
    private UsersDisplayer usersDisplayer;
    private File uploadedImage;


    private User selectedUserToEdit;
    private static final String defaultImage = "default.png";
    /**
     * holds the users in the system with the password and image
     */
    private ObservableMap<Integer, User> usersInTheSystem;
    /**
     * Users sorted by role
     */
    private ObservableList<User> sortedUsersByLastName;
    /**
     * the current displayed users on the view
     */
    private ObservableList<User> displayedUsers;

    /**
     * holds the sort subjects, 'shortcut buttons'
     */
    private List<SortSubject> observers;
    /**
     * hold the id of the latest shortcut button pressed
     */
    private String latestShortcutButtonPressed;
    /**
     * holds the current shortcut filter
     */
    private Status currentActiveFilter = Status.ALL;
    private AdminCoordinatorsDisplayer coordinatorsDisplayer;
    private EventStatus selectedEvent;


    /**
     * holds all the events in the system
     */
    private ObservableMap<Integer, EventStatus> allEvents;
    /**
     * holds the  current sorted list off events. When a shortcut button is pressed and the search filter
     * will be applied only for the sorted events by status
     */
    private List<EventStatus> currentSortedListEvents;
    /**
     * the collection that is displayed on the screen
     */
    private List<EventStatus> currentDisplayedEvents;
    /**
     * check If Filter Is Active in order to determine what list to sort when the sortButton is pressed
     */
    private boolean isFilterActive;


    /**
     * store the search results off the current search filter
     */
    private List<EventStatus> currentActiveFilterList;


    /**
     * holds the coordinators of the current selected event
     */
    private ObservableList<User> eventAssignedCoordinators;

    /**
     * holds all the coordinators in the system ,without the current selected event ones,without the password ,and image fields
     */
    private final ObservableList<User> allCoordinators;
    /**
     * holds all the selected coordinator that will be assigned to the selected event,without the password and the image fields
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
        roles.setAll(adminLogic.getRoles());
        this.usersInTheSystem = FXCollections.observableHashMap();
        this.sortedUsersByLastName = FXCollections.observableArrayList();
        this.displayedUsers = FXCollections.observableArrayList();
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
        this.currentSortedListEvents = currentDisplayedEvents;
    }

//THE FOLLOWING METHODS ARE RELATED TO THE SORTING OPERATIONS

    /**
     * sorts the events with the least amount of time remaining until it starts first
     */
    public List<EventStatus> sortedEventsList() {
        return this.currentDisplayedEvents;
    }

    /**
     * sorts the selected Events by status;
     */
    private List<EventStatus> sortEventsByStatus(Status status) {
        return adminLogic.getSortedEventsByStatus(allEvents.values(), status);
    }

    /**
     * sort a specific list by status
     */
    private List<EventStatus> sortSpecificEventsByStatus(List<EventStatus> events, Status status) {
        return adminLogic.getSortedEventsByStatus(events, status);
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
        boolean removed = adminLogic.deleteEvent(eventId);
        if (removed) {
            this.allEvents.remove(eventId);
            currentDisplayedEvents = sortEventsByStatus(Status.ALL);
            Platform.runLater(() -> this.getEventsDisplayer().displayEvents());
        }
        System.out.println(removed);
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

    private void deleteUserPermanent(int entityId) throws EventException {
        boolean deletionPerformed = adminLogic.deleteUserFromSystem(entityId);
        if (deletionPerformed) {
            this.usersInTheSystem.remove(entityId);
            sortDisplayedUsersByLastName();
            this.displayedUsers = sortedUsersByLastName;
            Platform.runLater(() -> {
                getUsersDisplayer().displayUsers();
            });
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
            case DELETE_USER_PERMANENT -> this.deleteUserPermanent(entityId);
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


    // Related to the sorting and searching functionality
    @Override
    public void performSortOperation(Status status) {
        currentActiveFilter = status;
        if (currentActiveFilterList != null && currentActiveFilterList.isEmpty()) {
            notifySubjects();
            return;
        }
        if (isFilterActive) {
            currentSortedListEvents = sortSpecificEventsByStatus(currentActiveFilterList, status);
        } else {
            currentSortedListEvents = sortEventsByStatus(status);
        }
        currentDisplayedEvents = currentSortedListEvents;
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

    @Override
    public void notifySubjects() {
        for (SortSubject sortSubject : observers) {
            if (sortSubject.isSelected() && !sortSubject.getIdentificationId().equals(latestShortcutButtonPressed)) {
                sortSubject.setSelected(false);
                sortSubject.changeToSort();
                sortSubject.changePerformedOperationToSort();
            }
        }
    }


    @Override
    public void setLatestSelected(String latestPressedId) {
        this.latestShortcutButtonPressed = latestPressedId;
    }

    /**
     * search for the even by name
     */
    public void searchForEvent(String eventName) {
        currentActiveFilterList = adminLogic.getSearchedEvents(eventName, currentSortedListEvents);
        currentDisplayedEvents = currentActiveFilterList;
        Platform.runLater(() -> eventsDisplayer.displayEvents());
    }


    /**
     * revert the events list to display all events (cancel the search filter)
     */
    public void cancelSearchEventFilter() {
        currentDisplayedEvents = sortEventsByStatus(currentActiveFilter);
        Platform.runLater(() -> eventsDisplayer.displayEvents());
    }

    public void setFilterActive(boolean filterActive) {
        isFilterActive = filterActive;
    }


    //Here ends the part that take care of the sort and search functionality


    //Here starts the user management behaviour
    public ObservableList<String> getRoles() {
        return roles;
    }

    public File getUploadedImage() {
        return uploadedImage;
    }

    public void setUploadedImage(File uploadedImage) {
        this.uploadedImage = uploadedImage;
    }

    public boolean isUnique(File file) {
        return adminLogic.fileExists(file);
    }

    public void saveUser(String firstName, String lastName, String userRole, String password) throws EventException {
        if (uploadedImage != null) {
            User user = new User(firstName, lastName, userRole, password);
            User insertedUser = adminLogic.saveUserWithImage(user, uploadedImage);
            if (insertedUser != null) {
                updateUsersMapSortedListDisplayedUsers(insertedUser);
            }
        } else {
            User user = new User(firstName, lastName, userRole, password, defaultImage);
            User insertedUser = adminLogic.saveUserWithDefaultImage(user);
            if (insertedUser != null) {
                updateUsersMapSortedListDisplayedUsers(insertedUser);
            }
        }
    }


    public void getAllUsersWithFullInfo() throws EventException {
        this.usersInTheSystem = adminLogic.getAllUsersWithFullData();
        sortDisplayedUsersByLastName();
        this.displayedUsers = sortedUsersByLastName;
    }

    /**
     * sort users by last name in alphabetically order
     */
    private void sortDisplayedUsersByLastName() {
        this.sortedUsersByLastName.setAll(adminLogic.sortedUsersByLastName(this.usersInTheSystem.values()));
    }

    public ObservableList<User> getUsersToDisplay() {
        return this.displayedUsers;
    }

    public User getUserById(int userId) {
        return usersInTheSystem.get(userId);
    }

    public void setSelectedUserToEdit(User user) {
        this.selectedUserToEdit = new User(user);
    }

    public User getSelectedUserToEdit() {
        return this.selectedUserToEdit;
    }


    //TODO to be implemented
    private void getSortedUserByRole() {
        this.sortedUsersByLastName = adminLogic.sortUserByRole(usersInTheSystem.values());
    }


    public void editUser() throws EventException {
        User editedUser = adminLogic.editUserOperation(this.selectedUserToEdit, uploadedImage, this.getUserById(selectedUserToEdit.getUserId()));
        if (editedUser != null) {
            updateUsersMapSortedListDisplayedUsers(editedUser);
        }
    }

    /**
     * updates the users map with the latest user updates
     * sort the list in alphabetically order and updates the displayed users
     *
     * @param user the newly created or updated user
     */
    private void updateUsersMapSortedListDisplayedUsers(User user) {
        this.usersInTheSystem.put(user.getUserId(), user);
        sortDisplayedUsersByLastName();
        this.displayedUsers = sortedUsersByLastName;
        this.uploadedImage = null;
    }

    public UsersDisplayer getUsersDisplayer() {
        return usersDisplayer;
    }

    public void setUsersDisplayer(UsersDisplayer usersDisplayer) {
        this.usersDisplayer = usersDisplayer;
    }
}
