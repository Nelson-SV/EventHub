package view.utility;

import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.css.PseudoClass;
import javafx.scene.control.Tooltip;
import exceptions.ExceptionHandler;

import java.math.BigDecimal;

public class TicketValidator {
    private static final PseudoClass ERROR_PSEUDO_CLASS = PseudoClass.getPseudoClass("error");
    private static final String VALID_TYPE_FORMAT = "Please enter the type of Ticket (e.g., General Admission).";
    private static final String VALID_PRICE_FORMAT = "Please enter the price of the Ticket (e.g., 300 or 400.50).";
    private static final String VALID_QUANTITY_FORMAT = "Please enter the quantity to generate (e.g., 1000).";

    /**
     * validate the user inputs for the add operation
     */
    public static boolean isTicketValid(MFXTextField typeTF, MFXTextField priceTF, MFXTextField quantityTF) {
        boolean isValid = true;

        if (typeTF.getText().isEmpty()) {
            typeTF.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, true);
            isValid = false;
            ExceptionHandler.errorAlertMessage("This field cannot be empty.");
        } else {
            typeTF.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, false);
        }

        if (priceTF.getText().isEmpty()) {
            priceTF.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, true);
            isValid = false;
            ExceptionHandler.errorAlertMessage("Price should be equal or greater than 0.");
        } else {
            try {
                BigDecimal price = new BigDecimal(priceTF.getText());
                if (price.compareTo(BigDecimal.ZERO) <= 0) {
                    priceTF.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, true);
                    isValid = false;
                    ExceptionHandler.errorAlertMessage("Price should be equal or greater than 0.");
                } else {
                    priceTF.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, false);
                }
            } catch (NumberFormatException e) {
                priceTF.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, true);
                isValid = false;
                ExceptionHandler.errorAlertMessage("Price should be equal or greater than 0.");
            }
        }

        if (quantityTF.getText().isEmpty()) {
            quantityTF.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, true);
            isValid = false;
            ExceptionHandler.errorAlertMessage("Quantity should be equal or greater than 0.");
        } else {
            try {
                int quantity = Integer.parseInt(quantityTF.getText());
                if (quantity <= 0) {
                    quantityTF.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, true);
                    isValid = false;
                    ExceptionHandler.errorAlertMessage("Quantity should be equal or greater than 0.");
                } else {
                    quantityTF.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, false);
                }
            } catch (NumberFormatException e) {
                quantityTF.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, true);
                isValid = false;
                ExceptionHandler.errorAlertMessage("Quantity should be equal or greater than 0.");
            }
        }

        return isValid;
    }

    /**
     * listeners for the inputs on textfields
     */
    public static void addTicketListeners(MFXTextField typeTF, MFXTextField priceTF, MFXTextField quantityTF) {
        typeTF.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                typeTF.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, true);
            } else
                typeTF.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, false);
        });

        priceTF.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                if (newValue.isEmpty() || new BigDecimal(newValue).compareTo(BigDecimal.ZERO) <= 0) {
                    priceTF.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, true);
                } else {
                    priceTF.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, false);
                }
            } catch (NumberFormatException | NullPointerException ex) {
                // Handle invalid input (not a number)
                priceTF.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, true);
            }
        });

        quantityTF.textProperty().addListener(((observable, oldValue, newValue) -> {
            try {
                if (newValue.isEmpty() || Integer.parseInt(newValue) <= 0) {
                    quantityTF.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, true);
                } else {
                    quantityTF.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, false);
                }
            } catch (NumberFormatException | NullPointerException ex) {
                // Handle invalid input (not a number)
                quantityTF.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, true);
            }
        }));
    }

    public static void addTypeToolTip(MFXTextField typeTF) {
        Tooltip errorTooltip = new Tooltip(VALID_TYPE_FORMAT);
        typeTF.setTooltip(errorTooltip);
    }

    public static void addPriceToolTip(MFXTextField priceTF) {
        Tooltip tooltip = new Tooltip(VALID_PRICE_FORMAT);
        priceTF.setTooltip(tooltip);
    }

    public static void addQuantityToolTip(MFXTextField quantityTF) {
        Tooltip tooltip = new Tooltip(VALID_QUANTITY_FORMAT);
        quantityTF.setTooltip(tooltip);
    }
}
