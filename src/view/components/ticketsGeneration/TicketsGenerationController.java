package view.components.ticketsGeneration;

import be.Ticket;
import exceptions.ErrorCode;
import exceptions.EventException;
import exceptions.ExceptionHandler;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import view.components.eventsPage.eventManagement.eventCreation.CreateEventController;
import view.components.main.Model;
import view.utility.TicketValidator;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static javafx.scene.paint.Color.*;

public class TicketsGenerationController {

    @FXML
    private FlowPane ticketTypeFlowPane;
    @FXML
    private CreateEventController createEventController;
    @FXML
    private StackPane secondaryLayout, thirdLayout;
    @FXML
    private MFXTextField ticketTypeTF, ticketPriceTF, ticketQuantityTF;

    @FXML
    private List<Ticket> newTickets;
    @FXML
    private Model model;



    public TicketsGenerationController(StackPane secondaryLayout, StackPane thirdLayout, CreateEventController createEventController, Model model) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("TicketsGeneration.fxml"));
        loader.setController(this);
        try {
            ticketTypeFlowPane=loader.load();
            this.secondaryLayout=secondaryLayout;
            this.thirdLayout = thirdLayout;
            this.createEventController = createEventController;
            this.model = model;
        } catch (IOException e) {
            ExceptionHandler.erorrAlertMessage(ErrorCode.LOADING_FXML_FAILED.getValue());
        }
    }

    @FXML
    private void initialize() {
        newTickets = new ArrayList<>();
        TicketValidator.addTicketListeners(ticketTypeTF, ticketPriceTF, ticketQuantityTF);

        TicketValidator.addTypeToolTip(ticketTypeTF);
        TicketValidator.addPriceToolTip(ticketPriceTF);
        TicketValidator.addQuantityToolTip(ticketQuantityTF);
    }

    @FXML
    private void addTickets(ActionEvent actionEvent) throws EventException {
        boolean isTicketValid = TicketValidator.isTicketValid(ticketTypeTF, ticketPriceTF, ticketQuantityTF );

        if(isTicketValid) {
            Ticket ticket = new Ticket(ticketTypeTF.getText(), Integer.parseInt(ticketQuantityTF.getText()), new BigDecimal(ticketPriceTF.getText()));

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

            model.getNewTicket(ticket);


            remove.setOnAction(event -> {
                createEventController.hBoxTickets.getChildren().remove(vBox);
                model.removeTicket(ticket);
            });
            closeWindow();
        }

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
