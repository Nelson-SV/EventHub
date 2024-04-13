package view.components.eventsPage;
import be.DeleteOperation;
import be.EventStatus;
import exceptions.ErrorCode;
import exceptions.ExceptionHandler;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import view.admin.searchComponent.SearchComponent;
import view.components.deleteEvent.DeleteButton;
import view.components.eventsPage.eventDescription.EventComponent;
import view.components.eventsPage.eventManagement.eventCreation.CreateEventController;
import view.components.eventsPage.searchDataHandler.EventSearchHandler;
import view.components.listeners.Displayable;
import view.components.main.Model;
import view.components.eventsPage.manageButton.ManageAction;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.input.MouseEvent;



public class EventsPageController extends VBox implements Displayable, Initializable {
    @FXML
    private VBox eventsPageView;
    @FXML
    private VBox mainEventContainer;
    private StackPane secondaryLayout, thirdLayout;
    private Model model;
    private Service<Void> getEvents;
    @FXML
    private GridPane eventActions;
    private SearchComponent<EventStatus> searchComponent;

    public EventsPageController(StackPane secondaryLayout, StackPane thirdLayout,Model model) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("EventsView.fxml"));
        loader.setController(this);
        try {
            this.model=model;
            eventsPageView = loader.load();
            this.getChildren().add(eventsPageView);
            this.secondaryLayout = secondaryLayout;
            this.thirdLayout = thirdLayout;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Updates the events off the user based on the user interaction
     */
    @Override
    public void displayEvents() {
        if(mainEventContainer.getScene()!=null){
            Platform.runLater(() -> {
                mainEventContainer.getChildren().clear();
                model.sortedEventsList()
                        .forEach(e ->
                                {
                                    ManageAction manageAction = new ManageAction(secondaryLayout,thirdLayout,e.getEventDTO().getId(),model);
                                    DeleteButton deleteButton = new DeleteButton(secondaryLayout,thirdLayout,model,e.getEventDTO().getId(), DeleteOperation.DELETE_EVENT);
                                    EventComponent eventComponent = new EventComponent(e.getEventDTO(),manageAction,deleteButton);
                                    mainEventContainer.getChildren().add(eventComponent);
                                }
                        );

            });
        }
    }

    @FXML
    private void createEvent(ActionEvent actionEvent) {
        this.secondaryLayout.setVisible(true);
        this.secondaryLayout.setDisable(false);
        CreateEventController createEventController = new CreateEventController(secondaryLayout, thirdLayout, model);
        secondaryLayout.getChildren().clear();
        secondaryLayout.getChildren().add(createEventController.getRoot());
        StackPane.setAlignment(createEventController.getRoot(), Pos.CENTER);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
            model.setEventsDisplayer(this);
            initializeEvents();
            initializeSearchWindow();
            closeSearchPopUpWindowListener();
    }

    private void initializeEvents() {
        getEvents = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        model.initializeEventsMap();
                        return null;
                    }
                };
            }
        };
        getEvents.setOnSucceeded(event -> displayEvents());
        getEvents.setOnFailed(event -> ExceptionHandler.errorAlertMessage(ErrorCode.FAILED_TO_LOAD_EVENTS.getValue()));
        getEvents.restart();
    }

    private void initializeSearchWindow(){
        EventSearchHandler eventSearchHandler = new EventSearchHandler(model);
        searchComponent= new SearchComponent<>(eventSearchHandler);
        searchComponent.setSearchWindowWidth(250);
        searchComponent.setSearchResponseHolderHeight(250);
        searchComponent.getPopupWindow().setMaxHeight(250);
        eventActions.add(searchComponent.getSearchRoot(),0,0);
    }
    private void closeSearchPopUpWindowListener(){
        eventsPageView.addEventHandler(MouseEvent.MOUSE_CLICKED,event->{
            this.searchComponent.closeWindow();
        });
    }
}
