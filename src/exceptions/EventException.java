package exceptions;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


public class EventException extends Exception {
    private ErrorCode errorCode;

    public EventException(String message, Throwable cause, ErrorCode code) {
        super(message, cause);
        this.errorCode = code;
    }
    public EventException (String message){
        super(message);
    }
    public EventException (ErrorCode code){
        super();
        this.errorCode=code;
    }
    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
