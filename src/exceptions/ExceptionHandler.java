package exceptions;

import javafx.scene.control.Alert;

public class ExceptionHandler {

    public void infoError(EventException eventException) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(eventException.getErrorCode().getValue());
        alert.show();
    }

    public void warningError(EventException eventException) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setContentText(eventException.getErrorCode().getValue());
        alert.show();
    }

    public void errorAlert(EventException eventException) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(eventException.getErrorCode().getValue());
        alert.show();
    }

}
