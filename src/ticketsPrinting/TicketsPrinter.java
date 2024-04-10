package ticketsPrinting;

import be.Event;
import com.google.zxing.WriterException;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.print.PrinterJob;
import javafx.scene.image.Image;
import ticketsPrinting.QrCodeGenerator;
import ticketsPrinting.TicketsSnapshot;
import view.admin.usersPage.threads.ImageLoader;
import view.components.regularTickets.ticketDesign.TicketComponentDescription;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TicketsPrinter {
 private static ObservableList<Image> imagesToPrint = FXCollections.observableArrayList();
 private  static ImageLoader imageLoader = new ImageLoader();
 private String uuid  =  "B5953972-DF42-4DDA-AB3A-1AA1DBAB1131";
private Event event = new Event("Christmas party","Mega christmas party", LocalDate.parse("2024-12-23"),LocalDate.parse("2024-12-23"), LocalTime.parse("12:00"),LocalTime.parse("23:59"),"Kirkevej");




//    public void print() throws WriterException {
//     Image qrCode= QrCodeGenerator.generateQRCodeImage(uuid,50,50);
//     TicketsSnapshot ticketsSnapshot = new TicketsSnapshot(event,qrCode);
//     setTheTicketSnapshot(ticketsSnapshot);
//    // setTheImageLoader(imagesToPrint);
// };
//
//
//
// private void setTheTicketSnapshot(TicketsSnapshot ticketsSnapshot){
//     ticketsSnapshot.setOnSucceeded((event)->{
//         imagesToPrint.add(ticketsSnapshot.getValue());
//         printImages();
//     });
//     ticketsSnapshot.setOnFailed((event)->{
//         ticketsSnapshot.getException().printStackTrace();
//     });
//     ExecutorService executorService = Executors.newSingleThreadExecutor();
//     executorService.execute(ticketsSnapshot);
//     executorService.shutdown();
// }


 private void setTheImageLoader(ObservableList<Image> imagesToPrint) {
        imageLoader.setImageLocation("http://res.cloudinary.com/deipyfz99/image/upload/v1712563835/mbwqk1wpanct5bdtdmgw.png");
        imageLoader.getServiceLoader().setOnSucceeded((event) -> {
                imagesToPrint.add(imageLoader.getServiceLoader().getValue());
                printImages();
           // CommonMethods.makeTheImageRound(imageLoader.getServiceLoader().getValue(), imageView);
        });

        imageLoader.getServiceLoader().setOnFailed((event) -> {
        });
        imageLoader.getServiceLoader().restart();
    }

    private void printImages() {
        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null && job.showPrintDialog(null)) {
            final boolean[] printed = {false};

            Platform.runLater(() -> {
                try {
                    TicketComponentDescription ticketTest = new TicketComponentDescription(event);
                    ticketTest.setQrCode(QrCodeGenerator.generateQRCodeImage(uuid, 50, 50));
                    ticketTest.getRoot().setStyle("-fx-background-color: #DAC0A3;");
                    ticketTest.setCustomerName("Ana are mere");
                    ticketTest.setCustomerEmail("ana@gmail.com");
                    // Optionally apply scaling here if needed
                    // For example, to set the scale: ticketTest.getRoot().setScaleX(3); ticketTest.getRoot().setScaleY(3);

                    printed[0] = job.printPage(ticketTest.getRoot());
                    System.console().writer();

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (printed[0]) {
                        job.endJob();
                    } else {
                        System.out.println("Failed to print.");
                    }
                }
            });
        }
    }

/*
    Yes, if you have multiple tickets to print, you can loop through them in the same `Platform.runLater` block. Here's how you can modify the `printImages` method to print multiple tickets:

            ```java
    private void printImages() {
        // Assuming each ticket is a Node and tickets is a Collection<Node> of all tickets to be printed
        List<Node> tickets = ...; // Your tickets

        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null && job.showPrintDialog(null)) {
            Platform.runLater(() -> {
                try {
                    for (Node ticket : tickets) {
                        // Set the ticket details
                        TicketComponentDescription ticketTest = new TicketComponentDescription(event);
                        ticketTest.setQrCode(QrCodeGenerator.generateQRCodeImage(uuid, 50, 50));
                        ticketTest.getRoot().setStyle("-fx-background-color: #DAC0A3;");
                        ticketTest.setCustomerName("Ana are mere");
                        ticketTest.setCustomerEmail("ana@gmail.com");

                        // Print the ticket
                        boolean success = job.printPage(ticketTest.getRoot());
                        if (!success) {
                            System.out.println("Failed to print a ticket.");
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    job.endJob();
                }
            });
        }
    }
```

    In this code, `tickets` is a `List<Node>` of all tickets to be printed. It loops through each ticket in the `tickets` list, sets the ticket details, and then prints the ticket. If printing a ticket fails, it prints an error message and breaks out of the loop. After all tickets have been printed (or if printing a ticket fails), it ends the job.

    Please replace `List<Node> tickets = ...;` with the actual list of tickets you want to print.



   */









    //Todo calculate and print them on a page
//    // Assuming each ticket is a Node and tickets is a Collection<Node> of all tickets to be printed
//    List<Node> tickets = ...; // Your tickets
//    int ticketsPerPage = ...; // Calculate based on ticket size and printable area
//    VBox currentContainer = new VBox(5); // 5 is the spacing between tickets
//    List<VBox> pages = new ArrayList<>();
//
//for (Node ticket : tickets) {
//        if (currentContainer.getChildren().size() >= ticketsPerPage) {
//            pages.add(currentContainer);
//            currentContainer = new VBox(5); // Create a new container for the next page
//        }
//        currentContainer.getChildren().add(ticket);
//    }
//// Don't forget to add the last container if it's not empty
//if (!currentContainer.getChildren().isEmpty()) {
//        pages.add(currentContainer);
//    }
//
//    PrinterJob job = PrinterJob.createPrinterJob();
//if (job != null && job.showPrintDialog(null)) {
//        for (VBox page : pages) {
//            boolean success = job.printPage(pageLayout, page);
//            if (!success) {
//                System.out.println("Failed to print a page.");
//                break;
//            }
//        }
//        job.endJob();
//    }
//

}












