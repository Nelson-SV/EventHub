package view.components.specialTickets;

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
import javafx.geometry.Pos;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
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
    public VBox ticketVBox;
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
    private SpecialComponentDescription specialComponentDescription;


    public SpecialTicketsController(VBox box, Model model){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SpecialTicketsWindow.fxml"));
        loader.setController(this);
        this.model = model;
        try {
            hBox=loader.load();
            this.box = box;
            displayTicketComponent();
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
                    setText(ticket.getTicketType() + "    Quantity: " + ticket.getQuantity() + "    Price: " + ticket.getTicketPrice());
                }
            }
        });

        updateTicketInformation();
        addListenerEventsComboBox();

    }

    @FXML
    private void displayTicketComponent() {
        if(ticketVBox.getScene()==null){
            ticketVBox.getChildren().clear();
            specialComponentDescription = new SpecialComponentDescription();
            ticketVBox.getChildren().add(specialComponentDescription);
            specialComponentDescription.setAlignment(Pos.CENTER);
        }
    }

    private void setEventsComboBox() {
        model.sortedEventsList().forEach(e -> eventsOfCoordinator.add(e.getEventDTO()));
        eventsOfCoordinator.add(new Event("Not Related to Events"));
        eventsCB.setItems(eventsOfCoordinator);
        eventToChooseCB.setItems(eventsOfCoordinator);
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

    public void updateTicketInformation() {
        ticketTypeTF.textProperty().addListener((observable, oldValue, newValue) -> {
            // Update the label with the new selected type
            specialComponentDescription.setTicketType(newValue);
        });

        colorPicker.valueProperty().addListener((observable, oldColor, newValue) -> {
            // Update the background color of the FlowPane
            specialComponentDescription.updateTicketColour(newValue);
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
            Event event = (Event) eventToChooseCB.getSelectionModel().getSelectedItem();
            String text = eventToChooseCB.getText();
            if(text == null || text.isEmpty())
                event = null;

            if (selectedTicket == null) {
                model.addSpecialTicket(specialTicket, event);
                if (event != null && event == eventsCB.getSelectionModel().getSelectedItem()) {
                    ticketsEventList.getItems().add(specialTicket);
                    ticketsEventList.refresh();
                }
            } else {
                selectedTicket.setTicketType(type);
                selectedTicket.setQuantity(quantity);
                selectedTicket.setTicketPrice(price);
                selectedTicket.setColor(color);
                ticketsEventList.refresh();
                model.updateSpecialTicket(selectedTicket);
            }
            selectedTicket = null;
            clearFields();
        }
    }

    public void editTicket(ActionEvent actionEvent) {
        selectedTicket = ticketsEventList.getSelectionModel().getSelectedItem();

        ticketTypeTF.setText(selectedTicket.getTicketType());
        ticketQuantityTF.setText(""+selectedTicket.getQuantity());
        ticketPriceTF.setText(""+selectedTicket.getTicketPrice());
        String ticketColor = selectedTicket.getColor();
        colorPicker.setValue(Color.valueOf(ticketColor));
        specialComponentDescription.setTicketColor(Color.valueOf(ticketColor));
        eventToChooseCB.setDisable(true);
    }

    public void removeTicket(ActionEvent actionEvent) throws EventException {
        selectedTicket = ticketsEventList.getSelectionModel().getSelectedItem();
        model.deleteSpecialTicket(selectedTicket);
        ticketsEventList.getItems().remove(selectedTicket);
        ticketsEventList.refresh();
        selectedTicket = null;
        clearFields();
    }

    public void clearAction(ActionEvent actionEvent) {
        clearFields();
    }

    public void clearFields(){
        ticketTypeTF.clear();
        ticketQuantityTF.clear();
        ticketPriceTF.clear();
        eventToChooseCB.setValue(null);
        eventToChooseCB.setDisable(false);
        specialComponentDescription.resetTicketColor();
        colorPicker.setValue(Color.WHITE);
    }
    public HBox getRoot() {
        return hBox;
    }

}
