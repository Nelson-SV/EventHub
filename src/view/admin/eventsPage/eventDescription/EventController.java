package view.admin.eventsPage.eventDescription;
import be.EventStatus;
import be.Status;
import javafx.beans.binding.StringBinding;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import view.admin.eventsPage.assignComponent.AssignButton;
import view.components.deleteEvent.DeleteButton;
import view.components.eventsPage.manageButton.ManageAction;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class EventController implements Initializable {
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
    private AssignButton assignButton;
    @FXML
    private DeleteButton deleteButton;

    private EventStatus event;
    public EventController(EventStatus event,AssignButton assignButton, DeleteButton deleteButton) {
        this.event = event;
        this.deleteButton= deleteButton;
        this.assignButton=assignButton;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bindViewToModel(event);
         this.eventActions.getChildren().add(assignButton);
        this.eventActions.getChildren().add(deleteButton);
        initializeEventStatus(eventStatus, event.getStatus());
    }

    private void bindViewToModel(EventStatus event) {
        this.eventName.textProperty().bind(event.getEventDTO().nameProperty());
        this.eventLocation.textProperty().bind(event.getEventDTO().descriptionProperty());
        this.eventTickets.textProperty().bind(event.getEventDTO().availableTicketsProperty().asString());
        bindDates(event, eventStart, eventEnd, startTime, endTime);
    }

    private void bindDates(EventStatus event, Label startDate, Label endDate, Label startTime, Label endTime) {
        StringBinding eventStartBinding = new StringBinding() {
            {
                super.bind(event.getEventDTO().startDateProperty());
            }

            @Override
            protected String computeValue() {
                LocalDate startDate = event.getEventDTO().getStartDate();
                return startDate.format(DateTimeFormatter.ofPattern("yy-MM-dd"));
            }
        };

        StringBinding eventEndBinding = new StringBinding() {
            {
               super.bind(event.getEventDTO().endDateProperty());

            }

            @Override
            protected String computeValue() {
                LocalDate endDate = event.getEventDTO().getEndDate();
                if (event.getEventDTO().getEndDate() != null) {
                    return endDate.format(DateTimeFormatter.ofPattern("yy-MM-dd"));
                }
                return "N/A";

            }
        };
        StringBinding eventStartTimeBinding = new StringBinding() {
            {
                super.bind(event.getEventDTO().startTimeProperty());
            }

            @Override
            protected String computeValue() {
                LocalTime startDateTime = event.getEventDTO().getStartTime();
                return startDateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
            }
        };

        StringBinding eventEndTimeBinding = new StringBinding() {
            {
                super.bind(event.getEventDTO().endTimeProperty());
            }

            @Override
            protected String computeValue() {
                LocalTime startEndTime = event.getEventDTO().getEndTime();
                if (event.getEventDTO().getEndTime() != null) {
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

    private void initializeEventStatus(Label eventStatus, Status status){
        eventStatus.getStyleClass().clear();
        eventStatus.getStyleClass().add("eventStatus");
        if(status==Status.UPCOMING){
            eventStatus.setText(status.getValue());
            eventStatus.getStyleClass().add("active");
        }else if(status==Status.ONGOING){
            eventStatus.setText(status.getValue());
            eventStatus.getStyleClass().add("ongoing");
        }else{
            eventStatus.setText(status.getValue());
            eventStatus.getStyleClass().add("ended");
        }
    }
}

