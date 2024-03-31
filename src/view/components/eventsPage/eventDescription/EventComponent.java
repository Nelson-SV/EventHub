package view.components.eventsPage.eventDescription;
import be.Event;
import exceptions.ErrorCode;
import exceptions.ExceptionHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import view.components.deleteEvent.DeleteButton;
import view.components.eventsPage.manageButton.ManageAction;
import java.io.IOException;

public class EventComponent extends HBox {

    private HBox eventContainer;

    public EventComponent(Event event, ManageAction manageAction, DeleteButton deleteButton) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("EventDescription.fxml"));
        loader.setController(new EventDescription(event,manageAction,deleteButton));
        try {
           eventContainer= loader.load();
           this.getChildren().add(eventContainer);
        } catch (IOException e) {
            e.printStackTrace();
            ExceptionHandler.erorrAlertMessage(ErrorCode.LOADING_FXML_FAILED.getValue());
        }
    }
}
