package view.components.ticketsGeneration;

import be.Ticket;
import exceptions.EventException;
import exceptions.TicketException;
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
import view.components.main.Model;

import java.io.IOException;

import static javafx.scene.paint.Color.*;

public class TicketsGeneration {

    @FXML
    private FlowPane ticketTypeFlowPane;
    @FXML
    private CreateEventController createEventController;
    @FXML
    private StackPane secondaryLayout, thirdLayout;
    @FXML
    private MFXTextField ticketTypeTF, ticketPriceTF, ticketQuantityTF;
    @FXML
    private Model model;



    public TicketsGeneration(StackPane secondaryLayout, StackPane thirdLayout, CreateEventController createEventController, Model model) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("TicketsGeneration.fxml"));
        loader.setController(this);
        try {
            ticketTypeFlowPane=loader.load();
            this.secondaryLayout=secondaryLayout;
            this.thirdLayout = thirdLayout;
            this.createEventController = createEventController;
            this.model = model;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void initialize() {

    }

    @FXML
    private void addTickets(ActionEvent actionEvent) throws EventException, TicketException {

        Ticket ticket = new Ticket(ticketTypeTF.getText(), Integer.parseInt(ticketQuantityTF.getText()), Float.parseFloat(ticketPriceTF.getText()));

        Label ticketType = new Label(ticket.getTicketType());
        ticketType.setTextFill(BLACK);
        Label ticketQuantity = new Label(ticket.getQuantity() + "");
        ticketQuantity.setTextFill(BLACK);

        Button remove = new Button("Remove");
        remove.setTextFill(RED);

        VBox vBox = new VBox(ticketType, ticketQuantity, remove);
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(10);

        createEventController.hBoxTickets.setAlignment(Pos.CENTER_LEFT);
        createEventController.hBoxTickets.getChildren().add(0, vBox);

        model.addTicket(ticket);

        remove.setOnAction(event -> { createEventController.hBoxTickets.getChildren().remove(vBox);
        });

        closeWindow();
    }

    @FXML
    private void cancelAction(ActionEvent actionEvent) {
        closeWindow();
    }

    private void closeWindow() {
        thirdLayout.getChildren().clear();
        thirdLayout.setDisable(true);
        thirdLayout.setVisible(false);
    }

    public FlowPane getRoot() {
        return ticketTypeFlowPane;
    }

    public MFXTextField getTicketTypeTF() {
        return ticketTypeTF;
    }

    public void setTicketTypeTF(MFXTextField ticketTypeTF) {
        this.ticketTypeTF = ticketTypeTF;
    }

    public MFXTextField getTicketPriceTF() {
        return ticketPriceTF;
    }

    public void setTicketPriceTF(MFXTextField ticketPriceTF) {
        this.ticketPriceTF = ticketPriceTF;
    }

    public MFXTextField getTicketQuantityTF() {
        return ticketQuantityTF;
    }

    public void setTicketQuantityTF(MFXTextField ticketQuantityTF) {
        this.ticketQuantityTF = ticketQuantityTF;
    }
}
