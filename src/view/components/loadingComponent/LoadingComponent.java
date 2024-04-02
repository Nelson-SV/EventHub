package view.components.loadingComponent;

import exceptions.ErrorCode;
import exceptions.ExceptionHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;
//Todo  remove the printstacktrace


public class LoadingComponent extends VBox {
    @FXML
    private VBox loadingComponent;
   @FXML
    private Label actionMessage;


    public LoadingComponent() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("LoadingComponent.fxml"));
        loader.setController(this);
        try{
            loadingComponent=loader.load();
            this.getChildren().add(loadingComponent);
            this.setAlignment(Pos.CENTER);
        } catch (IOException e) {
            ExceptionHandler.erorrAlertMessage(ErrorCode.LOADING_FXML_FAILED.getValue());
        }
    }

    public VBox getLoadingComponent() {
        return loadingComponent;
    }

    public void setAction(String currentAction){
        this.actionMessage.setText(currentAction);
    }

}
