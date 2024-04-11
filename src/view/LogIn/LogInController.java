package view.LogIn;

import be.Role;
import be.User;
import com.sun.tools.javac.Main;
import exceptions.ErrorCode;
import exceptions.EventException;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;
import view.components.listeners.InitializationErrorListener;
import view.components.main.Model;
import view.utility.EditEventValidator;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static java.awt.SystemColor.window;

public class LogInController implements Initializable {
    @FXML
    public MFXPasswordField password;
    @FXML
    public MFXTextField userName;
    private Model model;

    @FXML
    private Label invalidError;
    private static final PseudoClass ERROR_PSEUDO_CLASS = PseudoClass.getPseudoClass("error");


    public void signIn(ActionEvent actionEvent) {
        String enteredUsername = userName.getText().trim();
        String enteredPassword = password.getText().trim();


        try {
            if(!userName.getText().isEmpty() && !password.getText().isEmpty()) {
                System.out.println(enteredPassword + enteredUsername);
                User user = model.checkUser(enteredUsername, enteredPassword);
                System.out.println(user == null);


                if (user != null) {
                    // User authenticated successfully, load corresponding page
                    Stage stage = (Stage) userName.getScene().getWindow();
                    stage.close();

                    if (Role.ADMIN.getValue().equals(user.getRole())) {
                        loadAdminPage(new Stage());
                    } else if (Role.EVENT_COORDINATOR.getValue().equals(user.getRole())) {
                        loadCoordinatorPage(new Stage());
                    }

                } else {
                    userName.clear();
                    password.clear();
                    Platform.runLater(() -> {
                        invalidError.setText("Unrecognized userName or password");
                    });
                    // Schedule a task to clear the error message after 5 seconds
                    PauseTransition pauseTransition = new PauseTransition(Duration.seconds(5));
                    pauseTransition.setOnFinished(event -> {
                        invalidError.setText(""); // Clear the error message
                    });
                    pauseTransition.play();

                }
            } else {
                userName.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, true);
                password.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, true);
                Platform.runLater(() -> {
                    invalidError.setText("Fill in userName and password");
                });
                // Schedule a task to clear the error message after 5 seconds
                PauseTransition pauseTransition = new PauseTransition(Duration.seconds(5));
                pauseTransition.setOnFinished(event -> {
                    invalidError.setText(""); // Clear the error message
                });
                pauseTransition.play();
            }
        } catch (EventException e) {

        }
    }

    // Other method
    private void loadAdminPage(Stage stage){

        String resource = "/view/admin/mainAdmin/AdminMain.fxml";
        String title = "EventHub/admin";
        loadPage(stage,resource,title);
    }

    private void loadCoordinatorPage(Stage stage ){

        String resource="/view/components/main/MainView.fxml";
        String title = "EventHub/coordinator";
        loadPage(stage,resource,title);
    }

    private void loadPage(Stage window ,String resource,String title)  {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(resource));

        try {
            Parent root = loader.load();
            InitializationErrorListener errorListener = loader.getController();
            if(errorListener.isInitializationError()){
                showError(window, ErrorCode.OPERATION_DB_FAILED.getValue());
            }else{
                window.setTitle(title);
                window.setScene(new Scene(root));
                window.show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            showError(window,ErrorCode.LOADING_FXML_FAILED.getValue());
        }
    }


    private void showError(Stage stage, String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR, errorMessage);
        alert.show();
        stage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            model = Model.getInstance();
        } catch (EventException e) {
            throw new RuntimeException(e);
        }



        password.textProperty().addListener((observable, oldValue, newValue) -> {
            // Clear the error pseudo-class state when user starts typing
            password.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, false);
        });

        userName.textProperty().addListener((observable, oldValue, newValue) -> {
            // Clear the error pseudo-class state when user starts typing
            userName.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, false);
        });
    }
}

