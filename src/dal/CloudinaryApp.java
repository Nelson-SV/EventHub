package dal;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import java.util.Map;

public class CloudinaryApp {


    private FileHandler fileHandler;
    private final static String CLOUD_NAME = "cloud_name";
    private final static String KEY = "api_key";
    private final static String SECRET = "api_secret";
    private Cloudinary cloudinary;

    public CloudinaryApp() {
        fileHandler = new FileHandler();
        initializeCloudinary();
    }

    private void initializeCloudinary() {
        Map<String, String> settings = fileHandler.cloudinarySettings();
        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                CLOUD_NAME, settings.get(CLOUD_NAME),
                KEY, settings.get(KEY),
                SECRET, settings.get(SECRET),
                "secure", true
        ));
    }

    public Cloudinary getCloudinary() {
        return cloudinary;
    }

    //    Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
//            "cloud_name", "your_cloud_name",
//            "api_key", "your_api_key",
//            "api_secret", "your_api_secret",
//            "secure", true));
}
