package view.components.eventsObservers;
import be.Event;
import be.EventStatus;
import bll.EventManagementLogic;
import bll.ILogicManager;
import exceptions.ErrorCode;
import exceptions.EventException;
import exceptions.ExceptionHandler;
import exceptions.ExceptionLogger;
import javafx.collections.ObservableMap;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.util.Duration;
import view.components.listeners.Displayable;
import view.components.main.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class EventsObservable  implements DateObserver {
    private final List<Displayable> eventDisplayer;
    private final Model model;
    private final ILogicManager evmLogic;
    private final int periodTime = 300;
    private ScheduledService<ObservableMap<Integer, EventStatus>> scheduledService;

    public EventsObservable(Model model) throws EventException {
        this.eventDisplayer = new ArrayList<>();
        this.model = model;
        this.evmLogic = new EventManagementLogic();
    }

    public void addDisplayable(Displayable displayer) {
        this.eventDisplayer.add(displayer);
    }

    public void removeDisplayable(Displayable displayer) {
        this.eventDisplayer.remove(displayer);
    }

    private void callDisplayable() {
        eventDisplayer.forEach((displayable -> {
            if (displayable != null) {
                displayable.displayEvents();
            }
        }));
    }

    private void updateCoordinatorEvents(ObservableMap<Integer, EventStatus> newEvents) {
        model.setCoordinatorEvents(newEvents);
    }

    public void startService() {
        scheduledService = new ScheduledService<ObservableMap<Integer, EventStatus>>() {
            @Override
            protected Task<ObservableMap<Integer, EventStatus>> createTask() {
                return new Task<ObservableMap<Integer, EventStatus>>() {
                    @Override
                    protected ObservableMap<Integer, EventStatus> call() throws EventException {
                        return evmLogic.getEventsWithStatus(12);
                    }
                };
            }
        };
        scheduledService.setDelay(Duration.seconds(10));
        scheduledService.setPeriod(Duration.seconds(periodTime));
        scheduledService.setRestartOnFailure(true);
        scheduledService.setMaximumFailureCount(5);
        scheduledService.setOnSucceeded((event) -> {
            updateCoordinatorEvents(scheduledService.getValue());
            callDisplayable();
        });
        scheduledService.setOnFailed((event) -> {
            EventException cause = (EventException) scheduledService.getException();
            ExceptionLogger.getInstance().getLogger().log(Level.SEVERE, cause.getMessage());
            ExceptionHandler.errorAlertMessage(ErrorCode.FAILED_UPDATE_EVENTS.getValue());
        });
        scheduledService.restart();
    }
}