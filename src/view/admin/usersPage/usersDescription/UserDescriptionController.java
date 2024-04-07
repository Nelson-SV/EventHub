package view.admin.usersPage.usersDescription;

import be.DeleteOperation;
import be.User;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import view.admin.mainAdmin.AdminModel;
import view.admin.usersPage.threads.ImageLoader;
import view.components.confirmationWindow.ConfirmationWindow;
import view.components.deleteEvent.DeleteButton;
import view.components.loadingComponent.LoadingComponent;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class UserDescriptionController implements Initializable {
    @FXML
    private HBox userContainer;
    @FXML
    private ImageView userImageContainer;
    @FXML
    private Label userName;
    @FXML
    private HBox eventsContainer;
    @FXML
    private Label userEvents;
    @FXML
    private HBox roleContainer;
    @FXML
    private Label userRole;
    @FXML
    private HBox actionsContainer;
    private AdminModel adminModel;
    private StackPane secondaryLayout, thirdLayout, fourthLayout;
    private ImageLoader imageLoader;
    private User user;
    private Label placeholder;

    public UserDescriptionController(User user, AdminModel model, StackPane secondaryLayout, StackPane thirdLayout, StackPane fourthLayout) {
        this.adminModel = model;
        this.secondaryLayout = secondaryLayout;
        this.thirdLayout = thirdLayout;
        this.fourthLayout = fourthLayout;
        this.imageLoader = new ImageLoader();
        this.user = user;
        placeholder= new Label("Image");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeUser();
    }

    private void makeTheImageRound(Image image) {
        userImageContainer.setImage(image);
        userImageContainer.setPreserveRatio(false);
        double width = userImageContainer.getFitWidth();
        double height = userImageContainer.getFitHeight();
        Rectangle rectangle = new Rectangle(width, height);
        rectangle.setArcHeight(width / 2);
        rectangle.setArcWidth(height / 2);
        userImageContainer.setClip(rectangle);
    }

    private void initializeUser() {
        setTheImageLoader();
        initializeName(userName);
        initializeEvents(userEvents);
        initializeRole(userRole);
        deleteOperation(actionsContainer);
    }

    private void setTheImageLoader() {
        imageLoader.setImageLocation(user.getUserImageUrl());
        imageLoader.getServiceLoader().setOnSucceeded((event) -> {
            makeTheImageRound(imageLoader.getServiceLoader().getValue());
        });
        imageLoader.getServiceLoader().setOnFailed((event) -> {
            imageLoader.getServiceLoader().getException().printStackTrace();
            userContainer.getChildren().remove(userImageContainer);
            userContainer.getChildren().add(0, placeholder);
        });
        imageLoader.getServiceLoader().restart();
    }

    private void initializeName(Label name) {
        StringBinding nameBinding = Bindings.createStringBinding(() ->
                        user.getFirstName() + " " + user.getLastName(),
                user.firstNameProperty(), user.lastNameProperty()
        );
        name.textProperty().bind(nameBinding);
    }


    private void initializeEvents(Label events) {
        String eventsList = String.join(",", user.getUserEvents());
        events.setText(eventsList);
    }

    private void initializeRole(Label role) {
        role.textProperty().bindBidirectional(user.roleProperty());
        //role.setText(user.getRole());
    }

    private void deleteOperation(HBox actions) {
        DeleteButton deleteButton = new DeleteButton(secondaryLayout, thirdLayout, adminModel, user.getUserId(), DeleteOperation.DELETE_USER_PERMANENT);
        actions.getChildren().add(deleteButton);
    }


}
