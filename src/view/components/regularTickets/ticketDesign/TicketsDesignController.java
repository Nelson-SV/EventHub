package view.components.regularTickets.ticketDesign;

import be.DeleteOperation;
import be.Ticket;
import exceptions.ErrorCode;
import exceptions.ExceptionHandler;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import view.components.eventsPage.eventManagement.EventManagementController;
import view.components.eventsPage.eventManagement.ticketManagement.TicketDescriptionComponent;
import view.components.main.Model;
import view.components.regularTickets.deleteTicket.DeleteTicket;
import view.components.regularTickets.ticketManagement.ManageTicket;
import view.utility.TicketValidator;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

public class TicketsDesignController implements Initializable {

    @FXML
    private MFXButton addTicketBT;
    @FXML
    private ColorPicker colorPicker;
    @FXML
    private MFXTextField ticketTypeTF, ticketPriceTF, ticketQuantityTF;
    @FXML
    private FlowPane flowPane;
    @FXML
    private VBox ticketDesignVBox;
    @FXML
    private HBox ticketHBox;
    @FXML
    private EventManagementController eventManagementController;
    @FXML
    private TicketComponentDescription ticketComponentDescription;
    @FXML
    private StackPane secondaryLayout, thirdLayout;
    @FXML
    private Model model;
    @FXML
    private Ticket selectedTicket;

    public TicketsDesignController(StackPane secondaryLayout, StackPane thirdLayout, EventManagementController eventManagementController, Model model) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("TicketsDesignWindow.fxml"));
        loader.setController(this);
        try {
            flowPane=loader.load();
            this.secondaryLayout=secondaryLayout;
            this.thirdLayout = thirdLayout;
            this.eventManagementController = eventManagementController;
            this.model = model;
            displayTicketComponent();
        } catch (IOException e) {
            ExceptionHandler.errorAlertMessage(ErrorCode.LOADING_FXML_FAILED.getValue());
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updateTicketInformation();
    }

    @FXML
    private void displayTicketComponent() {
        if(ticketHBox.getScene()==null){
            ticketHBox.getChildren().clear();
            ticketComponentDescription = new TicketComponentDescription(this, model.getSelectedEvent());
            ticketHBox.getChildren().add(ticketComponentDescription);
        }
    }

    public void addTickets() {
        boolean isTicketValid = TicketValidator.isTicketValid(ticketTypeTF, ticketPriceTF, ticketQuantityTF);

        if (isTicketValid) {
            String type = ticketTypeTF.getText();
            int price = Integer.parseInt(ticketQuantityTF.getText());
            BigDecimal quantity = new BigDecimal(ticketPriceTF.getText());
            String color = colorPicker.getValue().toString();

            Ticket ticket = new Ticket(type, price, quantity, color);

            VBox ticketsVBox = eventManagementController.getTicketsVBox();

            ManageTicket manage = new ManageTicket(secondaryLayout,thirdLayout,model, eventManagementController, ticket);
            DeleteTicket delete = new DeleteTicket(secondaryLayout,thirdLayout,model, DeleteOperation.DELETE_TICKET, ticket, eventManagementController);
            TicketDescriptionComponent ticketDescriptionComponent = new TicketDescriptionComponent(ticket, manage, delete);
            ticketsVBox.getChildren().add(ticketDescriptionComponent);

            // Check if it's a new ticket or to edit one
            if (selectedTicket == null) {
                // Add new ticket
                model.getNewAddedTicket(ticket);
            } else {
                // Update existing ticket
                model.getTicketsToEdit(ticket, selectedTicket.getId());
                // Remove the existing ticket from the view
                removeTicket(ticketsVBox, selectedTicket);
            }
            closeWindow();
            selectedTicket = null;
        }
    }

    private void removeTicket(VBox ticketsVBox, Ticket selectedTicket) {
        for (Node node : ticketsVBox.getChildren()) {
            if (node instanceof TicketDescriptionComponent) {
                TicketDescriptionComponent ticketDescriptionComponent = (TicketDescriptionComponent) node;
                if (ticketDescriptionComponent.getTicket().equals(selectedTicket)) {
                    ticketsVBox.getChildren().remove(node);
                    break;
                }
            }
        }
    }

    public Ticket getTicketToEdit(Ticket ticket){
        selectedTicket = ticket;
        setTicketInformation();
        return selectedTicket;
    }

    public void cancelAction(){
        closeWindow();
        selectedTicket = null;
    }

    public void setTicketInformation() {
        if (selectedTicket!=null){
            ticketTypeTF.setText(selectedTicket.getTicketType());
            ticketPriceTF.setText(selectedTicket.getTicketPrice()+"");
            ticketQuantityTF.setText(selectedTicket.getQuantity()+"");

            String ticketColor = selectedTicket.getColor();

            if (ticketColor != null && !ticketColor.isEmpty()) {
                colorPicker.setValue(Color.valueOf(ticketColor));
            } else{
                ticketComponentDescription.setTicketColour(Color.WHITE);
            }
        }
    }

    public void updateTicketInformation() {
        ticketTypeTF.textProperty().addListener((observable, oldValue, newValue) -> {
            // Update the label with the new selected type
            ticketComponentDescription.setTicketType(newValue);
        });

        ticketPriceTF.textProperty().addListener((observable, oldValue, newValue) -> {
            // Update the label with the new selected type
            ticketComponentDescription.setTicketPrice(newValue);
        });

        colorPicker.valueProperty().addListener((observable, oldColor, newValue) -> {
            // Update the background color of the FlowPane
            ticketComponentDescription.setTicketColour(newValue);
        });
    }

    public FlowPane getRoot() {
        return flowPane;
    }

    private void closeWindow() {
        thirdLayout.getChildren().clear();
        thirdLayout.setDisable(true);
        thirdLayout.setVisible(false);
    }
}
