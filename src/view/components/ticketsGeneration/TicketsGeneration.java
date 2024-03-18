package view.components.ticketsGeneration;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import view.components.main.MainController;

import java.io.IOException;

public class TicketsGeneration {

    public MFXButton addTicketBT;
    public MFXComboBox ticketTypeCombo, ticketColorCombo;
    public MFXTextField ticketPriceTF;
    public MFXComboBox eventNameCombo;
    public MFXTextField eventLocationTF, eventDateTF;
    public MFXTextField line4TF, line5TF, line6TF;
    public Label eventNameLB, eventDateLB, eventLocationLB, line4LB, line5LB, line6LB, custNameLB, custEmailLB, ticketTypeLB, ticketPriceLB;
    public ImageView qrCode;
    public FlowPane ticketPane;
    public MFXScrollPane scrollPane;

    public MainController mainController;
    private StackPane secondaryLayout;

    public MFXScrollPane getRoot() {
        return scrollPane;
    }


    public TicketsGeneration(StackPane secondaryLayout) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("TicketsGeneration.fxml"));
        loader.setController(this);
        try {
            scrollPane=loader.load();
            this.secondaryLayout=secondaryLayout;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setMainController(MainController controller){
        this.mainController = controller;
    }

    public void initialize() {
        updateEventInformation();
        updateTicketInformation();
        updateExtraLines();

    }

    public void addTickets(ActionEvent actionEvent) {
        secondaryLayout.getChildren().remove(this.getRoot());
    }

    public void updateEventInformation(){
            ObservableList<String> list = FXCollections.observableArrayList();
            list.add("Test");
            list.add("Test2");
            eventNameCombo.setItems(list);

            eventNameCombo.textProperty().addListener((observable, oldValue, newValue) -> {
                // Update the label with the new selected Event
                eventNameLB.setText(newValue);
            });

            eventDateTF.textProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue.isEmpty()) {
                    eventDateLB.setText("Event Date Line");
                } else {
                    // If the text field has text, update the label with the new text
                    eventDateLB.setText(newValue);
                }
            });

            eventLocationTF.textProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue.isEmpty()) {
                    eventLocationLB.setText("Event Location Line");
                } else {
                    // If the text field has text, update the label with the new text
                    eventLocationLB.setText(newValue);
                }
            });
    }

    public void updateTicketInformation(){
        ObservableList<String> list = FXCollections.observableArrayList();
        list.add("Type");
        list.add("Type2");
        ticketTypeCombo.setItems(list);

        // Add a listener to ticketTypeCombo
        ticketTypeCombo.textProperty().addListener((observable, oldValue, newValue) -> {
            // Update the label with the new selected type
            ticketTypeLB.setText(newValue);
        });

        ticketPriceTF.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                ticketPriceLB.setText("dkk");
            } else {
                // If the text field has text, update the label with the new text
                ticketPriceLB.setText(newValue + "dkk");
            }
        });

        ObservableList<String> colorList = FXCollections.observableArrayList();
        colorList.add("Yellow");
        colorList.add("White");
        ticketColorCombo.setItems(colorList);

        // Add a listener to ticketColorCombo
        ticketColorCombo.textProperty().addListener((observable, oldValue, newValue) -> {
            // Update the ticketPane with the new selected color
            switch (newValue) {
                case "Yellow":
                    ticketPane.setStyle("-fx-background-color: lightyellow;");
                    break;
                case "White":
                    ticketPane.setStyle("-fx-background-color: white;");
                    break;
            }
        });
    }

    public void updateExtraLines(){
        line4TF.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                line4LB.setText("Fourth Line");
            } else {
                // If the text field has text, update the label with the new text
                line4LB.setText(newValue);
            }
        });

        line5TF.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                line5LB.setText("Fifth Line");
            } else {
                // If the text field has text, update the label with the new text
                line5LB.setText(newValue);
            }
        });

        line6TF.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                line6LB.setText("Sixth Line");
            } else {
                // If the text field has text, update the label with the new text
                line6LB.setText(newValue);
            }
        });
    }

}
