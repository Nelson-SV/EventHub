package ticketsPrinting;
import be.Ticket;
import be.TicketType;
import javafx.concurrent.Task;
import view.components.main.Model;
import java.util.List;
import java.util.Map;

public class TicketUUIdLoader extends Task<Map<TicketType, List<Ticket>>> {
    private List<Ticket> soldTickets;
    private Model model;

    public TicketUUIdLoader(List<Ticket> soldTickets, Model model) {
        this.soldTickets = soldTickets;
        this.model = model;
    }

    @Override
    protected Map<TicketType, List<Ticket>> call() throws Exception {

        if (isCancelled()) {
            return null;
        }
        return model.getTicketsWithUUId(soldTickets);
    }
}