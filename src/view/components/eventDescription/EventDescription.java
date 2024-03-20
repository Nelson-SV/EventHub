package view.components.eventDescription;

import be.Event;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import view.components.manageButton.ManageAction;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;


public class EventDescription implements Initializable {
    private final String START_DATE = "Start : ";
    private final String END_DATE = "End : ";
    private final String UPOMING = "UPCOMING";
    private final String FINALIZED = "FINALIZED";
    private final String ONGOING = "ONGOING";

    @FXML
    private HBox eventContainer;
    @FXML
    private Label eventName;
    @FXML
    private Label eventLocation;
    @FXML
    private Label eventStartDate;
    @FXML
    private Label eventStatus;
    @FXML
    private Label eventTickets;
    @FXML
    private Label eventStart;
    @FXML
    private HBox eventActions;
    @FXML
    private Label eventEnd;
    @FXML
    private ManageAction manageAction;


    @FXML
    private Event event;

    public EventDescription(Event event, ManageAction manageAction) {
        this.manageAction = manageAction;
        this.event = event;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bindViewToModel(event);
        this.eventActions.getChildren().add(manageAction);
//        this.eventName.setText(event.getName());
//        this.eventLocation.setText(event.getLocation());
        LocalDateTime startDateTime= LocalDateTime.of(event.getStartDate(),event.getEndTime());
        LocalDateTime endDateTime = LocalDateTime.of(event.getEndDate(),event.getEndTime());
        this.initializeStatus(startDateTime, endDateTime, eventStatus);
//        this.eventTickets.setText(event.getAvailableTickets() + "");
//        this.eventStart.setText(START_DATE + event.getStartDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        initializeEndDate(event.getEndDate(), eventEnd);

    }

    private void bindViewToModel(Event event) {
        System.out.println("sassss");
        this.eventName.textProperty().bind(event.nameProperty());
        this.eventLocation.textProperty().bind(event.descriptionProperty());
        this.eventTickets.textProperty().bind(event.availableTicketsProperty().asString());
        SimpleStringProperty simpleStringProperty = new SimpleStringProperty();
        simpleStringProperty.setValue(START_DATE + event.getStartDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        this.eventStart.textProperty().bind(simpleStringProperty);
    }

    private void initializeEndDate(LocalDate endDate, Label label) {
        if (endDate != null) {
            label.setText(END_DATE + event.getEndDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        } else {
            label.setText(END_DATE + "NA");
        }
    }


//        if (today.isBefore(startDate)) {
//        label.setText("UPCOMING");
//        label.getStyleClass().clear();
//        label.getStyleClass().addAll("eventStatus", "active");
//    }else if(today.isEqual(startDate)){
//        label.setText("ONGOING");
//        label.getStyleClass().clear();
//        label.getStyleClass().addAll("eventStatus", "ongoing");
//    }else if(endDate!=null && (today.isAfter(startDate) && today.isBefore(endDate)))){
//        label.setText("ONGOING");
//        label.getStyleClass().clear();
//        label.getStyleClass().addAll("eventStatus", "ongoing");
//    }
//        else if (today.isAfter(startDate) && (endDate == null || today.isAfter(endDate))) {
//        label.setText("");
//        label.getStyleClass().clear();
//        label.getStyleClass().addAll("eventStatus", "ongoing");
//    } else {
//        label.setText("FINALIZED");
//        label.getStyleClass().clear();
//        label.getStyleClass().addAll("eventStatus", "ended");
//    }

    private void initializeStatus(LocalDateTime startDate, LocalDateTime endDate, Label label) {
        LocalDateTime today = LocalDateTime.now();
        label.getStyleClass().clear();
        label.getStyleClass().add("eventStatus");
        if (endDate != null && today.isAfter(endDate)) {
            label.setText("FINALIZED");
            label.getStyleClass().add("ended");
        } else if (today.isBefore(startDate)) {
            label.setText("UPCOMING");
            label.getStyleClass().add("active");
        } else if (!today.isBefore(startDate) && (endDate == null || !today.isAfter(endDate))) {
            label.setText("ONGOING");
            label.getStyleClass().add("ongoing");
        }
    }
}
