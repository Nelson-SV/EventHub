package view.admin.searchComponent;

import exceptions.ErrorCode;
import exceptions.ExceptionHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import view.components.listeners.DataHandler;

import javax.xml.crypto.Data;
import java.io.IOException;

public class SearchComponent<T> {
    private HBox searchRoot;
    private SearchController<T> searchController;

    public SearchComponent(DataHandler<T> dataHandler) {
        searchController = new SearchController<>(dataHandler);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SearchWindow.fxml"));
        loader.setController(searchController);
        try {
            searchRoot = loader.load();
        } catch (IOException e) {
            ExceptionHandler.errorAlertMessage(ErrorCode.LOADING_FXML_FAILED.getValue());
        }
    }
}
