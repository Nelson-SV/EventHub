package view.admin.eventsPage.manageEventAdminPage;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import org.controlsfx.control.spreadsheet.Grid;
import view.admin.mainAdmin.AdminModel;

import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminEventManageController implements Initializable {
    @FXML
    private GridPane managePageContainer;

    private StackPane secondaryLayout,thirdLayout;
    private AdminModel adminModel;
    public AdminEventManageController(StackPane secondaryLayout, StackPane thirdLayout, AdminModel model) {
        this.secondaryLayout = secondaryLayout;
        this.thirdLayout = thirdLayout;
        this.adminModel= model;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


    @FXML
    private void saveChanges(ActionEvent event){
        System.out.println("saveOperation");
    }
    @FXML
    private void cancelChanges(ActionEvent event){
        System.out.println("cancelOperation");
    }

}
