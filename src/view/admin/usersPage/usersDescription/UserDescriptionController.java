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

public class UserDescriptionController  implements Initializable {
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
    private StackPane secondaryLayout,thirdLayout,fourthLayout;

    public UserDescriptionController(AdminModel model,StackPane secondaryLayout,StackPane thirdLayout,StackPane fourthLayout ) {
        this.model = model;
        this.secondaryLayout= secondaryLayout;
        this.thirdLayout = thirdLayout;
        this.fourthLayout = fourthLayout;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
makeTheImageRound();
    }
    private void makeTheImageRound(){
       // Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("../../../../resources/Image 1.png")));
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/resourcesEvent/usersimages/print.png")));

      //  Image image = new Image("../../../../resources/images/Image 1.png");
        userImageContainer.setImage(image);
        Rectangle rectangle =new Rectangle(userImageContainer.getFitWidth(),userImageContainer.getFitHeight());
        rectangle.setArcHeight(10);
        rectangle.setArcWidth(10);
        userImageContainer.setClip(rectangle);
    }


}
