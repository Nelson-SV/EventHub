package dal;

import exceptions.ErrorCode;
import exceptions.EventException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LogInDAO {
    private Connection connection;

    public String authenticateUser(String username, String password) throws EventException {
        String query = "SELECT Role FROM Users WHERE Username = ? AND Password = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            // Set username and password parameters
            statement.setString(1, username);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                // User found, return the role
                return resultSet.getString("Role");
            } else {
                // User not found, return null
                return null;
            }
        } catch (SQLException e) {
            throw new EventException(e.getMessage(), e.getCause(), ErrorCode.CONNECTION_FAILED);

        }
    }
}
