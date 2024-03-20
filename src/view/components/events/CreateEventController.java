package view.components.events;
import be.Event;
import exceptions.EventException;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import view.components.main.Model;
import view.components.ticketsGeneration.TicketsGeneration;

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
    public TextArea eventDescription;

    @FXML
    public TextArea eventLocation;

    @FXML
    public HBox hBoxTickets;

    private Model model;

    private final StackPane stackPane;

    @FXML
    public void initialize() {
//        startTime.setItems(FXCollections.observableArrayList(generateTimeOptions()));
//        endTime.setItems(FXCollections.observableArrayList(generateTimeOptions()));
//        model = Model.getInstance();
//        eventName.textProperty().addListener((observable, oldValue, newValue) -> {
//            if (!newValue.isEmpty()) {
//                markFieldAsValid(eventName);
//            }
//        });
//
//        startDate.valueProperty().addListener((observable, oldValue, newValue) -> {
//            if (newValue != null) {
//                markFieldAsValid(startDate);
//            }
//        });
//
//        startTime.valueProperty().addListener((observable, oldValue, newValue) -> {
//            if (newValue != null) {
//                markFieldAsValid(startTime);
//            }
//        });
//
//        location.textProperty().addListener((observable, oldValue, newValue) -> {
//            if (!newValue.isEmpty()) {
//                markFieldAsValid(location);
//            }
//        });

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
            String locationE = eventLocation.getText();
            Event event = new Event(name, description, startD, endD, startT, endT, locationE);
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


    private void markFieldAsInvalid(TextInputControl field) {
        field.getStyleClass().add("invalid-field");
    }

    private void markFieldAsValid(TextInputControl field) {
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

        if (eventLocation.getText().isEmpty()){
            markFieldAsInvalid(eventLocation);
            isValid = false;
        } else {
            markFieldAsValid(eventLocation);
        }


        return isValid;
    }


}




