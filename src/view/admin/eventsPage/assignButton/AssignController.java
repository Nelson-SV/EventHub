package view.admin.eventsPage.assignButton;
import be.User;
import exceptions.EventException;
import exceptions.ExceptionHandler;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import view.admin.eventsPage.manageEventAdminPage.AdminEventManagePage;
import view.admin.mainAdmin.AdminModel;
import view.utility.CommonMethods;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AssignController implements Initializable {
    @FXML
    private VBox assignContainer;
    private AdminModel adminModel;
    private StackPane secondaryLayout,thirdLayout,adminFourthLayout;
    private int eventId;

    private Service<List<User>>  eventCoordinatorsService;

    public AssignController(AdminModel adminModel, StackPane secondaryLayout, StackPane thirdLayout,StackPane adminFourthLayout, int eventId) {
        this.adminModel = adminModel;
        this.secondaryLayout = secondaryLayout;
        this.thirdLayout = thirdLayout;
        this.adminFourthLayout=adminFourthLayout;
        this.eventId = eventId;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        assignContainer.addEventHandler(MouseEvent.MOUSE_CLICKED, this::showManagementPage);
    }

    private void showManagementPage(MouseEvent mouseEvent){
        adminModel.setSelectedEvent(this.eventId);
        AdminEventManagePage adminEventManagePage = new AdminEventManagePage(secondaryLayout,thirdLayout,adminFourthLayout,adminModel);
        adminEventManagePage.getRoot().setAlignment(Pos.CENTER);
        adminModel.setCoordinatorsDisplayer(adminEventManagePage);
        CommonMethods.showSecondaryLayout(secondaryLayout, adminEventManagePage.getRoot());
        System.out.println(eventId);
        initializeEventCoordinatorsService();
    }
    private void initializeEventCoordinatorsService(){
        eventCoordinatorsService= new Service<List<User>>() {
            @Override
            protected Task<List<User>> createTask()  {
              return new Task<List<User>>() {
                  @Override
                  protected List<User> call() throws EventException {
                      adminModel.initializeEventCoordinators(eventId);
                      adminModel.initialiazeAllCoordinators(eventId);
                      return null;
                  }
              };
            }
        };
        eventCoordinatorsService.setOnSucceeded((event)->{
            adminModel.getCoordinatorsDisplayer().displayEventCoordinators();
            adminModel.getCoordinatorsDisplayer().displayAllCoordinators();
        });
        eventCoordinatorsService.setOnFailed((event -> {
            Throwable exception = eventCoordinatorsService.getException();
            Throwable cause = exception.getCause();
             String errorMessage = (cause != null) ? cause.getMessage() : exception.getMessage();
            ExceptionHandler.erorrAlertMessage(errorMessage);
        }));

    if(eventCoordinatorsService.isRunning()){
        eventCoordinatorsService.cancel();
    }
    eventCoordinatorsService.restart();
    }
}
