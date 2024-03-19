package view.components.events;
import be.Event;
import be.Location;
import exceptions.EventException;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import view.components.main.Model;
import view.components.ticketsGeneration.TicketsGeneration;

import javax.swing.event.ChangeListener;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class CreateEventController {
    @FXML
    private MFXScrollPane editScrolPane;
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
    @FXML
    public HBox hBoxTickets;

    private Model model;

    private final StackPane stackPane;

    @FXML
    public void initialize() {
        startTime.setItems(FXCollections.observableArrayList(generateTimeOptions()));
        endTime.setItems(FXCollections.observableArrayList(generateTimeOptions()));
        model = Model.getInstance();

        eventName.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                markFieldAsValid(eventName);
            }
        });

        startDate.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                markFieldAsValid(startDate);
            }
        });

        startTime.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                markFieldAsValid(startTime);
            }
        });

        city.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                markFieldAsValid(city);
            }
        });
        country.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                markFieldAsValid(country);
            }
        });
        postalCode.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                markFieldAsValid(postalCode);
            }
        });
        street.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                markFieldAsValid(street);
            }
        });

    }

    public CreateEventController(StackPane stackPane) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("CreateEventView.fxml"));
        loader.setController(this);
        try {
            editScrolPane=loader.load();
            this.stackPane= stackPane;
         } catch (IOException e) {
//            To be handled
            throw new RuntimeException(e);
        }
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
        TicketsGeneration ticketsGeneration = new TicketsGeneration(stackPane, this);
        stackPane.getChildren().add(ticketsGeneration.getRoot());
    }

    public void saveEvent(ActionEvent actionEvent) throws SQLException, EventException {
        if (isEventValid()) {
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
            Event event = new Event(name, description, startD, endD, startT, endT, new Location(streetE, additionalE, postalCodeE, countryE, cityE));

            model.addEvent(event);
            closeWindow(actionEvent);

        }
    }



    private void closeWindow(ActionEvent event) {
        this.stackPane.setDisable(true);
        this.stackPane.setVisible(false);
    }

public MFXScrollPane getRoot(){
        return editScrolPane;
}

    public void cancel(ActionEvent actionEvent) {
        closeWindow(actionEvent);
    }


    private void markFieldAsInvalid(TextField field) {
        field.getStyleClass().add("invalid-field");
    }

    private void markFieldAsValid(TextField field) {
        field.getStyleClass().add("valid-field");
    }



    private boolean isEventValid() {
        boolean isValid = true;

        // Check each required field and mark them as invalid if they are empty
        if (eventName.getText().isEmpty()) {
            markFieldAsInvalid(eventName);
            isValid = false;
        } else {
            markFieldAsValid(eventName);
        }

        LocalDate startDateValue = startDate.getValue();
        LocalTime startTimeValue = startTime.getValue();

        if (startDateValue == null || startTimeValue == null) {
            markFieldAsInvalid(startDate);
            markFieldAsInvalid(startTime);
            isValid = false;
        } else {
            markFieldAsValid(startDate);
            markFieldAsValid(startTime);
        }

        if (city.getText().isEmpty()) {
            markFieldAsInvalid(city);
            isValid = false;
        } else {
            markFieldAsValid(city);
        }

        if (country.getText().isEmpty()) {
            markFieldAsInvalid(country);
            isValid = false;
        } else {
            markFieldAsValid(country);
        }

        if (postalCode.getText().isEmpty()) {
            markFieldAsInvalid(postalCode);
            isValid = false;
        } else {
            markFieldAsValid(postalCode);
        }

        if (street.getText().isEmpty()) {
            markFieldAsInvalid(street);
            isValid = false;
        } else {
            markFieldAsValid(street);
        }
        return isValid;
    }


}




