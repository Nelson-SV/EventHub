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
import view.components.manageButton.ManageAction;
import view.components.ticketsGeneration.TicketsGeneration;
import view.utility.NavigationHoverControl;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
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


    private static ObservableList<String[]> strings = FXCollections.observableArrayList();

    static {
        strings.add(new String[]{"TestName 1", "Lorem ipsum donor,43", "24/03/2024", "Live", "23/450", "$4500"});
        strings.add(new String[]{"TestName 2", "Lorem ipsum donor,56", "30/03/2024", "Live", "23/450", "$5000"});
        strings.add(new String[]{"TestName 3", "Lorem ipsum donor,46", "22/02/2024", "Ended", "0/450", "$50000"});
        strings.add(new String[]{"TestName 4", "Lorem ipsum donor,45", "01/04/2024", "Live", "23/450", "$4500"});
        strings.add(new String[]{"TestName 10", "Lorem ipsum donor,56", "01/04/2024", "Live", "23/450", "$4500"});

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model = Model.getInstance();
        NavigationHoverControl navigationHoverControl = new NavigationHoverControl(eventsLine, sellingLine, ticketingLine, eventsNavButton, sellingNavButton, ticketingNavButton);
        navigationHoverControl.initializeNavButtons();
        displayEvents();
    }

    @FXML
    private void closeWindow(ActionEvent event) {
        this.secondaryLayout.setDisable(true);
        this.secondaryLayout.setVisible(false);
    }

//TODO
    // observe the size off the list, if is changing update the UI (Observable design pattern)

    private void displayEvents() {
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
}
