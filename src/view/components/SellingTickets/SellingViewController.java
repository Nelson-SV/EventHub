package view.components.SellingTickets;

import be.Customer;
import be.Ticket;
import exceptions.ErrorCode;
import exceptions.EventException;
import exceptions.ExceptionHandler;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableMap;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import view.components.main.Model;
import view.utility.SellingValidator;
import view.utility.TicketValidator;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.*;

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
    private static final PseudoClass ERROR_PSEUDO_CLASS = PseudoClass.getPseudoClass("error");

    public SellingViewController(VBox vbox, Model model) {
        this.model=model;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sellingView.fxml"));
        loader.setController(this);
        try {
            box=loader.load();
            this.vBox = vbox;
        } catch (IOException e) {
            ExceptionHandler.errorAlertMessage(ErrorCode.LOADING_FXML_FAILED.getValue());
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

        // Add event handler for eventTicketsAmount text field
        eventTicketsAmount.textProperty().addListener((observable, oldValue, newValue) -> {
            // Clear the error pseudo-class state when user starts typing
            eventTicketsAmount.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, false);
        });

       // Add event handler for specialTicketsAmount text field
        specialTicketsAmount.textProperty().addListener((observable, oldValue, newValue) -> {
            // Clear the error pseudo-class state when user starts typing
            specialTicketsAmount.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, false);
        });
        // Add event handler for eventTicketsAmount text field
        eventTicketsAmount.textProperty().addListener((observable, oldValue, newValue) -> {
            // Clear the error pseudo-class state when user starts typing
            eventTicketsAmount.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, false);
        });

        SellingValidator.emailToolTip(email);
        SellingValidator.addSellingListeners(eventTicketsAmount, specialTicketsAmount);
        SellingValidator.addSellingListeners2(name, lastName, email, allSelectedTickets, specialTickets, allEventTickets);
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
        Ticket selectedTicketInfo = ((Ticket) allEventTickets.getSelectionModel().getSelectedItem());
        String amountOfEventTickets = eventTicketsAmount.getText();
        if (selectedTicketInfo == null) {
            allEventTickets.setText("Select ticket");
            allEventTickets.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, true);
            return; // Exit the method since no item is selected
        }

        if (selectedTicketInfo != null && !amountOfEventTickets.isEmpty()) {
            int selectedQuantity = Integer.parseInt(amountOfEventTickets); // Parse selected quantity

            // Check if selected quantity does not exceed available inventory
            if (selectedQuantity <= selectedTicketInfo.getQuantity()) {
                BigDecimal ticketPrice = selectedTicketInfo.getTicketPrice();

                BigDecimal totalPrice = ticketPrice.multiply(BigDecimal.valueOf(selectedQuantity));
                selectedTicketInfo.setTicketPrice(totalPrice);
                selectedTicketInfo.setQuantity(selectedQuantity);
                selectedTicketInfo.setSpecial(false); //since this is a normal ticket
                allSelectedTickets.getItems().add(selectedTicketInfo);

                allEventTickets.getSelectionModel().clearSelection();
                eventTicketsAmount.clear();
            } else {
                eventTicketsAmount.clear();
                eventTicketsAmount.setText("overMax");
                eventTicketsAmount.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, true);

            }
        } else {
            eventTicketsAmount.clear();
            eventTicketsAmount.setText("empty");
            eventTicketsAmount.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, true);
        }

    }
    public void addSpecialTickets(ActionEvent actionEvent){
        Ticket selectedSpecialTicketInfo = (Ticket) specialTickets.getSelectionModel().getSelectedItem();
        String amountOfEventTickets = specialTicketsAmount.getText();
        if (selectedSpecialTicketInfo == null) {
            specialTickets.setText("Select ticket");
            specialTickets.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, true);
            return; // Exit the method since no item is selected
        }

        if (selectedSpecialTicketInfo != null && !amountOfEventTickets.isEmpty()) {
            int selectedQuantity = Integer.parseInt(amountOfEventTickets); // Parse selected quantity

            if (!isTicketAlreadyAdded(selectedSpecialTicketInfo)) {
                //the ticket is not in listview, so add the ticket
                // Check if selected quantity does not exceed available inventory
                if (selectedQuantity <= selectedSpecialTicketInfo.getQuantity()) {
                    BigDecimal ticketPrice = selectedSpecialTicketInfo.getTicketPrice();
                    BigDecimal totalPrice = ticketPrice.multiply(BigDecimal.valueOf(selectedQuantity));
                    selectedSpecialTicketInfo.setTicketPrice(totalPrice);
                    selectedSpecialTicketInfo.setQuantity(selectedQuantity);
                    selectedSpecialTicketInfo.setSpecial(true); //since this is a special ticket
                    allSelectedTickets.getItems().add(selectedSpecialTicketInfo);

                    specialTickets.getSelectionModel().clearSelection();
                    specialTicketsAmount.clear();
                } else {
                    specialTicketsAmount.clear();
                    specialTicketsAmount.setText("overMax");
                    specialTicketsAmount.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, true);
                }
            }

        } else {
            specialTicketsAmount.clear();
            specialTicketsAmount.setText("empty");
            specialTicketsAmount.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, true);
        }
    }

    private boolean isTicketAlreadyAdded(Ticket ticket) {
        // Check if the ticket is already in the list
        for (Ticket item : allSelectedTickets.getItems()) {
            if (item.getId() == ticket.getId()) {
                return true;
            }
        }
        return false;
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
        boolean isSellingValid = SellingValidator.isSellingValid(name, lastName, email, allSelectedTickets);
        if(isSellingValid) {
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


                boolean isSpecial = item.getSpecial();
                if (isSpecial) {
                    //call method for special ticket
                    model.deductSpecialQuantity(item.getId(), item.getQuantity());
                    model.insertSoldSpecialTicket(item.getId(), customerId);
                } else {
                    //call method for normal ticket
                    model.deductTicketQuantity(item.getId(), item.getQuantity());
                    model.insertSoldTicket(item.getId(), customerId);
                }
                // call ticket dao method


            }
        }


    }

    public void cancel (ActionEvent actionEvent){}







}
