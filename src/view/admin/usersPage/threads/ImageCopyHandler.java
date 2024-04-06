package view.admin.usersPage.threads;
import exceptions.ErrorCode;
import exceptions.EventException;
import javafx.concurrent.Task;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
public class ImageCopyHandler extends Task<Boolean> {
    private final static Path uploadedImagesPath = Paths.get(System.getProperty("user.dir"), "uploadImages","userUploadedImages");
    private File sourceFile;
    public ImageCopyHandler(File sourceFile) {
        this.sourceFile = sourceFile;
    }

    @Override
    protected Boolean call() throws Exception {
        System.out.println("executed");
        return copyFileToResources(sourceFile);
    }
    private boolean copyFileToResources(File sourceFile) throws EventException {
        boolean insertedFile= false;
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
            insertedFile =true;
        } catch (IOException e) {
            System.out.println("Failed to copy to: " + uploadedImagesPath);
            throw new EventException(e.getMessage(), e, ErrorCode.INVALID_INPUT);
        }
        return insertedFile;
    }
//
//    public boolean fileExists(File file){
//        Path uploadedImagesPath = Paths.get(System.getProperty("user.dir"), "uploadImages","userUploadedImages");
//        Path targetFilePath = uploadedImagesPath.resolve(sourceFile.getName());
//        return Files.exists(targetFilePath);
//    }
}
