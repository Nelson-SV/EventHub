package view.components.ticketsGeneration;

import be.Ticket;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import view.components.events.CreateEventController;

import java.io.IOException;

import static javafx.scene.paint.Color.*;

public class TicketsGeneration {

    @FXML
    private FlowPane ticketTypeFlowPane;
    @FXML
    private CreateEventController createEventController;
    @FXML
    private StackPane secondaryLayout, thirdLayout;



    public TicketsGeneration(StackPane secondaryLayout, StackPane thirdLayout, CreateEventController createEventController) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("TicketsGeneration.fxml"));
        loader.setController(this);
        try {
            ticketTypeFlowPane=loader.load();
            this.secondaryLayout=secondaryLayout;
            this.thirdLayout = thirdLayout;
            this.createEventController = createEventController;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public FlowPane getRoot() {
        return ticketTypeFlowPane;
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

    public void addTickets(ActionEvent actionEvent) {

        /*
        ticket = new Ticket(ticketTypeLB.getText(), ticketQuantity.getText());

        Label ticketType = new Label(ticket.getTicketType());
        ticketType.setTextFill(BLACK);
        Label ticketQuantity = new Label(ticket.getQuantity());
        ticketQuantity.setTextFill(BLACK);

        Button remove = new Button("Remove");
        remove.setTextFill(RED);

        VBox vBox = new VBox(ticketType, ticketQuantity, remove);
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(10);

        createEventController.hBoxTickets.getChildren().add(0, vBox);

        remove.setOnAction(event -> { createEventController.hBoxTickets.getChildren().remove(vBox);
        });

        secondaryLayout.getChildren().remove(this.getRoot());

         */
    }

    public void cancelAction(ActionEvent actionEvent) {
        //secondaryLayout.getChildren().remove(this.getRoot());
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
