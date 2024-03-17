package view.components.main;

import be.Event;
import bll.EventManagementLogic;
import bll.EventManager;
import bll.ILogicManager;
import exceptions.EventException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public class Model {
//TODO
    // when a new event is created return boolean is database insertion was successful, if yes add the event to the list
//    without selecting again from the DB


    private EventManager manager;
    private ILogicManager evmLogic;
    //holds all the events for a given user id
    private ObservableList<Event> events;
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
        manager = new EventManager();
        events= FXCollections.observableArrayList();
        evmLogic= new EventManagementLogic();
        initializeEventsList();
    }
    public static Model getInstance() {
        return instance;
    }


    public void addEvent(Event event) throws SQLException, EventException {
        manager.addEvent(event);
    }

    private void initializeEventsList(){
        this.events.setAll(evmLogic.getEvents());
        events.forEach(System.out::println);
    }
    public ObservableList<Event> getEvents(){
        return this.events;
    }


}
