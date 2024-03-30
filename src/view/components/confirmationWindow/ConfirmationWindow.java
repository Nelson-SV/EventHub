package view.components.confirmationWindow;

import exceptions.ErrorCode;
import exceptions.EventException;
import exceptions.ExceptionHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import view.components.listeners.OperationHandler;

import java.beans.EventHandler;
import java.io.IOException;

public class ConfirmationWindow extends VBox {
    @FXML
    private VBox confirmationWindow;

    public ConfirmationWindow(OperationHandler operationHandler, StackPane secondaryLayout) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ConfirmationWindow.fxml"));
        loader.setController(new ConfirmationWindowController(operationHandler,secondaryLayout));
        try {
            confirmationWindow=loader.load();
            this.getChildren().add(confirmationWindow);
        } catch (IOException e) {
            ExceptionHandler.erorrAlertMessage(ErrorCode.LOADING_FXML_FAILED.getValue());
        }
    }
}
