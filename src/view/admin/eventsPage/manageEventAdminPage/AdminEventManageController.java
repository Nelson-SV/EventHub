package view.admin.eventsPage.manageEventAdminPage;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import view.admin.mainAdmin.AdminModel;
import view.utility.CommonMethods;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminEventManageController implements Initializable {
    @FXML
    private GridPane managePageContainer;
    @FXML
    private MFXButton cancelButton;

    private StackPane secondaryLayout, thirdLayout;
    private AdminModel adminModel;

    public AdminEventManageController(StackPane secondaryLayout, StackPane thirdLayout, AdminModel model) {
        this.secondaryLayout = secondaryLayout;
        this.thirdLayout = thirdLayout;
        this.adminModel = model;
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

}
