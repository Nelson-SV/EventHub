package view.components.deleteEvent;
import be.DeleteOperation;
import exceptions.ErrorCode;
import exceptions.ExceptionHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import view.components.main.CommonModel;
import java.io.IOException;

public class DeleteButton extends VBox {
    @FXML
    private VBox deleteOperation;
    private DeleteButtonController deleteButtonController;

    public DeleteButton(StackPane secondaryLayout, StackPane thirdLayout, CommonModel model, int eventId, DeleteOperation deleteOperationPerformed) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("DeleteButton.fxml"));
        deleteButtonController = new DeleteButtonController(secondaryLayout, thirdLayout, model, eventId,deleteOperationPerformed);
        loader.setController(deleteButtonController);
        try {
            this.deleteOperation = loader.load();
            this.getChildren().add(this.deleteOperation);
        } catch (IOException e) {
            ExceptionHandler.errorAlertMessage(ErrorCode.LOADING_FXML_FAILED.getValue());
        }
    }

//TODO discuss if we need them otherwise delete them

//    public void setConfirmationWindowMessage(String message) {
//        deleteButtonController.setConfirmationWindowMessage(message);
//    }
//
//    public void setConfirmationEntityTitle(String entityTitle) {
//        deleteButtonController.setConfirmationEntityTitle(entityTitle);
//    }
//
//    public void setConfirmationWindowEntityStartDate(LocalDate startDate) {
//        deleteButtonController.setConfirmationWindowEntityStartDate(startDate);
//    }
//
//    public void setConfirmationWindowEntityLocation(String location) {
//        deleteButtonController.setConfirmationWindowEntityLocation(location);
//    }
}
