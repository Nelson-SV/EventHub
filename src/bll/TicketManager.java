package bll;

import be.Ticket;
import dal.TicketDAO;
import exceptions.EventException;
import javafx.collections.ObservableMap;

public class TicketManager {

    private TicketDAO ticketD = null;

    public TicketManager() throws EventException {
        this.ticketD = new TicketDAO();
    }

    public ObservableMap<Integer, Ticket> getTicketsForEvent(int eventId) throws EventException {
        return ticketD.retrieveTicketsForEvent(eventId);
    }

    public ObservableMap<Integer, Ticket> getSpecialTicketsRelatedOrNot(int eventId) throws EventException {
        return ticketD.retrieveSpecialTicketsForEventOrNot(eventId);
    }

    public void deductQuantity(int id, int quantity) throws EventException {
        ticketD.deductQuantity(id, quantity);
    }

    public void deductSpecialQuantity(int id, int quantity) throws EventException{
        ticketD.deductSpecialQuantity(id,quantity);
    }

    public void insertSoldTicket(int ticketId, int customerId) throws EventException{
        ticketD.insertSoldTicket(ticketId,customerId);
    }

    public void insertSoldSpecialTicket(int ticketId, int customerId) throws EventException{
        ticketD.insertSoldSpecialTicket(ticketId,customerId);
    }

}
