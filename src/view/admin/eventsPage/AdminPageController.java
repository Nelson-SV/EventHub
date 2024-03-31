package view.admin.eventsPage;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import view.admin.mainAdmin.AdminModel;
import view.components.deleteEvent.DeleteButton;
import view.components.eventsPage.eventDescription.EventComponent;
import view.components.eventsPage.manageButton.ManageAction;
import view.components.listeners.Displayable;
import view.components.main.CommonModel;

import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminPageController implements Initializable, Displayable {
    private AdminModel adminModel;
    @FXML
    private VBox adminEventPage;

    @FXML
    private VBox eventsContainer;

    private StackPane secondaryLayout,thirdLayout;


    public AdminPageController(AdminModel adminModel, StackPane secondaryLayout,StackPane thirdLayout) {
        this.adminModel= adminModel;
        adminModel.setEventsDisplayer(this);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


    @FXML
    private void searchEvent(ActionEvent event){
        System.out.println("searching events");
    }

    @Override
    public void displayEvents() {
        if(eventsContainer.getScene()!=null){
            Platform.runLater(() -> {
                eventsContainer.getChildren().clear();
                adminModel.sortedEventsList()
                        .forEach(e ->eventsContainer .getChildren()
                                .add(new EventComponent(e, new ManageAction(this.secondaryLayout, thirdLayout, e.getEventDTO().getId(), adminModel),new DeleteButton(secondaryLayout,thirdLayout,adminModel,e.getEventDTO().getId()))));
            });

        }
    }
}
