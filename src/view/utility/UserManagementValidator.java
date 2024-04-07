package view.utility;


import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.animation.PauseTransition;
import javafx.css.PseudoClass;
import javafx.util.Duration;

import java.awt.*;

public class UserManagementValidator {
    private static final PseudoClass ERROR_PSEUDO_CLASS = PseudoClass.getPseudoClass("error");
    private final static String validNamePattern = "^[A-Za-z]+(\\s[A-Za-z]+)*$";

    public static boolean isNameValid(MFXTextField name) {
        boolean isValid=!name.getText().isEmpty() && name.getText().matches(validNamePattern);
        name.pseudoClassStateChanged(ERROR_PSEUDO_CLASS,!isValid);
        return isValid;
    }

    private static boolean isNameValid(String name){
        return !name.isEmpty()&&name.matches(validNamePattern);
    }

    public static boolean isRoleValid(MFXComboBox<String> roles) {
        boolean notSelected = roles.getValue()==null;
        roles.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, notSelected);
        return !notSelected;
    }

    public static boolean isPasswordValid(MFXTextField password) {
        boolean isValid = !password.getText().isEmpty();
        password.pseudoClassStateChanged(ERROR_PSEUDO_CLASS,!isValid);
        return isValid;
    }


    public static void addNameValueListener(MFXTextField input) {
        PauseTransition pauseTransition = new PauseTransition(Duration.millis(300));
        input.textProperty().addListener(((observable, oldValue, newValue) -> {
            pauseTransition.setOnFinished((e) -> {
                input.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, !isNameValid(newValue));
            });
            pauseTransition.playFromStart();
        }));
    }

    public static void addRoleValueListener(MFXComboBox<String> comboBox) {
        PauseTransition pauseTransition = new PauseTransition(Duration.millis(300));
        comboBox.getSelectionModel().selectedIndexProperty().addListener(((observable, oldValue, newValue) -> {
            pauseTransition.setOnFinished((event) -> {
                boolean noSelection = newValue.intValue() == -1;
                comboBox.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, noSelection);
            });
            pauseTransition.playFromStart();
        }));
    }

    public static void addPasswordValidator(MFXPasswordField password) {
        PauseTransition pauseTransition = new PauseTransition(Duration.millis(300));
        password.textProperty().addListener((observable, oldValue, newValue) -> {
            pauseTransition.setOnFinished((event) -> {
                password.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, newValue.isEmpty());
            });
            pauseTransition.playFromStart();
        });
    }


}
