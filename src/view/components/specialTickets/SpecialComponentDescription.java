package view.components.specialTickets;

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

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SpecialComponentDescription extends FlowPane implements Initializable {

    @FXML
    private Label ticketTypeLB;
    @FXML
    private ImageView qrCode;
    @FXML
    private FlowPane ticketPane;

    public SpecialComponentDescription() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SpecialTicketsComponent.fxml"));
        loader.setController(this);
        try {
            ticketPane = loader.load();
            this.getChildren().add(ticketPane);

        } catch (IOException e) {
            e.printStackTrace();
            ExceptionHandler.errorAlertMessage(ErrorCode.LOADING_FXML_FAILED.getValue());
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        qrCode.setImage(new Image("/resources/images/Image 1.png"));
        ticketPane.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
    }

    public void setTicketType(String type) {
        ticketTypeLB.setText(type);
    }

    public void setTicketColor(Color fill){
        ticketPane.setBackground(new Background(new BackgroundFill(fill, null, null)));
        ticketPane.setStyle("-fx-border-color: black; -fx-border-width: 2px;");
    }

    public void resetTicketColor(){
        ticketPane.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
    }

    public void updateTicketColour(Color fill){
        ticketPane.setBackground(new Background(new BackgroundFill(fill, null, null)));
        adjustImageColors(fill);

        double brightness = fill.getBrightness();
        if (brightness < 0.5) {
            ticketPane.setStyle("-fx-border-color: white; -fx-border-width: 2px;");
            // Set font color to white
            ticketTypeLB.setTextFill(Color.WHITE);
        } else {
            ticketPane.setStyle("-fx-border-color: black; -fx-border-width: 2px;");
            // Set font color to black
            ticketTypeLB.setTextFill(Color.BLACK);
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
    }
}
