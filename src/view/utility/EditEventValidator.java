package view.utility;

import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import javafx.animation.PauseTransition;
import javafx.css.PseudoClass;
import javafx.scene.Node;
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
    private static final DateTimeFormatter textToDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * validate the user inputs for the edit operation
     */
    public static boolean isEventValid(TextField name, MFXDatePicker startDate, MFXComboBox<LocalTime> startTime, MFXDatePicker endDate, MFXComboBox<LocalTime> endTIme, TextArea eventLocation) {
        String startText = startDate.getText();
        String startTimeText = startTime.getText();
        boolean isValid = true;
        boolean isValidStartTime = startTimeText.matches(regex);
        LocalDate validStartDate = parseDate(startText, textToDate);

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




    //Edit event validator related
    /**
     * start time valid
     */
    private static boolean isTimeValid(String time) {
        return time.matches(regex);
    }

    /**
     * ccheck if is date valid
     */
    private static LocalDate parseDateOrNull(String date) {
        return parseDate(date, textToDate);
    }

    private static boolean isNameValid(String name) {
        return !name.isEmpty();
    }


    public static boolean isEventValidTimeAsString(TextField name, MFXDatePicker startDate, MFXComboBox<String> startTime, MFXDatePicker endDate, MFXComboBox<String> endTIme, TextArea eventLocation) {
       boolean isValid = true;

        //check if start time is valid
        if (!isTimeValid(startTime.getText())) {
            startTime.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, true);
            isValid = false;
            System.out.println("check start Time valid: " + isValid);
        } else {
            startTime.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, false);
        }


        //check if start date is valid
        if (parseDateOrNull(startDate.getText()) == null) {
            startDate.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, true);
            isValid = false;
            System.out.println("check start date valid" + isValid);
        } else {
            startDate.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, false);
        }

        //check endDate valid inputText
        if (!endDate.getText().isEmpty()) {
            if (parseDateOrNull(endDate.getText()) == null) {
                endDate.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, true);
                isValid = false;
                System.out.println("check end time valid" + isValid);
            } else {
                endDate.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, false);
            }
        }

        //checkEndDateValueInputText
        if (!endTIme.getText().isEmpty()) {
            if (!isTimeValid(endTIme.getText())) {
                endTIme.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, true);
                isValid = false;
                System.out.println(" check end time " + isValid);
            } else {
                endTIme.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, false);
            }
        }

        //check if name is valid
        if (!isNameValid(name.getText())) {
            name.pseudoClassStateChanged(ERROR_PSEUDO_CLASS,true);
            isValid=false;
            System.out.println("checked name Validity " + isValid);
        }else{
            name.pseudoClassStateChanged(ERROR_PSEUDO_CLASS,false);
        }

        if (eventLocation.getText().isEmpty()) {
            eventLocation.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, true);
            isValid = false;
            System.out.println("check location " + isValid);
        } else {
            eventLocation.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, false);
        }
        return isValid;
    }


    /**add event name value listener
     * @param eventName the event name node  */
    public static void addEventNameValueListener(TextField eventName){
        eventName.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() == 1) {
                eventName.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, false);
            } else if (newValue.isEmpty()) {
                eventName.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, true);
            }
        });
    }

    /**add event location  value listener
     * @param eventLocation the event location node  */
    public static void addEventLocationValueListener(TextArea eventLocation) {
        eventLocation.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() == 1) {
                eventLocation.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, false);
            } else if (newValue.isEmpty()) {
                eventLocation.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, true);
            }
        });
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

    /**add the tooltip for the Time ComboBox that is using String as type */
    public static void addTimeStringToolTip(MFXComboBox<String> time) {
        Tooltip errorTooltip = new Tooltip(VALID_TIME_FORMAT);
        time.setTooltip(errorTooltip);
    }

    /**
     * check if the start  time  text is valid
     */
    public static void addTimeTextEmptyChecker(MFXComboBox<LocalTime> time) {
        PauseTransition pauseTransition = new PauseTransition(Duration.millis(300));
        time.textProperty().addListener(((observable, oldValue, newValue) -> {
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
        PauseTransition pauseTransition = new PauseTransition(Duration.millis(300));
        date.textProperty().addListener(((observable, oldValue, newValue) -> {
            pauseTransition.setOnFinished((e) -> {
                date.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, parseDate(newValue, textToDate) == null);
            });
            pauseTransition.playFromStart();
        }));
    }

    /**check if the end date is valid, does not check if the text is empty
     * @param endDate event end date node  */
    public static void addEndDateTextValidityChecker(MFXDatePicker endDate){
        PauseTransition pauseTransition = new PauseTransition(Duration.millis(300));
        endDate.textProperty().addListener(((observable, oldValue, newValue) -> {
            pauseTransition.setOnFinished((e) -> {
                if(!newValue.isEmpty()){
                    endDate.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, parseDate(newValue, textToDate) == null);
                }else{
                    endDate.pseudoClassStateChanged(ERROR_PSEUDO_CLASS,false);
                }
            });
            pauseTransition.playFromStart();
        }));
    }

//    /**
//     * checks if the time inserted in the text box is valid
//     */
//    public static void addStartTimeValidityChecker(MFXComboBox<LocalTime> time) {
//        PauseTransition pauseTransition = new PauseTransition(Duration.millis(300));
//        time.textProperty().addListener((observable, oldValue, newValue) -> {
//            pauseTransition.setOnFinished(e -> {
//                boolean isValid =newValue.matches("^([01]?[0-9]|2[0-3]):[0-5][0-9]$");
//                time.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, !isValid);
//            });
//            pauseTransition.playFromStart();
//        });
//    }

    /**checks if the end time is valid, do not check for empty value
     * @param time the end time value node  */
    public static void addEndTimeValidityChecker(MFXComboBox<LocalTime> time) {
        PauseTransition pauseTransition = new PauseTransition(Duration.millis(300));
        time.textProperty().addListener((observable, oldValue, newValue) -> {
            pauseTransition.setOnFinished(e -> {
                boolean isValid = newValue.isEmpty()||newValue.matches("^([01]?[0-9]|2[0-3]):[0-5][0-9]$");
                time.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, !isValid);
            });
            pauseTransition.playFromStart();
        });
    }


    /** cheks if the start time is valid , checks fro empty value
     *@param time start time value node */
    public static void addStartTimeValidityCheckerString(MFXComboBox<String> time) {
        PauseTransition pauseTransition = new PauseTransition(Duration.millis(300));
        time.textProperty().addListener((observable, oldValue, newValue) -> {
            pauseTransition.setOnFinished(e -> {
                boolean isValid =  newValue.matches("^([01]?[0-9]|2[0-3]):[0-5][0-9]$");
                time.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, !isValid);
            });
            pauseTransition.playFromStart();
        });
    }

    public static void addEndTimeValidityCheckerString(MFXComboBox<String> time) {
        PauseTransition pauseTransition = new PauseTransition(Duration.millis(300));
        time.textProperty().addListener((observable, oldValue, newValue) -> {
            pauseTransition.setOnFinished(e -> {
                boolean isValid = newValue.isEmpty()||  newValue.matches("^([01]?[0-9]|2[0-3]):[0-5][0-9]$");
                time.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, !isValid);
            });
            pauseTransition.playFromStart();
        });
    }

//    /**
//     * listener that checks if the date inserted in the text is valid
//     */
//    public static void addStartDateValidityChecker(MFXDatePicker date) {
//        PauseTransition pauseTransition = new PauseTransition(Duration.millis(300));
//        date.textProperty().addListener((observable, oldValue, newValue) -> {
//            pauseTransition.setOnFinished(e -> {
//                boolean isValid = !newValue.isEmpty() && parseDate(newValue, textToDate) != null;
//                date.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, !isValid);
//            });
//            pauseTransition.playFromStart();
//        });
//    }


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
                    return textToDate.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, textToDate);
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
                LocalDate.parse(endDate.getText(), textToDate);
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

    public static void changePseudoClassValue(Node node){
        node.pseudoClassStateChanged(ERROR_PSEUDO_CLASS,true);
    }
}
