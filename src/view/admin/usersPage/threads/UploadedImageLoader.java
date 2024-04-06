package view.admin.usersPage.threads;

import exceptions.ErrorCode;
import exceptions.EventException;
import javafx.concurrent.Task;
import javafx.scene.image.Image;

public class UploadedImageLoader extends Task<Image> {
    private String imageLocation;

    public UploadedImageLoader(String imageLocation) {
        this.imageLocation = imageLocation;
    }

    @Override
    protected Image call() throws Exception {
        if (isCancelled()) {
            return null;
        }
        Image image = null;
        try {
            image = new Image(imageLocation);
        } catch (NullPointerException e) {
            throw new EventException(e.getMessage(), e, ErrorCode.INVALID_INPUT);
        }
        return image;
    }
}
