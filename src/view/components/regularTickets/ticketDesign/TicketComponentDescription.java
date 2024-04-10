package view.components.regularTickets.ticketDesign;

import be.Event;
import be.Ticket;
import exceptions.ErrorCode;
import exceptions.ExceptionHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import view.components.main.Model;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TicketComponentDescription extends FlowPane implements Initializable {
    @FXML
    private Label eventNameLB, eventDateLB, eventLocationLB, custNameLB, custEmailLB, ticketPriceLB, ticketTypeLB, extraLabel1, extraLabel2;
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

    public TicketComponentDescription(TicketsDesignController ticketsDesignController, Event event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("TicketsDesignComponent.fxml"));
        loader.setController(this);
        this.event = event;
        try {
            ticketPane = loader.load();
            this.getChildren().add(ticketPane);
            this.ticketsDesignController = ticketsDesignController;

        } catch (IOException e) {
            e.printStackTrace();
            ExceptionHandler.errorAlertMessage(ErrorCode.LOADING_FXML_FAILED.getValue());
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getQrCode().setImage(new Image("/resources/images/Image 1.png"));
        getBarCode().setImage(new Image("/resources/images/Image 2.png"));
        getLogoImg().setImage(new Image("/resources/images/Image 3.png"));

        setTicketLabel();
    }

    private void setTicketLabel() {
        eventNameLB.setText(event.getName());
        eventDateLB.setText(event.getStartDate() + ", " + event.getStartTime());
        eventLocationLB.setText(event.getLocation());
    }

    public void setTicketType(String type) {
        ticketTypeLB.setText(type);
    }

    public void setTicketPrice(String price) {
        ticketPriceLB.setText(price + "dkk");
    }

    public void setTicketColour(Color fill){
        ticketPane.setBackground(new Background(new BackgroundFill(fill, null, null)));
        adjustImageColors(fill);

        double brightness = fill.getBrightness();
        if (brightness < 0.5) {
            ticketPane.setStyle("-fx-border-color: white; -fx-border-width: 2px;");
            // Set font color to white
            eventNameLB.setTextFill(Color.WHITE);
            eventDateLB.setTextFill(Color.WHITE);
            eventLocationLB.setTextFill(Color.WHITE);
            ticketTypeLB.setTextFill(Color.WHITE);
            ticketPriceLB.setTextFill(Color.WHITE);
            custNameLB.setTextFill(Color.WHITE);
            custEmailLB.setTextFill(Color.WHITE);
            extraLabel1.setTextFill(Color.WHITE);
            extraLabel2.setTextFill(Color.WHITE);
        } else {
            ticketPane.setStyle("-fx-border-color: black; -fx-border-width: 2px;");
            // Set font color to black
            eventNameLB.setTextFill(Color.BLACK);
            eventDateLB.setTextFill(Color.BLACK);
            eventLocationLB.setTextFill(Color.BLACK);
            ticketTypeLB.setTextFill(Color.BLACK);
            ticketPriceLB.setTextFill(Color.BLACK);
            custNameLB.setTextFill(Color.BLACK);
            custEmailLB.setTextFill(Color.BLACK);
            extraLabel1.setTextFill(Color.BLACK);
            extraLabel2.setTextFill(Color.BLACK);
        }
    }

    private void adjustImageColors(Color fill) {
        double brightness = fill.getBrightness();
        ColorAdjust colorAdjust = new ColorAdjust();

        if (brightness > 0.5) {
            colorAdjust.setContrast(0.5);
        } else {
            colorAdjust.setBrightness(0.5);
        }

        qrCode.setEffect(colorAdjust);
        barCode.setEffect(colorAdjust);
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

    public FlowPane getRoot(){
        return ticketPane;
    }
}
