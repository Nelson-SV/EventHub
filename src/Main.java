
import exceptions.ErrorCode;
import exceptions.ExceptionLogger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import view.components.main.MainController;

import java.util.logging.Level;


public class Main extends Application {
    public static void main(String[] args) {
        Application.launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("view/components/main/MainView.fxml"));
        Parent root = loader.load();
        MainController mc = loader.getController();
        if(mc.isInitializationError()){
           primaryStage.setTitle("EventHub");
           Alert alert = new Alert(Alert.AlertType.ERROR,ErrorCode.OPERATION_DB_FAILED.getValue());
           alert.show();
           primaryStage.close();
       }else{primaryStage.setTitle("EventHub");
           primaryStage.setScene(new Scene(root));
           primaryStage.show();}
    }
}