package ticketsPrinting;
import be.Customer;
import be.Event;
import be.Ticket;
import be.TicketType;
import com.google.zxing.WriterException;
import exceptions.ErrorCode;
import exceptions.EventException;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
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
import java.util.concurrent.Semaphore;

public class TicketsSnapshot extends Task<List<WritableImage>>  {
    private Map<TicketType, List<TicketWithQrCode>> soldTickets;
    private Customer customer;
    private Event event;
    private  final List<WritableImage> createdTicketsImages;
    private CountDownLatch countDownLatch = new CountDownLatch(1);
    //private ExecutorService executorService;
    private Semaphore semaphore = new Semaphore(1);
    public TicketsSnapshot(Map<TicketType, List<TicketWithQrCode>> soldTickets, Customer customer, Event event) {
        this.customer = customer;
        this.createdTicketsImages = new ArrayList<>();
        this.event = event;
        setSoldTickets(soldTickets);
    }
    public synchronized void setSoldTickets(Map<TicketType, List<TicketWithQrCode>> soldTickets) {
        this.soldTickets = soldTickets;
    }

    public synchronized Map<TicketType, List<TicketWithQrCode>> getSoldTickets() {
        return this.soldTickets;
    }

    private  synchronized void createTicketImages(Map<TicketType,List<TicketWithQrCode>> soldTickets, Customer customer, Event event){
        Platform.runLater(()->{

            for(TicketWithQrCode ticketWithQrCode:soldTickets.get(TicketType.NORMAL)){
                try {
                    WritableImage image =takeSnapshotNormalTicket(event,ticketWithQrCode,customer);
                    createdTicketsImages.add(image);
                } catch (WriterException e) {
                    e.printStackTrace();
                    //
                }
            }
            countDownLatch.countDown();
        });
    }

    private WritableImage takeSnapshotNormalTicket(Event event, TicketWithQrCode ticketWithQrCode, Customer customer) throws WriterException {
        TicketComponentDescription ticketComponent = new TicketComponentDescription(event);
        int width = (int) ticketComponent.getBarCode().getFitWidth();
        int height = (int) ticketComponent.getBarCode().getFitHeight();
        ticketComponent.getBarCode().setImage(ticketWithQrCode.getQrcode());
        String colorName = ticketWithQrCode.getTicket().getColor();
        Color color = Color.web("#" + colorName.substring(2));
        BackgroundFill backgroundFill = new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY);
        Background background = new Background(backgroundFill);
        ticketComponent.getRoot().setBackground(background);
        ticketComponent.setCustomerEmail(customer.getEmail());
        ticketComponent.setCustomerName(customer.getName());
        ticketComponent.setEventName(event.getName());
        ticketComponent.setDateAndTime(event.getStartDate() + " " + event.getStartTime());
        ticketComponent.setLocation(event.getLocation());
        ticketComponent.setPrice(ticketWithQrCode.getTicket().getTicketPrice()+ "");
        ticketComponent.setType(ticketWithQrCode.getTicket().getTicketType());
        Scene scene = new Scene(ticketComponent);
        SnapshotParameters params = new SnapshotParameters();
        double scaleFactor = 2;
        params.setTransform(Transform.scale(scaleFactor, scaleFactor));
        return scene.getRoot().snapshot(params, null);
    }

    public synchronized void createTicketWritableImages() throws EventException {
        createTicketImages(getSoldTickets(),customer, event);
    }

    @Override
    protected List<WritableImage> call() throws Exception {
        createTicketWritableImages();
        countDownLatch.await();
        System.out.println(this.createdTicketsImages.size() +"snapshot insideeeeeee");
        return this.createdTicketsImages;
    }
}
