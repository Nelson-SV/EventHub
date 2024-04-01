package view.admin.eventsPage.assignButton;
import exceptions.ErrorCode;
import exceptions.ExceptionHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import view.admin.mainAdmin.AdminModel;
import java.io.IOException;

public class AssignButton  extends  VBox{
    private VBox assignContainer;
    public AssignButton(AdminModel adminModel, StackPane secondaryLayout,StackPane thirdLayout,StackPane adminFourthLayout,int eventId) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AssignComponent.fxml"));
        loader.setController(new AssignController(adminModel,secondaryLayout,thirdLayout,adminFourthLayout,eventId));
        try {
            assignContainer=loader.load();
            this.getChildren().add(assignContainer);
        } catch (IOException e) {
            e.printStackTrace();
           // ExceptionLogger.getInstance().getLogger().log(Level.SEVERE,e.getMessage());
            ExceptionHandler.erorrAlertMessage(ErrorCode.LOADING_FXML_FAILED.getValue());
        }
    }
}
