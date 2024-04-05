package view.admin.usersPage.usersDescription;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import view.admin.mainAdmin.AdminModel;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

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
    private AdminModel model;
    private StackPane secondaryLayout, thirdLayout, fourthLayout;

    public UserDescriptionController(AdminModel model, StackPane secondaryLayout, StackPane thirdLayout, StackPane fourthLayout) {
        this.model = model;
        this.secondaryLayout = secondaryLayout;
        this.thirdLayout = thirdLayout;
        this.fourthLayout = fourthLayout;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        makeTheImageRound();
    }

    private void makeTheImageRound() {
        Image image = null;
        try {
            image= new Image(Objects.requireNonNull(getClass().getResourceAsStream("/usersImages/im.png")));
        } catch (NullPointerException e) {
                   image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/usersImages/default.png")));
        }
        userImageContainer.setImage(image);
        userImageContainer.setPreserveRatio(false);
        double width = userImageContainer.getFitWidth();
        double height = userImageContainer.getFitHeight();
        Rectangle rectangle = new Rectangle(width, height);
        rectangle.setArcHeight(width / 2);
        rectangle.setArcWidth(height / 2);
        userImageContainer.setClip(rectangle);
    }
}
