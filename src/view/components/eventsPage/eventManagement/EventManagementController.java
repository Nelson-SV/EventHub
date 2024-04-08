package view.components.eventsPage.eventManagement;
import be.EventInvalidResponse;
import be.User;
import exceptions.ErrorCode;
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
import javafx.util.Duration;
import javafx.util.StringConverter;
import org.controlsfx.control.CheckComboBox;
import view.components.listeners.CoordinatorsDisplayer;
import view.components.loadingComponent.LoadingActions;
import view.components.loadingComponent.LoadingComponent;
import view.components.main.Model;
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
    private TextArea eventLocation;
    @FXML
    private TextArea eventDescription;
    @FXML
    private MFXComboBox<String> endTime;
    @FXML
    private MFXDatePicker endDate;
    @FXML
    private MFXComboBox<String> startTime;
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


    public EventManagementController(Model model, StackPane secondaryLayout, StackPane thirdLayout) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("EventManager.fxml"));
        loader.setController(this);
        try {
            managementRoot = loader.load();
            this.secondaryLayout = secondaryLayout;
            this.thirdLayout = thirdLayout;
            this.model = model;
            this.getChildren().add(managementRoot);
        } catch (IOException e) {
            ExceptionHandler.errorAlertMessage(ErrorCode.LOADING_FXML_FAILED.getValue());
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeEventTime(startTime, endTime);
        Platform.runLater(this::bindSelectedEventProprieties);
        EditEventValidator.initializeDateFormat(startDate);
        EditEventValidator.initializeDateFormat(endDate);
        addInputValuesListeners();
        addToolTipsForDates();
        addDatesValidityChecker();
        cancelEdit.setOnAction((e) -> cancelEditOperation());
        saveEdit.setOnAction((e) -> saveOperation());
    }


    private void addInputValuesListeners() {
        EditEventValidator.addEventNameValueListener(eventName);
        EditEventValidator.addEventLocationValueListener(eventLocation);
    }


    //TODO create event listeners for start an den time values string

    /**
     * add validity checker for the dates
     */
    private void addDatesValidityChecker() {
        EditEventValidator.addDateTextEmptyChecker(startDate);
        EditEventValidator.addStartTimeValidityCheckerString(startTime);
        EditEventValidator.addEndTimeValidityCheckerString(endTime);
        EditEventValidator.addEndDateTextValidityChecker(endDate);
    }

    /**
     * add tooltips for the dates
     */
    private void addToolTipsForDates() {
        EditEventValidator.addTimeStringToolTip(startTime);
        EditEventValidator.addDateToolTip(startDate);
        EditEventValidator.addDateToolTip(endDate);
        EditEventValidator.addTimeStringToolTip(endTime);
    }

    /**
     * initialize the event time variables
     */
    private ObservableList<String> generateTimeValues() {
        ObservableList<String> timeValues = FXCollections.observableArrayList();
        for (int i = 1; i < 24; i++) {
            String time = String.format("%02d:00", i);
            timeValues.add(time);
        }
        return timeValues;
    }

    /**
     * initialize the event time variables
     */
    private void initializeEventTime(MFXComboBox<String> startTime, MFXComboBox<String> endTime) {
        startTime.setItems(generateTimeValues());
        endTime.setItems(generateTimeValues());
    }


    /**
     * binds the selected event to the eventManagementPage
     */
    private void bindSelectedEventProprieties() {
        //bind event start date  with view
        bindSelectedEventWithDatesTextValues(startDate, model.getSelectedEvent().startDateProperty());
        //bind event end date with view
        bindSelectedEventWithDatesTextValues(endDate, model.getSelectedEvent().endDateProperty());
        //set start time in  view
        startTime.setText(model.getSelectedEvent().getStartTime().toString());
        //set end time with view
        endTime.setText(model.getSelectedEvent().getEndTime().toString());
        //bind event name with view
        eventName.textProperty().bindBidirectional(model.getSelectedEvent().nameProperty());
        //bind start date with view
        startDate.valueProperty().bindBidirectional(model.getSelectedEvent().startDateProperty());
        //bind end date with view
        endDate.valueProperty().bindBidirectional(model.getSelectedEvent().endDateProperty());
        //bind event description with view
        eventDescription.textProperty().bindBidirectional(model.getSelectedEvent().descriptionProperty());
        //bind event location with view
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
     * cancel the event editing
     */
    private void cancelEditOperation() {
        this.secondaryLayout.getChildren().clear();
        this.secondaryLayout.setDisable(true);
        this.secondaryLayout.setVisible(false);
    }


    private void convertInputAndSet(MFXComboBox<String> startTime, MFXComboBox<String> endTime) {
        LocalTime startTimeInput = model.convertStringToTime(startTime.getText());
        LocalTime endTimeInput = model.convertStringToTime(endTime.getText());
        model.getSelectedEvent().setStartTime(startTimeInput);
        model.getSelectedEvent().setEndTime(endTimeInput);
    }

    private void saveOperation() {
        boolean isEventValid = EditEventValidator.isEventValidTimeAsString(eventName, startDate, startTime, endDate, endTime, eventLocation);
        System.out.println(isEventValid + " from controller");
        convertInputAndSet(startTime, endTime);
        System.out.println(model.getSelectedEvent());
        System.out.println(isEventValid);
        System.out.println(model.isEditValid()+ "");
        boolean areDatesValid =model.isEditValid();

        if(!areDatesValid){
          initializeInvalidInputError(model.getEventEditResponse(),invalidInput);
        }





        //        if (isEventValidText) {
//            if (model.isEditValid()) {
//                initializeLoadingView();
//                initializeService();
//            }
//        }
    }



    /**set the error window to display the error message*/
    private void initializeInvalidInputError(EventInvalidResponse invalidResponse,TextArea inputError){
        StringBuilder errorMessageBuilder = new StringBuilder();
            if(invalidResponse.getStartDateInvalid()!=null){
                EditEventValidator.changePseudoClassValue(startDate);
                errorMessageBuilder.append(invalidResponse.getStartDateInvalid()).append("\n");
            }
            if(invalidResponse.getStartTimeInvalid()!=null){
                EditEventValidator.changePseudoClassValue(startTime);
                errorMessageBuilder.append(invalidResponse.getStartTimeInvalid()).append("\n");
            }
            if(invalidResponse.getEndDateInvalid()!=null){
                EditEventValidator.changePseudoClassValue(endDate);
                errorMessageBuilder.append(invalidResponse.getEndDateInvalid()).append("\n");
            }
            if(invalidResponse.getEndTimeInvalid()!=null){
                EditEventValidator.changePseudoClassValue(endTime);
                errorMessageBuilder.append(invalidResponse.getEndTimeInvalid()).append("\n");
            }
            invalidInput.setText(errorMessageBuilder.toString());
            invalidInput.setVisible(true);
            EditEventValidator.changePseudoClassValue(invalidInput);
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
                    Platform.runLater(() -> model.getEventsDisplayer().displayEvents());
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
