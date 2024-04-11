package view.utility;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

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


    public static void makeTheImageRound(Image image, ImageView imageView) {
        imageView.setImage(image);
        imageView.setPreserveRatio(false);
        double width = imageView.getFitWidth();
        double height = imageView.getFitHeight();
        Rectangle rectangle = new Rectangle(width, height);
        rectangle.setArcHeight(width / 2);
        rectangle.setArcWidth(height / 2);
        imageView.setClip(rectangle);
    }

}
