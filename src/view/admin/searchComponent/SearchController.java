package view.admin.searchComponent;
import be.User;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
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
    @FXML
    private HBox searchWindowContainer;
    @FXML
    private PopupControl noResultPopup;
    @FXML
    private Label noResultLabel;

    //TODO display info meesage if no data is returned by the search

    public SearchController(DataHandler dataHandler) {
        this.dataHandler = dataHandler;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        constructPopUpWindow();
        addSearchFieldListener();
        addUndoButtonListener();
        addSceneClickListener();
        addSelectionListener(this.searchResponseHolder);
    }

    private void constructPopUpWindow() {
        popupWindow = new PopupControl();
        searchResponseHolder = new ListView<>();
        popupWindow.getScene().setRoot(searchResponseHolder);
    }

    private void addSearchFieldListener() {
        PauseTransition pauseTransition = new PauseTransition(Duration.millis(500));
        PauseTransition hideNoResultWindow = new PauseTransition(Duration.millis(500));
        this.searchWindow.textProperty().addListener((observable, oldValue, newValue) -> {
            pauseTransition.setOnFinished((event) -> {
                if (!newValue.isEmpty()) {
                    searchResponseHolder.setItems(dataHandler.getResultData(newValue));
                    if (!searchResponseHolder.getItems().isEmpty()) {
                        configurePopUpWindow();
                    } else {
                        initializeEmptyResponse();
                        hideNoResultWindow.setOnFinished((actionEvent)->{
                            noResultPopup.hide();
                        });
                        popupWindow.hide();
                        hideNoResultWindow.playFromStart();
                    }
                } else {
                    popupWindow.hide();
                }
            });
            pauseTransition.playFromStart();

        });
        this.searchWindow.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                popupWindow.hide();
            }
        });
    }

    private void configurePopUpWindow() {
        Bounds boundsInScreen = searchWindow.localToScreen(searchWindow.getBoundsInLocal());
        searchResponseHolder.setPrefWidth(searchWindow.getWidth());
        searchResponseHolder.setPrefHeight(300);
        searchResponseHolder.setMaxWidth(searchWindow.getWidth());
        searchResponseHolder.setMaxWidth(300);
        popupWindow.getScene().getStylesheets().add("view/admin/searchComponent/SearchWindow.css");
        ((Parent) popupWindow.getScene().getRoot()).getStyleClass().add("popupView");
        searchResponseHolder.getStylesheets().add("view/admin/searchComponent/SearchWindow.css");
        popupWindow.setPrefWidth(searchWindow.getWidth());
        popupWindow.setMaxWidth(searchWindow.getWidth());
        popupWindow.setMaxHeight(300);
        popupWindow.show(searchWindow, boundsInScreen.getMinX(), boundsInScreen.getMaxY());
    }

    private void addUndoButtonListener() {
        this.undoButton.setOnAction((event) -> {
            this.searchWindow.setText("");
            this.dataHandler.undoSearchOperation();
            Platform.runLater(()->{
                undoButton.setDisable(true);
            });
        });
    }

    private void addSelectionListener(ListView<T> searchResponseHolder) {
        searchResponseHolder.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                dataHandler.performSelectSearchOperation(((User) newValue).getUserId());
                Platform.runLater(() -> {
                    if (!searchResponseHolder.getItems().isEmpty()) {
                        searchResponseHolder.getSelectionModel().clearSelection();
                    }
                });
                undoButton.setDisable(false);
                popupWindow.hide();
            }
        });
    }

    public TextField getSearchWindow() {
        return searchWindow;
    }

    private void addSceneClickListener() {
        searchWindow.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {

                    Node node = null;
                    if (popupWindow.getSkin() == null) {
                        return;
                    }

                    Node popupRoot = (Node) popupWindow.getSkin().getNode();
                    boolean isPopupOrChild = false;
                    while (node != null) {
                        if (node == popupRoot) {
                            isPopupOrChild = true;
                            break;
                        }
                        node = node.getParent();
                    }
                    if (!isPopupOrChild) {
                        searchWindow.setText("");
                        popupWindow.hide();
                    }
                });
            }
        });
    }


    public PopupControl getPopupWindow() {
        return popupWindow;
    }
    public void setSearchWindowWidth(int width) {
        this.searchWindowContainer.setMaxWidth(width);
        this.searchWindowContainer.setPrefWidth(width);
    }
    private void initializeEmptyResponse() {
        noResultPopup = new PopupControl();
        noResultLabel = new Label("No result");
        noResultLabel.setStyle("-fx-background-color: #051017; -fx-text-fill: #c2d1e2; -fx-border-radius: 5; -fx-background-radius: 5;");
        Bounds boundsInScreen = searchWindow.localToScreen(searchWindow.getBoundsInLocal());
        noResultPopup.getScene().setRoot(noResultLabel);
        noResultPopup.setPrefWidth(searchWindow.getWidth());
        noResultPopup.setMaxWidth(searchWindow.getWidth());
        noResultPopup.setMaxHeight(300);
        noResultPopup.show(searchWindow, boundsInScreen.getMinX(), boundsInScreen.getMaxY());
    }

}


