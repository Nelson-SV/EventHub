package view.admin.mainAdmin;
import exceptions.EventException;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.shape.Rectangle;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            adminModel = new AdminModel();
            NavigationHoverControl navigationHoverControl = new NavigationHoverControl(eventsLine, sellingLine, ticketingLine, eventsNavButton, usersNavButton, statsNavButton);
            navigationHoverControl.initializeNavButtons();
        } catch (EventException e) {
            System.out.println(e.getMessage());
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
}
