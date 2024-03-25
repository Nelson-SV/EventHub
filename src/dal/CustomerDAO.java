package dal;

import be.Customer;
import exceptions.ErrorCode;
import exceptions.EventException;

import java.sql.*;

public class CustomerDAO {
    private final ConnectionManager connectionManager;
    public CustomerDAO() throws EventException {
        this.connectionManager = new ConnectionManager();
    }

    public void addCustomer(Customer customer) throws EventException {
        Connection conn = null;
        try {
            conn = connectionManager.getConnection();
            String sql = "INSERT INTO Customers (FirstName, LastName, EmailAddress) VALUES (?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, customer.getName());
            statement.setString(2, customer.getLastName());
            statement.setString(3, customer.getEmail());
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int keys = generatedKeys.getInt(1);
                    customer.setId(keys);
                } else {
                    throw new EventException(ErrorCode.OPERATION_DB_FAILED);
                }
            }
        } catch (EventException | SQLException e) {
            throw new EventException(e.getMessage(), e.getCause(), ErrorCode.OPERATION_DB_FAILED);
        }


    }
}
