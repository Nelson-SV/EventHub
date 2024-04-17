package dal;

import exceptions.ErrorCode;
import exceptions.EventException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FileHandler {
    private final static Path uploadedImagesPath = Paths.get(System.getProperty("user.dir"), "uploadImages", "userUploadedImages");
    private final String dataBaseLoginPath = "src/resources/DbLogin.txt";
    private final String settingsPath = "src/resources/Cloudinary.txt";

    public String[] readDbLogin() throws EventException {
        try {
            String dbCredentials = Files.readString(Path.of(dataBaseLoginPath));
            return dbCredentials.split(",");
        } catch (IOException e) {
            throw new EventException(e.getMessage(), e.getCause(), ErrorCode.OPERATION_DB_FAILED);
        }
    }

    public Map<String, String> cloudinarySettings() {
        Map<String, String> settings = new HashMap<>();

        try {
            List<String> settingsVal = Files.readAllLines(Path.of(settingsPath));
            for (String str : settingsVal) {
                if (!str.isEmpty()) {
                    String[] split = str.split(",");
                    settings.put(split[0], split[1]);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return settings;
    }

}
