package view.admin.eventsPage.eventDescription;

import be.EventStatus;
import exceptions.ErrorCode;
import exceptions.ExceptionHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import view.admin.eventsPage.assignButton.AssignButton;
import view.components.deleteEvent.DeleteButton;

import java.io.IOException;


public class EventDescription extends HBox {
    @FXML
    private HBox eventContainer;

    public EventDescription(EventStatus eventStatus, AssignButton assignButton, DeleteButton deleteButton) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("EventDescription.fxml"));
        loader.setController(new EventController(eventStatus,assignButton,deleteButton));
        try {
            eventContainer=loader.load();
            this.getChildren().add(eventContainer);
        } catch (IOException e) {
            ExceptionHandler.errorAlertMessage(ErrorCode.LOADING_FXML_FAILED.getValue());
        }
    }
}
