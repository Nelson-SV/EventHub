package view.components.manageButton;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import view.components.events.CreateEventController;

import java.net.URL;
import java.util.ResourceBundle;


public class ManageController implements Initializable {
    @FXML
    private VBox manageControl;
    @FXML
    private StackPane editWindow;
    private CreateEventController createEventController;

    public ManageController(StackPane editwindow) {
        this.editWindow = editwindow;
    }

    private void openEditWindow(MouseEvent event) {
        this.editWindow.setVisible(true);
        this.editWindow.setDisable(false);
        this.createEventController = new CreateEventController(editWindow);
        editWindow.getChildren().add(createEventController.getRoot());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        manageControl.addEventHandler(MouseEvent.MOUSE_CLICKED, this::openEditWindow);

    }
}
