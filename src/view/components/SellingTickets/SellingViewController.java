package view.components.SellingTickets;

import be.Customer;
import be.Event;
import be.Ticket;
import dal.TicketDAO;
import exceptions.ErrorCode;
import exceptions.EventException;
import exceptions.ExceptionHandler;
import exceptions.TicketException;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import view.components.main.Model;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.*;
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
    private TextField specialTicketsAmount;
    @FXML
    private ListView<Ticket> allSelectedTickets;
    @FXML
    private Label totalPrice;

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


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<String> eventNames = model.getAllEventNames();
        allEvents.setItems(FXCollections.observableArrayList(eventNames));

        allEvents.setOnAction(event -> {
            loadTicketsInfo();
            loadSpecialTicketsInfo();
        });
        //update total price based on changes
        allSelectedTickets.getItems().addListener((ListChangeListener<Ticket>) change -> {
            updateTotalPrice();
        });
    }

    private void loadTicketsInfo(){
        allEventTickets.clearSelection();
        String selectedEventName = (String) allEvents.getSelectionModel().getSelectedItem();
        int eventId = model.getEventIdByName(selectedEventName);
        if (eventId != -1) {
            try {
                ObservableMap<Integer, Ticket> ticketsMap = model.getTicketsForEvent(eventId);
                List<Ticket> ticketList = new ArrayList<>(ticketsMap.values());
                allEventTickets.setItems(FXCollections.observableList(ticketList));
                System.out.println(ticketsMap.values());


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
                    List<Ticket> ticketList = new ArrayList<>(specialTicketsMap.values());
                    specialTickets.setItems(FXCollections.observableList(ticketList));
                } catch (EventException e) {
                    // Handle exception
                }
            }

    }



    public void addEventTicket(ActionEvent actionEvent){
        Ticket selectedTicketInfo = new Ticket((Ticket) allEventTickets.getSelectionModel().getSelectedItem());
        String amountOfEventTickets = eventTicketsAmount.getText();

        if (selectedTicketInfo != null && !amountOfEventTickets.isEmpty()) {

           BigDecimal ticketPrice = selectedTicketInfo.getTicketPrice();

           int selectedQuantity = Integer.parseInt(amountOfEventTickets); // Parse selected quantity
           BigDecimal totalPrice = ticketPrice.multiply(BigDecimal.valueOf(selectedQuantity));
           selectedTicketInfo.setTicketPrice(totalPrice);
           selectedTicketInfo.setQuantity(selectedQuantity);
           selectedTicketInfo.setSpecial(false); //since this is a normal ticket
           allSelectedTickets.getItems().add(selectedTicketInfo);

           allEventTickets.getSelectionModel().clearSelection();
           eventTicketsAmount.clear();
        } else {
        }


    }
    public void addSpecialTickets(ActionEvent actionEvent){
        Ticket selectedSpecialTicketInfo = (Ticket) specialTickets.getSelectionModel().getSelectedItem();
        String amountOfEventTickets = specialTicketsAmount.getText();

        if (selectedSpecialTicketInfo != null && !amountOfEventTickets.isEmpty()) {
            BigDecimal ticketPrice = selectedSpecialTicketInfo.getTicketPrice();

            int selectedQuantity = Integer.parseInt(amountOfEventTickets); // Parse selected quantity
            BigDecimal totalPrice = ticketPrice.multiply(BigDecimal.valueOf(selectedQuantity));
            selectedSpecialTicketInfo.setTicketPrice(totalPrice);
            selectedSpecialTicketInfo.setQuantity(selectedQuantity);
            selectedSpecialTicketInfo.setSpecial(true); //since this is a special ticket
            allSelectedTickets.getItems().add(selectedSpecialTicketInfo);

            specialTickets.getSelectionModel().clearSelection();
            specialTicketsAmount.clear();
        } else {
        }

    }

    private void updateTotalPrice() {
        BigDecimal total = BigDecimal.ZERO; // Initialize to zero
        for (Ticket item : allSelectedTickets.getItems()) {
            BigDecimal totalPrice = item.getTicketPrice(); // Parse total price to BigDecimal
            total = total.add(totalPrice); // Add total price to running total
        }

        totalPrice.setText("Total Price: " + total.toString() + " DKK");
    }

    public void removeTicket(ActionEvent actionEvent){
        if (allSelectedTickets.getSelectionModel().getSelectedItem() != null) {
            // Cast the selected item to String
            Ticket selectedTicket = allSelectedTickets.getSelectionModel().getSelectedItem();
            allSelectedTickets.getItems().remove(selectedTicket);
        }
    }


    public void sell(ActionEvent actionEvent) throws EventException {
        String customerName = name.getText();
        String customerLastName = lastName.getText();
        String customerEmail = email.getText();

        Customer customer = new Customer(customerName, customerLastName, customerEmail);
        model.addCustomer(customer); // possibly return the id here

        // get customer ID
        int customerId = customer.getId();

        //minus the quantity
        //create sold ticket

        for (Ticket item : allSelectedTickets.getItems()) {

            model.deductTicketQuantity(item.getId(), item.getQuantity());
            boolean isSpecial = item.getSpecial();
            if(isSpecial){
                //call method for special ticket
                model.insertSoldSpecialTicket(item.getId(), customerId);
            }else{
                //call method for normal ticket
                model.insertSoldTicket(item.getId(),customerId);
            }
            // call ticket dao method


        }


    }





}
