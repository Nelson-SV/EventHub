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
import javafx.scene.control.ListView;
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
    @FXML
    private MFXComboBox specialTickets;
    @FXML
    private TextField eventTicketsAmount;
    @FXML
    private ListView allSelectedTickets;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<String> eventNames = model.getAllEventNames();
        allEvents.setItems(FXCollections.observableArrayList(eventNames));
        allEvents.setOnAction(event -> {
            loadTicketsInfo();
            loadSpecialTicketsInfo();
        });
    }

    private void loadTicketsInfo(){
        allEventTickets.clearSelection();
        String selectedEventName = (String) allEvents.getSelectionModel().getSelectedItem();
        int eventId = model.getEventIdByName(selectedEventName);
        if (eventId != -1) {
            try {
                ObservableMap<Integer, Ticket> ticketsMap = model.getTicketsForEvent(eventId);

                List<String> ticketInfoList = ticketsMap.values().stream()
                        .map(ticket -> ticket.getTicketType() + " - Q" + ticket.getQuantity() + " - " + ticket.getTicketPrice() +"DKK")
                        .collect(Collectors.toList());

                allEventTickets.setItems(FXCollections.observableArrayList(ticketInfoList));
            } catch (EventException e) {
                // Handle exception
            }
        }


    }

    private void loadSpecialTicketsInfo(){
            specialTickets.clearSelection();
            String selectedEventName = (String) allEvents.getSelectionModel().getSelectedItem();
            int eventId = model.getEventIdByName(selectedEventName);
            if (eventId != -1) {
                try {
                    ObservableMap<Integer, Ticket> specialTicketsMap  = model.getSpecialTicketsForEventOrNot(eventId);

                    List<String> specialTicketInfoList = specialTicketsMap .values().stream()
                            .map(ticket -> ticket.getTicketType() + " - Q" + ticket.getQuantity() + " - " + ticket.getTicketPrice() +"DKK")
                            .collect(Collectors.toList());

                    specialTickets.setItems(FXCollections.observableArrayList(specialTicketInfoList));
                } catch (EventException e) {
                    // Handle exception
                }
            }

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

    public void addEventTicket(ActionEvent actionEvent){
        String selectedTicketInfo = (String) allEventTickets.getSelectionModel().getSelectedItem();
        String amountOfEventTickets = eventTicketsAmount.getText();

        if (selectedTicketInfo != null && !amountOfEventTickets.isEmpty()) {
            // Add the selected ticket information along with quantity to the ListView
            String[] ticketParts = selectedTicketInfo.split(" - ");
            String ticketType = ticketParts[0]; // Extract ticket type
            String ticketPriceString = ticketParts[2].replaceAll("[^0-9]", "");// Extract ticket price
            float ticketPrice = Float.parseFloat(ticketPriceString);

            int selectedQuantity = Integer.parseInt(amountOfEventTickets); // Parse selected quantity
            float totalPrice = ticketPrice * selectedQuantity;

            // Create a string containing the ticket information along with the total price
            String ticketDetails = ticketType + " - Quantity: " + selectedQuantity + " - Total Price: " + totalPrice + "DKK";
            allSelectedTickets.getItems().add(ticketDetails);

            allEventTickets.getSelectionModel().clearSelection();
            eventTicketsAmount.clear();
        } else {
        }


    }
    public void addSpecialTickets(ActionEvent actionEvent){

    }


    public void sell(ActionEvent actionEvent) throws EventException {
        String customerName = name.getText();
        String customerLastName = lastName.getText();
        String customerEmail = email.getText();

        Customer customer = new Customer(customerName, customerLastName, customerEmail);
        model.addCustomer(customer);

    }





}
