package bll;

import be.Event;
import exceptions.EventException;
import javafx.collections.ObservableMap;
public interface ILogicManager {
    ObservableMap<Integer,Event> getEvents() throws EventException;
}
