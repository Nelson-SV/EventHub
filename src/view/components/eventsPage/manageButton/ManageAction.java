package view.components.eventsPage.manageButton;
import exceptions.ErrorCode;
import exceptions.ExceptionHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import view.components.main.Model;

import java.io.IOException;

public class ManageAction extends VBox {
    private VBox manageControl;
    public ManageAction(StackPane editWindow,StackPane thirdLayout, int eventId, Model model) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ManageButton.fxml"));
        loader.setController(new ManageController(editWindow,thirdLayout,model,eventId));
        try {
            manageControl= loader.load();
            this.setAlignment(Pos.CENTER);
            this.getChildren().add(manageControl);
        } catch (IOException e) {
            ExceptionHandler.errorAlertMessage(ErrorCode.LOADING_FXML_FAILED.getValue());
        }
    }

}
