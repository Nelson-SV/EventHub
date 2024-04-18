package view.components.regularTickets.ticketGeneration;

import be.Ticket;
import exceptions.ErrorCode;
import exceptions.EventException;
import exceptions.ExceptionHandler;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
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
    @FXML
    private Ticket selectedTicket;
    @FXML
    private VBox selectedVbox;



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
            ExceptionHandler.errorAlertMessage(ErrorCode.LOADING_FXML_FAILED.getValue());
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
        boolean isTicketValid = TicketValidator.isTicketValid(ticketTypeTF, ticketPriceTF, ticketQuantityTF);
        if(isTicketValid) {
            Ticket ticket = new Ticket(ticketTypeTF.getText(), Integer.parseInt(ticketQuantityTF.getText()),
                    new BigDecimal(ticketPriceTF.getText()), null);

            Hyperlink ticketType = new Hyperlink(ticket.getTicketType());
            ticketType.setTextFill(BLACK);
            ticketType.setUnderline(true);
            ticketType.setOnMouseClicked(event -> editTicket(ticket, ticketType.getParent()));

            Label ticketQuantity = new Label(ticket.getQuantity() + "");
            ticketQuantity.setTextFill(BLACK);

            Button remove = new Button("Remove");
            remove.setTextFill(RED);

            VBox vBox = new VBox(ticketType, ticketQuantity, remove);
            vBox.setAlignment(Pos.CENTER);
            vBox.setSpacing(10);

            if (selectedTicket  != ticket && selectedTicket != null){
                removeTicket(selectedVbox, selectedTicket);
            }
            createEventController.hBoxTickets.setAlignment(Pos.CENTER_LEFT);
            createEventController.hBoxTickets.getChildren().add(0, vBox);

            model.getNewAddedTicket(ticket);

            remove.setOnAction(event -> {
                removeTicket(vBox, ticket);
            });

            closeWindow();
        }

    }

    private void removeTicket(VBox selectedVbox, Ticket selectedTicket) {
        createEventController.hBoxTickets.getChildren().remove(selectedVbox);
        model.removeAddedTicket(selectedTicket);
    }

    private void editTicket(Ticket ticket, Parent parent) {
        selectedTicket = ticket;
        selectedVbox = (VBox) parent;

        if (selectedTicket != null) {
            createEventController.editTicket(this);

            ticketTypeTF.setText(selectedTicket.getTicketType());
            ticketQuantityTF.setText(String.valueOf(selectedTicket.getQuantity()));
            ticketPriceTF.setText(selectedTicket.getTicketPrice().toString());
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
