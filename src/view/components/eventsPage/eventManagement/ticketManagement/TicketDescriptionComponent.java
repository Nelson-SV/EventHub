package view.components.eventsPage.eventManagement.ticketManagement;

import be.Ticket;
import exceptions.ErrorCode;
import exceptions.ExceptionHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class TicketDescriptionComponent extends HBox {

    private HBox ticketContainer;

    public TicketDescriptionComponent(Ticket ticket) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("TicketDescriptionComponent.fxml"));
        loader.setController(new TicketDescription(ticket));
        try {
            ticketContainer= loader.load();
            this.getChildren().add(ticketContainer);
        } catch (IOException e) {
            e.printStackTrace();
            ExceptionHandler.errorAlertMessage(ErrorCode.LOADING_FXML_FAILED.getValue());
        }
    }
}
