package view.utility;

import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.css.PseudoClass;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.util.Duration;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class EditEventValidator {
    private static final PseudoClass ERROR_PSEUDO_CLASS = PseudoClass.getPseudoClass("error");
    private static final String VALID_TIME_FORMAT = "Please enter the time in the 24-hour format: HH:mm.";
    private static final String VALID_DATE_FORMAT = "Please enter the date in this format 'yyyy-MM-dd' (e.g.,2024-03-22).";
    private static final String regex = "^([01]?[0-9]|2[0-3]):[0-5][0-9]$";
    private static final DateTimeFormatter textToTime =  DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static boolean isEventValid(TextField name, MFXDatePicker startDate, MFXComboBox<LocalTime> startTime, TextArea eventLocation, TextArea errorBox) {

        String startText = startDate.getText();
        String startTimeText = startTime.getText();
        boolean isValid = true;
        boolean isValidTime = startTimeText.matches(regex);
        LocalDate validDate = parseDate(startText,textToTime);

        if (!isValidTime) {
            return false;
        }

        if(validDate==null){
            return false;
        }


        if (name.getText().isEmpty()) {
            name.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, true);
            isValid = false;
        } else {
            name.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, false);
        }

        LocalDate startDateValue = startDate.getValue();
        LocalTime startTimeValue = startTime.getValue();


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


    private static void enableInvalidMessage(TextArea textArea) {
        textArea.setDisable(false);
        textArea.setVisible(true);
    }

    private static void hideInvalidMessage(TextArea textArea) {
        System.out.println("hide");
        textArea.setDisable(true);
        textArea.setVisible(false);
    }

    public static void addTimeToolTip(MFXComboBox<LocalTime> time) {
        Tooltip errorTooltip = new Tooltip(VALID_TIME_FORMAT);
        time.setTooltip(errorTooltip);
    }

    public static void  addDateToolTip(MFXDatePicker date){
        Tooltip tooltip = new Tooltip(VALID_DATE_FORMAT);
        date.setTooltip(tooltip);
    }

    public static void addTimeValidityChecker(MFXComboBox<LocalTime> startTime) {
        startTime.textProperty().addListener(((observable, oldValue, newValue) -> {
            PauseTransition pauseTransition = new PauseTransition(Duration.millis(100));
            pauseTransition.setOnFinished((e) -> {
                startTime.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, !newValue.matches("^([01]?[0-9]|2[0-3]):[0-5][0-9]$"));
            });
            pauseTransition.playFromStart();
        }));
    }


    public static void addDateValidityChecker(MFXDatePicker date){
        date.textProperty().addListener(((observable, oldValue, newValue) -> {
            PauseTransition pauseTransition = new PauseTransition(Duration.millis(100));
            pauseTransition.setOnFinished((e) -> {
                date.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, parseDate(newValue, textToTime) == null);
            });
            pauseTransition.playFromStart();
        }));
    }

    private static LocalDate parseDate(String text, DateTimeFormatter formatter) {
        try {
            return LocalDate.parse(text, formatter);
        } catch (DateTimeParseException e) {
            return null;
        }

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
