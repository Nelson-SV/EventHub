package bll;

import be.User;
import dal.LogInDAO;
import dal.TicketDAO;
import exceptions.EventException;

import java.sql.SQLException;

public class LogInManager {
    private LogInDAO logInDAO;

    public LogInManager() throws EventException {
        this.logInDAO = new LogInDAO();
    }

    public User checkUser (String username, String password) throws EventException {
        System.out.println(username + password + " userInputInManager");
        return logInDAO.authenticateUser(username, password);
    }
}