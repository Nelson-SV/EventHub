package view.components.eventManagement;

import be.User;
import exceptions.ErrorCode;
import exceptions.EventException;
import exceptions.ExceptionHandler;
import exceptions.TicketException;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import org.controlsfx.control.CheckComboBox;
import view.components.listeners.CoordinatorsDisplayer;
import view.components.loadingComponent.LoadingActions;
import view.components.loadingComponent.LoadingComponent;
import view.components.main.Model;
import view.utility.EditEventValidator;
import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.util.ResourceBundle;


public class EventManagementController implements Initializable, CoordinatorsDisplayer {
    @FXML
    private MFXButton saveEdit;
    @FXML
    private MFXButton cancelEdit;

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
    @FXML
    private GridPane managementRoot;
    @FXML
    private ComboBox<User> normal;
    @FXML
    CheckComboBox<User> coordinators;
    private Model model;
 @FXML
    private TextArea invalidInput;
    private StackPane secondaryLayout, thirdLayout;
    private Service<Void> service;

    private LoadingComponent loadingComponent;


    //TODO initialize the coordinators comboBox with the user Name and checkBox.

    public EventManagementController(StackPane secondaryLayout, StackPane thirdLayout) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("EventManager.fxml"));
        loader.setController(this);
        try {
            managementRoot = loader.load();
            this.secondaryLayout = secondaryLayout;
            this.thirdLayout = thirdLayout;
        } catch (IOException e) {
            ExceptionHandler.erorrAlertMessage(ErrorCode.LOADING_FXML_FAILED.getValue());
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            this.model = Model.getInstance();
        } catch (EventException | TicketException e) {
            ExceptionHandler.errorAlert((EventException) e);
        }
        initializeEventTime(startTime, endTime);
        Platform.runLater(this::bindSelectedEventProprieties);
        EditEventValidator.addEventListeners(eventName,startDate,startTime,eventLocation);
        EditEventValidator.addTimeToolTip(startTime);
        EditEventValidator.addTimeValidityChecker(startTime);
        EditEventValidator.addDateToolTip(startDate);
        EditEventValidator.addDateValidityChecker(startDate);
        cancelEdit.setOnAction((e) -> cancelEditOperation());
        saveEdit.setOnAction((e) -> saveOperation());
    }


    /**
     * initialize the event time variables
     */
    private ObservableList<LocalTime> generateTimeOptions() {
        ObservableList<LocalTime> timeOptions = FXCollections.observableArrayList();
        LocalTime time = LocalTime.of(0, 0);
        while (time.isBefore(LocalTime.of(23, 0))) {
            time = time.plusHours(1);
            timeOptions.add(time);
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

    private void saveOperation() {
        if(EditEventValidator.isEventValid(eventName,startDate,startTime,eventLocation,invalidInput)){
            initializeLoadingView();
            Platform.runLater(this::initializeService);
        }
    }

    private void initializeLoadingView() {
        this.thirdLayout.getChildren().clear();
        loadingComponent = new LoadingComponent();
        this.thirdLayout.getChildren().add(loadingComponent);
        this.thirdLayout.setVisible(true);
        this.thirdLayout.setDisable(false);
    }

    private void closeLoader() {
        this.thirdLayout.getChildren().clear();
        this.thirdLayout.setVisible(false);
        this.thirdLayout.setDisable(true);
    }

    @Override
    public void setCoordinators(ObservableList<User> users) {
        coordinators.getItems().setAll(users);
        coordinators.setTitle("Assigned collaborators");
        coordinators.setShowCheckedCount(true);
        coordinators.getCheckModel().getCheckedItems().addListener((ListChangeListener<User>) c -> {
            coordinators.getCheckModel().getCheckedItems().forEach(e -> System.out.println(e.getUserId()));
        });
    }

    private void initializeService() {
        service = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {

                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        return null;
                    }
                };
            }

            ;
        };
        service.setOnSucceeded((e) -> {
            this.loadingComponent.setAction(LoadingActions.SUCCES.getActionValue());
            Platform.runLater(() -> {
                PauseTransition pauseTransition = new PauseTransition(Duration.millis(1000));
                pauseTransition.setOnFinished((ev) -> {
                    closeLoader();
                    cancelEditOperation();
                });
                pauseTransition.play();
            });
        });

        service.setOnFailed((e) -> {
            Throwable cause = service.getException();
            ExceptionHandler.erorrAlertMessage(cause.getMessage());
            Platform.runLater(this::closeLoader);
        });
        service.restart();
    }

    public GridPane getRoot() {
        return managementRoot;
    }
}
