package view.components.manageButton;

import be.User;
import exceptions.ErrorCode;
import exceptions.EventException;
import exceptions.ExceptionHandler;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import view.components.eventManagement.EventManagementController;
import view.components.main.Model;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class ManageController implements Initializable {
    @FXML
    private VBox manageControl;
    private StackPane editWindow;
    private StackPane thirdLayout;
    private Model model;
    private int eventId;
    private Service<List<User>> service;

    public ManageController(StackPane editwindow,StackPane thirdLayout, Model model, int eventId) {
        this.model = model;
        this.editWindow = editwindow;
        this.eventId = eventId;
        this.thirdLayout= thirdLayout;
    }

    private void openEditWindow(MouseEvent event) {
        this.editWindow.setVisible(true);
        this.editWindow.setDisable(false);
        model.setSelectedEvent(this.eventId);
        EventManagementController manageEventController = new EventManagementController(editWindow,thirdLayout);
        manageEventController.setAlignment(Pos.CENTER);
        model.setCoordinatorsDisplayer(manageEventController);
        editWindow.getChildren().clear();
        editWindow.getChildren().add(manageEventController);
        initializeOrUpdateService(this.eventId);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        manageControl.addEventHandler(MouseEvent.MOUSE_CLICKED, this::openEditWindow);
    }
    public void initializeOrUpdateService(int newEventId) {
        if (service == null) {
            service = new Service<List<User>>() {
                @Override
                protected Task<List<User>> createTask() {
                    return model.executeData(eventId);
                }
            };
            service.setOnSucceeded(event -> {
                Platform.runLater(() -> {
                    model.getCoordinatorsDisplayer().setCoordinators(FXCollections.observableArrayList(service.getValue()));
                });
            });

            service.setOnFailed(event -> {
                Throwable cause = service.getException();
                ExceptionHandler.errorAlert(new EventException(cause.getMessage(), cause, ErrorCode.OPERATION_DB_FAILED));
            });
        }

        if (service.isRunning()) {
            service.cancel();
        }
        service.restart();
    }

}
