package view.components.regularTickets.deleteTicket;

import be.DeleteOperation;
import exceptions.ErrorCode;
import exceptions.ExceptionHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import view.components.main.CommonModel;

import java.io.IOException;

public class DeleteTicket extends VBox {
    @FXML
    private VBox deleteOperation;
    private DeleteTicketController deleteTicketController;

    public DeleteTicket(StackPane secondaryLayout, StackPane thirdLayout, CommonModel model, DeleteOperation deleteOperationPerformed) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("DeleteTicketButton.fxml"));
        deleteTicketController = new DeleteTicketController(secondaryLayout, thirdLayout, model,deleteOperationPerformed);
        loader.setController(deleteTicketController);
        try {
            this.deleteOperation = loader.load();
            this.getChildren().add(this.deleteOperation);
        } catch (IOException e) {
            ExceptionHandler.errorAlertMessage(ErrorCode.LOADING_FXML_FAILED.getValue());
        }
    }

}
