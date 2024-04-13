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
    public ObservableList<User> getResultData() {
        model.getUserSearchResultData();
        return null;
    }

    @Override
    public void performSearchOperation() {
        model.performUnassignedUsersSearchOperation();
    }

    @Override
    public void undoSearchOperation() {
        model.performUnassignedUsersUndoOperation();
    }
}
