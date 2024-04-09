package view.components.regularTickets.ticketManagement;

import be.User;
import exceptions.ErrorCode;
import exceptions.EventException;
import exceptions.ExceptionHandler;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import view.components.eventsPage.eventManagement.EventManagementController;
import view.components.main.Model;
import view.components.regularTickets.ticketDesign.TicketsDesignController;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class ManageTicketController implements Initializable {
    @FXML
    private VBox manageControl;
    private StackPane editWindow;
    private StackPane thirdLayout;
    private Model model;
    private int ticketId;
    private Service<List<User>> service;
    @FXML
    private EventManagementController eventManagementController;

    public ManageTicketController(StackPane editwindow, StackPane thirdLayout, Model model, EventManagementController eventManagementController) {
        this.model = model;
        this.editWindow = editwindow;
        this.thirdLayout= thirdLayout;
        this.eventManagementController = eventManagementController;
    }

    private void openEditWindow(MouseEvent event) {
        this.thirdLayout.setVisible(true);
        this.thirdLayout.setDisable(false);
        TicketsDesignController ticketsDesignController = new TicketsDesignController(editWindow, thirdLayout, eventManagementController, model);
        thirdLayout.getChildren().clear();
        thirdLayout.getChildren().add(ticketsDesignController.getRoot());
        //initializeOrUpdateService(this.ticketId);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        manageControl.addEventHandler(MouseEvent.MOUSE_CLICKED, this::openEditWindow);
    }

    /*
    public void initializeOrUpdateService(int newEventId) {
        if (service == null) {
            service = new Service<List<User>>() {
                @Override
                protected Task<List<User>> createTask() {
                    return model.executeData(ticketId);
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

     */

}
