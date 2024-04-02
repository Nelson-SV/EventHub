package view.admin.eventsPage.assignedCoordinatorView;

import be.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import view.components.deleteEvent.DeleteButton;

import java.net.URL;
import java.util.ResourceBundle;

public class AssignedCoordinatorController implements Initializable {
    @FXML
    private HBox assignComponentContainer;
    @FXML
    private Label firstName;

    private String userFirstName,userLastName;


    public AssignedCoordinatorController(String firstName,String lastName) {
        this.userFirstName=firstName;
        this.userLastName= lastName;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.firstName.setText(userFirstName + " " +userLastName);

    }
}
