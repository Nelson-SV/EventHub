package view.admin.eventsPage.assignButton;
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
import java.util.ResourceBundle;

public class AssignController implements Initializable {
    @FXML
    private VBox assignContainer;
    private AdminModel adminModel;
    private StackPane secondaryLayout,thirdLayout;
    private int eventId;

    public AssignController(AdminModel adminModel, StackPane secondaryLayout, StackPane thirdLayout, int eventId) {
        this.adminModel = adminModel;
        this.secondaryLayout = secondaryLayout;
        this.thirdLayout = thirdLayout;
        this.eventId = eventId;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("initialized");
        assignContainer.addEventHandler(MouseEvent.MOUSE_CLICKED, this::showManagementPage);
    }

    private void showManagementPage(MouseEvent mouseEvent){
        adminModel.setSelectedEvent(this.eventId);
        AdminEventManagePage adminEventManagePage = new AdminEventManagePage(secondaryLayout,thirdLayout,adminModel);
        adminEventManagePage.getRoot().setAlignment(Pos.CENTER);
        CommonMethods.showSecondaryLayout(secondaryLayout, adminEventManagePage.getRoot());
    }
}
