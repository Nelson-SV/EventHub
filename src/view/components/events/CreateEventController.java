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
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import view.components.main.Model;
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

    private Model model;

    private final StackPane stackPane;

    @FXML
    public void initialize() {
        startTime.setItems(FXCollections.observableArrayList(generateTimeOptions()));
        endTime.setItems(FXCollections.observableArrayList(generateTimeOptions()));
        model = Model.getInstance();

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
         //the close method needs to be moved here after a cancel button is included
        } catch (EventException e){
            Alert a = new Alert(Alert.AlertType.ERROR, e.getMessage());
            e.printStackTrace();
            a.show();
        }
        closeWindow(actionEvent);
//        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
//        stage.close();
    }


    private void closeWindow(ActionEvent event) {
        this.stackPane.setDisable(true);
        this.stackPane.setVisible(false);
    }

public MFXScrollPane getRoot(){
        return editScrolPane;
}

} //hhhhhhh
