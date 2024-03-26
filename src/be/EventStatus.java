package be;
import javafx.beans.property.SimpleObjectProperty;

//Do not use this class not sure if we will need it in the end

public class EventStatus {
    private final Event eventDTO;
    private final  SimpleObjectProperty<Status> status;

    public EventStatus(Event eventDTO) {
        this.eventDTO = eventDTO;
        this.status = new SimpleObjectProperty<>();
    }
    public void setStatus(Status status) {
        this.status.set(status);
    }
    public Event getEventDTO() {
        return eventDTO;
    }
    public Status getStatus() {
        return status.get();
    }
    public SimpleObjectProperty<Status> statusProperty() {
        return status;
    }
}
