package view.components.ticketsGeneration.ticketDesign;

import be.Event;
import be.Ticket;
import exceptions.ErrorCode;
import exceptions.ExceptionHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import view.components.main.Model;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TicketComponentDescription extends FlowPane implements Initializable {
    @FXML
    private Label eventNameLB, eventDateLB, eventLocationLB, custNameLB, custEmailLB, ticketPriceLB, ticketTypeLB;
    @FXML
    private Ticket ticket;
    @FXML
    private Event event;
    @FXML
    private ImageView qrCode, logoImg, barCode;
    @FXML
    private FlowPane ticketPane;
    @FXML
    private TicketsDesignController ticketsDesignController;
    @FXML
    private Model model;

    public TicketComponentDescription(TicketsDesignController ticketsDesignController, Model model) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("TicketsDesignComponent.fxml"));
        loader.setController(this);
        try {
            ticketPane = loader.load();
            this.getChildren().add(ticketPane);
            this.ticketsDesignController = ticketsDesignController;
            this.model = model;
        } catch (IOException e) {
            e.printStackTrace();
            ExceptionHandler.errorAlertMessage(ErrorCode.LOADING_FXML_FAILED.getValue());
        }
    }

    public FlowPane getRoot(){
        return ticketPane;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getQrCode().setImage(new Image("/resources/images/Image 1.png"));
        getBarCode().setImage(new Image("/resources/images/Image 2.png"));
        getLogoImg().setImage(new Image("/resources/images/Image 3.png"));
    }

    public void setNewTicketType(String type) {
        ticketTypeLB.setText(type);
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
