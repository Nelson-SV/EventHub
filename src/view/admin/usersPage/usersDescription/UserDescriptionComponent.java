package view.admin.usersPage.usersDescription;

import exceptions.ErrorCode;
import exceptions.ExceptionHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import view.admin.mainAdmin.AdminModel;

import java.io.IOException;

public class UserDescriptionComponent extends VBox {


    public UserDescriptionComponent(AdminModel model,StackPane secondaryLayout,StackPane thirdLayout,StackPane fourthLayout ) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("UserDescriptionView.fxml"));
        loader.setController(new UserDescriptionController(model,secondaryLayout,thirdLayout,fourthLayout));
        try {
            Parent root = loader.load();
            this.getChildren().add(root);
        } catch (IOException e) {
            e.printStackTrace();
            ExceptionHandler.errorAlertMessage(ErrorCode.LOADING_FXML_FAILED.getValue());
        }
    }
}
