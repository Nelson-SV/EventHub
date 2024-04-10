package view.admin.usersPage.editUserPage;

import dal.FileHandler;
import exceptions.ErrorCode;
import exceptions.ExceptionHandler;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.jetbrains.annotations.NotNull;
import view.admin.mainAdmin.AdminModel;
import view.admin.usersPage.threads.ImageLoader;
import view.admin.usersPage.threads.UploadedImageLoader;
import view.components.loadingComponent.LoadingActions;
import view.components.loadingComponent.LoadingComponent;
import view.utility.CommonMethods;
import view.utility.ImageLoadingHandler;
import view.utility.UserManagementValidator;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EditUserPageController implements Initializable {

    @FXML
    private GridPane editUserContainerComponent;
    @FXML
    private MFXTextField firstName;
    @FXML
    private MFXTextField lastName;
    @FXML
    private MFXComboBox<String> userRoles;
    @FXML
    private MFXPasswordField password;
    @FXML
    private Label errorUpload;
    @FXML
    private ImageView addUserImageView;
    @FXML
    private MFXButton uploadImage;
    @FXML
    private MFXButton saveUser;
    @FXML
    private MFXButton cancelUser;

    private StackPane secondaryLayout, thirdLayout, fourthLayout;
    private AdminModel adminModel;
    private ImageLoader imageLoader;
    private ImageLoadingHandler imageLoadingHandler;
    private Service<Void> saveService;
    private LoadingComponent loadingComponent;
    private static final String defaultImageName = "default.png";

    public EditUserPageController(StackPane secondaryLayout, StackPane thirdLayout, StackPane fourthLayout, AdminModel adminModel, int userId) {
        this.thirdLayout = thirdLayout;
        this.adminModel = adminModel;
        this.secondaryLayout = secondaryLayout;
        this.fourthLayout = fourthLayout;
        imageLoader = new ImageLoader(defaultImageName);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        UserManagementValidator.addNameValueListener(firstName);

        addCancelAction();
        UserManagementValidator.addNameValueListener(firstName);
        UserManagementValidator.addNameValueListener(lastName);
        UserManagementValidator.addRoleValueListener(userRoles);
        UserManagementValidator.addPasswordValidator(password);
        populateRolesValues(userRoles);
        populateFieldsWithUserData(firstName, lastName, userRoles, password);
        addOnSaveAction(saveUser);
        addLoadImageBtnAction();
    }
    private void addCancelAction() {
        this.cancelUser.setOnAction((event -> {
            adminModel.setUploadedImage(null);
            CommonMethods.closeWindow(this.secondaryLayout);
        }));
    }
    private void populateFieldsWithUserData(MFXTextField firstName, MFXTextField lastName, MFXComboBox<String> roles, MFXPasswordField password) {
        firstName.textProperty().bindBidirectional(adminModel.getSelectedUserToEdit().firstNameProperty());
        lastName.textProperty().bindBidirectional(adminModel.getSelectedUserToEdit().lastNameProperty());
        roles.textProperty().bindBidirectional(adminModel.getSelectedUserToEdit().roleProperty());
        roles.valueProperty().bindBidirectional(adminModel.getSelectedUserToEdit().roleProperty());
        password.textProperty().bindBidirectional(adminModel.getSelectedUserToEdit().passwordProperty());
        imageLoader.setImageLocation(adminModel.getSelectedUserToEdit().getUserImageUrl());
        initializeImageLoader();
    }

    private void populateRolesValues(MFXComboBox<String> userRoles) {
        userRoles.setItems(adminModel.getRoles());
     }

    private void initializeImageLoader() {
        imageLoader.getServiceLoader().setOnSucceeded((event -> {
            errorUpload.setVisible(false);
            addUserImageView.setImage(imageLoader.getServiceLoader().getValue());
        }));
        imageLoader.getServiceLoader().setOnFailed((event) -> {
            imageLoader.getServiceLoader().getException().printStackTrace();
            errorUpload.setVisible(true);
        });
        imageLoader.getServiceLoader().restart();
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
            UploadedImageLoader uploadedImageLoader = getUploadedImageLoader(file);
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.execute(uploadedImageLoader);
            executorService.shutdown();
        }
    }

    @NotNull
    private UploadedImageLoader getUploadedImageLoader(File file) {
        UploadedImageLoader uploadedImageLoader = new UploadedImageLoader(file.toURI().toString());
        uploadedImageLoader.setOnSucceeded((event) -> {
            adminModel.setUploadedImage(file);
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
            startSaveService();
            loadingComponent = new LoadingComponent();
            CommonMethods.showSecondaryLayout(thirdLayout,loadingComponent);
        }
    }

    private void startSaveService() {
        saveService = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        adminModel.editUser();
                        return null;
                    }
                };
            }
        };
        saveService.setOnSucceeded((event -> {
            loadingComponent.setAction(LoadingActions.SUCCES.getActionValue());

            PauseTransition pauseTransition = new PauseTransition(Duration.millis(500));
            pauseTransition.setOnFinished((ev) -> {
                CommonMethods.closeWindow(secondaryLayout);
                CommonMethods.closeWindow(thirdLayout);
                adminModel.getUsersDisplayer().displayUsers();
            });
            pauseTransition.play();
        }));
        saveService.setOnFailed(event -> {
            loadingComponent.setAction(LoadingActions.FAIL.getActionValue());
            PauseTransition pauseTransition = new PauseTransition(Duration.millis(500));
            pauseTransition.setOnFinished((ev) -> {
                CommonMethods.closeWindow(thirdLayout);
                ExceptionHandler.errorAlertMessage(ErrorCode.COPY_FAILED.getValue());
            });
            pauseTransition.play();
            saveService.getException().printStackTrace();
        });
        saveService.restart();
    }

}
