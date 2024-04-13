package ticketsPrinting;
import be.Ticket;
import javafx.concurrent.Task;
import java.util.ArrayList;
import java.util.List;

public class TiketsQrCodeAtacher  extends Task<List<TicketWithQrCode>> {
    private List<Ticket> ticketsWithoutQrCode;
    public TiketsQrCodeAtacher(List<Ticket> ticketsWithoutQrCode) {
        this.ticketsWithoutQrCode = ticketsWithoutQrCode;
    }
    @Override
    protected List<TicketWithQrCode> call() throws Exception {
        if(isCancelled()){
            return null;
        }
      List<TicketWithQrCode> ticketsWithQrCode = new ArrayList<>();
        for(Ticket ticket:ticketsWithoutQrCode){
            TicketWithQrCode ticketWithQrCode = new TicketWithQrCode(ticket,QrCodeGenerator.generateQRCodeImage(ticket.getUUID(),100,100));
        ticketsWithQrCode.add(ticketWithQrCode);
        }
        return ticketsWithQrCode;
    }
}
