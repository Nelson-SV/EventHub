package view.components.confirmationWindow;
import exceptions.ErrorCode;
import exceptions.ExceptionHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import view.components.listeners.OperationHandler;
import java.io.IOException;
import java.time.LocalDate;


public class ConfirmationWindow extends VBox {
    @FXML
    private VBox confirmationWindow;
    private ConfirmationWindowController confirmationWindowController;

    public ConfirmationWindow(OperationHandler operationHandler, StackPane secondaryLayout) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ConfirmationWindow.fxml"));
        confirmationWindowController = new ConfirmationWindowController(operationHandler, secondaryLayout);
        loader.setController(confirmationWindowController);
        try {
            confirmationWindow = loader.load();
            this.getChildren().add(confirmationWindow);
            this.setAlignment(Pos.CENTER);
        } catch (IOException e) {
            ExceptionHandler.erorrAlertMessage(ErrorCode.LOADING_FXML_FAILED.getValue());
        }
    }

    public Label getConfirmationTitle() {
        return confirmationWindowController.getConfirmationTitle();
    }

    public void setConfirmationTitle(String confirmationTitle) {
        confirmationWindowController.setConfirmationTitle(confirmationTitle);
    }

    public Label getEntityTitle() {
        return confirmationWindowController.getEntityTitle();
    }

    public void setEntityTitle(String entityTitle) {
        confirmationWindowController.setEntityTitle(entityTitle);
    }

    public Label getEventStartDate() {
        return confirmationWindowController.getEventStartDate();
    }

    public void setEventStartDate(LocalDate startDate) {
        confirmationWindowController.setEventStartDate(startDate);
    }

    public Label getEventLocation() {
        return confirmationWindowController.getEventLocation();
    }

    public void setEventLocation(String eventLocation) {
        confirmationWindowController.setEventLocation(eventLocation);
    }
    public void setErrorMessage(String errorMessage){
        confirmationWindowController.setErrorMessage(errorMessage);
    }

}
