package view.components.eventDescription;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import view.components.manageButton.ManageAction;

import java.net.URL;
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
 private String []event;

    public EventDescription(String[] event , ManageAction manageAction) {
        this.manageAction=manageAction;
        this.event=event;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
this.eventActions.getChildren().add(manageAction);
this.eventName.setText(event[0]);
this.eventLocation.setText(event[1]);
this.eventStartDate.setText(event[2]);
this.eventStatus.setText(event[3]);
this.eventTickets.setText(event[4]);
this.eventRevenue.setText(event[5]);




    }
}
