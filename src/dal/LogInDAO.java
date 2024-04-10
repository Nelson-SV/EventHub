package dal;

import be.User;
import exceptions.ErrorCode;
import exceptions.EventException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LogInDAO {
    private Connection connection;
    private ConnectionManager connectionManager;

    public LogInDAO() throws EventException {
        connectionManager= new ConnectionManager();
    }

    public User authenticateUser(String username, String password) throws EventException {
        String query = "SELECT * FROM Users WHERE FirstName = ? AND Password = ?";
        connection = connectionManager.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            // Set username and password parameters
            statement.setString(1, username);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) {
                int userId = resultSet.getInt("UserId");
                String firstName = resultSet.getString("FirstName");
                String passwordU = resultSet.getString("Password");
                String lastName = resultSet.getString("LastName");
                String profilePicture = resultSet.getString("ProfilePicture");
                String role = resultSet.getString("Role");
                User user = new User( firstName,lastName, role, passwordU, profilePicture);

                user.setUserId(userId);
                System.out.println(user + "fromDAO");
                return user;
            }
        } catch (SQLException e) {
            throw new EventException(e.getMessage(), e.getCause(), ErrorCode.CONNECTION_FAILED);

        }
        return null;
    }
}
