package view.components.eventsPage.eventManagement.ticketManagement;

import be.Ticket;
import exceptions.ErrorCode;
import exceptions.ExceptionHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import view.components.regularTickets.deleteTicket.DeleteTicket;
import view.components.regularTickets.ticketManagement.ManageTicket;

import java.io.IOException;

public class TicketDescriptionComponent extends HBox {

    @FXML
    private HBox ticketContainer;
    @FXML
    private Ticket ticket;


    public TicketDescriptionComponent(Ticket ticket, ManageTicket manage, DeleteTicket deleteButton) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("TicketDescriptionComponent.fxml"));
        loader.setController(new TicketDescription(ticket, manage, deleteButton));
        this.ticket = ticket;
        try {
            ticketContainer= loader.load();
            ticketContainer.setAlignment(Pos.BOTTOM_CENTER);
            this.setAlignment(Pos.BOTTOM_CENTER);
            this.getChildren().add(ticketContainer);
        } catch (IOException e) {
            e.printStackTrace();
            ExceptionHandler.errorAlertMessage(ErrorCode.LOADING_FXML_FAILED.getValue());
        }
    }

    public Ticket getTicket(){
        return ticket;
    }
}
