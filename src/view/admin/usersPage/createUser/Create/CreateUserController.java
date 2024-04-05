package view.admin.usersPage.createUser.Create;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import view.admin.mainAdmin.AdminModel;
import view.admin.usersPage.threads.ImageLoader;
import view.utility.CommonMethods;
import view.utility.UserManagementValidator;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CreateUserController implements Initializable {
    @FXML
    private MFXTextField firstName;
    @FXML
    private MFXTextField lastName;
    @FXML
    private MFXComboBox<String> userRoles;
    @FXML
    private MFXPasswordField password;
    @FXML
    private ImageView addUserImageView;
    @FXML
    private MFXButton uploadImage;
    @FXML
    private MFXButton saveUser;
    @FXML
    private MFXButton cancelUser;
    private StackPane secondaryLayout,thirdLayout;
    private AdminModel adminModel;
    private ImageLoader imageLoader;
    @FXML
    private Label errorUpload;

    public CreateUserController(StackPane secondaryLayout, StackPane thirdLayout, AdminModel adminModel) {
        this.thirdLayout = thirdLayout;
        this.adminModel = adminModel;
        this.secondaryLayout= secondaryLayout;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        UserManagementValidator.addFirstNameValueListener(firstName);
        initializeImageLoader();
        addCancelAction();
        errorUpload.setVisible(true);
    }
    private void initializeImageLoader() {
        imageLoader = new ImageLoader();
        imageLoader.getDefaultImage();
        imageLoader.setOnSucceeded((event -> {
            errorUpload.setVisible(false);
            addUserImageView.setImage(imageLoader.getValue());
        }));
        imageLoader.setOnFailed((event) -> {
            errorUpload.setVisible(true);
        });
        imageLoader.setOnCancelled((event) -> {
            System.out.println("canceled");
        });
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(imageLoader);
        executorService.shutdown();
    }

    private void addCancelAction() {
        this.cancelUser.setOnAction((event -> {
            imageLoader.cancel();
            CommonMethods.closeWindow(this.secondaryLayout);
        }));
    }
}
