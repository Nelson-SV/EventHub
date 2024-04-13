package view.admin.searchComponent;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.PopupControl;
import javafx.scene.control.TextField;
import view.components.listeners.DataHandler;

import java.net.URL;
import java.util.ResourceBundle;


public class SearchController<T> implements Initializable {
    @FXML
    private Button undoButton;
    @FXML
    private TextField searchWindow;
    private DataHandler<T> dataHandler;
    @FXML
    private PopupControl popupWindow;
    @FXML
    private ListView<T> searchResponseHolder;

    public SearchController(DataHandler dataHandler) {
        this.dataHandler = dataHandler;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        constructPopUpWindow();
        setTheSelectedItem();
        addSearchFieldListener();
        addUndoButtonListener();
    }

    private void constructPopUpWindow() {
        popupWindow = new PopupControl();
        searchResponseHolder = new ListView<>();
    }


    private void setTheSelectedItem(){
       searchResponseHolder.selectionModelProperty().addListener((observable, oldValue, newValue) -> {
           this.searchWindow.setText(newValue.toString());
           System.out.println(newValue);
       });
    }

    private void addSearchFieldListener(){
        this.searchWindow.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.isEmpty()){
                dataHandler.performSearchOperation();
                searchResponseHolder.setItems(dataHandler.getResultData());
                if (!searchResponseHolder.getItems().isEmpty()) {
                    Bounds boundsInScreen = searchWindow.localToScreen(searchWindow.getBoundsInLocal());
                    popupWindow.show(searchWindow, boundsInScreen.getMinX(), boundsInScreen.getMaxY());
                } else {
                    popupWindow.hide();
                }
            }
        });
    }

    private void addUndoButtonListener(){
        this.undoButton.setOnAction((event)->{
            this.searchWindow.setText("");
            this.dataHandler.undoSearchOperation();
        });
    }


}




