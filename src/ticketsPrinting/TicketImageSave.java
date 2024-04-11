package ticketsPrinting;
import be.Ticket;
import be.TicketType;
import exceptions.EventException;
import exceptions.ExceptionHandler;
import javafx.application.Platform;
import view.components.main.Model;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class TicketImageSave {
    private TicketUUIdLoader uuIdLoader;
    private Model model;
    private List<Ticket> soldTickets;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private ExecutorService consumerExecutor = Executors.newSingleThreadExecutor();
    private BlockingQueue<Map<TicketType, List<Ticket>>> queue = new LinkedBlockingQueue<>(10);

    public TicketImageSave(List<Ticket> soldTickets, Model model) {
        this.soldTickets = soldTickets;
        this.model = model;
        initiateConsumerTask();
    }

    public synchronized void saveSoldTicketsImages() {
        initializeUUIDRetrieval();
    }

    private void initiateConsumerTask() {
        Runnable consumerTask = () -> {
            try {
                while (true) {
                    Map<TicketType, List<Ticket>> soldTicketsWithUUID = queue.take();
                    Platform.runLater(() -> {
                        try {
                            TicketsSnapshot ticketImageSnapshot = new TicketsSnapshot(soldTicketsWithUUID, model.getTheCurrentCustomer(), model.getCurrentEventSell());
                            ticketImageSnapshot.createTicketWritableImages();
                        } catch (EventException e) {
                            e.printStackTrace();
                        }
                    });
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };
        consumerExecutor.execute(consumerTask);
    }

    private synchronized void initializeUUIDRetrieval() {
        uuIdLoader = new TicketUUIdLoader(soldTickets, model);
        uuIdLoader.setOnSucceeded((event) -> {
            System.out.println(soldTickets + " sold tickets");
            System.out.println(uuIdLoader.getValue()+"uuid");
            try {
                queue.put(uuIdLoader.getValue());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        uuIdLoader.setOnFailed((event) -> {ExceptionHandler.errorAlertMessage(uuIdLoader.getException().getMessage());
            System.out.println(uuIdLoader.getValue()+"uuid");});

        executorService.execute(uuIdLoader);
    }

    public void shutdownExecutors() {
        executorService.shutdown();
        consumerExecutor.shutdown();
    }
}
