package view.components.main;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import view.components.eventDescription.EventComponenet;
import view.components.events.CreateEventController;
import view.components.listeners.Displayable;
import view.components.manageButton.ManageAction;
import view.components.ticketsGeneration.TicketsGeneration;
import view.utility.NavigationHoverControl;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable, Displayable {
    private Model model;
    @FXML
    private MFXButton eventsNavButton;
    @FXML
    private MFXButton sellingNavButton;
    @FXML
    private MFXButton ticketingNavButton;
    @FXML
    private Rectangle sellingLine;
    @FXML
    private Rectangle ticketingLine;
    @FXML
    private Rectangle eventsLine;


    @FXML
    private VBox mainEventContainer;
    @FXML
    private StackPane mainLayout;
    @FXML
    private StackPane secondaryLayout;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model = Model.getInstance();
        model.setEventsDisplayer(this);

        NavigationHoverControl navigationHoverControl = new NavigationHoverControl(eventsLine, sellingLine, ticketingLine, eventsNavButton, sellingNavButton, ticketingNavButton);
        navigationHoverControl.initializeNavButtons();
        displayEvents();
    }

    @FXML
    private void closeWindow(ActionEvent event) {
        this.secondaryLayout.setDisable(true);
        this.secondaryLayout.setVisible(false);
    }


    // observe the size off the list, if is changing update the UI (Observable design pattern)

    /**Updates the events off the user based on the user interaction*/
    @Override
    public void displayEvents() {
        mainEventContainer.getChildren().clear();
        model.getEvents().forEach(e -> mainEventContainer.getChildren().add(new EventComponenet(e, new ManageAction(this.secondaryLayout))));
    }


    public void createTicket(ActionEvent actionEvent) throws IOException {


        //TicketsGeneration ticketController = loader.getController();
        //ticketController.setMainController(this);

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Create Ticket");
        stage.setScene(new Scene(new TicketsGeneration(secondaryLayout).getRoot()));
        stage.show();
    }

    public void createEvent(ActionEvent actionEvent) {


    }
}
