package view.components.eventDescription;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import view.components.manageButton.ManageAction;
import view.components.manageButton.ManageController;

public class ActionCell  extends TableCell<String,VBox> {
    @FXML
    private VBox manageControl;
    @FXML
    private StackPane secondaryLayout;
    public ActionCell(StackPane stackPane){
        this.setPrefWidth(58);
        this.setPrefHeight(36);
        this.manageControl= new ManageAction(stackPane);
        this.secondaryLayout=stackPane;
    }
    protected void updateItem(VBox item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            if (manageControl == null) {
                manageControl = new ManageAction(this.secondaryLayout);
            }
            setGraphic(manageControl);
            setText(null);
        } else {

            setGraphic(manageControl);
        }
    }
}
