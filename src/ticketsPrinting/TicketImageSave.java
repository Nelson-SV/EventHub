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
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private ExecutorService consumerExecutor = Executors.newSingleThreadExecutor();
    private BlockingQueue<Map<TicketType, List<Ticket>>> queue = new LinkedBlockingQueue<>(10);

    public TicketImageSave(Model model) {
        this.model = model;
    }

    public void saveSoldTicketsImages() {
        initializeUUIDRetrieval();
        initiateConsumerTask();
    }

    private void initiateConsumerTask() {
        Runnable consumerTask = () -> {
            try {
                while (true) {
                    Map<TicketType, List<Ticket>> soldTicketsWithUUID = queue.take();
//                    Platform.runLater(() -> {
//                        try {
//                            TicketsSnapshot ticketImageSnapshot = new TicketsSnapshot(soldTicketsWithUUID, model.getTheCurrentCustomer(), model.getCurrentEventSell());
//                            ticketImageSnapshot.createTicketWritableImages();
//                        } catch (EventException e) {
//                            e.printStackTrace();
//                        }
//                    });


                    System.out.println(soldTicketsWithUUID + "from consumer");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };
        consumerExecutor.execute(consumerTask);
    }

    private synchronized void initializeUUIDRetrieval() {
        uuIdLoader = new TicketUUIdLoader(model);
        uuIdLoader.setOnSucceeded((event) -> {
            System.out.println(model.getSoldTickets() + "sold tickets");
            System.out.println(uuIdLoader.getValue()+ "uuid");
            try {
                queue.put(uuIdLoader.getValue());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        uuIdLoader.setOnFailed((event) -> {
            ExceptionHandler.errorAlertMessage(uuIdLoader.getException().getMessage());
            System.out.println(uuIdLoader.getValue()+"uuid");});
        executorService.execute(uuIdLoader);
    }

    public void shutdownExecutors() {
        executorService.shutdown();
        consumerExecutor.shutdown();
    }
}
