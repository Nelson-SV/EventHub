package view.components.deleteEvent;
import be.DeleteOperation;
import exceptions.ErrorCode;
import exceptions.EventException;
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
import view.utility.CommonMethods;
import java.net.URL;
import java.time.LocalDate;
import java.util.Arrays;
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
    private DeleteOperation performedDeleteOperation;
    public DeleteButtonController(StackPane secondaryLayout, StackPane thirdLayout, CommonModel model, int eventId,DeleteOperation deleteOperation) {
        this.secondaryLayout = secondaryLayout;
        this.thirdLayout = thirdLayout;
        this.model = model;
        this.eventId = eventId;
        this.performedDeleteOperation = deleteOperation;
    }

    @Override
    public void performOperation() {
        initializeLoadingComponent();
        initializeDeleteService();
    }


    private void initializeDeleteOperation() {
        secondaryLayout.getChildren().clear();
        confirmationWindow= new ConfirmationWindow(this,secondaryLayout);
        secondaryLayout.getChildren().add(confirmationWindow);
        secondaryLayout.setDisable(false);
        secondaryLayout.setVisible(true);
    }


    //TODO discuss if we need them otherwise, delete them
//    public void setConfirmationWindowMessage(String message){
//        this.confirmationWindow.setConfirmationTitle(message);
//    }
//
//    public void setConfirmationEntityTitle(String entityTitle){
//        this.confirmationWindow.setEntityTitle(entityTitle);
//    }
//
//    public void setConfirmationWindowEntityStartDate(LocalDate startDate){
//             this.confirmationWindow.setEventStartDate(startDate);
//    }
//
//    public void setConfirmationWindowEntityLocation(String location){
//        this.confirmationWindow.setEventLocation(location);
//    }

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
                    protected Void call() throws EventException {
                        model.performDeleteOperation(eventId, performedDeleteOperation);
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
            ExceptionLogger.getInstance().getLogger().log(Level.SEVERE, Arrays.toString(deleteEventService.getException().getStackTrace()));
            CommonMethods.closeWindow(thirdLayout);
              confirmationWindow.setErrorMessage(ErrorCode.OPERATION_DB_FAILED.getValue());
        });
        pauseTransition.play();
    });
    deleteEventService.restart();
    }
}
