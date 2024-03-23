package exceptions;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


public class EventException extends Exception {
    private ErrorCode errorCode;
    private static final Logger logger = Logger.getLogger("EventException");

    static {
        FileHandler fileHandler = null;
        try {
             fileHandler =  new FileHandler("ExceptionsLog.log",true);
             logger.addHandler(fileHandler);
             SimpleFormatter simpleFormatter = new SimpleFormatter();
             fileHandler.setFormatter(simpleFormatter);
        } catch (IOException e) {
            System.out.println("Could not log");
        }
    }

    public EventException(String message, Throwable cause, ErrorCode code) {
        super(message, cause);
        this.errorCode = code;
        logException(message, cause, code,Level.FINEST);
    }
    public EventException (String message){
        super(message);
        logException(message,null,null,Level.FINEST);
    }
    public EventException (ErrorCode code){
        super();
        this.errorCode=code;
        logException(null,null,code,Level.FINEST);
    }

    private void logException(String message, Throwable cause, ErrorCode code, Level level) {
        if (cause != null) {
            logger.log(level, "Exception Occurred: " + message + " | ErrorCode: " + code, cause);
        } else {
            logger.log(level, "Exception Occurred: " + message + " | ErrorCode: " + code);
        }
    }
    public ErrorCode getErrorCode() {
        return errorCode;
    }


}
