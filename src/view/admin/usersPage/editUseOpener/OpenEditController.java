package view.admin.usersPage.editUseOpener;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import view.admin.mainAdmin.AdminModel;
import view.admin.usersPage.editUserPage.EditUserPageComponent;
import view.utility.CommonMethods;
import java.net.URL;
import java.util.ResourceBundle;
public class OpenEditController  implements Initializable {
    @FXML
    private VBox editContainer;
    private AdminModel adminModel;
    private StackPane secondaryLayout,thirdLayout,adminFourthLayout;
    private int userId;

    public OpenEditController(AdminModel adminModel, StackPane secondaryLayout, StackPane thirdLayout, StackPane adminFourthLayout, int userId) {
        this.adminModel = adminModel;
        this.secondaryLayout = secondaryLayout;
        this.thirdLayout = thirdLayout;
        this.adminFourthLayout = adminFourthLayout;
        this.userId = userId;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
     editContainer.addEventHandler(MouseEvent.MOUSE_CLICKED, event->addOnAction());
    }
    private void addOnAction(){
        adminModel.setSelectedUserToEdit(adminModel.getUserById(userId));
        EditUserPageComponent editUserPageComponent = new EditUserPageComponent(secondaryLayout,thirdLayout,adminFourthLayout,adminModel,userId);
        editUserPageComponent.getRoot().setAlignment(Pos.CENTER);
        CommonMethods.showSecondaryLayout(secondaryLayout,editUserPageComponent.getRoot());


    }
}
