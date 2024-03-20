package exceptions;

public enum ErrorCode {
    INVALID_INPUT("Invalid Input"),
    CONNECTION_FAILED("Connection failed,please verify your network connection"),
    OPERATION_DB_FAILED("Operation failed,please verify your network connection, ore try again");


    private String value;

    public String getValue() {
        return value;
    }

    ErrorCode(String value) {
        this.value = value;
    }
}
