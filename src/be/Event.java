package be;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class Event {
    private SimpleIntegerProperty id;
    private SimpleStringProperty name;
    private SimpleStringProperty description;
    private SimpleObjectProperty<LocalDate> startDate;
    private SimpleObjectProperty<LocalDate> endDate;
    private SimpleObjectProperty<LocalTime> startTime;
    private SimpleObjectProperty<LocalTime> endTime;
    private SimpleStringProperty location;
    private SimpleIntegerProperty availableTickets;

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getDescription() {
        return description.get();
    }

    public SimpleStringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public LocalDate getStartDate() {
        return startDate.get();
    }

    public SimpleObjectProperty<LocalDate> startDateProperty() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate.set(startDate);
    }

    public LocalDate getEndDate() {
        return endDate.get();
    }

    public SimpleObjectProperty<LocalDate> endDateProperty() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate.set(endDate);
    }

    public LocalTime getStartTime() {
        return startTime.get();
    }

    public SimpleObjectProperty<LocalTime> startTimeProperty() {
        return startTime;
    }

    @Override
    public String toString() {
        return name.get();
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime.set(startTime);
    }

    public LocalTime getEndTime() {
        return endTime.get();
    }

    public SimpleObjectProperty<LocalTime> endTimeProperty() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime.set(endTime);
    }

    public String getLocation() {
        return location.get();
    }

    public SimpleStringProperty locationProperty() {
        return location;
    }

    public void setLocation(String location) {
        this.location.set(location);
    }

    public int getAvailableTickets() {
        return availableTickets.get();
    }

    public SimpleIntegerProperty availableTicketsProperty() {
        return availableTickets;
    }

    public void setAvailableTickets(int availableTickets) {
        this.availableTickets.set(availableTickets);
    }

    public Event(String name, String description, LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime, String location) {
        initializeFields();
        this.name.setValue(name);
        this.description.setValue(description);
        this.startDate.setValue(startDate);
        this.endDate.setValue(endDate);
        this.startTime.setValue(startTime);
        this.endTime.setValue(endTime);
        this.description.setValue(description);
        this.location.setValue(location);
    }

    public Event(Event originalEvent) {
        initializeFields();
        this.setId(originalEvent.getId());
        this.name.setValue(originalEvent.getName());
        this.description.setValue(originalEvent.getDescription());
        this.startDate.setValue(originalEvent.getStartDate());
        this.endDate.setValue(originalEvent.getEndDate());
        this.startTime.setValue(originalEvent.getStartTime());
        this.endTime.setValue(originalEvent.getEndTime());
        this.description.setValue(originalEvent.getDescription());
        this.location.setValue(originalEvent.getLocation());
        this.availableTickets.setValue(originalEvent.getAvailableTickets());
    }
    public Event(String name) {
        initializeFields();
        this.name.setValue(name);
        }


    private void initializeFields() {
        this.name = new SimpleStringProperty();
        this.description = new SimpleStringProperty();
        this.startDate = new SimpleObjectProperty<>();
        this.endDate = new SimpleObjectProperty<>();
        this.startTime = new SimpleObjectProperty<>();
        this.endTime = new SimpleObjectProperty<>();
        this.location = new SimpleStringProperty();
        this.id = new SimpleIntegerProperty();
        this.availableTickets = new SimpleIntegerProperty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(id.getValue(), event.id.getValue()) && Objects.equals(name.getValue(), event.name.getValue())
                && Objects.equals(description.getValue(), event.description.getValue()) && Objects.equals(startDate.getValue(), event.startDate.getValue())
                && Objects.equals(endDate.getValue(), event.endDate.getValue()) && Objects.equals(startTime.getValue(), event.startTime.getValue()) && Objects.equals(endTime.getValue(), event.endTime.getValue())
                && Objects.equals(location.getValue(), event.location.getValue()) && Objects.equals(availableTickets.getValue(), event.availableTickets.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, startDate, endDate, startTime, endTime, location, availableTickets);
    }

    public String getNameForDisplay() {
        return getName();
    }
}
