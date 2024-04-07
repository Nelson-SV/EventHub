package view.admin.usersPage.editUseOpener;

import exceptions.ErrorCode;
import exceptions.ExceptionHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import view.admin.mainAdmin.AdminModel;

import java.io.IOException;

public class OpenEditButton {
    private VBox editContainer;
    private OpenEditController openEditController;
    public OpenEditButton(AdminModel adminModel, StackPane secondaryLayout, StackPane thirdLayout, StackPane adminFourthLayout, int userId) {
       this.openEditController = new OpenEditController(adminModel,secondaryLayout,thirdLayout,adminFourthLayout,userId);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("OpenEditComponent.fxml"));
        loader.setController(openEditController);
        try {
            editContainer=loader.load();
        } catch (IOException e) {
            ExceptionHandler.errorAlertMessage(ErrorCode.LOADING_FXML_FAILED.getValue());
        }
    }

    public VBox getRoot() {
        return editContainer;
    }
}
