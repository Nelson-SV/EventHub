package view.utility;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import javafx.animation.PauseTransition;
import javafx.css.PseudoClass;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.util.Duration;
import javafx.util.StringConverter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class EditEventValidator {
    private static final PseudoClass ERROR_PSEUDO_CLASS = PseudoClass.getPseudoClass("error");
    private static final String VALID_TIME_FORMAT = "Please enter the time in the 24-hour format: HH:mm.";
    private static final String VALID_DATE_FORMAT = "Please enter the date in this format 'yyyy-MM-dd' (e.g.,2024-03-22).";
    private static final String regex = "^([01]?[0-9]|2[0-3]):[0-5][0-9]$";
    private static final DateTimeFormatter textToTime = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * validate the user inputs for the edit operation
     */
    public static boolean isEventValid(TextField name, MFXDatePicker startDate, MFXComboBox<LocalTime> startTime, MFXDatePicker endDate, MFXComboBox<LocalTime> endTIme, TextArea eventLocation) {
        String startText = startDate.getText();
        String startTimeText = startTime.getText();
        boolean isValid = true;
        boolean isValidStartTime = startTimeText.matches(regex);
        LocalDate validStartDate = parseDate(startText, textToTime);

        if (!isValidStartTime) {
            return false;
        }

        if (validStartDate == null) {
            return false;
        }

        if (!endDate.getText().isEmpty() || !endTIme.getText().isEmpty()) {
            if (!checkEndValid(endDate, endTIme)) {
                return false;
            }
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

    /**
     * listeners for the empty values
     */
    public static void addEventListeners(TextField eventName, MFXDatePicker startDate, MFXComboBox<LocalTime> startTime, MFXDatePicker endDate, MFXComboBox<LocalTime> endTime, TextArea eventLocation) {
        eventName.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() == 1) {
                eventName.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, false);
            } else if (newValue.isEmpty()) {
                eventName.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, true);
            }
        });

        startDate.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                startDate.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, false);
            } else {
                startDate.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, true);
            }
        });

        startDate.textProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue.length() == 1) {
                startDate.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, false);
            } else if (newValue.isEmpty()) {
                startDate.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, true);
            }
        }));


        startTime.textProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue.length() == 1) {
                startTime.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, false);
            } else if (newValue.isEmpty()) {
                startTime.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, true);
            }
        }));

        startTime.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                startTime.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, false);
            } else {
                startTime.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, true);
            }
        });

        eventLocation.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() == 1) {
                eventLocation.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, false);
            } else if (newValue.isEmpty()) {
                eventLocation.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, true);
            }
        });

    }

    public static void addTimeToolTip(MFXComboBox<LocalTime> time) {
        Tooltip errorTooltip = new Tooltip(VALID_TIME_FORMAT);
        time.setTooltip(errorTooltip);
    }

    public static void addDateToolTip(MFXDatePicker date) {
        Tooltip tooltip = new Tooltip(VALID_DATE_FORMAT);
        date.setTooltip(tooltip);
    }

    /**
     * check if the date text is valid
     */
    public static void addTimeTextEmptyChecker(MFXComboBox<LocalTime> time) {

        time.textProperty().addListener(((observable, oldValue, newValue) -> {
            PauseTransition pauseTransition = new PauseTransition(Duration.millis(100));
            pauseTransition.setOnFinished((e) -> {
                time.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, !newValue.matches("^([01]?[0-9]|2[0-3]):[0-5][0-9]$"));
            });
            pauseTransition.playFromStart();
        }));
    }

    /**
     * check if the start date text is valid
     */
    public static void addDateTextEmptyChecker(MFXDatePicker date) {
        date.textProperty().addListener(((observable, oldValue, newValue) -> {
            PauseTransition pauseTransition = new PauseTransition(Duration.millis(100));
            pauseTransition.setOnFinished((e) -> {
                date.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, parseDate(newValue, textToTime) == null);
            });
            pauseTransition.playFromStart();
        }));
    }

    /**
     * checks if the time inserted in the text box is valid
     */
    public static void addTimeValidityChecker(MFXComboBox<LocalTime> time) {
        time.textProperty().addListener(((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                PauseTransition pauseTransition = new PauseTransition(Duration.millis(100));
                pauseTransition.setOnFinished((e) -> {



                  time.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, !newValue.matches("^([01]?[0-9]|2[0-3]):[0-5][0-9]$"));

                    System.out.println(newValue.length());
                  if(oldValue.contains("24")||newValue.length()>5){
                      time.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, true);
                  }else{
                      time.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, !newValue.matches("^([01]?[0-9]|2[0-3]):[0-5][0-9]$"));
                  }

                });
                pauseTransition.playFromStart();
            } else {
                PauseTransition pauseTransition = new PauseTransition(Duration.millis(100));
                pauseTransition.setOnFinished((e) -> {
                    time.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, false);
                });
                pauseTransition.playFromStart();
            }
        }));
    }

    /**
     * listener that checks if the date inserted in the text is valid
     */
    public static void addDateValidityChecker(MFXDatePicker date) {
        date.textProperty().addListener(((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                PauseTransition pauseTransition = new PauseTransition(Duration.millis(300));
                pauseTransition.setOnFinished((e) -> {
                    date.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, parseDate(newValue, textToTime) == null);
                });
                pauseTransition.playFromStart();
            } else {
                PauseTransition pauseTransition = new PauseTransition(Duration.millis(300));
                pauseTransition.setOnFinished((e) -> {
                    date.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, false);
                });
                pauseTransition.playFromStart();
            }
        }));
    }


    /**
     * parse a string to a LocalDate used to check if a date is valid or not
     */
    private static LocalDate parseDate(String text, DateTimeFormatter formatter) {
        try {
            return LocalDate.parse(text, formatter);
        } catch (DateTimeParseException e) {
            return null;
        }
    }


    /**
     * changes the MFXDatePicker format display
     */
    public static void initializeDateFormat(MFXDatePicker date) {
        date.converterSupplierProperty().set(() -> new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return textToTime.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, textToTime);
                } else {
                    return null;
                }
            }
        });
    }


    /**
     * Checks if the end date and end time are in the valid format
     */
    private static boolean checkEndValid(MFXDatePicker endDate, MFXComboBox<LocalTime> endTime) {
        if (endDate.getText() != null && !endDate.getText().isEmpty()) {
            try {
                LocalDate.parse(endDate.getText(), textToTime);
            } catch (DateTimeParseException e) {
                endDate.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, true);
                return false;
            }
        }

        if (endTime.getText() != null && !endTime.getText().isEmpty()) {
            if (!endTime.getText().matches(regex)) {
                endTime.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, true);
                return false;
            }
        }
        return true;
    }
}
