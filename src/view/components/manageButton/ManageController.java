package view.components.manageButton;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import view.components.eventManagement.EventManagementController;
import view.components.events.CreateEventController;
import view.components.main.Model;

import java.net.URL;
import java.util.ResourceBundle;


public class ManageController implements Initializable {
    @FXML
    private VBox manageControl;
    private StackPane editWindow;
    private EventManagementController manageEventController;
    private Model model;

    public ManageController(StackPane editwindow, Model model) {
        this.model = model;
        this.editWindow = editwindow;
    }

    private void openEditWindow(MouseEvent event) {
        this.editWindow.setVisible(true);
        this.editWindow.setDisable(false);
        this.manageEventController = new EventManagementController(model);
        editWindow.getChildren().add(manageEventController.getRoot());
        model.setSelectedEvent(Integer.parseInt(manageControl.getId()));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        manageControl.addEventHandler(MouseEvent.MOUSE_CLICKED, this::openEditWindow);
    }
}
