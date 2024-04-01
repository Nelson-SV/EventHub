package view.components.confirmationWindow;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import view.components.listeners.OperationHandler;
import view.utility.CommonMethods;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ConfirmationWindowController {
    private OperationHandler operationHandler;
    @FXML
    private Label confirmationTitle;
    @FXML
    private Label entityTitle;
    @FXML
    private Label eventStartDate;
    @FXML
    private Label eventLocation;
    @FXML
    private Label errorMessage;
    private final StackPane secondLayout;

    public ConfirmationWindowController(OperationHandler operationHandler, StackPane secondLayout) {
        this.secondLayout = secondLayout;
        this.operationHandler=operationHandler;

    }

    public Label getConfirmationTitle() {
        return confirmationTitle;
    }

    public void setConfirmationTitle(String confirmationTitle) {
        this.confirmationTitle.setText(confirmationTitle);
    }

    public Label getEntityTitle() {
        return entityTitle;
    }

    public void setEntityTitle(String entityTitle) {
        this.entityTitle.setText(entityTitle);
    }

    public Label getEventStartDate() {
        return eventStartDate;
    }

    public void setEventStartDate(LocalDate startDate) {
        this.eventStartDate.setText(startDate.format(DateTimeFormatter.ofPattern("yyyy:MM:dd")));
    }

    public Label getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation.setText(eventLocation);
    }
    public void setErrorMessage(String message){
        this.errorMessage.setText(message);
    }
    @FXML
    private void cancelOperation(ActionEvent event) {
        CommonMethods.closeWindow(event,this.secondLayout);
    }

    @FXML
    private void confirmOperation(ActionEvent event) {
        operationHandler.performOperation();
    }

}
