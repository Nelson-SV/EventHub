package view.components.eventDescription;
import be.Event;
import exceptions.ErrorCode;
import exceptions.EventException;
import exceptions.ExceptionHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import view.components.manageButton.ManageAction;
import java.io.IOException;

public class EventComponent extends HBox {

    private HBox eventContainer;

    public EventComponent(Event event, ManageAction manageAction) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("EventDescription.fxml"));
        loader.setController(new EventDescription(event,manageAction));
        try {
           eventContainer= loader.load();
           this.getChildren().add(eventContainer);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getCause());
            ExceptionHandler.erorrAlertMessage(ErrorCode.LOADING_FXML_FAILED.getValue());
        }
    }
}
