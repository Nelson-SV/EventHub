package view.admin.usersPage.editUserPage;

import exceptions.ErrorCode;
import exceptions.ExceptionHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import view.admin.mainAdmin.AdminModel;

import java.io.IOException;

public class EditUserPageComponent {
    private GridPane editUserContainerComponent;
    private EditUserPageController editUserPageController;
    public EditUserPageComponent(StackPane secondaryLayout, StackPane thirdLayout, StackPane fourthLayout, AdminModel adminModel, int userId) {

     FXMLLoader loader = new FXMLLoader(getClass().getResource("EditUser.fxml"));
     editUserPageController = new EditUserPageController(secondaryLayout,thirdLayout,fourthLayout,adminModel,userId);
     loader.setController(editUserPageController);
        try {
            editUserContainerComponent=loader.load();
        } catch (IOException e) {
            ExceptionHandler.errorAlertMessage(ErrorCode.LOADING_FXML_FAILED.getValue());
        }
    }
    public GridPane getRoot() {
        return editUserContainerComponent;
    }
}
