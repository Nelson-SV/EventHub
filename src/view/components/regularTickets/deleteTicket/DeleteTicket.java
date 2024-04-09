package view.components.regularTickets.deleteTicket;

import be.DeleteOperation;
import be.Ticket;
import exceptions.ErrorCode;
import exceptions.ExceptionHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import view.components.eventsPage.eventManagement.EventManagementController;
import view.components.main.CommonModel;
import view.components.main.Model;

import java.io.IOException;

public class DeleteTicket extends VBox {
    @FXML
    private VBox deleteOperation;
    private DeleteTicketController deleteTicketController;

    public DeleteTicket(StackPane secondaryLayout, StackPane thirdLayout, Model model, DeleteOperation deleteOperationPerformed, Ticket ticket, EventManagementController eventManagementController) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("DeleteTicketButton.fxml"));
        deleteTicketController = new DeleteTicketController(secondaryLayout, thirdLayout, model, deleteOperationPerformed, ticket, eventManagementController);
        loader.setController(deleteTicketController);
        try {
            this.deleteOperation = loader.load();
            deleteOperation.setAlignment(Pos.CENTER);
            this.setAlignment(Pos.CENTER);
            this.getChildren().add(this.deleteOperation);
        } catch (IOException e) {
            ExceptionHandler.errorAlertMessage(ErrorCode.LOADING_FXML_FAILED.getValue());
        }
    }

}
