package view.admin.usersPage;

import exceptions.ErrorCode;
import exceptions.ExceptionHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import view.admin.mainAdmin.AdminModel;

import java.io.IOException;

public class UserPageComponent {
    @FXML
    private VBox userPageContainer;
    private UserPageController userPageController;

    public UserPageComponent(StackPane secondaryLayout, StackPane thirdLayout, StackPane fourthLayout, AdminModel model) {
        userPageController = new UserPageController(secondaryLayout, thirdLayout, fourthLayout, model);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("UsersPage.fxml"));
        loader.setController(userPageController);
        try {
            userPageContainer = loader.load();
        } catch (IOException e) {
            ExceptionHandler.errorAlertMessage(ErrorCode.LOADING_FXML_FAILED.getValue());
        }
    }

    public VBox getRoot() {
        return userPageContainer;
    }
}
