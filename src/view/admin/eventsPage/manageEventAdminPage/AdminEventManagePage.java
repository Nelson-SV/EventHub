package view.admin.eventsPage.manageEventAdminPage;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import view.admin.eventsPage.AdminPageController;
import view.admin.mainAdmin.AdminModel;

import java.io.IOException;

public class AdminEventManagePage extends GridPane {
    public AdminEventManagePage(StackPane secondaryLayout, StackPane thirdLayout, AdminModel model) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminEventManagePage.fxml"));
        loader.setRoot(this);
        loader.setController(new AdminPageController(model,secondaryLayout,thirdLayout));
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
