package dal;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import exceptions.ErrorCode;
import exceptions.EventException;
import java.sql.Connection;
import java.sql.DriverManager;



public class ConnectionManager {
    private final long establishConnectionTime = 5000;
    private final SQLServerDataSource ds;
    private String user;
    private String password;

    public ConnectionManager() throws EventException {
        ds = new SQLServerDataSource();
        this.getCredentials();
        ds.setDatabaseName("CSe2023b_e_10_Event_App");
        ds.setUser(user);
        ds.setPassword(password);
        ds.setServerName("EASV-DB4");
        ds.setTrustServerCertificate(true);
    }

    public Connection getConnection() throws EventException {
        Connection conn = null;
        long time = System.currentTimeMillis();
        DriverManager.setLoginTimeout(2);
        while (conn == null && System.currentTimeMillis() < time + establishConnectionTime) {
            try {
                return ds.getConnection();
            } catch (SQLServerException e) {
                if (System.currentTimeMillis() >= time + establishConnectionTime) {
                    throw new EventException(e.getMessage(), e.getCause(), ErrorCode.CONNECTION_FAILED);
                }
            }
        }
       if (conn == null) {
            throw new EventException(ErrorCode.OPERATION_DB_FAILED); }
      return conn;
    }

    private void getCredentials() throws EventException {
        String[] credentials = new FileHandler().readDbLogin();
        this.user = credentials[1].trim();
        this.password = credentials[2].trim();
    }
}



