package view.utility;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;

public class ImageLoadingHandler {
    private FileChooser fileChooser ;
    private Stage stage;
    public ImageLoadingHandler(Stage stage) {
        this.fileChooser =  new FileChooser();
        this.stage= new Stage();
        setupFileChooser();
    }
    private void setupFileChooser() {
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Images", "*.jpeg", "*.png"),
                new FileChooser.ExtensionFilter("JPEG files", "*.jpeg"),
                new FileChooser.ExtensionFilter("PNG files", "*.png")
        );
    }
    public File openFileChooser() {
        return fileChooser.showOpenDialog(stage);
    }
}
