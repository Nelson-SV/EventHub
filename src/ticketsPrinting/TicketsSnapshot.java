package ticketsPrinting;

import be.Customer;
import be.Event;
import be.Ticket;
import be.TicketType;
import com.google.zxing.WriterException;
import exceptions.ErrorCode;
import exceptions.EventException;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.transform.Transform;
import view.components.regularTickets.ticketDesign.TicketComponentDescription;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TicketsSnapshot {
    private Map<TicketType, List<Ticket>> soldTickets;
    private Customer customer;
    private Event event;
    private final List<WritableImage> createdTicketsImages;
    private CountDownLatch countDownLatch = new CountDownLatch(1);
    private ExecutorService executorService;

    public TicketsSnapshot(Map<TicketType, List<Ticket>> soldTickets, Customer customer, Event event) {
        this.soldTickets = soldTickets;
        this.customer = customer;
        this.createdTicketsImages = new ArrayList<>();
    }

    private void createdTicketImages(Map<TicketType, List<Ticket>> soldTickets, Customer customer, Event event) throws EventException {

        Platform.runLater(() -> {
            System.out.println("executed");
            for (Ticket ticket : soldTickets.get(TicketType.NORMAL)) {
                try {
                    createdTicketsImages.add(takeSnapshotNormalTicket(event, ticket, customer));
                } catch (WriterException e) {
                    e.printStackTrace();
                }
            }
            countDownLatch.countDown();
        });
        executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            try {
                countDownLatch.await();
                int index = 0;
                for (WritableImage image : createdTicketsImages) {
                    // Convert the WritableImage to a BufferedImage
                    BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
                    // Create the directory if it doesn't exist
                    File dir = new File("uploadImages/userUploadedImages");
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }

                    // Write the image to a file
                    String ticketType = soldTickets.get(TicketType.NORMAL).get(index).getTicketType();
                    String fileName = customer.getName() + event.getName() + ticketType + ".png";
                    File outputFile = new File(dir, fileName); // Replace "image.png" with your desired file name
                    try {
                        ImageIO.write(bufferedImage, "png", outputFile);
                        index++;
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw new EventException(e.getMessage(),e, ErrorCode.FAILED_TO_SAVE_IMAGES);
                    }
                }
            } catch (InterruptedException|EventException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        });
executorService.shutdown();

    }


    private WritableImage takeSnapshotNormalTicket(Event event, Ticket objectTicket, Customer customer) throws WriterException {
        TicketComponentDescription ticketComponent = new TicketComponentDescription(event);
        int width = (int) ticketComponent.getBarCode().getFitWidth();
        int height = (int) ticketComponent.getBarCode().getFitHeight();
        Image qrCode = QrCodeGenerator.generateQRCodeImage(objectTicket.getUUID(), width, height);
        ticketComponent.getBarCode().setImage(qrCode);
        String color = objectTicket.getColor();
        ticketComponent.getRoot().setStyle("-fx-background-color: " + color);
        ticketComponent.setCustomerEmail(customer.getEmail());
        ticketComponent.setCustomerName(customer.getName());
        ticketComponent.setEventName(event.getName());
        ticketComponent.setDateAndTime(event.getStartDate() + " " + event.getStartTime());
        ticketComponent.setLocation(event.getLocation());
        ticketComponent.setPrice(objectTicket.getTicketPrice() + "");
        ticketComponent.setType(objectTicket.getTicketType());
        Scene scene = new Scene(ticketComponent);
        SnapshotParameters params = new SnapshotParameters();
        double scaleFactor = 2;
        params.setTransform(Transform.scale(scaleFactor, scaleFactor));
        return scene.getRoot().snapshot(params, null);
    }

    public List<WritableImage> createTicketWritableImages() {
        return this.createdTicketsImages;
    }


}
