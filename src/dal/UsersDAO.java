package dal;
import be.Role;
import be.User;
import exceptions.ErrorCode;
import exceptions.EventException;
import exceptions.ExceptionLogger;
import javafx.concurrent.Task;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class UsersDAO {
    private ConnectionManager connectionManager;
    public UsersDAO() throws EventException {
        this.connectionManager = new ConnectionManager();
    }
    public Task<List<User>> getEventUsers(int eventId){
        return getUsers(eventId);
    }
    private Task<List<User>> getUsers(int eventId) {
        Task<List<User>> retrieve = new Task<List<User>>() {
            @Override
            protected List<User> call() throws Exception {
            List<User> evCoord=new ArrayList<>();
                String sql = "SELECT U.UserId,U.FirstName,U.LastName,U.Role FROM USERS AS U " +
                        "Where U.Role Like ? " +
                        "AND U.UserId NOT IN (SELECT us.UserId FROM Users us join UsersEvents ue ON us.UserId=ue.UserId  join Event e ON e.EventId=ue.EventId WHERE e.EventId=?)";
                try (Connection conn = connectionManager.getConnection()) {
                    try (PreparedStatement psmt = conn.prepareStatement(sql)) {
                        psmt.setString(1, Role.EVENT_COORDINATOR.getValue());
                        psmt.setInt(2, eventId);
                        ResultSet rs = psmt.executeQuery();
                        while (rs.next()) {
                            int userId = rs.getInt(1);
                            String firstName = rs.getString(2);
                            String lastName = rs.getString(3);
                            String role = rs.getString(4);
                            User user = new User(firstName, lastName, role);
                            user.setUserId(userId);
                            evCoord.add(user);
                        }
                    }
                } catch (SQLException | EventException e) {
                    ExceptionLogger.getInstance().getLogger().log(Level.SEVERE,e.getMessage());
                    System.out.println(e.getMessage()+e.getCause()+ErrorCode.OPERATION_DB_FAILED);
                   // throw new EventException(e.getMessage(), e.getCause(), ErrorCode.OPERATION_DB_FAILED);
                };
                return evCoord;
            };
        };
return retrieve;
    }
}
