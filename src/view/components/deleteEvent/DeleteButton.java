package view.components.deleteEvent;
import exceptions.ErrorCode;
import exceptions.ExceptionHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import view.components.main.CommonModel;
import view.components.main.Model;

import java.io.IOException;

public class DeleteButton extends VBox {
    @FXML
    private VBox deleteOperation;

    public DeleteButton(StackPane secondaryLayout, StackPane thirdLayout, CommonModel model, int eventId) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("DeleteButton.fxml"));
        loader.setController(new DeleteButtonController(secondaryLayout, thirdLayout, model, eventId));
        try {
            deleteOperation = loader.load();
            this.getChildren().add(deleteOperation);
        } catch (IOException e) {
            ExceptionHandler.erorrAlertMessage(ErrorCode.LOADING_FXML_FAILED.getValue());
        }

    }
}
