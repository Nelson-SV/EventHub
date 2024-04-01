package view.admin.eventsPage.manageEventAdminPage;

import be.DeleteOperation;
import be.User;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import view.admin.eventsPage.assignedCoordinatorView.AssignedCoordinatorComponent;
import view.admin.mainAdmin.AdminModel;
import view.components.deleteEvent.DeleteButton;
import view.utility.CommonMethods;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminEventManageController implements Initializable {
    @FXML
    private GridPane managePageContainer;
    @FXML
    private MFXButton cancelButton;
    @FXML
    private VBox assignedCoordinatorsContainer;

    private StackPane secondaryLayout, thirdLayout,fourthLayout;
    private AdminModel adminModel;

    public AdminEventManageController(StackPane secondaryLayout, StackPane thirdLayout,StackPane fourthLayout, AdminModel model) {
        this.secondaryLayout = secondaryLayout;
        this.thirdLayout = thirdLayout;
        this.adminModel = model;
        this.fourthLayout=fourthLayout;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cancelButton.setOnAction(this::cancelChanges);
    }


    public void saveChanges(ActionEvent event) {
        System.out.println("saveOperation");
    }

    public void cancelChanges(ActionEvent event) {
        CommonMethods.closeWindow(secondaryLayout);
    }

    public void displayEventCoordinators() {
        if(managePageContainer.getScene()!=null){
            adminModel.getEventAssignedCoordinators().forEach(user -> {
                DeleteButton deleteButton = new DeleteButton(thirdLayout,fourthLayout,adminModel,user.getUserId(), DeleteOperation.DELETE_USER);
                AssignedCoordinatorComponent assignedCoordinatorComponent = new AssignedCoordinatorComponent(user.getFirstName(),user.getLastName());
                assignedCoordinatorComponent.getAssignComponentContainer().getChildren().add(deleteButton);
                assignedCoordinatorsContainer.getChildren().add(assignedCoordinatorComponent);
            });
        }
    }
    public void displayAllCoordinators() {
        System.out.println("setThem");
    }
}
