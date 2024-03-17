package view.components.manageButton;

import exceptions.ErrorCode;
import exceptions.EventException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class ManageController implements Initializable{
    @FXML
    private VBox manageControl;
    @FXML
    private StackPane editWindow;
    public ManageController(StackPane editwindow){
        this.editWindow=editwindow;
    }
    private void openEditWindow(MouseEvent event) {
        System.out.println("your window will be opened");
        this.editWindow.setVisible(true);
        this.editWindow.setDisable(false);
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        manageControl.addEventHandler(MouseEvent.MOUSE_CLICKED, this::openEditWindow);
    }
}
