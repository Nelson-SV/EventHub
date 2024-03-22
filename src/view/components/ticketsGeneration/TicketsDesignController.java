package view.components.ticketsGeneration;

import be.Ticket;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import view.components.events.CreateEventController;

import java.io.IOException;

public class TicketsDesignController extends FlowPane {

    public MFXButton addTicketBT;
    public MFXComboBox ticketTypeCombo, ticketColorCombo;
    public MFXTextField ticketPriceTF, ticketQuantity;
    public MFXComboBox eventNameCombo;
    public MFXTextField eventLocationTF, eventDateTF;
    public MFXTextField line4TF, line5TF, line6TF;
    public Label eventNameLB, eventDateLB, eventLocationLB, line4LB, line5LB, line6LB, custNameLB, custEmailLB, ticketTypeLB, ticketPriceLB;
    public FlowPane ticketPane, ticketTypeFlowPane;
    public MFXScrollPane scrollPane;
    public HBox ticketHBox;

    public CreateEventController createEventController;
    private StackPane secondaryLayout, thirdLayout;

    private Ticket ticket;
    @FXML
    private ImageView qrCode, logoImg, barCode;

    public TicketsDesignController() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("TicketsDesignCreation.fxml"));
        loader.setController(this);
        try{
            ticketPane = loader.load();
            this.getChildren().add(ticketPane);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ImageView getQrCode() {
        return qrCode;
    }

    public void setQrCode(ImageView qrCode) {
        this.qrCode = qrCode;
    }

    public ImageView getLogoImg() {
        return logoImg;
    }

    public void setLogoImg(ImageView logoImg) {
        this.logoImg = logoImg;
    }

    public ImageView getBarCode() {
        return barCode;
    }

    public void setBarCode(ImageView barCode) {
        this.barCode = barCode;
    }
}
