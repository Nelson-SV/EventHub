package view.admin.searchComponent;
import exceptions.ErrorCode;
import exceptions.ExceptionHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.PopupControl;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import view.components.listeners.DataHandler;
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

    public HBox getSearchRoot() {
        return searchRoot;
    }

    public TextField getSearchWindow() {
        return this.searchController.getSearchWindow();
    }

    public PopupControl getPopupWindow(){
        return this.searchController.getPopupWindow();
    }

    public void setSearchWindowWidth(int width){
        this.searchController.setSearchWindowWidth(width);
    }

    public void setSearchResponseHolderHeight(int height){
        this.searchController.setSearchResponseHolderHeight(height);
    }
    public void closeWindow(){
        this.searchController.closePopWindow();
    }
}
