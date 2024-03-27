package be;

public enum Status {

    UPCOMING("UPCOMING"),
    FINALIZED("FINALIZED"),
    ONGOING("ONGOING");
private  final String status;

    public String getValue() {
        return status;
    }

    Status(String status) {
        this.status= status;

    }
}
