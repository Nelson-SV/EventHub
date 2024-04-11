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
            return null;
        }
        return model.getTicketsWithUUId(model.getSoldTickets());
    }
}
