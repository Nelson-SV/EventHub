package ticketsPrinting;
import exceptions.ErrorCode;
import exceptions.EventException;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class TicketImageSaver extends Task<Void> {
    private List<WritableImage> createdTicketsImages;

    public TicketImageSaver(List<WritableImage> writableImageList) {
        this.createdTicketsImages = writableImageList;
    }

    @Override
    protected Void call() throws Exception {
        saveImagesLocal();
        return null;
    }
    private void saveImagesLocal() throws EventException {
        for (WritableImage image : createdTicketsImages) {
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
            File dir = new File("./uploadImages/userUploadedImages");
            if (!dir.exists()) {
                dir.mkdirs();
            }

            System.out.println(createdTicketsImages.size() +" from created images after snapshot");
//                    String ticketType = soldTickets.get(TicketType.NORMAL).get(index).getTicketType();
//                    String uuid= soldTickets.get(TicketType.NORMAL).get(index).getUUID();
//                    String fileName = uuid+customer.getName() + "_" + event.getName() + "_" + ticketType + ".png";
//                    System.out.println(fileName);


// ...

            String fileName = LocalDateTime.now().toString().replace(":", "-") + "_" + UUID.randomUUID().toString();
            File outputFile = new File(dir, fileName+".png");
            try {
                ImageIO.write(bufferedImage, "png", outputFile);
            } catch (IOException e) {
                e.printStackTrace();
                throw new EventException(e.getMessage(), e, ErrorCode.FAILED_TO_SAVE_IMAGES);
            }
        }
    }
}
