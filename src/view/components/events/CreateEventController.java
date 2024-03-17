package view.components.events;

import be.Event;
import be.Location;
import exceptions.EventException;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import view.components.Model;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class CreateEventController {
    @FXML
    public TextField eventName;
    @FXML
    public MFXDatePicker startDate;
    @FXML
    public MFXComboBox <LocalTime> startTime;
    @FXML
    public MFXDatePicker endDate;
    @FXML
    public MFXComboBox <LocalTime> endTime;
    @FXML
    public TextField eventDescription;
    @FXML
    public TextField country;
    @FXML
    public TextField city;
    @FXML
    public TextField street;
   @FXML
   public TextField additional;
    @FXML
    public TextField postalCode;

    private Model model;

    @FXML
    public void initialize() {
        startTime.setItems(FXCollections.observableArrayList(generateTimeOptions()));
        endTime.setItems(FXCollections.observableArrayList(generateTimeOptions()));
        model = Model.getInstance();
    }

    private List<LocalTime> generateTimeOptions() {
        List<LocalTime> timeOptions = new ArrayList<>();
        LocalTime time = LocalTime.of(0, 0);
        while (time.isBefore(LocalTime.of(23, 0))) { // Adjust the end time as needed
            timeOptions.add(time);
            time = time.plusHours(1);
        }
        return timeOptions;
    }

    public void addTicket(ActionEvent actionEvent) {
    }

    public void saveEvent(ActionEvent actionEvent) throws SQLException, EventException {
        String name = eventName.getText();
        LocalDate startD = startDate.getValue();
        LocalTime startT = startTime.getValue();
        LocalDate endD = endDate.getValue();
        LocalTime endT = endTime.getValue();
        String description = eventDescription.getText();
        String countryE = country.getText();
        String cityE = city.getText();
        String streetE = street.getText();
        String additionalE = additional.getText();
        String postalCodeE = postalCode.getText();

        Event event = new Event(name, description, startD, endD, startT, endT, new Location(streetE,additionalE, postalCodeE, countryE, cityE));
        try {
            model.addEvent(event);
        } catch (EventException e){
            Alert a = new Alert(Alert.AlertType.ERROR, e.getMessage());
            e.printStackTrace();
            a.show();
        }
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }
}
