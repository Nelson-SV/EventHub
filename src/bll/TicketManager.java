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

    public Integer addTicket(Ticket ticket) throws TicketException {
        return ticketD.insertTicket(ticket);
    }

}
