
import exceptions.ErrorCode;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import view.admin.mainAdmin.AdminController;
import view.components.listeners.InitializationErrorListener;
import view.components.main.MainController;

import java.io.IOException;
import java.util.Scanner;


public class Main extends Application {

    public void start(Stage primaryStage) throws Exception {
        String userRole = getUserRole();
        switch (userRole){
            case "admin": loadAdminPage(primaryStage);
                         break;
            case "coordinator" : loadCoordinatorPage(primaryStage);
            break;
            default:
                System.out.println("User not recognized, the system will close down");
                System.exit(1);
        }
    }


    private String getUserRole() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Type 'admin' for admin page or 'coordinator' for coordinators page");
        return scanner.nextLine();
    }

    private void loadAdminPage(Stage stage){
        String resource = "view/admin/mainAdmin/AdminMain.fxml";
        String title = "EventHub/admin";
    loadPage(stage,resource,title);
    }

    private void loadCoordinatorPage(Stage stage ){
        String resource="view/components/main/MainView.fxml";
        String title = "EventHub/coordinator";
        loadPage(stage,resource,title);
    }

    private void loadPage(Stage window ,String resource,String title)  {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(resource));

        try {
            Parent root = loader.load();
            InitializationErrorListener errorListener = loader.getController();
            if(errorListener.isInitializationError()){
                showError(window,ErrorCode.OPERATION_DB_FAILED.getValue());
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