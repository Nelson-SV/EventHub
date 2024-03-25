package view.components.main;

import exceptions.EventException;
import exceptions.TicketException;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import view.components.SellingTickets.SellingViewController;
import view.components.eventDescription.EventComponent;
import view.components.events.CreateEventController;
import view.components.eventsPage.EventsPageController;
import view.components.listeners.Displayable;
import view.components.manageButton.ManageAction;
import view.utility.NavigationHoverControl;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable, Displayable {
    private boolean initializationError = false;
    private Model model;
    @FXML
    private MFXButton eventsNavButton;
    @FXML
    private MFXButton sellingNavButton;
    @FXML
    private MFXButton specialTicketNavButton;
    @FXML
    private Rectangle sellingLine;
    @FXML
    private Rectangle ticketingLine;
    @FXML
    private Rectangle eventsLine;
    @FXML
    private VBox mainEventContainer;
    @FXML
    private StackPane mainLayout;
    @FXML
    private VBox pageDisplayer;


    @FXML
    private StackPane secondaryLayout, thirdLayout;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            model = Model.getInstance();
            //model.setEventsDisplayer(this);
            NavigationHoverControl navigationHoverControl = new NavigationHoverControl(eventsLine, sellingLine, ticketingLine, eventsNavButton, sellingNavButton, specialTicketNavButton);
            navigationHoverControl.initializeNavButtons();
            initializeMainPageEvents();
            //displayEvents();
            // bindParentWidth();
        } catch (EventException | TicketException e) {
            initializationError = true;
            //throw new RuntimeException(e);
        }

    }


    @FXML
    private void closeWindow(ActionEvent event) {
        this.secondaryLayout.setDisable(true);
        this.secondaryLayout.setVisible(false);
    }


    // observe the size off the list, if is changing update the UI (Observable design pattern)

    /**
     * Updates the events off the user based on the user interaction
     */
    @Override
    public void displayEvents() {
        mainEventContainer.getChildren().clear();
        model.sortedEventsList().forEach(e -> mainEventContainer.getChildren().add(new EventComponent(e, new ManageAction(this.secondaryLayout, thirdLayout, e.getId(), model))));
    }


    public void createSpecialTicket(ActionEvent actionEvent) {

    }

    @FXML
    private void createEvent(ActionEvent actionEvent) {
        this.secondaryLayout.setVisible(true);
        this.secondaryLayout.setDisable(false);
        CreateEventController createEventController = new CreateEventController(secondaryLayout, thirdLayout, model);
        secondaryLayout.getChildren().clear();
        secondaryLayout.getChildren().add(createEventController.getRoot());
    }


    private void bindParentWidth() {
        secondaryLayout.minWidthProperty().bind(mainLayout.widthProperty());
        secondaryLayout.minHeightProperty().bind(mainLayout.heightProperty());
        secondaryLayout.maxWidthProperty().bind(mainLayout.widthProperty());
        secondaryLayout.maxHeightProperty().bind(mainLayout.heightProperty());
    }

    public boolean isInitializationError() {
        return initializationError;
    }

    @FXML
    private void selling(ActionEvent actionEvent) {
        SellingViewController sellingViewController = new SellingViewController(pageDisplayer, model);
        pageDisplayer.getChildren().clear();
        pageDisplayer.getChildren().add(sellingViewController.getRoot());
    }

    @FXML
    private void navigateEventsPage(ActionEvent actionEvent) {
        EventsPageController eventsPageController = new EventsPageController(secondaryLayout, thirdLayout);
        pageDisplayer.getChildren().clear();
        pageDisplayer.getChildren().add(eventsPageController);
    }

    private void initializeMainPageEvents() {
        EventsPageController eventsPageController = new EventsPageController(secondaryLayout, thirdLayout);
        pageDisplayer.getChildren().clear();
        pageDisplayer.getChildren().add(eventsPageController);
    }




    public VBox getPageDisplayer() {
        return pageDisplayer;
    }

}
