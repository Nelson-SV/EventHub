package view.components.regularTickets.ticketManagement;
import be.Ticket;
import exceptions.ErrorCode;
import exceptions.ExceptionHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import view.components.eventsPage.eventManagement.EventManagementController;
import view.components.main.Model;

import java.io.IOException;

public class ManageTicket extends VBox {
    private VBox manageControl;
    @FXML
    private EventManagementController eventManagementController;

    public ManageTicket(StackPane editWindow, StackPane thirdLayout, Model model, EventManagementController eventManagementController, Ticket ticket) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ManageTicketButton.fxml"));
        loader.setController(new ManageTicketController(editWindow,thirdLayout,model, eventManagementController, ticket));
        try {
            manageControl= loader.load();
            manageControl.setAlignment(Pos.CENTER);
            this.setAlignment(Pos.CENTER);
            this.getChildren().add(manageControl);
        } catch (IOException e) {
            ExceptionHandler.errorAlertMessage(ErrorCode.LOADING_FXML_FAILED.getValue());
        }
    }

}
