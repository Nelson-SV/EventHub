package view.components.SellingTickets;

import be.Customer;
import be.Event;
import be.Ticket;
import exceptions.ErrorCode;
import exceptions.EventException;
import exceptions.ExceptionHandler;
import exceptions.TicketException;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import view.components.main.Model;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class SellingViewController implements Initializable {

    @FXML
    //private GridPane editGridPane;
    private VBox box;
    private VBox vBox;
    private Model model;
    @FXML
    private TextField name;
    @FXML
    private  TextField lastName;
    @FXML
    private TextField email;
    @FXML
    private MFXComboBox allEvents;
    @FXML
    private MFXComboBox allEventTickets;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<String> eventNames = model.getAllEventNames();
        allEvents.setItems(FXCollections.observableArrayList(eventNames));
        allEvents.setOnAction(event -> {
            allEventTickets.clearSelection();
            String selectedEventName = (String) allEvents.getSelectionModel().getSelectedItem();
            int eventId = model.getEventIdByName(selectedEventName);
            if (eventId != -1) {
                try {
                    ObservableMap<Integer, Ticket> tickets = model.getTicketsForEvent(eventId);
                    List<String> ticketInfoList = tickets.values().stream()
                            .map(ticket -> ticket.getTicketType() + " - Q" + ticket.getQuantity() + " - " + ticket.getTicketPrice() +"DKK")
                            .collect(Collectors.toList());
                    allEventTickets.setItems(FXCollections.observableArrayList(ticketInfoList));
                } catch (TicketException e) {
                    // Handle exception
                }
            }
        });




    }

    public SellingViewController(VBox vbox, Model model) {
        this.model=model;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sellingView.fxml"));
        loader.setController(this);
        try {
          box=loader.load();
          this.vBox = vbox;
        } catch (IOException e) {
            ExceptionHandler.erorrAlertMessage(ErrorCode.LOADING_FXML_FAILED.getValue());
        }

    }

    public VBox getRoot() {
        return box;
    }

    public void sell(ActionEvent actionEvent) throws EventException {
        String customerName = name.getText();
        String customerLastName = lastName.getText();
        String customerEmail = email.getText();

        Customer customer = new Customer(customerName, customerLastName, customerEmail);
        model.addCustomer(customer);

    }





}
