package exceptions;



import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class ExceptionLogger {
    private static ExceptionLogger instance;
    private Logger logger;

    private ExceptionLogger() {
        logger = Logger.getLogger("EventLog");
        try {
            FileHandler fh = new FileHandler("D:\\computer_science\\sco\\compolsory\\tiketSystem\\EventHub\\src\\exceptions\\ExceptionsLog.txt", true);
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
            logger.setLevel(Level.FINEST);
            logger.info("Logger initialized \n" + ".......................................");
        } catch (Exception e) {
            logger.log(Level.WARNING, "Exception in Logger Initialization: ", e);
        }
    }

    public static synchronized ExceptionLogger getInstance() {
        if (instance == null) {
            instance = new ExceptionLogger();
        }
        return instance;
    }

    public Logger getLogger() {
        return logger;
    }
}

