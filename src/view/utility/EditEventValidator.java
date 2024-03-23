package view.utility;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import javafx.application.Platform;
import javafx.css.PseudoClass;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import java.time.LocalDate;
import java.time.LocalTime;

public class EditEventValidator {
    private static final PseudoClass ERROR_PSEUDO_CLASS = PseudoClass.getPseudoClass("error");

    public static boolean isEventValid(TextField name, MFXDatePicker startDate, MFXComboBox<LocalTime> startTime, TextArea eventLocation) {
        boolean isValid = true;
        if (name.getText().isEmpty()) {
            name.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, true);
            isValid = false;
        } else {
            name.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, false);
        }

        LocalDate startDateValue = startDate.getValue();
        LocalTime startTimeValue = startTime.getValue();
        String startText = startDate.getText();
        String startTimeText = startTime.getText();

        if (startText.isEmpty()) {
            startDate.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, true);
            isValid = false;
        } else {
            startDate.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, false);
        }


        if (startTimeText.isEmpty()) {
            startTime.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, true);
            isValid = false;
        } else {
            startTime.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, false);
        }

        if (startDateValue == null && startTimeValue == null) {
            startDate.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, true);
            startTime.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, true);
            isValid = false;
        } else {
            startDate.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, false);
            startTime.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, false);
        }

        if (eventLocation.getText().isEmpty()) {
            eventLocation.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, true);
            isValid = false;
        } else {
            eventLocation.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, false);
        }
        return isValid;
    }


    public static void addEventListeners(TextField eventName, MFXDatePicker startDate, MFXComboBox<LocalTime> startTime, TextArea eventLocation) {

        eventName.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() == 1) {
                Platform.runLater(() -> eventName.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, false));
            } else if (newValue.isEmpty()) {
                Platform.runLater(() -> eventName.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, true));
            }
        });

        startDate.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                Platform.runLater(() -> startDate.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, false));
            } else {
                Platform.runLater(() -> startDate.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, true));
            }
        });

        startDate.textProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue.length() == 1) {
                Platform.runLater(() -> startDate.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, false));
            } else if (newValue.isEmpty()) {
                Platform.runLater(() -> startDate.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, true));
            }
        }));


        startTime.textProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue.length() == 1) {
                Platform.runLater(() -> startTime.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, false));
            } else if (newValue.isEmpty()) {
                Platform.runLater(() -> startTime.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, true));
            }
        }));

        startTime.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                Platform.runLater(() -> startTime.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, false));
            } else {
                Platform.runLater(() -> startTime.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, true));
            }
        });

        eventLocation.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() == 1) {
                Platform.runLater(() -> eventLocation.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, false));
            } else if (newValue.isEmpty()) {
                Platform.runLater(() -> eventLocation.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, true));
            }
        });
    }

//    private static void markFieldAsInvalid(TextInputControl field) {
//        field.getStyleClass().clear();
//        field.getStyleClass().addAll("mfx-text-field", "mfx-combo-box", "mfx-date-picker","normal","invalid-field");
//    }
//
//    private static void markFieldAsValid(TextInputControl field) {
//        field.getStyleClass().clear();
//        field.getStyleClass().addAll("mfx-text-field", "mfx-combo-box","mfx-date-picker", "normal");
//    }
}
