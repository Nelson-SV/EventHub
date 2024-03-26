package bll;
import be.Event;
import be.User;
import dal.EventDAO;
import dal.UsersDAO;
import exceptions.EventException;
import javafx.collections.ObservableMap;
import javafx.concurrent.Task;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

public class EventManagementLogic implements ILogicManager{
    private EventDAO  eventData;
    private UsersDAO usersDao ;
    public EventManagementLogic() throws  EventException {
        this.eventData = new EventDAO();
        this.usersDao=  new UsersDAO();
    }

    @Override
    public ObservableMap<Integer,Event> getEvents() throws EventException {
        return eventData.getEvents();
    }

    @Override
    public Task<List<User>> getevCoord(int eventId){
        return  usersDao.getEventUsers(eventId);
    }

    @Override
    public ObservableMap<Integer, User> getEventCoordinators(int eventId) throws EventException {
        return eventData.getEventCoordinators(eventId);
    }
  /**check if edit operations was performed
   * @param selectedEvent the current edited event
   * @param assignedCoordinators the coordinates assigned to this event
   * @param original the original event before edit operation;
   */
    @Override
    public boolean isModifyed(Map<Integer, List<Integer>> assignedCoordinators, Event selectedEvent,Event original) {
        return !assignedCoordinators.get(selectedEvent.getId()).isEmpty() || !selectedEvent.equals(original);
    }
    public boolean isEditValid(Event selectedEvent){
    boolean endDateValid = true;
    boolean endTimeValid = true;
    if(selectedEvent.getEndDate()!=null){
        endDateValid=isEndDateValid(selectedEvent.startDateProperty().get(),selectedEvent.endDateProperty().get());
    }
    if(selectedEvent.getEndTime()!=null) {
        endTimeValid = isEndTimeValid(selectedEvent.startTimeProperty().get(), selectedEvent.endTimeProperty().get());
    }
     return isNameValid(selectedEvent.getName())&&
            !isStartDateNull(selectedEvent.getStartDate())
             && !isStartTimeNull(selectedEvent.getStartTime())
             && !isLocationEmpty(selectedEvent.getLocation())
             && endDateValid
             && endTimeValid;
    }

    private boolean isNameValid(String name){
        return !name.isEmpty();
    }

    private boolean isStartDateNull(LocalDate startDate){
        return startDate==null;
    }

    private boolean isStartTimeNull(LocalTime startTime){
        return startTime==null;
    }

    private boolean isEndDateValid(LocalDate startDate,LocalDate endDate){
        return startDate.isBefore(endDate);
    }

    private boolean isEndTimeValid(LocalTime startTime, LocalTime endTime){
        return startTime.isBefore(endTime);
    }

    private boolean isLocationEmpty(String location){
        return location.isEmpty();
    }


    /**persist the edit event operation
     * @param selectedEvent the current edited event
     * @param assignedCoordinators the coordinates assigned to this event*/
    @Override
    public boolean saveEditOperation(Event selectedEvent, Map<Integer, List<Integer>> assignedCoordinators) throws EventException {
     return eventData.saveEditOperation(selectedEvent,assignedCoordinators);
    }
}
