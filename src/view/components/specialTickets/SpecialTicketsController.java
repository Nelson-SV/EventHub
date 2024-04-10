package view.components.specialTickets;

import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import view.components.main.Model;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SpecialTicketsController implements Initializable {

    public VBox vBox;
    @FXML
    private VBox box;
    public HBox ticketHBox;
    public MFXTextField ticketTypeTF;
    public MFXTextField ticketQuantityTF;
    public MFXComboBox eventCB;
    public MFXComboBox ticketColorCB;
    public MFXTextField ticketPriceTF;
    @FXML
    private StackPane secondaryLayout;
    @FXML
    private Model model;

    public SpecialTicketsController(VBox box, Model model){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SpecialTicketsWindow.fxml"));
        loader.setController(this);
        try {
            vBox=loader.load();
            this.model = model;
            this.box = box;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public VBox getRoot() {
        return vBox;
    }

    public void addTicket(ActionEvent actionEvent) {
    }

    public void cancelAction(ActionEvent actionEvent) {
    }
}
