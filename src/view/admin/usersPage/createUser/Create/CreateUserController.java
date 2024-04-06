package view.admin.usersPage.createUser.Create;

import exceptions.ErrorCode;
import exceptions.ExceptionHandler;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;
import view.admin.mainAdmin.AdminModel;
import view.admin.usersPage.threads.ImageCopyHandler;
import view.admin.usersPage.threads.ImageLoader;
import view.admin.usersPage.threads.UploadedImageLoader;
import view.utility.CommonMethods;
import view.utility.ImageLoadingHandler;
import view.utility.UserManagementValidator;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CreateUserController implements Initializable {
    @FXML
    private GridPane addUserContainerComponent;
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
    @FXML
    private Label errorUpload;
    private StackPane secondaryLayout, thirdLayout;
    private AdminModel adminModel;
    private ImageLoader imageLoader;
    private ImageLoadingHandler imageLoadingHandler;
    private ImageCopyHandler imageCopyHandler;

    public CreateUserController(StackPane secondaryLayout, StackPane thirdLayout, AdminModel adminModel) {
        this.thirdLayout = thirdLayout;
        this.adminModel = adminModel;
        this.secondaryLayout = secondaryLayout;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        UserManagementValidator.addNameValueListener(firstName);
        initializeImageLoader();
        addCancelAction();
        UserManagementValidator.addNameValueListener(firstName);
        UserManagementValidator.addNameValueListener(lastName);
        populateRolesValues(userRoles);
        UserManagementValidator.addRoleValueListener(userRoles);
        UserManagementValidator.addPasswordValidator(password);
        addOnSaveAction(saveUser);
        addLoadImageBtnAction();
    }

    private void initializeImageLoader() {
        imageLoader = new ImageLoader();
        imageLoader.getDefaultImage();
        imageLoader.setOnSucceeded((event -> {
            errorUpload.setVisible(false);
            addUserImageView.setImage(imageLoader.getValue());
        }));
        imageLoader.setOnFailed((event) -> {
            imageLoader.getException().printStackTrace();
            errorUpload.setVisible(true);
        });
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(imageLoader);
        executorService.shutdown();
    }


    private void addCancelAction() {
        this.cancelUser.setOnAction((event -> {
            adminModel.setUploadedImage(null);
            CommonMethods.closeWindow(this.secondaryLayout);
        }));
    }

    private void populateRolesValues(MFXComboBox<String> userRoles) {
        userRoles.setItems(adminModel.getRoles());
    }

    private void addOnSaveAction(MFXButton saveBtn) {
        saveBtn.setOnAction((event) -> {
            validateAndSave();
        });
    }

    private void validateAndSave() {
        boolean isRoleValid = UserManagementValidator.isRoleValid(userRoles);
        boolean isFirstNameValid = UserManagementValidator.isNameValid(firstName);
        boolean isLastNameValid = UserManagementValidator.isNameValid(lastName);
        boolean isPasswordValid = UserManagementValidator.isPasswordValid(password);
        if (isRoleValid && isFirstNameValid && isLastNameValid && isPasswordValid) {
            System.out.println("is valid");
        }
    }

    private void saveImage() {
        imageCopyHandler = new ImageCopyHandler(adminModel.getUploadedImage());
    }

    private void addLoadImageBtnAction() {
        uploadImage.setOnAction((event) -> {
            openFileChooser();
        });
    }

    private void openFileChooser() {

        this.errorUpload.setVisible(false);
        Stage stage = (Stage) secondaryLayout.getScene().getWindow();
        this.imageLoadingHandler = new ImageLoadingHandler(stage);
        File file = imageLoadingHandler.openFileChooser();
        if (file != null) {
            if (!adminModel.isUnique(file)) {
                adminModel.setUploadedImage(file);
                UploadedImageLoader uploadedImageLoader = getUploadedImageLoader(file);
                ExecutorService executorService = Executors.newSingleThreadExecutor();
                executorService.execute(uploadedImageLoader);
                executorService.shutdown();
            } else {
                ExceptionHandler.errorAlertMessage(ErrorCode.FILE_ALREADY_EXISTS.getValue());
            }
        }
    }

    @NotNull
    private UploadedImageLoader getUploadedImageLoader(File file) {
        UploadedImageLoader uploadedImageLoader = new UploadedImageLoader(file.toURI().toString());
        uploadedImageLoader.setOnSucceeded((event) -> {
            if (this.addUserImageView.getImage() != null) {
                this.addUserImageView.getImage().cancel();
            }
            this.addUserImageView.setImage(uploadedImageLoader.getValue());
        });
        uploadedImageLoader.setOnFailed((event) -> {
            this.errorUpload.setVisible(true);
        });
        return uploadedImageLoader;
    }
}
