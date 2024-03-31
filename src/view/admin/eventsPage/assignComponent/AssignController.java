package view.admin.eventsPage.assignComponent;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import view.admin.mainAdmin.AdminModel;

import java.net.URL;
import java.util.ResourceBundle;

public class AssignController implements Initializable {
    @FXML
    private VBox assignedContainer;
    private AdminModel adminModel;
    private StackPane secondaryLayout,thirdLayout;
    private int eventId;

    public AssignController(AdminModel adminModel, StackPane secondaryLayout, StackPane thirdLayout, int eventId) {
        this.adminModel = adminModel;
        this.secondaryLayout = secondaryLayout;
        this.thirdLayout = thirdLayout;
        this.eventId = eventId;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("initialized + " + eventId);
    }
}
