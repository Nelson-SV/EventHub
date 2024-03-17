package view.components.eventDescription;

import be.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import view.components.manageButton.ManageAction;

import java.io.IOException;

public class EventComponenet extends HBox {
    @FXML
    private HBox eventContainer;

    public EventComponenet(Event event, ManageAction manageAction) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("EventDescription.fxml"));
        loader.setController(new EventDescription(event,manageAction));
        try {
           eventContainer= loader.load();
           this.getChildren().add(eventContainer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
