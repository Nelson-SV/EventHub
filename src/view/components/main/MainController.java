package view.components.main;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import view.components.eventDescription.EventComponenet;
import view.components.events.CreateEventController;
import view.components.manageButton.ManageAction;
import view.utility.NavigationHoverControl;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private MFXButton eventsNavButton;
    @FXML
    private MFXButton sellingNavButton;
    @FXML
    private MFXButton ticketingNavButton;
    @FXML
    private Rectangle sellingLine;
    @FXML
    private Rectangle ticketingLine;
    @FXML
    private Rectangle eventsLine;


    @FXML
    private VBox mainEventContainer;
    @FXML
    private StackPane mainLayout;
    @FXML
    private StackPane secondaryLayout;


    private  static ObservableList<String[]> strings = FXCollections.observableArrayList();
    static {
        strings.add(new  String[]{"TestName 1","Lorem ipsum donor,43","24/03/2024", "Live" , "23/450", "$4500" }   );
        strings.add(new  String[]{"TestName 2","Lorem ipsum donor,56","30/03/2024", "Live" , "23/450", "$5000" });
        strings.add(new  String[]{"TestName 3","Lorem ipsum donor,46","22/02/2024", "Ended" , "0/450", "$50000" });
        strings.add(new  String[]{"TestName 4","Lorem ipsum donor,45","01/04/2024", "Live" , "23/450", "$4500" });
        strings.add(new  String[]{"TestName 10","Lorem ipsum donor,56","01/04/2024", "Live" , "23/450", "$4500" });

    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        NavigationHoverControl navigationHoverControl =  new NavigationHoverControl(eventsLine,sellingLine, ticketingLine,eventsNavButton,sellingNavButton,ticketingNavButton);
        navigationHoverControl.initializeNavButtons();
        for(String [] test : strings ){
            mainEventContainer.getChildren().add(new EventComponenet(test,new ManageAction(this.secondaryLayout)));
        }
    }
    @FXML
    private void closeWindow(ActionEvent event) {
        this.secondaryLayout.setDisable(true);
        this.secondaryLayout.setVisible(false);
    }
}
