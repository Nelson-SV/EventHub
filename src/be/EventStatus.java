package be;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

//Do not use this class not sure if we will need it in the end

public class EventStatus {
    private final Event eventDTO;
    private final  SimpleObjectProperty<Status> status;
    private final SimpleIntegerProperty coordinatorCount;

    @Override
    public String toString() {
        return  eventDTO.getName();
    }

    public EventStatus(Event eventDTO) {
        this.eventDTO = eventDTO;
        this.status = new SimpleObjectProperty<>();
        this.coordinatorCount= new SimpleIntegerProperty();
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

    public int getCoordinatorCount() {
        return coordinatorCount.get();
    }

    public SimpleIntegerProperty coordinatorCountProperty() {
        return coordinatorCount;
    }

    public void setCoordinatorCount(int coordinatorCount) {
        this.coordinatorCount.set(coordinatorCount);
    }

    public SimpleObjectProperty<Status> statusProperty() {
        return status;
    }
}
