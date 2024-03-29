package bll;

import be.Event;
import be.Ticket;
import dal.EventDAO;
import dal.TicketDAO;
import exceptions.EventException;
import exceptions.TicketException;
import javafx.collections.ObservableMap;

public class TicketManager {

    private TicketDAO ticketD = null;

    public TicketManager() throws TicketException, EventException {
        this.ticketD = new TicketDAO();
    }

    public ObservableMap<Integer, Ticket> getTicketsForEvent(int eventId) throws EventException {
        return ticketD.retrieveTicketsForEvent(eventId);
    }

    public ObservableMap<Integer, Ticket> getSpecialTicketsRelatedOrNot(int eventId) throws EventException {
        return ticketD.retrieveSpecialTicketsForEventOrNot(eventId);
    }

}
