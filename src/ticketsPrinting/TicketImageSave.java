package ticketsPrinting;

import be.Ticket;
import be.TicketType;
import exceptions.EventException;
import exceptions.ExceptionHandler;
import view.components.main.Model;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TicketImageSave {
    private TicketUUIdLoader uuIdLoader;
    private Model model;
    private List<Ticket> soldTickets;
private  TicketsSnapshot ticketImageSnapshot;
    private Map<TicketType, List<Ticket>> soldTicketsWithUUID;

    public TicketImageSave(List<Ticket> soldTickets, Model model) {
        this.soldTickets = soldTickets;
        this.model = model;
    }
    public synchronized void saveSoldTicketsImages() {
        initializeUUIDRetrieval();
    }

    private synchronized void initializeUUIDRetrieval() {
        uuIdLoader = new TicketUUIdLoader(soldTickets, model);
        uuIdLoader.setOnSucceeded((event) -> {
            soldTicketsWithUUID = uuIdLoader.getValue();
            ticketImageSnapshot = new TicketsSnapshot(soldTicketsWithUUID,model.getTheCurrentCustomer(),model.getCurrentEventSell());
            try {
                ticketImageSnapshot.createTicketWritableImages();
            } catch (EventException e) {
                e.printStackTrace();
            }
        });
        uuIdLoader.setOnFailed((event) -> {
            ExceptionHandler.errorAlertMessage(uuIdLoader.getException().getMessage());
        });

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(uuIdLoader);
        executorService.shutdown();
    }
}
