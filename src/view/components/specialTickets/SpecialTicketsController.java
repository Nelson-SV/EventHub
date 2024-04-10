package view.components.specialTickets;

import be.DeleteOperation;
import be.Event;
import be.Ticket;
import exceptions.EventException;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import view.components.deleteEvent.DeleteButton;
import view.components.eventsPage.eventDescription.EventComponent;
import view.components.eventsPage.manageButton.ManageAction;
import view.components.main.Model;
import view.utility.TicketValidator;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class SpecialTicketsController implements Initializable {

    public HBox hBox;
    @FXML
    private VBox box;
    public HBox ticketHBox;
    public MFXTextField ticketTypeTF, ticketQuantityTF, ticketPriceTF;
    @FXML
    private ListView<Ticket> ticketsEventList;
    @FXML
    private ColorPicker colorPicker;
    public MFXComboBox eventsCB, eventToChooseCB;
    @FXML
    private StackPane secondaryLayout;
    @FXML
    private Model model;

    private ObservableList<Event> eventsOfCoordinator;
    private ObservableList<Ticket> ticketsOfEvent;
    private Ticket selectedTicket;
    private Event selectedEvent;


    public SpecialTicketsController(VBox box, Model model){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SpecialTicketsWindow.fxml"));
        loader.setController(this);
        this.model = model;
        try {
            hBox=loader.load();
            this.box = box;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ticketsOfEvent = FXCollections.observableArrayList();
        eventsOfCoordinator = FXCollections.observableArrayList();
        setEventsComboBox();

        ticketsEventList.setCellFactory(param -> new ListCell<Ticket>() {
            @Override
            protected void updateItem(Ticket ticket, boolean empty) {
                super.updateItem(ticket, empty);
                if (empty || ticket == null) {
                    setText(null);
                } else {
                    setText(ticket.getTicketType() + ", Quantity: " + ticket.getQuantity());
                }
            }
        });

        addListenerEventsComboBox();

    }

    private void setEventsComboBox() {
        model.sortedEventsList().forEach(e -> eventsOfCoordinator.add(e.getEventDTO()));
        eventsCB.setItems(eventsOfCoordinator);
    }

    private void addListenerEventsComboBox(){

        eventsCB.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedEvent = (Event) newValue;

                List<Ticket> tickets = null;
                try {
                    ObservableMap<Integer, Ticket> specialTicketsMap  = model.getSpecialTicketsForEventOrNot(selectedEvent.getId());
                    tickets = new ArrayList<>(specialTicketsMap.values());
                } catch (EventException e) {
                    throw new RuntimeException(e);
                }

                ticketsOfEvent.clear();
                ticketsOfEvent.addAll(tickets);

                ticketsEventList.getItems().clear();
                ticketsEventList.getItems().addAll(tickets);
            }
        });
    }

    public void addTicket(ActionEvent actionEvent) throws EventException {
        boolean isTicketValid = TicketValidator.isTicketValid(ticketTypeTF, ticketPriceTF, ticketQuantityTF);

        if (isTicketValid) {
            String type = ticketTypeTF.getText();
            int quantity = Integer.parseInt(ticketQuantityTF.getText());
            BigDecimal price = new BigDecimal(ticketPriceTF.getText());
            String color = colorPicker.getValue().toString();

            Ticket specialTicket = new Ticket(type, quantity, price, color);

            model.addSpecialTicket(specialTicket, selectedEvent);
        }
    }

    public void editTicket(ActionEvent actionEvent) {

    }

    public void removeTicket(ActionEvent actionEvent) {
        //model.deleteSpecialTicket();
    }

    public void cancelAction(ActionEvent actionEvent) {
        clearFields();
    }

    private void clearFields() {

    }

    public HBox getRoot() {
        return hBox;
    }

}
