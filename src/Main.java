import dal.DataManager;
import dal.IData;
import exceptions.EventException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.logging.Level;

public class Main extends Application {
    public static void main(String[] args) {
        Application.launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainView.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("EventHub");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        /***
         *  The following 3 lines are for testing only
         */

        EventException eventException = new EventException("Test",null,null, Level.SEVERE);
        IData teste= new DataManager();
        teste.test();
    }
}