package view.components.eventsPage;
import exceptions.EventException;
import exceptions.ExceptionHandler;
import exceptions.TicketException;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import view.components.eventDescription.EventComponent;
import view.components.events.CreateEventController;
import view.components.listeners.Displayable;
import view.components.main.Model;
import view.components.manageButton.ManageAction;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
public class EventsPageController extends VBox implements Displayable, Initializable {
    @FXML
    private VBox eventsPageView;
    @FXML
    private VBox mainEventContainer;
    private StackPane secondaryLayout, thirdLayout;
    private Model model;

    private Service getEvents;

    public EventsPageController(StackPane secondaryLayout, StackPane thirdLayout) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("EventsView.fxml"));
        loader.setController(this);
        try {
            eventsPageView=loader.load();
            this.getChildren().add(eventsPageView);
            this.secondaryLayout = secondaryLayout;
            this.thirdLayout = thirdLayout;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void displayEvents() {
        mainEventContainer.getChildren().clear();
        model.sortedEventsList().forEach(e -> mainEventContainer.getChildren().add(new EventComponent(e, new ManageAction(this.secondaryLayout,thirdLayout, e.getId(),model))));
    }
@FXML
    private void createEvent(ActionEvent actionEvent) {
        this.secondaryLayout.setVisible(true);
        this.secondaryLayout.setDisable(false);
        CreateEventController createEventController = new CreateEventController(secondaryLayout, thirdLayout, model);
        secondaryLayout.getChildren().clear();
        secondaryLayout.getChildren().add(createEventController.getRoot());
    }

    @FXML
    private void searchEvent(ActionEvent event){

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            model= Model.getInstance();
            model.setEventsDisplayer(this);
            initializeEvents();
        } catch (EventException | TicketException e) {
            //exception needs to be treated with the ExceptionHandler , error message
            ExceptionHandler.erorrAlertMessage(e.getMessage());
        }
    }

    private void initializeEvents(){
        getEvents = new Service() {
            @Override
            protected Task createTask() {
                return new Task() {
                    @Override
                    protected Object call() throws Exception {
                        model.initializeEventsMap();
                        return null;
                    }
                };
            }
        };
        getEvents.setOnSucceeded(event -> displayEvents());
        getEvents.setOnFailed(event -> ExceptionHandler.erorrAlertMessage("Failed to load events"));
        getEvents.restart();
    }
}
