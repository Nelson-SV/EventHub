package view.admin.mainAdmin;
import exceptions.EventException;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import view.admin.eventsPage.AdminEventPage;
import view.admin.usersPage.UserPageComponent;
import view.components.listeners.InitializationErrorListener;
import view.utility.NavigationHoverControl;
import java.net.URL;
import java.util.ResourceBundle;
public class AdminController implements Initializable, InitializationErrorListener {
    private boolean initializationError = false;
    private AdminModel adminModel;
    @FXML
    private MFXButton eventsNavButton;
    @FXML
    private MFXButton usersNavButton;
    @FXML
    private Rectangle sellingLine;
    @FXML
    private Rectangle eventsLine;
    @FXML
    private VBox adminPageDisplayer;
    @FXML
    private StackPane adminSecondaryLayout,adminThirdLayout,adminFourthLayout;
    private UserPageComponent userPageComponent;
    private AdminEventPage adminEventPage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            adminModel = new AdminModel();
            NavigationHoverControl navigationHoverControl = new NavigationHoverControl(eventsLine, sellingLine, eventsNavButton, usersNavButton);
            navigationHoverControl.initializeNavButtons();
            initializeStartingPage();
            addUsersNavListener(usersNavButton);
            addAdminEventsNavListener(eventsNavButton);
        } catch (EventException e) {
            e.printStackTrace();
            initializationError = true;
        }
    }

    @Override
    public boolean isInitializationError() {
        return initializationError;
    }

    private void navigateEventsPage() {
     initializeStartingPage();
     userPageComponent=null;
    }

    private void initializeStartingPage(){
        if(adminEventPage==null){
            adminEventPage = new AdminEventPage(adminModel,adminSecondaryLayout,adminThirdLayout,adminFourthLayout);
            adminPageDisplayer.getChildren().clear();
            adminPageDisplayer.getChildren().add(adminEventPage);
        }

        //adminPageDisplayer.getChildren().clear();
        //adminPageDisplayer.getChildren().add(new AdminEventPage(this.adminModel,adminSecondaryLayout,adminThirdLayout,adminFourthLayout));
    }



    private void openUsersPage(){
        if(userPageComponent==null){
            userPageComponent = new UserPageComponent(adminSecondaryLayout,adminThirdLayout,adminFourthLayout,adminModel);
            adminPageDisplayer.getChildren().clear();
            adminPageDisplayer.getChildren().add(userPageComponent.getRoot());
        }
        adminEventPage=null;
    }

    private void addUsersNavListener(MFXButton usersNavButton){
        usersNavButton.addEventHandler(MouseEvent.MOUSE_CLICKED,event->{
            openUsersPage();
        });
    }

    private void addAdminEventsNavListener(MFXButton adminEventsButton){
        adminEventsButton.setOnAction((event)->{
            navigateEventsPage();
        });
    }
}
