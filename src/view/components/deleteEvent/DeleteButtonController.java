package view.components.deleteEvent;
import exceptions.ErrorCode;
import exceptions.ExceptionLogger;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import view.components.confirmationWindow.ConfirmationWindow;
import view.components.listeners.OperationHandler;
import view.components.loadingComponent.LoadingActions;
import view.components.loadingComponent.LoadingComponent;
import view.components.main.CommonModel;
import view.components.main.Model;
import view.utility.CommonMethods;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;

public class DeleteButtonController implements OperationHandler, Initializable {
    @FXML
    private VBox deleteOperation;
    private StackPane secondaryLayout;
    private StackPane thirdLayout;
    private CommonModel model;
    private int eventId;
    private Service<Void> deleteEventService;
    private LoadingComponent loadingComponent;
    private ConfirmationWindow confirmationWindow;
    public DeleteButtonController(StackPane secondaryLayout, StackPane thirdLayout, CommonModel model, int eventId) {
        this.secondaryLayout = secondaryLayout;
        this.thirdLayout = thirdLayout;
        this.model = model;
        this.eventId = eventId;
    }

    @Override
    public void performOperation()
    {
        initializeLoadingComponent();
        initializeDeleteService();
    }


    private void initializeDeleteOperation() {
        secondaryLayout.getChildren().clear();
        secondaryLayout.getChildren().add(initializeConfirmationWindow());
        secondaryLayout.setDisable(false);
        secondaryLayout.setVisible(true);
    }

    private ConfirmationWindow initializeConfirmationWindow() {
        confirmationWindow = new ConfirmationWindow(this, secondaryLayout);
        confirmationWindow.setEntityTitle(model.getEventById(this.eventId).getName());
        confirmationWindow.setEventStartDate(model.getEventById(this.eventId).getStartDate());
        confirmationWindow.setEventLocation(model.getEventById(this.eventId).getLocation());
        confirmationWindow.setAlignment(Pos.CENTER);
        return confirmationWindow;
    }


    private void initializeLoadingComponent(){
        loadingComponent= new LoadingComponent();
        loadingComponent.setAlignment(Pos.CENTER);
        CommonMethods.showSecondaryLayout(thirdLayout,loadingComponent);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        deleteOperation.addEventHandler(MouseEvent.MOUSE_CLICKED, this::addEventHandler);
    }
    private void addEventHandler(MouseEvent event) {
        initializeDeleteOperation();
    }


    private void initializeDeleteService(){
        PauseTransition pauseTransition = new PauseTransition(Duration.millis(500));
        deleteEventService = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        model.deleteEvent(eventId);
                        return null;
                    }
                };
            }
        };
    deleteEventService.setOnSucceeded((event)->{
        loadingComponent.setAction(LoadingActions.SUCCES.getActionValue());
        pauseTransition.setOnFinished((ev) -> {
            CommonMethods.closeWindow(thirdLayout);
            CommonMethods.closeWindow(secondaryLayout);
            Platform.runLater(()->model.getEventsDisplayer().displayEvents());
        });
        pauseTransition.play();
    });
    deleteEventService.setOnFailed((event)->{
        loadingComponent.setAction(LoadingActions.FAIL.getActionValue());
        pauseTransition.setOnFinished((ev)->{
            ExceptionLogger.getInstance().getLogger().log(Level.SEVERE,deleteEventService.getException().getMessage());
            CommonMethods.closeWindow(thirdLayout);
              confirmationWindow.setErrorMessage(ErrorCode.OPERATION_DB_FAILED.getValue());
        });
        pauseTransition.play();
    });
    deleteEventService.restart();
    }
}
