package view.admin.eventsPage;

import be.DeleteOperation;
import be.Status;
import exceptions.ErrorCode;
import exceptions.EventException;
import exceptions.ExceptionHandler;
import exceptions.ExceptionLogger;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import view.admin.eventsPage.assignButton.AssignButton;
import view.admin.eventsPage.eventDescription.EventDescription;
import view.admin.eventsPage.shortcutButton.ShortcutButton;
import view.admin.mainAdmin.AdminModel;
import view.components.deleteEvent.DeleteButton;
import view.components.listeners.Displayable;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;

public class AdminPageController implements Initializable, Displayable {
    private final String ONGOING = "Ongoing";
    private final String UPCOMING = "Upcoming";
    private final String FINALIZED = "Finalized";
    private AdminModel adminModel;
    @FXML
    private VBox eventsContainer;
    @FXML
    private VBox adminEventPage;
    @FXML
    private
    MFXButton ongoingEvents;
    @FXML
    private MFXButton upcomingEvents;
    @FXML
    private MFXButton finalizedEvents;
    @FXML
    private HBox shortcutContainer;

    private StackPane secondaryLayout, thirdLayout, adminFourthLayout;
    private Service<Void> getEvents;

    public AdminPageController(AdminModel adminModel, StackPane secondaryLayout, StackPane thirdLayout, StackPane adminFourthLayout) {
        this.adminModel = adminModel;
        this.secondaryLayout = secondaryLayout;
        this.thirdLayout = thirdLayout;
        this.adminFourthLayout = adminFourthLayout;
        adminModel.setEventsDisplayer(this);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeEvents();
        initializeShortcutButtons(shortcutContainer);
    }

    /**
     * initialize the  shortcut buttons and add them to the parent children
     */
    private void initializeShortcutButtons(HBox shortcutButtonsContainer) {
        ShortcutButton sortOngoing = new ShortcutButton(ONGOING, Status.ONGOING, adminModel);
        ShortcutButton sortUpcoming = new ShortcutButton(UPCOMING, Status.UPCOMING, adminModel);
        ShortcutButton sortFinalized = new ShortcutButton(FINALIZED, Status.FINALIZED, adminModel);
        adminModel.addSubject(sortOngoing);
        adminModel.addSubject(sortUpcoming);
        adminModel.addSubject(sortFinalized);
        shortcutButtonsContainer.getChildren().addAll(sortUpcoming.getShortcutButton(), sortOngoing.getShortcutButton(), sortFinalized.getShortcutButton());
    }


    @Override
    public void displayEvents() {
        if (eventsContainer.getScene() != null) {
            Platform.runLater(() -> {
                eventsContainer.getChildren().clear();
                adminModel.sortedEventsList()
                        .forEach(e -> eventsContainer.getChildren()
                                .add(new EventDescription(e, new AssignButton(adminModel, secondaryLayout, thirdLayout, adminFourthLayout, e.getEventDTO().getId()), new DeleteButton(secondaryLayout, thirdLayout, adminModel, e.getEventDTO().getId(), DeleteOperation.DELETE_EVENT))));
            });
        }
    }


    private void initializeEvents() {
        getEvents = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws EventException {
                        adminModel.initializeEvents();
                        return null;
                    }
                };
            }
        };
        getEvents.setOnSucceeded(event -> displayEvents());
        getEvents.setOnFailed(event -> {
            ExceptionLogger.getInstance().getLogger().log(Level.SEVERE, getEvents.getException().getMessage());
            ExceptionHandler.errorAlertMessage(ErrorCode.FAILED_TO_LOAD_EVENTS.getValue());
        });
        getEvents.restart();
    }

}
