package dal;
import be.User;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import java.util.List;

public class CoordinatorsService extends Service<List<User>> {
    List<User> users;
    public CoordinatorsService(List<User> users) {
        this.users=users;
    }

    @Override
    protected Task<List<User>> createTask() {
        return new Task<List<User>>() {
            @Override
            protected List<User> call() throws Exception {
                return getUsers();
            }
        };
    }
    public List<User> getUsers() {
        return users;
    }
}
