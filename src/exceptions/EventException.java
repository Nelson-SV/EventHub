package exceptions;

import javax.swing.*;
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
             fileHandler.setLevel(Level.SEVERE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            assert fileHandler != null;
            fileHandler.close();
        }

    }

    public EventException(String message, Throwable cause, ErrorCode code, Level level) {
        super(message, cause);
        this.errorCode = code;
        logException(message, cause, code,level);
    }
    public EventException (String message,Level level){
        super(message);
        logException(message,null,null,level);
    }
    public EventException (ErrorCode code, Level level){
        super();
        this.errorCode=code;
        logException(null,null,code,level);
    }

    private void logException(String message, Throwable cause, ErrorCode code,Level level) {
        logger.log( level ,"Exception Occurred: " + message + " | ErrorCode: " + code + " | Cause: " + cause);
    }
    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
