package bll;

import be.Event;
import exceptions.EventException;
import javafx.collections.ObservableMap;

import java.util.HashMap;
import java.util.List;

public interface ILogicManager {
    ObservableMap<Integer,Event> getEvents() throws EventException;
}
