package bll;

import be.Customer;
import be.Event;
import be.Ticket;
import dal.TicketDAO;
import exceptions.EventException;
import javafx.collections.ObservableMap;

import java.util.List;

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


    public void soldTickets(List<Ticket> allSelectedTickets, Customer customer) throws EventException {
        ticketD.insertSoldTickets(allSelectedTickets, customer);
    }

    public Integer addSpecialTicket(Ticket ticket, Event event) throws EventException {
        return ticketD.addSpecialTicket(ticket, event);
    }

    public void updateSpecialTicket(Ticket specialTicket) throws EventException {
        ticketD.updateSpecialTicket(specialTicket);
    }

    public void deleteSpecialTicket(Ticket specialTicket) throws EventException {
        ticketD.deleteSpecialTicket(specialTicket);
    }

}
