package view.components.main;
import be.DeleteOperation;
import be.Event;
import exceptions.EventException;
import view.components.listeners.Displayable;

public interface CommonModel {
    void performDeleteOperation(int entityId, DeleteOperation deleteOperation) throws EventException;
    Event getEventById(int eventId);
    Displayable getEventsDisplayer();
}
