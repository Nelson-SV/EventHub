package view.utility;
import javafx.event.ActionEvent;
import javafx.scene.layout.StackPane;
public class CommonMethods {
    public static void closeWindow(ActionEvent event , StackPane secondaryLayout){
        secondaryLayout.getChildren().clear();
        secondaryLayout.setDisable(true);
        secondaryLayout.setVisible(false);
    }
}
