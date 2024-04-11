import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LogIn/LogInView.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("LogIn");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
//        TicketsPrinter ticketsPrinter = new TicketsPrinter();
//
//        ticketsPrinter.print();
    }
}