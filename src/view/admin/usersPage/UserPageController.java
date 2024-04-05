package view.admin.usersPage;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import view.admin.mainAdmin.AdminModel;
import view.admin.usersPage.createUser.Create.CreateUserComponent;
import view.utility.CommonMethods;

import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;

public class UserPageController implements Initializable {
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

    private StackPane secondaryLayout,thirdLayout,fourthLayout;
    private AdminModel model;

    public UserPageController(StackPane secondaryLayout, StackPane thirdLayout, StackPane fourthLayout, AdminModel model) {
        this.secondaryLayout = secondaryLayout;
        this.thirdLayout = thirdLayout;
        this.fourthLayout = fourthLayout;
        this.model = model;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addOnActionCreateUser(createUser);
    }

    private void addOnActionCreateUser(Button createUser){
    createUser.setOnAction((event)->{
        initializeCreateUserPage();
    });
    }

    private void initializeCreateUserPage(){
        CreateUserComponent createUserComponent = new CreateUserComponent(secondaryLayout,thirdLayout,model);
        createUserComponent.getRoot().setAlignment(Pos.CENTER);
        CommonMethods.showSecondaryLayout(secondaryLayout,createUserComponent.getRoot());
    }





}
