package view.components.main;

import exceptions.EventException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import view.components.manageButton.ManageController;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private StackPane secondaryLayout;
    @FXML
    private HBox navigation;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            ManageController manageController = new ManageController(secondaryLayout);
            navigation.getChildren().add(manageController.getRoot());
        } catch (EventException e) {
            // needs to be handled, maybe add Exception handler class
            throw new RuntimeException(e);
        }
    }


    @FXML
    private void closeWindow(ActionEvent event) {
        this.secondaryLayout.setDisable(true);
        this.secondaryLayout.setVisible(false);
    }
}
