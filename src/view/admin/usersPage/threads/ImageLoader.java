package view.admin.usersPage.threads;

import exceptions.ErrorCode;
import exceptions.EventException;
import javafx.concurrent.Task;
import javafx.scene.image.Image;

import java.io.InputStream;

public class ImageLoader extends Task<Image> {
    private String imageLocation;
    private static final String defaultImage = "/usersImages/default.png";

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
        InputStream resourceStream = getClass().getResourceAsStream(getFallBackImage ? defaultImage : imageLocation);
        if (resourceStream == null) {
            resourceStream = getClass().getResourceAsStream(defaultImage);
            if (resourceStream == null) {
                throw new EventException(exceptionNow().getMessage(),exceptionNow(), ErrorCode.INVALID_INPUT);
            }
        }
        return new Image(resourceStream);
    }

    public void getDefaultImage() {
        this.getFallBackImage = true;
    }
}
