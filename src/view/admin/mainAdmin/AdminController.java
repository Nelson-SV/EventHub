package view.admin.mainAdmin;
import exceptions.EventException;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import view.admin.eventsPage.AdminEventPage;
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
    private MFXButton statsNavButton;
    @FXML
    private Rectangle sellingLine;
    @FXML
    private Rectangle ticketingLine;
    @FXML
    private Rectangle eventsLine;
    @FXML
    private VBox adminPageDisplayer;
    @FXML
    private StackPane adminSecondaryLayout,adminThirdLayout,adminFourthLayout;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            adminModel = new AdminModel();
            NavigationHoverControl navigationHoverControl = new NavigationHoverControl(eventsLine, sellingLine, ticketingLine, eventsNavButton, usersNavButton, statsNavButton);
            navigationHoverControl.initializeNavButtons();
            initializeStartingPage();
        } catch (EventException e) {
            initializationError = true;
        }
    }

    @Override
    public boolean isInitializationError() {
        return initializationError;
    }

    public void navigateEventsPage(ActionEvent event) {
    }

    public void selling(ActionEvent event) {
    }

    public void createSpecialTicket(ActionEvent event) {
    }

    private void initializeStartingPage(){
        adminPageDisplayer.getChildren().clear();
        adminPageDisplayer.getChildren().add(new AdminEventPage(this.adminModel,adminSecondaryLayout,adminThirdLayout,adminFourthLayout));
    }

}
