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

    public static void erorrAlertMessage(String errorMessage){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(errorMessage);
        alert.show();
    }

    /*

    public static void infoError(TicketException ticketException) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(ticketException.getErrorCode().getValue()+"\\n"+ticketException.getMessage());
        alert.show();
    }

    public static void warningError(TicketException ticketException) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setContentText(ticketException.getErrorCode().getValue()+"\\n"+ticketException.getMessage());
        alert.show();
    }

    public static void errorAlert(TicketException ticketException) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(ticketException.getErrorCode().getValue()+"\\n"+ticketException.getMessage());
        alert.show();
    }

     */
}
