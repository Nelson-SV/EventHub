package be;

public enum Role {
    ADMIN("administrator"),
    EVENT_COORDINATOR("event_coordinator");
    private final String value;

    public String getValue() {
        return value;
    }

    Role(String value) {
        this.value = value;
    }
}
