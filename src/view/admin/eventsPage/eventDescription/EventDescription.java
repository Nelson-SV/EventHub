package view.admin.eventsPage.eventDescription;

import be.EventStatus;
import exceptions.ErrorCode;
import exceptions.ExceptionHandler;
import exceptions.ExceptionLogger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import view.admin.eventsPage.assignComponent.AssignButton;
import view.components.deleteEvent.DeleteButton;

import java.io.IOException;
import java.util.logging.Level;


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
            ExceptionLogger.getInstance().getLogger().log(Level.SEVERE,e.getMessage());
            ExceptionHandler.erorrAlertMessage(ErrorCode.LOADING_FXML_FAILED.getValue());
        }
    }
}
