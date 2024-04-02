package view.admin.eventsPage.assignedCoordinatorView;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class AssignedCoordinatorComponent extends HBox {

@FXML
private HBox assignedComponentContainer;
    public AssignedCoordinatorComponent(String firstName, String lastName) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AssignedCoordinatorView.fxml"));
        AssignedCoordinatorController assignedCoordinatorController = new AssignedCoordinatorController(firstName,lastName);
        loader.setController(assignedCoordinatorController);
        try {
            assignedComponentContainer=loader.load();
            this.getChildren().add(assignedComponentContainer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public HBox getAssignComponentContainer() {
        return assignedComponentContainer;
    }
}
