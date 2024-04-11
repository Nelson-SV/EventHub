package bll;
import be.Event;
import be.EventStatus;
import be.Status;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;

public class EventStatusCalculator {
    public static Status calculateStatus(EventStatus event) {
        Status status = null;
        LocalDateTime startDateTime = LocalDateTime.of(event.getEventDTO().getStartDate(), event.getEventDTO().getStartTime());
        LocalDateTime endDateTime = null;
        if (event.getEventDTO().getEndDate() == null && event.getEventDTO().getEndTime() == null) {
            status = initializeStatusByStartDate(startDateTime);
        } else if (event.getEventDTO().getEndTime() != null && event.getEventDTO().getEndDate() == null) {
            status = initializeStatusByStartAndEndTime(startDateTime, event.getEventDTO().getEndTime());
        } else if (event.getEventDTO().getEndTime() == null && event.getEventDTO().getEndDate() != null) {
            endDateTime = LocalDateTime.of(event.getEventDTO().getEndDate(), LocalTime.MAX);
            status = initializeStatus(startDateTime, endDateTime);
        } else if (event.getEventDTO().getEndTime() != null) {
            endDateTime = LocalDateTime.of(event.getEventDTO().getEndDate(), event.getEventDTO().getEndTime());
            status = initializeStatus(startDateTime, endDateTime);
        }
        return status;
    }

    public static Status calculateStatus(Event event) {
        Status status = null;
        LocalDateTime startDateTime = LocalDateTime.of(event.getStartDate(), event.getStartTime());
        LocalDateTime endDateTime = null;
        if (event.getEndDate() == null && event.getEndTime() == null) {
            status = initializeStatusByStartDate(startDateTime);
        } else if (event.getEndTime() != null && event.getEndDate() == null) {
            status = initializeStatusByStartAndEndTime(startDateTime, event.getEndTime());
        } else if (event.getEndTime() == null && event.getEndDate() != null) {
            endDateTime = LocalDateTime.of(event.getEndDate(), LocalTime.MAX);
            status = initializeStatus(startDateTime, endDateTime);
        } else if (event.getEndTime() != null) {
            endDateTime = LocalDateTime.of(event.getEndDate(), event.getEndTime());
            status = initializeStatus(startDateTime, endDateTime);
        }
        return status;
    }


    private static Status initializeStatusByStartDate(LocalDateTime startDate) {
        LocalDateTime today = LocalDateTime.now();
        if (startDate.isAfter(today)) {
            return Status.UPCOMING;
        } else if (startDate.toLocalDate().isEqual(today.toLocalDate())) {
            return Status.ONGOING;
        } else {
            return Status.FINALIZED;
        }
    }

    private static Status initializeStatusByStartAndEndTime(LocalDateTime startDate, LocalTime endTime) {
        LocalDateTime today = LocalDateTime.now();
        if (startDate.isAfter(today)) {
            return Status.UPCOMING;
        } else if (startDate.toLocalDate().isEqual(today.toLocalDate()) &&
                (today.toLocalTime().isAfter(startDate.toLocalTime()) && today.toLocalTime().isBefore(endTime))) {
            return Status.ONGOING;
        } else {
            return Status.FINALIZED;
        }
    }

    private static Status initializeStatus(LocalDateTime startDate, LocalDateTime endDate) {
        LocalDateTime today = LocalDateTime.now();
        Status status = null;
        if (endDate != null && today.isAfter(endDate)) {
            status = Status.FINALIZED;
        } else if (today.isBefore(startDate)) {
            status = Status.UPCOMING;
        } else if (!today.isBefore(startDate) && (endDate == null || !today.isAfter(endDate))) {
            status = Status.ONGOING;
        }
        return status;
    }

    private static boolean isStatusChanged(EventStatus event) {
        boolean changed = false;
        LocalDateTime endDateTime;
        LocalDateTime startDateTime = LocalDateTime.of(event.getEventDTO().getStartDate(), event.getEventDTO().getStartTime());
        if (event.getEventDTO().getEndDate() == null) {
            if (event.getEventDTO().getEndTime() == null) {
                endDateTime = LocalDateTime.of(event.getEventDTO().getStartDate().plusDays(1), LocalTime.MAX);
            } else {
                endDateTime = LocalDateTime.of(event.getEventDTO().getStartDate(), event.getEventDTO().getEndTime());
            }
        } else {
            endDateTime = LocalDateTime.of(event.getEventDTO().getEndDate(),
                    event.getEventDTO().getEndTime() != null ? event.getEventDTO().getEndTime() : LocalTime.MAX);
        }
        if (event.getStatus() == Status.ONGOING) {
            if (LocalDateTime.now().isAfter(endDateTime)) {
                changed = true;
            }
        } else if (event.getStatus() == Status.UPCOMING) {
            if (LocalDateTime.now().isAfter(startDateTime)) {
                changed = true;
            }
        }
        return changed;
    }

    public static boolean isStatusChanged(Collection<EventStatus> events){
        for(EventStatus event:events){
            if(isStatusChanged(event)){
                return true;
            }
        }
        return false;
    }
}
