package bll;

import be.Event;
import dal.EventDAO;
import exceptions.EventException;

import java.sql.SQLException;

public class EventManager {

    EventDAO eventD  = new EventDAO();

    public EventManager() throws SQLException, EventException {

    }

/** Exception handling */

   public boolean addEvent(Event event) throws SQLException, EventException {
       return eventD.insertEvent(event);
   }



}
