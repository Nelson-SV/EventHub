package view.admin.eventsPage.manageEventAdminPage;

import be.User;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import view.admin.mainAdmin.AdminModel;
import view.admin.listeners.AdminCoordinatorsDisplayer;

import java.io.IOException;

public class AdminEventManagePage implements AdminCoordinatorsDisplayer {
    @FXML
    private GridPane managePageContainer;
    private AdminEventManageController adminEventManageController;

    public AdminEventManagePage(StackPane secondaryLayout, StackPane thirdLayout,StackPane fourthLayout, AdminModel model) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminEventManagePage.fxml"));
        adminEventManageController = new AdminEventManageController(secondaryLayout, thirdLayout,fourthLayout, model);
        loader.setController(adminEventManageController);
        try {
            managePageContainer = loader.load();
            System.out.println(managePageContainer.getId());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public GridPane getRoot() {
        return managePageContainer;
    }

    @Override
    public void displayEventCoordinators() {
        adminEventManageController.displayEventCoordinators();
    }

    @Override
    public void displayAllCoordinators() {
        adminEventManageController.displayAllCoordinators();
    }
}
