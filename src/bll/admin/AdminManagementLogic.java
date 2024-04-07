package bll.admin;

import be.EventStatus;
import be.Role;
import be.Status;
import be.User;
import bll.EventStatusCalculator;
import dal.EventDAO;
import dal.UsersDAO;
import dal.admindal.AdminDao;
import dal.admindal.IAdminDao;
import exceptions.EventException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.concurrent.Task;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class AdminManagementLogic implements IAdminLogic {
    private EventDAO eventDAO;
    private UsersDAO usersDAO;
    private IAdminDao adminDao;

    public AdminManagementLogic() throws EventException {
        this.eventDAO = new EventDAO();
        this.usersDAO = new UsersDAO();
        this.adminDao = new AdminDao();
    }


    public List<String> getRoles() {
        return Arrays.stream(Role.values()).map(Role::getValue).toList();
    }

    /**
     * To not be used, not sure if we need it
     * Convert from event objects  to event status objects
     */
    @Override
    public ObservableMap<Integer, EventStatus> getEventsWithStatus() throws EventException {
        ObservableMap<Integer, EventStatus> eventsWithStatus = adminDao.getAllEvents();
        eventsWithStatus.values().forEach(item -> item.setStatus(computeEventStatus(item)));
        return eventsWithStatus;
    }


    @Override
    public List<User> getEventCoordinators(int eventId) throws EventException {
        return usersDAO.getEventCoordinators(eventId);
    }


    /**
     * compute the status off the current based on the start date, time, end date, time
     *
     * @param event the event that needs to be calculated for
     */
    public Status computeEventStatus(EventStatus event) {
        return EventStatusCalculator.calculateStatus(event);
    }
    //TODO to be deleted if not needed anymore

    /**
     * sort the events by the status and startDate
     */
    public List<EventStatus> getAllSortedEventsByStatus(Collection<EventStatus> events) {
        List<EventStatus> sortedEvents = new ArrayList<>();
        //sort ongoing events
        List<EventStatus> ongoingEvents = sortOngoing(events);
        //sort upcoming events
        List<EventStatus> upcomingEvents = sortUpcoming(events);
        //sort finalized events
        List<EventStatus> finalizedEvents = sortFinalized(events);

        sortedEvents.addAll(ongoingEvents);
        sortedEvents.addAll(upcomingEvents);
        sortedEvents.addAll(finalizedEvents);
        return sortedEvents;
    }

    /**
     * sorts the events by status ahead, and store them in a map ,by status
     */
    public List<EventStatus> getSortedEventsByStatus(Collection<EventStatus> events, Status status) {
        List<EventStatus> sorted = new ArrayList<>();
        switch (status) {
            case Status.UPCOMING -> sorted = sortUpcoming(events);
            case Status.ONGOING -> sorted = sortOngoing(events);
            case Status.FINALIZED -> sorted = sortFinalized(events);
            case Status.ALL -> sorted = getAllSortedEventsByStatus(events);
        }
        return sorted;
    }

    @Override
    public boolean unassignUser(int entityId, int eventId) throws EventException {
        return usersDAO.unassignUser(entityId, eventId);
    }


    @Override
    public List<User> getAllCoordinators(int eventId) throws EventException {
        return usersDAO.getAllEvents(eventId);
    }

    @Override
    public boolean assignCoordinatorsToEvent(ObservableList<Integer> selectedUsers, int eventId) throws EventException {
        return usersDAO.assignCoordinatorsToEvent(selectedUsers, eventId);
    }

    @Override
    public boolean deleteEvent(int eventId) throws EventException {
        return eventDAO.deleteEvent(eventId);
    }

    @Override
    public List<EventStatus> getSearchedEvents(String eventName, List<EventStatus> events) {
        return events.stream().filter(e -> e.getEventDTO().getName().toLowerCase().contains(eventName)).toList();
    }

    private List<EventStatus> sortOngoing(Collection<EventStatus> events) {
        List<EventStatus> ongoing = events.stream().filter((item) -> item.getStatus().getValue().equals(Status.ONGOING.getValue())).toList();
        return sortByStartingDateAndTime(ongoing);
    }

    private List<EventStatus> sortUpcoming(Collection<EventStatus> events) {
        List<EventStatus> upcoming = events.stream().filter((item) -> item.getStatus().getValue().equals(Status.UPCOMING.getValue())).toList();
        return sortByStartingDateAndTime(upcoming);
    }

    private List<EventStatus> sortFinalized(Collection<EventStatus> events) {
        List<EventStatus> finalized = events.stream().filter((item) -> item.getStatus().getValue().equals(Status.FINALIZED.getValue())).toList();
        return sortByStartingDateAndTime(finalized);
    }

//    private List<EventStatus> sortByStartingDate(List<EventStatus> events) {
//        return events.stream()
//                .sorted(Comparator.comparing(event -> Math.abs(ChronoUnit.DAYS.between(LocalDate.now(), event.getEventDTO().getStartDate()))))
//                .collect(Collectors.toList());
//    }

    private List<EventStatus> sortByStartingDateAndTime(List<EventStatus> events) {
        return events.stream()
                .sorted(Comparator.comparing((EventStatus event) -> event.getEventDTO().getStartDate())
                        .thenComparing(event -> event.getEventDTO().getStartTime()))
                .collect(Collectors.toList());
    }

    //user management actions
    public boolean fileExists(File file) {
        Path uploadedImagesPath = Paths.get(System.getProperty("user.dir"), "uploadImages", "userUploadedImages");
        Path targetFilePath = uploadedImagesPath.resolve(file.getName());
        return Files.exists(targetFilePath);
    }

    @Override
    public User saveUserWithImage(User user,File uploadedImage) throws EventException {
        return usersDAO.saveUserWithCustomImage(user,uploadedImage);
    }

    @Override
    public User saveUserWithDefaultImage(User user) throws EventException {
        return usersDAO.saveUserWithDefaultImage(user);
    }

    @Override
    public ObservableMap<Integer, User> getAllUsersWithFullData() throws EventException {
        return usersDAO.getFullUserInfo();
    }

    @Override
    public ObservableList<User> sortUserByRole(Collection<User> values) {
        return null;
    };

    /**sort users by LastName alphabetically */
    public List<User> sortedUsersByLastName(Collection<User> values){
      return values.stream().sorted(Comparator.comparing(User::getLastName)).collect(Collectors.toList());
    }

    @Override
    public User editUserOperation(User selectedUserToEdit, File uploadedImage,User uneditedUser) throws EventException {
        if(uploadedImage==null){
            if(selectedUserToEdit.equals(uneditedUser)){
                return selectedUserToEdit;
            }
        }
        return usersDAO.editUserOperation(selectedUserToEdit,uploadedImage);
    }
}
