package exceptions;

import javafx.scene.control.Alert;

public class ExceptionHandler {

    public static void infoError(EventException eventException) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(eventException.getErrorCode().getValue()+"\\n"+eventException.getMessage());
        alert.show();
    }

    public static void warningError(EventException eventException) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setContentText(eventException.getErrorCode().getValue()+"\\n"+eventException.getMessage());
        alert.show();
    }

    public static void errorAlert(EventException eventException) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(eventException.getErrorCode().getValue()+"\\n"+eventException.getMessage());
        alert.show();
    }

    public static void errorAlertMessage(String errorMessage){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(errorMessage);
        alert.show();
    }
}
