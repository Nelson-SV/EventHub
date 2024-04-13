package view.admin.usersPage.SearchDataHandler;
import be.User;
import javafx.collections.ObservableList;
import view.admin.mainAdmin.AdminModel;
import view.components.listeners.DataHandler;

public class SearchUserHandler implements DataHandler<User> {

    private AdminModel model;

    public SearchUserHandler(AdminModel model) {
        this.model = model;
    }

    @Override
    public ObservableList<User> getResultData(String filter) {
        return model.getUserSearchResultData(filter);
    }

   public  void performSelectSearchOperation(int entityId){
        model.performSelectUserSearchOperation(entityId);
    }

    @Override
    public void undoSearchOperation() {
         model.performUserSearchUndoOperation();
    }
}
