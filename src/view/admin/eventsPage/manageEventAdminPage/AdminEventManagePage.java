package view.admin.eventsPage.manageEventAdminPage;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import view.admin.eventsPage.AdminPageController;
import view.admin.mainAdmin.AdminModel;

import java.io.IOException;

public class AdminEventManagePage  {
    @FXML
     private GridPane managePageContainer;
    public AdminEventManagePage(StackPane secondaryLayout, StackPane thirdLayout, AdminModel model) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminEventManagePage.fxml"));
        loader.setController(new AdminEventManageController(secondaryLayout, thirdLayout, model));
        try {
            managePageContainer=loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public GridPane getRoot() {
        return managePageContainer;
    }
}
