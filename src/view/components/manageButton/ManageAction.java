package view.components.manageButton;
import exceptions.ErrorCode;
import exceptions.ExceptionHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import view.components.main.Model;

import java.io.IOException;

public class ManageAction extends VBox {
    private VBox manageControl;
    public ManageAction(StackPane editWindow, int eventId, Model model) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ManageButton.fxml"));
        loader.setController(new ManageController(editWindow,model));
        try {
            manageControl= loader.load();
            this.getChildren().add(manageControl);
            this.setId(eventId+"");
        } catch (IOException e) {
            ExceptionHandler.erorrAlertMessage(ErrorCode.LOADING_FXML_FAILED.getValue());

        }
    }

}
