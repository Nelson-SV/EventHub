package view.components.main;

import be.Event;
import exceptions.EventException;
import view.components.listeners.Displayable;

public interface CommonModel {
    void deleteEvent(int eventId)throws EventException;

    Event getEventById(int eventId);

    Displayable getEventsDisplayer();
}
