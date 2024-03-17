package dal;
import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import exceptions.ErrorCode;
import exceptions.EventException;
import java.sql.Connection;
import java.util.logging.Level;


public class ConnectionManager {
        private final SQLServerDataSource ds;
        private String user ;
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
            try {
                return ds.getConnection();

            } catch (SQLServerException e) {
                throw new EventException(e.getMessage(),e.getCause(), ErrorCode.CONNECTION_FAILED, Level.SEVERE);
            }
        }

        private  void getCredentials () throws EventException {
            String [] credentials = new FileHandler().readDbLogin();
            this.user=credentials[1].trim();
            this.password= credentials[2].trim();
        }
    }



