package bll;

import be.Customer;
import be.Event;
import be.Ticket;
import be.TicketType;
import dal.TicketDAO;
import exceptions.EventException;
import javafx.collections.ObservableMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


    public boolean soldTickets(List<Ticket> allSelectedTickets, Customer customer) throws EventException {
        return ticketD.insertSoldTickets(allSelectedTickets, customer);
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

    /**
     * get the tickets with UUID filtered by type
     */
    public Map<TicketType, List<Ticket>> getTicketsWithUUId(List<Ticket> allTickets) throws EventException {
        return ticketD.retrieveTicketsUUID(sortTicketsByType(allTickets));
    }

    private Map<TicketType, List<Ticket>> sortTicketsByType(List<Ticket> allTickets) {
        Map<TicketType, List<Ticket>> sortedTicketsByType = new HashMap<>();
        sortedTicketsByType.put(TicketType.NORMAL, sortNormalTicketsType(allTickets));
        sortedTicketsByType.put(TicketType.SPECIAL, sorSpecialTicketsType(allTickets));
        return sortedTicketsByType;
    }

    private List<Ticket> sorSpecialTicketsType(List<Ticket> allTickets) {
        return allTickets.stream().filter(Ticket::getSpecial).toList();
    }

    private List<Ticket> sortNormalTicketsType(List<Ticket> allTickets) {
        return allTickets.stream().filter((e) -> !e.getSpecial()).toList();
    }
}
