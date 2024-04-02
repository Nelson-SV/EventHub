package view.admin.eventsPage.assignCoordinatorView;
import exceptions.ErrorCode;
import exceptions.ExceptionHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.io.IOException;

public class AssignCoordinatorComponent extends HBox {
    @FXML
    private HBox assignCoordinatorContainer;
    private AssignCoordinatorController assignCoordinatorController;
    public AssignCoordinatorComponent(int entityId,String firstName,String lastName) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AssignCoordinatorView.fxml"));
        assignCoordinatorController=new AssignCoordinatorController(entityId,firstName,lastName);
        loader.setController(assignCoordinatorController);
        try {
            assignCoordinatorContainer =loader.load();
          this.getChildren().add(assignCoordinatorContainer);
        } catch (IOException e) {
            e.printStackTrace();
            ExceptionHandler.erorrAlertMessage(ErrorCode.LOADING_FXML_FAILED.getValue());
        }
    }

    public boolean isSelected(){
        return assignCoordinatorController.isChecked();
    }
    public int getSelectedEntity(){
        return assignCoordinatorController.getSelectedEntityId();
    }

}
