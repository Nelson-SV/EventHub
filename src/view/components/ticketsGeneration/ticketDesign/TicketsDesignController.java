package view.components.ticketsGeneration.ticketDesign;

import be.Ticket;
import com.google.gson.internal.bind.util.ISO8601Utils;
import exceptions.ErrorCode;
import exceptions.EventException;
import exceptions.ExceptionHandler;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import view.components.eventsPage.eventManagement.EventManagementController;
import view.components.eventsPage.eventManagement.eventCreation.CreateEventController;
import view.components.eventsPage.eventManagement.ticketManagement.TicketDescriptionComponent;
import view.components.main.Model;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class TicketsDesignController implements Initializable {

    @FXML
    private MFXButton addTicketBT;
    @FXML
    private MFXComboBox ticketColorCombo;
    @FXML
    private MFXTextField ticketTypeTF, ticketPriceTF, ticketQuantity;
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
            displayTicket();
        } catch (IOException e) {
            ExceptionHandler.errorAlertMessage(ErrorCode.LOADING_FXML_FAILED.getValue());
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //ticketComponentDescription = new TicketComponentDescription(model.getSelectedEvent());

        updateTicketInformation();
    }

    @FXML
    private void displayTicket() {
        if(ticketHBox.getScene()==null){
            ticketHBox.getChildren().clear();
            ticketComponentDescription = new TicketComponentDescription(this, model);
            ticketHBox.getChildren().add(ticketComponentDescription);
        }
    }

    public void addTickets(){



        closeWindow();
    }

    public void cancelAction(){

        closeWindow();
    }

    public void updateTicketInformation() {
        ticketTypeTF.textProperty().addListener((observable, oldValue, newValue) -> {
            // Update the label with the new selected type
            ticketComponentDescription.setNewTicketType(newValue);
        });
    }

/*
    public void updateEventInformation(){
            eventNameCombo.textProperty().addListener((observable, oldValue, newValue) -> {
                // Update the label with the new selected Event
                eventNameLB.setText(newValue);
            });

            eventDateTF.textProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue.isEmpty()) {
                    eventDateLB.setText("Event Date Line");
                } else {
                    // If the text field has text, update the label with the new text
                    eventDateLB.setText(newValue);
                }
            });

            eventLocationTF.textProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue.isEmpty()) {
                    eventLocationLB.setText("Event Location Line");
                } else {
                    // If the text field has text, update the label with the new text
                    eventLocationLB.setText(newValue);
                }
            });
    }

    public void updateTicketInformation(){
        ObservableList<String> list = FXCollections.observableArrayList();
        list.add("Normal TicketsDesignController");
        list.add("Special TicketsDesignController");
        ticketTypeCombo.setItems(list);

        // Add a listener to ticketTypeCombo
        ticketTypeCombo.textProperty().addListener((observable, oldValue, newValue) -> {
            // Update the label with the new selected type
            ticketTypeLB.setText(newValue);
        });

        ticketPriceTF.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                ticketPriceLB.setText("dkk");
            } else {
                // If the text field has text, update the label with the new text
                ticketPriceLB.setText(newValue + "dkk");
            }
        });

        ObservableList<String> colorList = FXCollections.observableArrayList();
        colorList.add("Yellow");
        colorList.add("Light Red");
        ticketColorCombo.setItems(colorList);

        // Add a listener to ticketColorCombo
        ticketColorCombo.textProperty().addListener((observable, oldValue, newValue) -> {
            // Update the ticketPane with the new selected color
            switch (newValue) {
                case "Yellow":
                    ticketPane.setStyle("-fx-background-color: lightyellow;");
                    break;
                case "Light Red":
                    ticketPane.setStyle("-fx-background-color: #F5979A;");
                    break;
            }
        });
    }

 */

    public FlowPane getRoot() {
        return flowPane;
    }

    public MFXTextField ticketPrice(){
        return ticketPriceTF;
    }

    private void closeWindow() {
        thirdLayout.getChildren().clear();
        thirdLayout.setDisable(true);
        thirdLayout.setVisible(false);
    }
}
