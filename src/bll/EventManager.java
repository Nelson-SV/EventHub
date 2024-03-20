package bll;

import be.Event;
import dal.EventDAO;
import exceptions.EventException;

import java.sql.SQLException;

public class EventManager {
    private EventDAO eventD = null;

    public EventManager() throws EventException {
        this.eventD = new EventDAO();
    }

    /**
     * Exception handling
     */

    public Integer addEvent(Event event) throws EventException {
        return eventD.insertEvent(event);
    }

}
