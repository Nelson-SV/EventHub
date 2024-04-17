package ticketsPrinting;
import be.Ticket;
import be.TicketType;
import view.components.main.Model;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class TicketImageSaveFacade {
    private TicketUUIdLoader uuIdLoader;
    private Model model;

    public TicketImageSaveFacade(Model model) {
        this.model = model;
    }

    public void printTicket(){
       saveTicketImage();
    }

    private void saveTicketImage(){
        CompletableFuture.supplyAsync(() -> {
            TicketUUIdLoader  ticketUUIdLoader = new TicketUUIdLoader(model);
            try {
                Map<TicketType, List<Ticket>> result = ticketUUIdLoader.call();
                return result;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).thenApply(soldTicketsWithUUID -> {
            Map<TicketType,List<TicketWithQrCode>> ticketsWithQrCode =  new HashMap<>();
            TiketsQrCodeAtacher tiketsQrCodeAtacher= new TiketsQrCodeAtacher(soldTicketsWithUUID.get(TicketType.NORMAL));
            try {
                ticketsWithQrCode.put(TicketType.NORMAL,tiketsQrCodeAtacher.call());
                return ticketsWithQrCode;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).thenApply(createdTicketsWithQRCode -> {
            TicketsSnapshot ticketsSnapshot = new TicketsSnapshot(createdTicketsWithQRCode,model.getTheCurrentCustomer(),model.getCurrentEventSell());
            try {
                return ticketsSnapshot.call();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).thenAccept((ticketSnapshots)->{
            TicketImageSaver ticketSaver = new TicketImageSaver(ticketSnapshots);
            try {
                ticketSaver.call();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).exceptionally(ex -> {
            return null;
        });
      }
}
