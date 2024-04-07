package view.admin.usersPage;
import exceptions.ErrorCode;
import exceptions.ExceptionHandler;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import view.admin.listeners.UsersDisplayer;
import view.admin.mainAdmin.AdminModel;
import view.admin.usersPage.createUser.Create.CreateUserComponent;
import view.admin.usersPage.usersDescription.UserDescriptionComponent;
import view.utility.CommonMethods;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class UserPageController implements Initializable, UsersDisplayer {
    @FXML
    private Button createUser;
    @FXML
    private TextField searchEventButton;
    @FXML
    private VBox resetFilterButton;
    @FXML
    private HBox shortcutContainer;
    @FXML
    private ScrollPane usersView;
    @FXML
    private VBox usersContainer;
    @FXML
    private VBox userPageContainer;

    private StackPane secondaryLayout, thirdLayout, fourthLayout;
    private AdminModel adminModel;
    private Service<Void> loadUsersFromDb;
    public UserPageController(StackPane secondaryLayout, StackPane thirdLayout, StackPane fourthLayout, AdminModel model) {
        this.secondaryLayout = secondaryLayout;
        this.thirdLayout = thirdLayout;
        this.fourthLayout = fourthLayout;
        this.adminModel = model;
        adminModel.setUsersDisplayer(this);
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addOnActionCreateUser(createUser);
        initializeUsersLoadingService();
    }

    private void addOnActionCreateUser(Button createUser) {
        createUser.setOnAction((event) -> {
            initializeCreateUserPage();
        });
    }

    private void initializeCreateUserPage() {
        CreateUserComponent createUserComponent = new CreateUserComponent(secondaryLayout, thirdLayout, adminModel);
        createUserComponent.getRoot().setAlignment(Pos.CENTER);
        CommonMethods.showSecondaryLayout(secondaryLayout, createUserComponent.getRoot());
    }


    @Override
    public void displayUsers() {
        if (userPageContainer.getScene() != null) {
            Platform.runLater(() -> {
                usersContainer.getChildren().clear(); // Clear existing content
                List<HBox> allUsersDescription = adminModel.getUsersToDisplay().stream()
                        .map(item -> new UserDescriptionComponent(item, adminModel, secondaryLayout, thirdLayout, fourthLayout).getRoot())
                        .toList();
                if (allUsersDescription.isEmpty()) {
                    Label noUsersLabel = new Label("No users available");
                    usersContainer.getChildren().add(noUsersLabel);
                } else {
                    usersContainer.getChildren().addAll(allUsersDescription);
                }
            });
        }
    }

    private void initializeUsersLoadingService(){
        loadUsersFromDb= new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        adminModel.getAllUsersWithFullInfo();
                        return null;
                    };
                };
            }
        };

        loadUsersFromDb.setOnSucceeded((event)->displayUsers());
        loadUsersFromDb.setOnFailed((event)->{
            loadUsersFromDb.getException().printStackTrace();
            ExceptionHandler.errorAlertMessage(ErrorCode.FAILED_LOAD_USERS.getValue());
        });
        loadUsersFromDb.restart();
    }

}
