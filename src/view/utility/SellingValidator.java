package view.utility;

import be.Ticket;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.collections.ListChangeListener;
import javafx.css.PseudoClass;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;

import java.math.BigDecimal;

public class SellingValidator {
    private static final PseudoClass ERROR_PSEUDO_CLASS = PseudoClass.getPseudoClass("error");
    private static final String VALID_EMAIL_FORMAT = "Please enter your email address (e.g., john@example.com).";
    private static final String VALID_AMOUNT_FORMAT = "Please select a number (e.g., 10)";

    public static void emailToolTip(TextField email) {
        Tooltip tooltip = new Tooltip(VALID_EMAIL_FORMAT);
        email.setTooltip(tooltip);
    }

    public static boolean isSellingValid(TextField name, TextField lastName, TextField email, ListView<Ticket> allSelectedTickets) {
        boolean isValid = true;

        if (name.getText().isEmpty()) {
            name.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, true);
            isValid = false;
        } else {
            name.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, false);
        }

        if (lastName.getText().isEmpty()) {
            lastName.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, true);
            isValid = false;
        } else {
            lastName.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, false);
        }

        if (email.getText().isEmpty()) {
            email.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, true);
            isValid = false;
        } else {
            email.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, false);
        }

        if (allSelectedTickets.getItems().isEmpty()) {
            allSelectedTickets.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, true);
            isValid = false;
        } else {
            allSelectedTickets.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, false);
        }
        return isValid;
    }

    public static void addSellingListeners(TextField eventTicketsAmount, TextField specialTicketsAmount) {

        // Add event handler for eventTicketsAmount text field
        eventTicketsAmount.textProperty().addListener((observable, oldValue, newValue) -> {
            // Clear the error pseudo-class state when user starts typing
            eventTicketsAmount.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, false);

            // Check if the new value is not a number
            if (!newValue.matches("\\d*")) {
                // Set error styling if the new value contains non-digit characters
                Tooltip amount = new Tooltip(VALID_AMOUNT_FORMAT);
                eventTicketsAmount.setTooltip(amount);
                eventTicketsAmount.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, true);

            }
            else {
                /*Tooltip amount = new Tooltip(VALID_AMOUNT_FORMAT);
                eventTicketsAmount.setTooltip(amount);*/
            }


        });


        specialTicketsAmount.textProperty().addListener((observable, oldValue, newValue) -> {
            // Clear the error pseudo-class state when user starts typing
            specialTicketsAmount.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, false);

            // Check if the new value is not a number
            if (!newValue.matches("\\d*")) {
                // Set error styling if the new value contains non-digit characters
                Tooltip amount = new Tooltip(VALID_AMOUNT_FORMAT);
                specialTicketsAmount.setTooltip(amount);
                specialTicketsAmount.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, true);

            }
            else {
               /* Tooltip amount = new Tooltip(VALID_AMOUNT_FORMAT);
                eventTicketsAmount.setTooltip(amount);*/
            }
        });


    }
    public static void addSellingListeners2(TextField name, TextField lastName, TextField email, ListView<Ticket> allSelectedTickets, MFXComboBox specialTickets, MFXComboBox allEventTickets){
        name.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                name.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, false);
            }
        });

        lastName.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                lastName.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, false);
            }
        });

        email.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                email.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, false);
            }
        });
        allSelectedTickets.getItems().addListener((ListChangeListener<Ticket>) change -> {
            if (!allSelectedTickets.getItems().isEmpty()) {
                allSelectedTickets.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, false);
            }
        });
        specialTickets.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            // Check if a new item is selected
            if (newValue != null) {
                // Clear the error pseudo-class state of the specialTickets ComboBox
                specialTickets.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, false);
            }
        });
        allEventTickets.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            // Check if a new item is selected
            if (newValue != null) {
                // Clear the error pseudo-class state of the specialTickets ComboBox
                allEventTickets.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, false);
            }
        });
    }
}
