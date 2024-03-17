package view.components.main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import view.components.eventDescription.ActionCell;
import view.components.eventDescription.EventComponenet;
import view.components.manageButton.ManageAction;
import view.components.manageButton.ManageController;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private VBox mainEventContainer;
    @FXML
    private StackPane mainLayout;
    // go from table to  custom components and replace with the scroll pane
    @FXML
    private TableColumn description;
    @FXML
    private TableColumn status;
    @FXML
    private TableColumn tickets;
    @FXML
    private TableColumn revenue;
    @FXML
    private StackPane secondaryLayout;
    @FXML
    private HBox navigation;
    @FXML
    private TableView<String> eventTable;
    @FXML
    private TableColumn<String ,VBox> actionsColumn;
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
