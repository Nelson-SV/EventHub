package view.utility;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
public class CommonMethods {
    public static void closeWindow(ActionEvent event , StackPane secondaryLayout){
        secondaryLayout.getChildren().clear();
        secondaryLayout.setDisable(true);
        secondaryLayout.setVisible(false);
    }
    public static void closeWindow(StackPane secondaryLayout){
        secondaryLayout.getChildren().clear();
        secondaryLayout.setDisable(true);
        secondaryLayout.setVisible(false);
    }

    public  static  void showSecondaryLayout(StackPane stackPane,Node node) {
        stackPane.getChildren().clear();
        stackPane.getChildren().add(node);
        stackPane.setVisible(true);
        stackPane.setDisable(false);
    }


}
