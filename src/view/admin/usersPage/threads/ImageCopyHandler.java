package view.admin.usersPage.threads;
import exceptions.ErrorCode;
import exceptions.EventException;
import javafx.concurrent.Task;
import java.io.File;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.*;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
public class ImageCopyHandler extends Task<String> {
    private final static Path uploadedImagesPath = Paths.get(System.getProperty("user.dir"), "uploadImages","userUploadedImages");
    private File sourceFile;
    public ImageCopyHandler(File sourceFile) {
        this.sourceFile = sourceFile;
    }

    @Override
    protected String call() throws Exception {
        System.out.println("executed");
        return copyFileToResources(sourceFile);
    }
    private String copyFileToResources(File sourceFile) throws EventException {
        String insertedFilePath= null;
        try {
            if (!Files.exists(uploadedImagesPath)) {
                Files.createDirectories(uploadedImagesPath);
            }
            Path targetFilePath = uploadedImagesPath.resolve(sourceFile.getName());
            if (Files.exists(targetFilePath)) {
                throw new EventException("File already exists: " + targetFilePath, null, ErrorCode.FILE_ALREADY_EXISTS);
            }
            Files.copy(sourceFile.toPath(), targetFilePath, StandardCopyOption.COPY_ATTRIBUTES);
            System.out.println("File copied to: " + targetFilePath);
            insertedFilePath =sourceFile.getName();
        } catch (IOException e) {
            System.out.println("Failed to copy to: " + uploadedImagesPath);
            throw new EventException(e.getMessage(), e, ErrorCode.INVALID_INPUT);
        }
        return insertedFilePath;
    }

    public boolean fileExists(File file){
        Path uploadedImagesPath = Paths.get(System.getProperty("user.dir"), "uploadImages","userUploadedImages");
        Path targetFilePath = uploadedImagesPath.resolve(sourceFile.getName());
        return Files.exists(targetFilePath);
    }
}
