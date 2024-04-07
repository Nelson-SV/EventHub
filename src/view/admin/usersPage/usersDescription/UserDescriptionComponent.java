package view.admin.usersPage.usersDescription;

import be.User;
import exceptions.ErrorCode;
import exceptions.ExceptionHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import view.admin.mainAdmin.AdminModel;

import java.io.IOException;

public class UserDescriptionComponent  {

 private HBox userDescription;
    public UserDescriptionComponent(User user, AdminModel model, StackPane secondaryLayout, StackPane thirdLayout, StackPane fourthLayout) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("UserDescriptionView.fxml"));
        loader.setController(new UserDescriptionController(user,model,secondaryLayout,thirdLayout,fourthLayout));
        try {
            userDescription = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            ExceptionHandler.errorAlertMessage(ErrorCode.LOADING_FXML_FAILED.getValue());
        }
    }

    public HBox getRoot() {
        return userDescription;
    }
}
