package be;

public class EventInvalidResponse {
    private String startDateInvalid;
    private String endDateInvalid;
    private String endTimeInvalid;
    private String startTimeInvalid;

    public EventInvalidResponse(String startDateInvalid,String endDateInvalid,String endTimeInvalid,String startTimeInvalid ) {
        this.startDateInvalid = startDateInvalid;
        this.endDateInvalid = endDateInvalid;
        this.endTimeInvalid = endTimeInvalid;
        this.startTimeInvalid = startTimeInvalid;
    }

    @Override
    public String toString() {
        return "EventInvalidResponse{" +
                "startDateInvalid='" + startDateInvalid + '\'' +
                ", endDateInvalid='" + endDateInvalid + '\'' +
                ", endTimeInvalid='" + endTimeInvalid + '\'' +
                ", startTimeInvalid='" + startTimeInvalid + '\'' +
                '}';
    }

    public EventInvalidResponse(){

    }

    public String getStartDateInvalid() {
        return startDateInvalid;
    }

    public void setStartDateInvalid(String startDateInvalid) {
        this.startDateInvalid = startDateInvalid;
    }

    public String getEndDateInvalid() {
        return endDateInvalid;
    }

    public void setEndDateInvalid(String endDateInvalid) {
        this.endDateInvalid = endDateInvalid;
    }

    public String getEndTimeInvalid() {
        return endTimeInvalid;
    }

    public void setEndTimeInvalid(String endTimeInvalid) {
        this.endTimeInvalid = endTimeInvalid;
    }

    public String getStartTimeInvalid() {
        return startTimeInvalid;
    }

    public void setStartTimeInvalid(String startTimeInvalid) {
        this.startTimeInvalid = startTimeInvalid;
    }
}
