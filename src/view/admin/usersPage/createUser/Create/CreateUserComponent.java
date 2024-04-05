package view.admin.usersPage.createUser.Create;
import exceptions.ExceptionHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import view.admin.mainAdmin.AdminModel;

import java.io.IOException;

public class CreateUserComponent {
    @FXML
    private GridPane  addUserContainerComponent;
    private CreateUserController createUserController;
    public CreateUserComponent(StackPane secondaryLayout,StackPane thirdStackPane, AdminModel adminModel) {
        createUserController = new CreateUserController( secondaryLayout,thirdStackPane,adminModel);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("EditUser.fxml"));
        loader.setController(createUserController);
        try {
            addUserContainerComponent=loader.load();
        } catch (IOException e) {
            ExceptionHandler.errorAlertMessage(e.getMessage());
        }
    }

    public GridPane getRoot() {
        return addUserContainerComponent;
    }
}
