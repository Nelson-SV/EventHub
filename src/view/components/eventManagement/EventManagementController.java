package view.components.eventManagement;
import exceptions.ErrorCode;
import exceptions.ExceptionHandler;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import view.components.main.Model;
import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class EventManagementController implements Initializable {
    @FXML
    private MFXButton saveEdit;
    @FXML
    private MFXButton cancelEdit;
    @FXML
    private MFXFilterComboBox<String> coordinators;
    @FXML
    private TextArea eventLocation;
    @FXML
    private TextArea eventDescription;
    @FXML
    private MFXComboBox endTime;
    @FXML
    private MFXDatePicker endDate;
    @FXML
    private MFXComboBox startTime;
    @FXML
    private MFXDatePicker startDate;
    @FXML
    private TextField eventName;
    //TODO put the generateTimeOptions into an utility class
    @FXML
    private GridPane managementRoot;

    private Model model;

    public GridPane getRoot() {
        return managementRoot;
    }

    public EventManagementController(Model model) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("EventManager.fxml"));
        loader.setController(this);
        try {
           managementRoot= loader.load();
           this.model= model;
        } catch (IOException e) {
            ExceptionHandler.erorrAlertMessage(ErrorCode.LOADING_FXML_FAILED.getValue());
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
      initializeEventTime(this.startTime,this.endTime);
    }


    private ObservableList<LocalTime> generateTimeOptions() {
        ObservableList<LocalTime> timeOptions = FXCollections.observableArrayList();
        LocalTime time = LocalTime.of(0, 0);
        while (time.isBefore(LocalTime.of(23, 0))) {
            timeOptions.add(time);
            time = time.plusHours(1);
        }
        return timeOptions;
    }

    private void  initializeEventTime(MFXComboBox startTime,MFXComboBox endTime){
        startTime.setItems(generateTimeOptions());
        endTime.setItems(generateTimeOptions());
        bindSelectedEventProprieties();
    }

    private void bindSelectedEventProprieties(){
        eventName.textProperty().bindBidirectional(model.getSelectedEvent().nameProperty());
        startDate.valueProperty().bindBidirectional(model.getSelectedEvent().startDateProperty());
        endDate.valueProperty().bindBidirectional(model.getSelectedEvent().endDateProperty());
        startTime.valueProperty().bindBidirectional(model.getSelectedEvent().startTimeProperty());
        endTime.valueProperty().bindBidirectional(model.getSelectedEvent().endTimeProperty());
        eventDescription.textProperty().bindBidirectional(model.getSelectedEvent().descriptionProperty());
        eventLocation.textProperty().bindBidirectional(model.getSelectedEvent().locationProperty());
    }
}
