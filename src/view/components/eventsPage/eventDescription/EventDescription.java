package view.components.eventsPage.eventDescription;

import be.Event;
import be.Status;
import bll.EventStatusCalculator;
import javafx.beans.binding.StringBinding;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import view.components.deleteEvent.DeleteButton;
import view.components.eventsPage.manageButton.ManageAction;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class EventDescription implements Initializable {
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
    private DeleteButton deleteButton;
    @FXML
    private Event event;
    public EventDescription(Event event, ManageAction manageAction, DeleteButton deleteButton) {
        this.manageAction = manageAction;
        this.event = event;
        this.deleteButton= deleteButton;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bindViewToModel(event);
        this.eventActions.getChildren().add(manageAction);
        this.eventActions.getChildren().add(deleteButton);
        initializeEventStatus(eventStatus,EventStatusCalculator.calculateStatus(event));
    }

    private void bindViewToModel(Event event) {
        this.eventName.textProperty().bind(event.nameProperty());
        this.eventLocation.textProperty().bind(event.locationProperty());
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