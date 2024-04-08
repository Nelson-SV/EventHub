package view.components.eventsPage.eventManagement.ticketManagement;

import be.Ticket;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class TicketDescription implements Initializable {
    @FXML
    private HBox ticketContainer;
    @FXML
    private Label ticketName;
    @FXML
    private Label ticketQuantity;
    @FXML
    private Ticket ticket;


    public TicketDescription(Ticket ticket) {
        this.ticket = ticket;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setTicketInformation(ticket);
    }

    private void setTicketInformation(Ticket ticket) {
        this.ticketName.setText(ticket.getTicketType());
        this.ticketQuantity.setText(""+ticket.getQuantity());
    }

}
