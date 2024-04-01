package dal.admindal;

import be.EventStatus;
import javafx.collections.ObservableMap;
import org.w3c.dom.events.EventException;

public interface IAdminDao {


    ObservableMap<Integer, EventStatus> getAllEvents() throws EventException, exceptions.EventException;
}
