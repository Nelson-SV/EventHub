package dal;
import exceptions.ErrorCode;
import exceptions.EventException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


public class FileHandler {
     private final  String dataBaseLoginPath = "src/resources/DbLogin.txt";
    public String []readDbLogin () throws EventException {
        try {
            String dbCredentials = Files.readString(Path.of(dataBaseLoginPath));
            return dbCredentials.split(",");
        } catch (IOException e) {
            throw new EventException(e.getMessage(),e.getCause(), ErrorCode.OPERATION_DB_FAILED);
        }
    }
}
