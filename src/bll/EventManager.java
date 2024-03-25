package bll;

import be.Event;
import be.Ticket;
import dal.EventDAO;
import exceptions.EventException;

import java.util.List;

public class EventManager {
    private EventDAO eventD = null;

    public EventManager() throws EventException {
        this.eventD = new EventDAO();
    }

    /**
     * Exception handling
     */

    public Integer addEvent(Event event, List<Ticket> tickets) throws EventException {
        return eventD.insertEvent(event, tickets);
    }

}
