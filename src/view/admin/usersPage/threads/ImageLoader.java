package view.admin.usersPage.threads;

import exceptions.ErrorCode;
import exceptions.EventException;
import javafx.concurrent.Task;
import javafx.scene.image.Image;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
public class ImageLoader extends Task<Image> {
    private String imageLocation;
    private static final String defaultImage = "/usersImages/default.png";
    private static final Path toImage = Paths.get(System.getProperty("user.dir"), "uploadImages" ,"userUploadedImages");
    private boolean getFallBackImage;

    public ImageLoader(String imageLocation) {
        this.imageLocation = imageLocation;
    }
    public ImageLoader(){
    }
    @Override
    protected Image call() throws EventException {
        if (isCancelled()) {
            return null;
        }
        if(getFallBackImage){
            InputStream resourceStream = getClass().getResourceAsStream(defaultImage);
            if (resourceStream == null) {
                resourceStream = getClass().getResourceAsStream(defaultImage);
                if (resourceStream == null) {
                    throw new EventException(exceptionNow().getMessage(),exceptionNow(), ErrorCode.INVALID_INPUT);
                }
            }
            return new Image(resourceStream);
        }
        Path imagePath = toImage.resolve(imageLocation);
        return new Image(imagePath.toString());
    }

    public void getDefaultImage() {
        this.getFallBackImage = true;
    }
}
