package view.components.eventDescription;

import be.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import view.components.manageButton.ManageAction;
import java.net.URL;
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
   private  Label eventStartDate;
   @FXML
   private  Label eventStatus;
   @FXML
   private  Label eventTickets;
   @FXML
   private  Label eventRevenue;
   @FXML
   private HBox eventActions;
   @FXML
   private ManageAction manageAction;
 @FXML
 private Event event;

    public EventDescription(Event event, ManageAction manageAction) {
        this.manageAction=manageAction;
        this.event=event;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
this.eventActions.getChildren().add(manageAction);
this.eventName.setText(event.getName());
this.eventLocation.setText(event.getLocation().getStreet()+", "+event.getLocation().getCity());
this.eventStartDate.setText(event.getStartDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
this.eventStatus.setText("Active");
this.eventTickets.setText(event.getAvailableTickets()+"");
this.eventRevenue.setText( "$"+event.getAvailableTickets()*120);
    }
}
