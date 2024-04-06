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


    public Path copyFileToResources(File sourceFile) throws EventException {
        if (!Files.exists(uploadedImagesPath)) {
            try {
                Files.createDirectories(uploadedImagesPath);
            } catch (IOException e) {
                throw new EventException("Failed to create directory " + e.getMessage(), e, ErrorCode.COPY_FAILED);
            }
        }
        Path targetFilePath = uploadedImagesPath.resolve(sourceFile.getName());
        if (Files.exists(targetFilePath)) {
            throw new EventException("File already exists: " + targetFilePath, null, ErrorCode.FILE_ALREADY_EXISTS);
        }

        try {
            Files.copy(sourceFile.toPath(), targetFilePath, StandardCopyOption.COPY_ATTRIBUTES);
        } catch (IOException e) {
            throw new EventException("Failed to copy the file " + e.getMessage(), e, ErrorCode.COPY_FAILED);
        }
        System.out.println("File copied to: " + targetFilePath);
        return targetFilePath; // Return the path of the copied file
    }


}
