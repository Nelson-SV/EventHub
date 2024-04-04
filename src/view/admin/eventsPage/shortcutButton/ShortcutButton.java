package view.admin.eventsPage.shortcutButton;

import be.Status;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import view.admin.listeners.SortCommander;
import view.admin.listeners.SortSubject;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ShortcutButton extends MFXButton implements SortSubject, Initializable {
    private String sortOperationText;
    private final String ALL_TEXT = "All events";
    private final Status defaultStatus = Status.ALL;
    private final Status sortByStatus;
    @FXML
    private MFXButton shortcutButton;
    private boolean isSelected;
    private Status operationToBePerformed;
    private SortCommander sortCommander;
    private String identificcationId;

    public ShortcutButton(String sortText, Status status, SortCommander sortCommander) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ShortcutButtonView.fxml"));
        loader.setController(this);
        try {
            this.sortCommander = sortCommander;
            this.sortByStatus = status;
            this.sortOperationText = sortText;
            this.identificcationId = sortText;
            shortcutButton = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public MFXButton getShortcutButton() {
        return shortcutButton;
    }

    @Override
    public void changeToAll() {
        //isSelected=false;
        this.shortcutButton.setText(ALL_TEXT);
    }

    @Override
    public void changeToSort() {
       // isSelected=false;
        this.shortcutButton.setText(sortOperationText);
    }

    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public String getIdentificationId() {
        return identificcationId;
    }

    @Override
    public void changePerformedOperationToDefault() {
        this.operationToBePerformed = defaultStatus;
    }

    @Override
    public void changePerformedOperationToSort() {
        this.operationToBePerformed = sortByStatus;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.shortcutButton.setText(sortOperationText);
        this.operationToBePerformed = sortByStatus;
        this.shortcutButton.setOnAction(event -> {
            Status currentOperation = this.operationToBePerformed;
            isSelected = !isSelected;
            sortCommander.setLatestSelected(identificcationId);
            sortCommander.performSortOperation(currentOperation);
            if (isSelected) {
                changeToAll();
                changePerformedOperationToDefault();
            } else {
                changeToSort();
                changePerformedOperationToSort();
            }
        });
    }
    @Override
    public void setSelected(boolean val) {
        isSelected = val;
    }
}
