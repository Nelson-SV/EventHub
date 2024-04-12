package ticketsPrinting;

import be.Ticket;
import javafx.scene.image.Image;

public class TicketWithQrCode {
    private Ticket ticket ;
    private Image qrcode;
    public TicketWithQrCode(Ticket ticket, Image qrcode) {
        this.ticket = ticket;
        this.qrcode = qrcode;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public Image getQrcode() {
        return qrcode;
    }

    public void setQrcode(Image qrcode) {
        this.qrcode = qrcode;
    }
}
