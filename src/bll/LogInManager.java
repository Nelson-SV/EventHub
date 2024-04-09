package bll;

import dal.LogInDAO;
import dal.TicketDAO;
import exceptions.EventException;

import java.sql.SQLException;

public class LogInManager {
    private LogInDAO logInDAO;

    public LogInManager() {
        this.logInDAO = new LogInDAO();
    }

    public String checkUser (String username, String password) throws EventException {
        return logInDAO.authenticateUser(username, password);
    }
}
