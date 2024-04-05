package view.utility;


import javafx.animation.PauseTransition;
import javafx.css.PseudoClass;
import javafx.scene.control.TextField;
import javafx.util.Duration;

public class UserManagementValidator {
    private static final PseudoClass ERROR_PSEUDO_CLASS = PseudoClass.getPseudoClass("error");
    private final static String validNamePattern = "^[A-Za-z]+$";
    public static boolean isFirstNameValid(String firstName){
        return !firstName.isEmpty() && firstName.matches(validNamePattern);
    }

    public static boolean isLastNameValid(String lastName){
        return lastName.isEmpty();
    }
    public static boolean isRoleValid(String role){
        return role.isEmpty();
    }
    public static boolean isPasswordValid(String password){
        return password.isEmpty();
    }


    public static void addFirstNameValueListener(TextField input){
        PauseTransition pauseTransition = new PauseTransition(Duration.millis(500));
       input.textProperty().addListener(((observable, oldValue, newValue) -> {
           pauseTransition.setOnFinished((e)->{
              String text= input.getText();
              input.pseudoClassStateChanged(ERROR_PSEUDO_CLASS,!isFirstNameValid(text));
           });
           pauseTransition.playFromStart();
       }));
    }









}
