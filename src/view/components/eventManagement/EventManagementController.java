package view.components.eventManagement;

import be.User;
import exceptions.ErrorCode;
import exceptions.EventException;
import exceptions.ExceptionHandler;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.WindowEvent;
import view.components.listeners.CoordinatorsDisplayer;
import view.components.main.Model;

import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.util.List;
import java.util.ResourceBundle;

public class EventManagementController implements Initializable, CoordinatorsDisplayer {
    @FXML
    private MFXButton saveEdit;
    @FXML
    private MFXButton cancelEdit;
    @FXML
    private MFXFilterComboBox<User> coordinators;
    @FXML
    private TextArea eventLocation;
    @FXML
    private TextArea eventDescription;
    @FXML
    private MFXComboBox<LocalTime> endTime;
    @FXML
    private MFXDatePicker endDate;
    @FXML
    private MFXComboBox<LocalTime> startTime;
    @FXML
    private MFXDatePicker startDate;
    @FXML
    private TextField eventName;
    //TODO put the generateTimeOptions into an utility class
    @FXML
    private GridPane managementRoot;
    private Model model;
    private StackPane secondaryLayout;

    public GridPane getRoot() {
        return managementRoot;
    }


    //TODO initialize the coordinators comboBox with the user Name and checkBox.

    public EventManagementController(StackPane secondaryLayout) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("EventManager.fxml"));
        loader.setController(this);
        try {
            managementRoot = loader.load();
            this.secondaryLayout = secondaryLayout;
        } catch (IOException e) {
            e.printStackTrace();
            ExceptionHandler.erorrAlertMessage(ErrorCode.LOADING_FXML_FAILED.getValue());
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            this.model = Model.getInstance();
        } catch (EventException e) {
            ExceptionHandler.errorAlert(e);
        }

        initializeEventTime(startTime,endTime);
        Platform.runLater(this::bindSelectedEventProprieties);
       cancelEdit.setOnAction((e)->cancelEditOperation());

    }


    /**
     * initialize the event time variables
     */
    private ObservableList<LocalTime> generateTimeOptions() {
        ObservableList<LocalTime> timeOptions = FXCollections.observableArrayList();
        LocalTime time = LocalTime.of(0, 0);
        while (time.isBefore(LocalTime.of(23, 0))) {
            timeOptions.add(time);
            time = time.plusHours(1);
        }
        return timeOptions;
    }

    /**
     * initialize the event time variables
     */
    private void initializeEventTime(MFXComboBox<LocalTime> startTime, MFXComboBox<LocalTime> endTime) {
        startTime.setItems(generateTimeOptions());
        endTime.setItems(generateTimeOptions());
    }


    /**
     * binds the selected event to the eventManagementPage
     */
    private void bindSelectedEventProprieties() {
        eventName.textProperty().bindBidirectional(model.getSelectedEvent().nameProperty());
        startDate.valueProperty().bindBidirectional(model.getSelectedEvent().startDateProperty());
        endDate.valueProperty().bindBidirectional(model.getSelectedEvent().endDateProperty());
        startTime.valueProperty().bindBidirectional(model.getSelectedEvent().startTimeProperty());
        endTime.valueProperty().bindBidirectional(model.getSelectedEvent().endTimeProperty());
        eventDescription.textProperty().bindBidirectional(model.getSelectedEvent().descriptionProperty());
        eventLocation.textProperty().bindBidirectional(model.getSelectedEvent().locationProperty());
    }


    /**
     * cancel the event editing
     */
    private void cancelEditOperation() {
        this.secondaryLayout.getChildren().clear();
        this.secondaryLayout.setDisable(true);
        this.secondaryLayout.setVisible(false);
    }

    @Override
    public void setCoordinators(ObservableList<User> users) {
        coordinators.setItems(users);
    }
}
