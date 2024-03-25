package bll;

import be.Event;
import be.Ticket;
import dal.EventDAO;
import dal.TicketDAO;
import exceptions.EventException;
import exceptions.TicketException;

public class TicketManager {

    private TicketDAO ticketD = null;

    public TicketManager() throws TicketException, EventException {
        this.ticketD = new TicketDAO();
    }

}
