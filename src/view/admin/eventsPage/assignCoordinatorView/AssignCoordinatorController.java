package view.admin.eventsPage.assignCoordinatorView;

import io.github.palexdev.materialfx.controls.MFXCheckbox;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class AssignCoordinatorController implements Initializable {
   @FXML
    private HBox assignCoordinatorContainer;
    @FXML
    MFXCheckbox checkBox;
    private boolean isChecked;
    private final boolean[] checkBoxValue ={false};
    private int entityId;
    private String firstName,lastName;

    public AssignCoordinatorController(int entityId,String firstName,String lastName) {
        this.entityId=entityId;
        this.firstName=firstName;
        this.lastName=lastName;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeCheckBox(checkBox,firstName,lastName);
        addCheckListener(checkBox);
    }

    private void initializeCheckBox( MFXCheckbox checkBox,String firstName, String lastName){
        checkBox.setText(firstName+" " + lastName);
    }

    private void addCheckListener(MFXCheckbox checkBox){
        checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            this.checkBoxValue[0]=newValue;
            System.out.println("Element checked + " + entityId);
        });
    }

    public boolean isChecked(){
        return this.checkBoxValue[0];
    }

    public int getSelectedEntityId(){
        return this.entityId;
    }
}
