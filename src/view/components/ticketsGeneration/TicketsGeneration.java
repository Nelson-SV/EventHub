package view.components.ticketsGeneration;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import view.components.main.MainController;

public class TicketsGeneration {

    public MFXButton addTicketBT;
    public MFXComboBox ticketTypeCombo, ticketColorCombo;
    public MFXTextField ticketPriceTF;
    public MFXComboBox eventNameCombo;
    public MFXTextField eventLocationTF, eventDateTF;
    public MFXTextField line4TF, line5TF, line6TF;
    public Label eventNameLB, eventDateLB, eventLocationLB, line4LB, line5LB, line6LB, custNameLB, custEmailLB, ticketTypeLB, ticketPriceLB;
    public ImageView qrCode;

    public MainController mainController;
    public MFXScrollPane scrollPane;

    public void setMainController(MainController controller){
        this.mainController = controller;
    }

    public void initialize() {
        updateEventName();
    }

    public void addTickets(ActionEvent actionEvent) {
    }

    public void updateEventName(){
            ObservableList<String> list = FXCollections.observableArrayList();
            list.add("Test");
            list.add("Test2");
            eventNameCombo.setItems(list);

            // Add a listener to the selected Name of eventNameCombo
            eventNameCombo.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                // Update the label with the new selected Name
                eventNameLB.setText((String) newValue);
            });
    }

}
