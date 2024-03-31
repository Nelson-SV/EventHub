package view.admin.eventsPage;
import exceptions.ErrorCode;
import exceptions.EventException;
import exceptions.ExceptionHandler;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import view.admin.eventsPage.assignComponent.AssignButton;
import view.admin.eventsPage.eventDescription.EventController;
import view.admin.eventsPage.eventDescription.EventDescription;
import view.admin.mainAdmin.AdminModel;
import view.components.deleteEvent.DeleteButton;
import view.components.eventsPage.eventDescription.EventComponent;
import view.components.eventsPage.manageButton.ManageAction;
import view.components.listeners.Displayable;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminPageController implements Initializable, Displayable {
    private AdminModel adminModel;
    @FXML
    private VBox eventsContainer;
    @FXML
    private VBox adminEventPage;

    private StackPane secondaryLayout,thirdLayout;
    private Service<Void> getEvents;

    public AdminPageController(AdminModel adminModel, StackPane secondaryLayout,StackPane thirdLayout) {
        this.adminModel= adminModel;
        this.secondaryLayout=secondaryLayout;
        this.thirdLayout=thirdLayout;
        adminModel.setEventsDisplayer(this);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeEvents();
    }

    @Override
    public void displayEvents() {
        if(eventsContainer.getScene()!=null){
            Platform.runLater(() -> {
                eventsContainer.getChildren().clear();
                adminModel.sortedEventsList()
                        .forEach(e ->eventsContainer .getChildren()
                                .add(new EventDescription(e,new AssignButton(adminModel,secondaryLayout,thirdLayout,e.getEventDTO().getId()),new DeleteButton(secondaryLayout,thirdLayout,adminModel,e.getEventDTO().getId()))));
            });
        }
    }

    private void initializeEvents() {
        getEvents = new Service<Void>() {
            @Override
            protected Task<Void> createTask()  {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws EventException {
                        adminModel.initializeEvents();
                        return null;
                    }
                };
            }
        };
        getEvents.setOnSucceeded(event -> displayEvents());
        getEvents.setOnFailed(event -> ExceptionHandler.erorrAlertMessage(ErrorCode.FAILED_TO_LOAD_EVENTS.getValue()));
        getEvents.restart();
    }

}
