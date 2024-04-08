package view.components.eventsPage.eventManagement;

import be.DeleteOperation;
import be.User;
import exceptions.ErrorCode;
import exceptions.EventException;
import exceptions.ExceptionHandler;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import javafx.util.StringConverter;
import org.controlsfx.control.CheckComboBox;
import view.components.deleteEvent.DeleteButton;
import view.components.eventsPage.eventManagement.ticketManagement.TicketDescriptionComponent;
import view.components.eventsPage.manageButton.ManageAction;
import view.components.listeners.CoordinatorsDisplayer;
import view.components.loadingComponent.LoadingActions;
import view.components.loadingComponent.LoadingComponent;
import view.components.main.Model;
import view.components.regularTickets.deleteTicket.DeleteTicket;
import view.components.regularTickets.ticketDesign.TicketsDesignController;
import view.components.regularTickets.ticketManagement.ManageTicket;
import view.utility.CommonMethods;
import view.utility.EditEventValidator;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;


public class EventManagementController extends GridPane implements Initializable, CoordinatorsDisplayer {
    @FXML
    private MFXButton saveEdit;
    @FXML
    private MFXButton cancelEdit;
    @FXML
    private VBox ticketsVBox;
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


    public EventManagementController(StackPane secondaryLayout, StackPane thirdLayout) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("EventManager.fxml"));
        loader.setController(this);
        try {
            managementRoot = loader.load();
            this.secondaryLayout = secondaryLayout;
            this.thirdLayout = thirdLayout;
            this.getChildren().add(managementRoot);
        } catch (IOException e) {
            ExceptionHandler.errorAlertMessage(ErrorCode.LOADING_FXML_FAILED.getValue());
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            this.model = Model.getInstance();
        } catch (EventException e) {
            ExceptionHandler.errorAlert((EventException) e);
        }
        initializeEventTime(startTime, endTime);
        Platform.runLater(this::bindSelectedEventProprieties);
        EditEventValidator.initializeDateFormat(startDate);
        EditEventValidator.initializeDateFormat(endDate);
        EditEventValidator.addEventListeners(eventName, startDate, startTime, endDate, endTime, eventLocation);
        addToolTipsForDates();
        addDatesValidityChecker();
        displayTickets();
        cancelEdit.setOnAction((e) -> cancelEditOperation());
        saveEdit.setOnAction((e) -> saveOperation());
    }


    /**
     * add validity checker for the dates
     */
    private void addDatesValidityChecker() {
        EditEventValidator.addTimeTextEmptyChecker(startTime);
        EditEventValidator.addDateTextEmptyChecker(startDate);
        EditEventValidator.addTimeValidityChecker(endTime);
        EditEventValidator.addTimeValidityChecker(startTime);
        EditEventValidator.addDateValidityChecker(endDate);
    }

    /**
     * add tooltips for the dates
     */
    private void addToolTipsForDates() {
        EditEventValidator.addTimeToolTip(startTime);
        EditEventValidator.addDateToolTip(startDate);
        EditEventValidator.addDateToolTip(endDate);
        EditEventValidator.addTimeToolTip(endTime);
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
        bindSelectedEventWithDatesTextValues(startDate,model.getSelectedEvent().startDateProperty());
        bindSelectedEventWithDatesTextValues(endDate,model.getSelectedEvent().endDateProperty());
        bindSelectedEventWithTimeTextPropriety(startTime,model.getSelectedEvent().startTimeProperty());
        bindSelectedEventWithTimeTextPropriety(endTime,model.getSelectedEvent().endTimeProperty());
        eventName.textProperty().bindBidirectional(model.getSelectedEvent().nameProperty());
        startDate.valueProperty().bindBidirectional(model.getSelectedEvent().startDateProperty());
        endDate.valueProperty().bindBidirectional(model.getSelectedEvent().endDateProperty());
        startTime.valueProperty().bindBidirectional(model.getSelectedEvent().startTimeProperty());
        endTime.valueProperty().bindBidirectional(model.getSelectedEvent().endTimeProperty());
        eventDescription.textProperty().bindBidirectional(model.getSelectedEvent().descriptionProperty());
        eventLocation.textProperty().bindBidirectional(model.getSelectedEvent().locationProperty());
    }

    /**bind the selected event with the text propriety of the datepicker */
    private void bindSelectedEventWithDatesTextValues(MFXDatePicker date, SimpleObjectProperty<LocalDate> eventDate){
     DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        StringConverter<LocalDate> converter = new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return date.format(dateTimeFormatter);
                } else {
                    return "";
                }
            }
            @Override
            public LocalDate fromString(String string) {
                try {
                    return LocalDate.parse(string, dateTimeFormatter);
                } catch (DateTimeParseException e) {
                    return null;
                }
            }
        };
     date.textProperty().bindBidirectional(eventDate,converter);
    }


    /**
     * bind the selected event with the text propriety of the time values*/
    private void bindSelectedEventWithTimeTextPropriety(MFXComboBox<LocalTime> time, SimpleObjectProperty<LocalTime> eventTime){
        DateTimeFormatter  timeFormatter  = DateTimeFormatter.ofPattern("HH:mm");
        StringConverter<LocalTime> timeConverter =  new StringConverter<LocalTime>() {
            @Override
            public String toString(LocalTime timeValue) {
                if (timeValue != null) {

                    return timeValue.format(timeFormatter);
                } else {
                    return "";
                }
            }
            @Override
            public LocalTime fromString(String timeString) {
                try{
                    return LocalTime.parse(timeString,timeFormatter);
                }catch (DateTimeParseException e){
                    return null;
                }
            }
        };
             time.textProperty().bindBidirectional(eventTime,timeConverter);
    }

    /**
     * loads the tickets of the selected event
     */
    @FXML
    private void displayTickets() {
        if(ticketsVBox.getScene()==null){
            ticketsVBox.getChildren().clear();
            try {
                model.getTicketsForEvent(model.getSelectedEvent().getId()).values()
                        .forEach(t ->
                                {
                                    System.out.println("EventsManagement SL: " + secondaryLayout);
                                    System.out.println("EventsManagement TL: " + thirdLayout);
                                    ManageTicket manage = new ManageTicket(secondaryLayout,thirdLayout,model, this);
                                    DeleteTicket delete = new DeleteTicket(secondaryLayout,thirdLayout,model, DeleteOperation.DELETE_TICKET);
                                    TicketDescriptionComponent ticketDescriptionComponent = new TicketDescriptionComponent(t, manage, delete);
                                    ticketsVBox.getChildren().add(ticketDescriptionComponent);
                                }
                        );
            } catch (EventException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    private void addNewTicket() {
        showThirdLayout();
        System.out.println("EventsManagement OTHER SL: " + secondaryLayout);
        System.out.println("EventsManagement OTHER TL: " + thirdLayout);
        TicketsDesignController ticketsDesignController = new TicketsDesignController(secondaryLayout, thirdLayout, this, model);
        this.thirdLayout.getChildren().add(ticketsDesignController.getRoot());
    }

    private void showThirdLayout() {
        this.thirdLayout.getChildren().clear();
        this.thirdLayout.setDisable(false);
        this.thirdLayout.setVisible(true);
    }

    public VBox getTicketsVBox(){
        return ticketsVBox;
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
        boolean isEventValid = EditEventValidator.isEventValid(eventName, startDate, startTime, endDate, endTime, eventLocation);
        if (isEventValid) {
            if (model.isEditValid()) {
                initializeLoadingView();
                initializeService();
            }
        }
    }

    private void initializeLoadingView() {
        this.thirdLayout.getChildren().clear();
        loadingComponent = new LoadingComponent();
        loadingComponent.setAlignment(Pos.CENTER);
        this.thirdLayout.getChildren().add(loadingComponent);
        this.thirdLayout.setVisible(true);
        this.thirdLayout.setDisable(false);
    }

    private void closeLoader() {
        CommonMethods.closeWindow(thirdLayout);
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

    //TODO GROSU IONUT ANDREI throw exceptions from the DAO level all the way to the model,
    // throw the exception in the task creation, handle the exception in the servie failed,
    //return boolean , in order to update the map, that will update the events page.
    private void initializeService() {
        service = new Service<>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<>() {
                    @Override
                    protected Void call() throws Exception {
                        model.saveEditEventOperation(coordinators.getCheckModel().getCheckedItems());
                        return null;
                    }
                };
            }
        };
        service.setOnSucceeded((e) -> {
            Platform.runLater(() -> {
                loadingComponent.setAction(LoadingActions.SUCCES.getActionValue());
                PauseTransition pauseTransition = new PauseTransition(Duration.millis(500));
                pauseTransition.setOnFinished((ev) -> {
                    closeLoader();
                    cancelEditOperation();
                    Platform.runLater(()->model.getEventsDisplayer().displayEvents());
                });
                pauseTransition.play();
            });
        });
        service.setOnFailed((e) -> {
            Throwable cause = service.getException();
            ExceptionHandler.errorAlertMessage(cause.getMessage());
            Platform.runLater(() -> {
                loadingComponent.setAction(LoadingActions.FAIL.getActionValue());
                PauseTransition pauseTransition = new PauseTransition(Duration.millis(500));
                pauseTransition.setOnFinished((ev) -> {
                    closeLoader();
                });
                pauseTransition.play();
            });

        });
        service.restart();
    }

    public GridPane getRoot() {
        return managementRoot;
    }
}
