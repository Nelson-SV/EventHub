package view.admin.eventsPage;

import exceptions.ErrorCode;
import exceptions.ExceptionHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import view.admin.mainAdmin.AdminModel;

import java.io.IOException;

public class AdminEventPage extends VBox {
    @FXML
    private VBox adminEventPage;

    public AdminEventPage(AdminModel adminModel, StackPane secondaryLayout, StackPane thirdLayout,StackPane adminFourthLayout) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminEventPage.fxml"));
        loader.setController(new AdminPageController(adminModel,secondaryLayout,thirdLayout,adminFourthLayout));
        try {
            adminEventPage=loader.load();
            this.getChildren().add(adminEventPage);
        } catch (IOException e) {
            e.printStackTrace();
            ExceptionHandler.errorAlertMessage(ErrorCode.LOADING_FXML_FAILED.getValue());
        }
    }
}
