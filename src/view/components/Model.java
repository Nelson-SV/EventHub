package view.components;

import be.Event;
import be.Location;
import bll.eventManager;
import exceptions.EventException;

import java.sql.SQLException;

public class Model {

    private eventManager manager;
    private final static Model instance;  //ensures that by using Singelton all controllers use the same model
    static {
        try {
            instance = new Model();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (EventException e) {
            throw new RuntimeException(e);
        }

    }
    private Model() throws SQLException, EventException {
        manager = new eventManager();

    }
    public static Model getInstance() {
        return instance;
    }


    public void addEvent(Event event) throws SQLException, EventException {
        manager.addEvent(event);
    }


}
