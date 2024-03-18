package bll;

import be.Event;
import dal.EventDAO;
import exceptions.EventException;

import java.sql.SQLException;

public class EventManager {

    EventDAO eventD  = new EventDAO();

    public EventManager() throws SQLException, EventException {

    }


    public void addEvent(Event event) throws SQLException, EventException {
        if (!isEventValid(event))
            throw new EventException("Movie is incomplete");
        eventD.insertEvent(event);
    }
    private boolean isEventValid (Event event){
        return event != null && !event.getName().isEmpty() &&
                event.getStartDate() != null && event.getStartTime() != null &&
                event.getLocation() != null && event.getLocation().getCity() != null && event.getLocation().getCountry() != null&& event.getLocation().getPostalCode() != null&& event.getLocation().getStreet() != null;
    }
}
