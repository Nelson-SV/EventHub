package view.components.eventDescription;

import be.Event;
import javafx.beans.binding.StringBinding;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import view.components.manageButton.ManageAction;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class EventDescription implements Initializable {
    private final String START_DATE = "Start date: ";
    private final String END_DATE = "End date: ";
    private final String START_TIME = "Start time:";
    private final String END_TIME = "End time:";
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
    private Label eventEnd;
    @FXML
    private Label startTime;
    @FXML
    private Label endTime;
    @FXML
    private HBox eventActions;

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
        LocalDateTime startDateTime = LocalDateTime.of(event.getStartDate(), event.getStartTime());
        LocalDateTime endDateTime = null;
        if (event.getEndDate() == null && event.getEndTime() == null) {
            this.initializeStatusByStartDate(startDateTime, eventStatus);
        } else if (event.getEndTime() != null && event.getEndDate() == null) {
            initializeStatusByStartAndEndTime(startDateTime, event.getEndTime(), eventStatus);
        } else {
            endDateTime = LocalDateTime.of(event.getEndDate(), event.getEndTime());
            this.initializeStatus(startDateTime, endDateTime, eventStatus);
        }
    }

    private void bindViewToModel(Event event) {
        this.eventName.textProperty().bind(event.nameProperty());
        this.eventLocation.textProperty().bind(event.descriptionProperty());
        this.eventTickets.textProperty().bind(event.availableTicketsProperty().asString());
        bindDates(event, eventStart, eventEnd, startTime, endTime);
    }

    private void bindDates(Event event, Label startDate, Label endDate, Label startTime, Label endTime) {
        StringBinding eventStartBinding = new StringBinding() {
            {
                super.bind(event.startDateProperty());
            }

            @Override
            protected String computeValue() {
                LocalDate startDate = event.getStartDate();
                return startDate.format(DateTimeFormatter.ofPattern("yy-MM-dd"));
            }
        };

        StringBinding eventEndBinding = new StringBinding() {
            {
                super.bind(event.endDateProperty());
            }

            @Override
            protected String computeValue() {
                LocalDate endDate = event.getEndDate();
                if (event.getEndDate() != null) {
                    return endDate.format(DateTimeFormatter.ofPattern("yy-MM-dd"));
                }
                return "N/A";

            }
        };
        StringBinding eventStartTimeBinding = new StringBinding() {
            {
                super.bind(event.startTimeProperty());
            }

            @Override
            protected String computeValue() {
                LocalTime startDateTime = event.getStartTime();
                return startDateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
            }
        };
        StringBinding eventEndTimeBinding = new StringBinding() {
            {
                super.bind(event.endTimeProperty());
            }

            @Override
            protected String computeValue() {
                LocalTime startEndTime = event.getEndTime();
                if (event.getEndTime() != null) {
                    return startEndTime.format(DateTimeFormatter.ofPattern("HH:mm"));
                }
                return "N/A";
            }
        };
        startDate.textProperty().bind(eventStartBinding);
        endDate.textProperty().bind(eventEndBinding);
        startTime.textProperty().bind(eventStartTimeBinding);
        endTime.textProperty().bind(eventEndTimeBinding);
    }

    private void initializeStatusByStartDate(LocalDateTime startDate, Label label) {
        LocalDateTime today = LocalDateTime.now();
        label.getStyleClass().clear();
        label.getStyleClass().add("eventStatus");
        if (startDate.isAfter(today)) {
            label.setText("UPCOMING");
            label.getStyleClass().add("active");
        } else if (startDate.toLocalDate().isEqual(today.toLocalDate())) {
            label.setText("ONGOING");
            label.getStyleClass().add("ongoing");
        } else {
            label.setText("FINALIZED");
            label.getStyleClass().add("ended");
        }
    }

    private void initializeStatusByStartAndEndTime(LocalDateTime startDate, LocalTime endTime, Label label) {
        LocalDateTime today = LocalDateTime.now();
        label.getStyleClass().clear();
        label.getStyleClass().add("eventStatus");
        if (startDate.isAfter(today)) {
            label.setText("UPCOMING");
            label.getStyleClass().add("active");
        } else if ((startDate.toLocalTime().isAfter(today.toLocalTime())) && today.toLocalTime().isBefore(endTime)) {
            label.setText("ONGOING");
            label.getStyleClass().add("ongoing");
        } else {
            label.setText("FINALIZED");
            label.getStyleClass().add("ended");
        }
    }

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
