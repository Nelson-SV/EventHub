package view.components.manageButton;
import exceptions.EventException;
import exceptions.ExceptionHandler;
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
    private Model model;

    public ManageController(StackPane editwindow, Model model) {
        this.model = model;
        this.editWindow = editwindow;
    }

    private void openEditWindow(MouseEvent event) {
        this.editWindow.setVisible(true);
        this.editWindow.setDisable(false);
        model.setSelectedEvent(Integer.parseInt(manageControl.getId()));
        EventManagementController manageEventController = new EventManagementController(editWindow);
        model.setCoordinatorsDisplayer(manageEventController);
        editWindow.getChildren().clear();
        editWindow.getChildren().add(manageEventController.getRoot());
        try {
            model.initializeEventCoordinators(Integer.parseInt(manageControl.getId()));
        } catch (EventException e) {
            ExceptionHandler.errorAlert(e);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        manageControl.addEventHandler(MouseEvent.MOUSE_CLICKED, this::openEditWindow);
    }
}
