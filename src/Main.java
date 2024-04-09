
import exceptions.ErrorCode;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import view.components.listeners.InitializationErrorListener;

import java.io.IOException;
import java.util.Scanner;

public class Main extends Application {
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LogIn/LogInView.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("LogIn");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

}