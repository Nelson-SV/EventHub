package view.components.main;
import exceptions.ErrorCode;
import exceptions.EventException;
import exceptions.ExceptionHandler;
import exceptions.ExceptionLogger;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.util.Duration;
import view.components.listeners.Displayable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class DateObservable {
    private final List<Displayable> eventDisplayer;
    private final Model model;
    private final int executionInterval = 60;
    private ScheduledService<Boolean> eventStatusService;

    public DateObservable(Model model) {
        this.eventDisplayer = new ArrayList<>();
        this.model = model;
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

    public void startDateService() {
        this.eventStatusService = new ScheduledService<Boolean>() {
            @Override
            protected Task<Boolean> createTask() {
                return new Task<Boolean>() {
                    @Override
                    protected Boolean call() throws Exception {
                        return model.compareEventDatesWithCurrentDate();
                    }
                };
            }
        };
        eventStatusService.setDelay(Duration.seconds(10));
        eventStatusService.setPeriod(Duration.seconds(executionInterval));
        eventStatusService.setOnSucceeded((event) -> {
            if(eventStatusService.getValue()) {
                System.out.println("executed");
                callDisplayable();
            }
        });
        eventStatusService.setOnFailed((event) -> {
            EventException cause = (EventException) eventStatusService.getException();
            ExceptionLogger.getInstance().getLogger().log(Level.SEVERE, cause.getMessage());
            ExceptionHandler.erorrAlertMessage(ErrorCode.FAILED_UPDATE_STATUS.getValue());
        });
        eventStatusService.restart();
    }
}
