package view.admin.eventsPage.manageEventAdminPage;

import be.DeleteOperation;
import exceptions.EventException;
import exceptions.ExceptionHandler;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import view.admin.eventsPage.assignCoordinatorView.AssignCoordinatorComponent;
import view.admin.eventsPage.assignedCoordinatorView.AssignedCoordinatorComponent;
import view.admin.mainAdmin.AdminModel;
import view.components.deleteEvent.DeleteButton;
import view.components.loadingComponent.LoadingActions;
import view.components.loadingComponent.LoadingComponent;
import view.utility.CommonMethods;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminEventManageController implements Initializable {
    @FXML
    private GridPane managePageContainer;
    @FXML
    private MFXButton cancelButton;
    @FXML
    private MFXButton saveButton;
    @FXML
    private VBox assignedCoordinatorsContainer;
    @FXML
    private VBox allCoordinatorsContainer;
    private LoadingComponent loadingComponent;
    private StackPane secondaryLayout, thirdLayout, fourthLayout;
    private AdminModel adminModel;
    private Service<Void> performAssignOperation;

    public AdminEventManageController(StackPane secondaryLayout, StackPane thirdLayout, StackPane fourthLayout, AdminModel model) {
        this.secondaryLayout = secondaryLayout;
        this.thirdLayout = thirdLayout;
        this.adminModel = model;
        this.fourthLayout = fourthLayout;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cancelButton.setOnAction(this::cancelChanges);
        saveButton.setOnAction(this::saveChanges);
    }


    public void saveChanges(ActionEvent event) {
        getSelectedUsers();
        initializeAssignOperationService();
    }

    private void cancelChanges(ActionEvent event) {
        CommonMethods.closeWindow(secondaryLayout);
    }

    private void getSelectedUsers() {
        for (Node node : allCoordinatorsContainer.getChildren()) {
            if (((AssignCoordinatorComponent) node).isSelected()) {
                adminModel.addSelectedUser(((AssignCoordinatorComponent) node).getSelectedEntity());
            }
        }
    }

    private void initializeLoadingComponent() {
        loadingComponent = new LoadingComponent();
        CommonMethods.showSecondaryLayout(thirdLayout, loadingComponent);
    }

    public void displayEventCoordinators() {
        if (managePageContainer.getScene() != null) {
            assignedCoordinatorsContainer.getChildren().clear();
            adminModel.getEventAssignedCoordinators().forEach(user -> {
                DeleteButton deleteButton = new DeleteButton(thirdLayout, fourthLayout, adminModel, user.getUserId(), DeleteOperation.DELETE_USER);
                AssignedCoordinatorComponent assignedCoordinatorComponent = new AssignedCoordinatorComponent(user.getFirstName(), user.getLastName());
                assignedCoordinatorComponent.getAssignComponentContainer().getChildren().add(deleteButton);
                assignedCoordinatorsContainer.getChildren().add(assignedCoordinatorComponent);
            });
        }
    }

    public void displayAllCoordinators() {
        if (managePageContainer.getScene() != null) {
            adminModel.getAllCoordinators().forEach((elem) -> {
                AssignCoordinatorComponent assignCoordinatorComponent = new AssignCoordinatorComponent(elem.getUserId(), elem.getFirstName(), elem.getLastName());
                allCoordinatorsContainer.getChildren().add(assignCoordinatorComponent);
            });
        }
    }

    private void initializeAssignOperationService() {
        initializeLoadingComponent();
        performAssignOperation = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws EventException {
                        adminModel.saveSelectedCoordinators();
                        return null;
                    }
                };
            }
        };
        performAssignOperation.setOnSucceeded((e) -> {
            Platform.runLater(() -> {
                loadingComponent.setAction(LoadingActions.SUCCES.getActionValue());
                PauseTransition pauseTransition = new PauseTransition(Duration.millis(500));
                pauseTransition.setOnFinished((ev) -> {
                    CommonMethods.closeWindow(thirdLayout);
                    CommonMethods.closeWindow(secondaryLayout);
                });
                pauseTransition.play();
            });
        });
        performAssignOperation.setOnFailed((e) -> {
            Throwable exception = performAssignOperation.getException();
            Throwable cause = exception.getCause();
            String message =  cause!=null?cause.getMessage():exception.getMessage();
            ExceptionHandler.erorrAlertMessage(message);
            Platform.runLater(() -> {
                loadingComponent.setAction(LoadingActions.FAIL.getActionValue());
                PauseTransition pauseTransition = new PauseTransition(Duration.millis(500));
                pauseTransition.setOnFinished((ev) -> {
                    CommonMethods.closeWindow(thirdLayout);
                });
                pauseTransition.play();
            });

        });
        try {
            performAssignOperation.restart();
        } catch (Exception ex) {
            ExceptionHandler.erorrAlertMessage("Failed to start the operation: " + ex.getMessage());
            CommonMethods.closeWindow(thirdLayout);
        }
    }
}
