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
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
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
    private MFXComboBox ticketColorCombo;
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
    private Ticket ticket;

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
            Ticket ticket = new Ticket(ticketTypeTF.getText(), Integer.parseInt(ticketQuantityTF.getText()), new BigDecimal(ticketPriceTF.getText()));

            VBox ticketsVBox = eventManagementController.getTicketsVBox();

            ManageTicket manage = new ManageTicket(secondaryLayout,thirdLayout,model, eventManagementController);
            DeleteTicket delete = new DeleteTicket(secondaryLayout,thirdLayout,model, DeleteOperation.DELETE_TICKET);
            TicketDescriptionComponent ticketDescriptionComponent = new TicketDescriptionComponent(ticket, manage, delete);
            ticketsVBox.getChildren().add(ticketDescriptionComponent);

            /*
            if (selectedTicket != ticket && selectedTicket != null) {
                removeTicket(selectedVbox, selectedTicket);
            }
            */

            model.getNewAddedTicket(ticket);

            closeWindow();
        }
    }

    public void cancelAction(){

        closeWindow();
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
