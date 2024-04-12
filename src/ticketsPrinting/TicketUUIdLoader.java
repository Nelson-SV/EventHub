package ticketsPrinting;

import be.Ticket;
import be.TicketType;
import javafx.concurrent.Task;
import view.components.main.Model;

import java.util.List;
import java.util.Map;

public class TicketUUIdLoader extends Task<Map<TicketType, List<Ticket>>> {
    private Model model;

    public TicketUUIdLoader( Model model) {
        this.model = model;
    }

    @Override
    protected Map<TicketType, List<Ticket>> call() throws Exception {
        if (isCancelled()) {
            System.out.println("Task was cancelled");
            return null;
        }
        List<Ticket> soldTickets = model.getSoldTickets();
        System.out.println("Sold tickets: " + soldTickets);
        Map<TicketType, List<Ticket>> ticketsWithUUId = model.getTicketsWithUUId(soldTickets);
        System.out.println("Tickets with UUID: " + ticketsWithUUId);
        return ticketsWithUUId;
    }
}
