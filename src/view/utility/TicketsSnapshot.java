package view.utility;

import be.Event;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Transform;
import view.components.regularTickets.ticketDesign.TicketComponentDescription;

import java.util.concurrent.CountDownLatch;

public class TicketsSnapshot extends Task<WritableImage> {

    private Event eventToPrint;
    private Image qrCode;

    public TicketsSnapshot(Event event, Image qrCode) {
        this.eventToPrint = event;
        this.qrCode = qrCode;
    }

    private WritableImage takeSnapshotNormalTicket(Event event, Image qrCode) throws InterruptedException {
        final WritableImage[] imageHolder = new WritableImage[1];
        final CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                TicketComponentDescription ticketTest = new TicketComponentDescription(event);
                ticketTest.setQrCode(qrCode);
                ticketTest.getRoot().setStyle("-fx-background-color: #DAC0A3;");
                Scene scene = new Scene(ticketTest);
                SnapshotParameters params = new SnapshotParameters();
                double scaleFactor =3;
                params.setTransform(Transform.scale(3,3));
                imageHolder[0] = ticketTest.snapshot(params, null);
            } finally {
                latch.countDown();
            }
        });
        latch.await();
        return imageHolder[0];
    }
    @Override
    protected WritableImage call() throws Exception {
        return takeSnapshotNormalTicket(eventToPrint, qrCode);
    }
}
