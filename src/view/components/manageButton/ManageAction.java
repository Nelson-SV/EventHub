package view.components.manageButton;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import view.components.manageButton.ManageController;

import java.io.IOException;

public class ManageAction extends VBox {
    @FXML
    private VBox manageControl;
    public ManageAction(StackPane editWindow) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ManageButton.fxml"));
        loader.setController(new ManageController(editWindow));
        try {
            manageControl= loader.load();
            this.getChildren().add(manageControl);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            // throw new EventException(e.getMessage(), e.getCause(), ErrorCode.OPERATION_FAILED, Level.SEVERE);
        }
    }
    public VBox getManageControl() {
        return manageControl;
    }
}
