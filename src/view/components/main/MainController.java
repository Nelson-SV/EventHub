package view.components.main;

import exceptions.EventException;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import view.admin.usersPage.threads.ImageLoader;
import view.components.SellingTickets.SellingViewController;
import view.components.eventsPage.EventsPageController;
import view.components.listeners.InitializationErrorListener;
import view.components.specialTickets.SpecialTicketsController;
import view.utility.CommonMethods;
import view.utility.NavigationHoverControl;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable, InitializationErrorListener {
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
    private VBox eventsPageController;
    private boolean sellingDisplayed;
    private SpecialTicketsController specialTicketsController;
    @FXML
    private HBox navigation;
    @FXML
    private ImageView userImage;
    @FXML
    private StackPane secondaryLayout, thirdLayout;
    private ImageLoader imageLoader;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            model = Model.getInstance();
            NavigationHoverControl navigationHoverControl = new NavigationHoverControl(eventsLine, sellingLine, ticketingLine, eventsNavButton, sellingNavButton, specialTicketNavButton);
            navigationHoverControl.initializeNavButtons();
            initializeMainPageEvents();
            imageLoader = new ImageLoader();
            setTheImageLoader(userImage,navigation);
        } catch (EventException e) {
            initializationError = true;
        }

    }

    public void createSpecialTicket(ActionEvent actionEvent) {
        if (specialTicketsController == null) {
            specialTicketsController = new SpecialTicketsController(pageDisplayer, model);
            pageDisplayer.getChildren().clear();
            pageDisplayer.getChildren().add(specialTicketsController.getRoot());
        }
        sellingDisplayed = false;

    }

    public boolean isInitializationError() {
        return initializationError;
    }

    @FXML
    private void selling(ActionEvent actionEvent) {

        if (!sellingDisplayed) {
            SellingViewController sellingViewController = new SellingViewController(pageDisplayer, model, secondaryLayout);
            pageDisplayer.getChildren().clear();
            pageDisplayer.getChildren().add(sellingViewController.getRoot());
            sellingDisplayed = true;

        }
        specialTicketsController = null;
    }

    @FXML
    private void navigateEventsPage(ActionEvent actionEvent) {
        if (!pageDisplayer.getChildren().contains(eventsPageController)) {
            model.sortedEventsList();
            eventsPageController = new EventsPageController(secondaryLayout, thirdLayout,model);
            pageDisplayer.getChildren().clear();
            pageDisplayer.getChildren().add(eventsPageController);
            sellingDisplayed = false;
        }
        specialTicketsController = null;
    }

    private void initializeMainPageEvents() {
        eventsPageController = new EventsPageController(secondaryLayout, thirdLayout,model);
        pageDisplayer.getChildren().clear();
        pageDisplayer.getChildren().add(eventsPageController);
    }

    public VBox getPageDisplayer() {
        return pageDisplayer;
    }

    private void setTheImageLoader(ImageView userImageContainer, HBox navigation) {
        imageLoader.setImageLocation(model.getLoggedUser().getUserImageUrl());
        imageLoader.getServiceLoader().setOnSucceeded((event) -> {
            CommonMethods.makeTheImageRound(imageLoader.getServiceLoader().getValue(), userImageContainer);
        });
        imageLoader.getServiceLoader().setOnFailed((event) -> {
            navigation.getChildren().remove(userImageContainer);
            Label placeholder = new Label("UserImage");
            navigation.getChildren().add(placeholder);
            HBox.setMargin(placeholder, new Insets(0, 10, 0, 0));
        });
        imageLoader.getServiceLoader().restart();
    }

}