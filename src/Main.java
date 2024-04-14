import exceptions.ErrorCode;
import exceptions.ExceptionHandler;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import view.LogIn.LogInController;
import view.components.main.MainController;

public class Main extends Application {
    public void start(Stage primaryStage) throws Exception{

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LogIn/LogInView.fxml"));
        Parent root = loader.load();
       LogInController loginController = loader.getController();

        if(loginController.isInitializationError()){
            ExceptionHandler.errorAlertMessage(ErrorCode.OPERATION_DB_FAILED.getValue());
            primaryStage.close();
        }else{
            primaryStage.setTitle("LogIn");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        }
    }
}