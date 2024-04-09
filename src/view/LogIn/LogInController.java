package view.LogIn;

import com.sun.tools.javac.Main;
import exceptions.ErrorCode;
import exceptions.EventException;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import view.components.listeners.InitializationErrorListener;
import view.components.main.Model;

import java.io.IOException;

import static java.awt.SystemColor.window;

public class LogInController {
    @FXML
    public MFXPasswordField password;
    @FXML
    public MFXTextField userName;
    private Model model;



    public void signIn(ActionEvent actionEvent) {
        String enteredUsername = userName.getText();
        String enteredPassword = password.getText();

        try {
            String role = model.checkUser(enteredUsername, enteredPassword);

            if (role != null) {
                // User authenticated successfully, load corresponding page
                Stage stage = (Stage) userName.getScene().getWindow();
                stage.close();

                if ("admin".equals(role)) {
                    loadAdminPage(new Stage());
                } else if ("coordinator".equals(role)) {
                    loadCoordinatorPage(new Stage());
                }

                // Save the current user's state
                // You can create a model class to store information about the logged-in user
                // For example:
                // UserModel currentUser = new UserModel(enteredUsername, role);
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
}

