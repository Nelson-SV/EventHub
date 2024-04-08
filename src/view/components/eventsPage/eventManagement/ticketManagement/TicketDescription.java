package view.components.eventsPage.eventManagement.ticketManagement;

import be.Ticket;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import view.components.regularTickets.deleteTicket.DeleteTicket;
import view.components.regularTickets.ticketManagement.ManageTicket;

import java.net.URL;
import java.util.ResourceBundle;

public class TicketDescription implements Initializable {
    @FXML
    private HBox ticketContainer, ticketActions;
    @FXML
    private Label ticketName;
    @FXML
    private Label ticketQuantity;
    @FXML
    private Ticket ticket;
    @FXML
    private ManageTicket manageTicket;
    @FXML
    private DeleteTicket deleteTicket;

    public TicketDescription(Ticket ticket, ManageTicket manage, DeleteTicket delete) {
        this.ticket = ticket;
        this.manageTicket = manage;
        this.deleteTicket = delete;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.ticketActions.getChildren().add(manageTicket);
        this.ticketActions.getChildren().add(deleteTicket);
        setTicketInformation(ticket);
    }

    private void setTicketInformation(Ticket ticket) {
        this.ticketName.setText(ticket.getTicketType());
        this.ticketQuantity.setText(""+ticket.getQuantity());
    }

}
