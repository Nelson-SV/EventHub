package view.admin.usersPage.threads;

import exceptions.ErrorCode;
import exceptions.EventException;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.image.Image;

import java.io.InputStream;

public class ImageLoader {
    private String imageLocation;
    private static final String defaultImageLocation = "/usersImages/default.png";
    private static final String defaultImageName = "default.png";
    private Service<Image> serviceLoader;

    public ImageLoader(String imageLocation) {
        this.imageLocation = imageLocation;
        initializeServiceLoader();
    }

    public ImageLoader() {
        initializeServiceLoader();
    }


    private void initializeServiceLoader() {
        serviceLoader = new Service<Image>() {
            @Override
            protected Task<Image> createTask() {
                return new Task<Image>() {
                    @Override
                    protected Image call() throws Exception {
                        if (isCancelled()) {
                            return null;
                        }
                        if (imageLocation.equals(defaultImageName)) {
                            return loadImageFromResources(defaultImageLocation);
                        } else {
                            return new Image(imageLocation);
                        }
                    }

                    private Image loadImageFromResources(String resourcePath) throws EventException {
                        InputStream resourceStream = getClass().getResourceAsStream(resourcePath);
                        if (resourceStream == null) {
                            throw new EventException("Resource not found: " + resourcePath, null, ErrorCode.INVALID_INPUT);
                        }
                        return new Image(resourceStream);
                    }
                };
            }
        };
    }


    public Service<Image> getServiceLoader() {
        return serviceLoader;
    }

    public void setImageLocation(String imageLocation) {
        this.imageLocation = imageLocation;
    }
}
