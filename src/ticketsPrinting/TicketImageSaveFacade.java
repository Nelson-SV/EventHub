package ticketsPrinting;
import be.Ticket;
import be.TicketType;
import exceptions.ExceptionHandler;
import view.components.main.Model;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class TicketImageSaveFacade {
    private TicketUUIdLoader uuIdLoader;
    private Model model;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private ExecutorService consumerExecutor = Executors.newSingleThreadExecutor();
    private BlockingQueue<Map<TicketType, List<Ticket>>> queue = new LinkedBlockingQueue<>(10);

    public TicketImageSaveFacade(Model model) {
        this.model = model;
    }


    public void saveTicketImage(){
        CompletableFuture.supplyAsync(() -> {
            System.out.println("Starting Task 1");
            TicketUUIdLoader  ticketUUIdLoader = new TicketUUIdLoader(model);
            try {
                Map<TicketType, List<Ticket>> result = ticketUUIdLoader.call();
                System.out.println("Result of Task 1: " + result);
                return result;
            } catch (Exception e) {
                System.out.println("Exception in Task 1: " + e.getMessage());
                throw new RuntimeException(e);
            }
        }).thenApply(soldTicketsWithUUID -> {
            System.out.println("Starting Task 2");
            Map<TicketType,List<TicketWithQrCode>> ticketswithqrCode =  new HashMap<>();
            TiketsQrCodeAtacher tiketsQrCodeAtacher= new TiketsQrCodeAtacher(soldTicketsWithUUID.get(TicketType.NORMAL));
            try {
                ticketswithqrCode.put(TicketType.NORMAL,tiketsQrCodeAtacher.call());
                return ticketswithqrCode;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).thenApply(createdTicketsWithQRCode -> {
            System.out.println("Starting Task 3");
            TicketsSnapshot ticketsSnapshot = new TicketsSnapshot(createdTicketsWithQRCode,model.getTheCurrentCustomer(),model.getCurrentEventSell());
            try {
                return ticketsSnapshot.call();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).thenAccept((ticketSnapshots)->{
            System.out.println("Starting Task 4");
            TicketImageSaver ticketSaver = new TicketImageSaver(ticketSnapshots);
            try {
                ticketSaver.call();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).exceptionally(ex -> {
            System.out.println("Exception occurred: " + ex.getMessage());
            ex.printStackTrace();
            return null;
        });










//        CompletableFuture.supplyAsync(() -> {
//            TicketUUIdLoader  ticketUUIdLoader = new TicketUUIdLoader(model);
//            // Task 1: Initialize UUID Retrieval
//            try {
//                return ticketUUIdLoader.get();
//            } catch (InterruptedException | ExecutionException e) {
//                throw new RuntimeException(e);
//            }
//        }).thenApply(soldTicketsWithUUID -> {
//            System.out.println(soldTicketsWithUUID.size() + "withuuid");
//            Map<TicketType,List<TicketWithQrCode>> ticketswithqrCode =  new HashMap<>();
//            TiketsQrCodeAtacher tiketsQrCodeAtacher= new TiketsQrCodeAtacher(soldTicketsWithUUID.get(TicketType.NORMAL));
//            // Task 2: Create Ticket Images
//            try {
//                ticketswithqrCode.put(TicketType.NORMAL,tiketsQrCodeAtacher.get());
//                return ticketswithqrCode;
//            } catch (InterruptedException | ExecutionException e) {
//                throw new RuntimeException(e);
//            }
//        }).thenApply(createdTicketsWithQRCode -> {
//            System.out.println(createdTicketsWithQRCode.size() + "withqrd");
//            TicketsSnapshot ticketsSnapshot = new TicketsSnapshot(createdTicketsWithQRCode,model.getTheCurrentCustomer(),model.getCurrentEventSell());
//            try {
//                return ticketsSnapshot.get();
//            } catch (InterruptedException | ExecutionException e) {
//                throw new RuntimeException(e);
//            }
//        }).thenAccept((ticketSnapshots)->{
//            System.out.println(ticketSnapshots.size() + "witsnap");
//            TicketImageSaver ticketSaver = new TicketImageSaver(ticketSnapshots);
//            try {
//                ticketSaver.get();
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        }).exceptionally(ex -> {
//            // Handle exception
//            ex.printStackTrace();
//            return null;
//        });
    }



    public void saveSoldTicketsImages() {
        initializeUUIDRetrieval();
       // initiateConsumerTask();
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
            for (Ticket ticket :uuIdLoader.getValue().get(TicketType.NORMAL)){
                System.out.println(ticket.getTicketType()+ticket.getColor()+ticket.getUUID()+ticket.getTicketPrice()+ "from consumer");
            }
            try {
                queue.put(uuIdLoader.getValue());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        uuIdLoader.setOnFailed((event) -> {
            ExceptionHandler.errorAlertMessage(uuIdLoader.getException().getMessage());
            System.out.println(uuIdLoader.getValue()+"uuid failed");});
        executorService.execute(uuIdLoader);
    }

    public void shutdownExecutors() {
        executorService.shutdown();
        consumerExecutor.shutdown();
    }
}
