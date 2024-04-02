package view.components.eventsObservers;

import view.components.listeners.Displayable;

public interface DateObserver {
     void addDisplayable(Displayable displayer);
    void removeDisplayable(Displayable displayer);
     void startService();
}
