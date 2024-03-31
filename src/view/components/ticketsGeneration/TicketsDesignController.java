package view.components.ticketsGeneration;

import be.Ticket;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import view.components.eventsPage.eventManagement.eventCreation.CreateEventController;

import java.io.IOException;

public class TicketsDesignController extends FlowPane {

    public MFXButton addTicketBT;
    public MFXComboBox ticketTypeCombo, ticketColorCombo;
    public MFXTextField ticketPriceTF, ticketQuantity;
    public MFXComboBox eventNameCombo;
    public MFXTextField eventLocationTF, eventDateTF;
    public MFXTextField line4TF, line5TF, line6TF;
    public Label eventNameLB, eventDateLB, eventLocationLB, line4LB, line5LB, line6LB, custNameLB, custEmailLB, ticketTypeLB, ticketPriceLB;
    public FlowPane ticketPane, ticketTypeFlowPane;
    public MFXScrollPane scrollPane;
    public HBox ticketHBox;

    public CreateEventController createEventController;
    private StackPane secondaryLayout, thirdLayout;

    private Ticket ticket;
    @FXML
    private ImageView qrCode, logoImg, barCode;

    public TicketsDesignController() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("TicketsDesignCreation.fxml"));
        loader.setController(this);
        try{
            ticketPane = loader.load();
            this.getChildren().add(ticketPane);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ImageView getQrCode() {
        return qrCode;
    }

    public void setQrCode(ImageView qrCode) {
        this.qrCode = qrCode;
    }

    public ImageView getLogoImg() {
        return logoImg;
    }

    public void setLogoImg(ImageView logoImg) {
        this.logoImg = logoImg;
    }

    public ImageView getBarCode() {
        return barCode;
    }

    public void setBarCode(ImageView barCode) {
        this.barCode = barCode;
    }

    @FXML
    private void initialize() {

        /*
        TicketsDesignController ticketsDesignController = new TicketsDesignController();
        ticketHBox.getChildren().add(ticketsDesignController);

        ticketsDesignController.getQrCode().setImage(new Image("/resources/images/Image 1.png"));
        ticketsDesignController.getBarCode().setImage(new Image ("/resources/images/Image 2.png"));
        ticketsDesignController.getLogoImg().setImage(new Image("/resources/images/Image 3.png"));

         */


    }

    /*

    public void updateEventInformation(){
            ObservableList<String> list = FXCollections.observableArrayList();
            list.add("Event 1");
            list.add("Event 2");
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
        list.add("Normal TicketsDesignController");
        list.add("Special TicketsDesignController");
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
        colorList.add("Light Red");
        ticketColorCombo.setItems(colorList);

        // Add a listener to ticketColorCombo
        ticketColorCombo.textProperty().addListener((observable, oldValue, newValue) -> {
            // Update the ticketPane with the new selected color
            switch (newValue) {
                case "Yellow":
                    ticketPane.setStyle("-fx-background-color: lightyellow;");
                    break;
                case "Light Red":
                    ticketPane.setStyle("-fx-background-color: #F5979A;");
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

     */
}
