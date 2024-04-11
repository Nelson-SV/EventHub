package exceptions;

public enum ErrorCode {
    INVALID_INPUT("Invalid Input"),
    CONNECTION_FAILED("Connection failed,please verify your network connection"),
    OPERATION_DB_FAILED("Operation failed,please verify your network connection, or try again"),
    LOADING_FXML_FAILED("Operation failed, problems reading files, please try again or restart the application"),
    FAILED_TO_LOAD_EVENTS("Failed to load events, please try again or restart the application"),
    FAILED_UPDATE_EVENTS("Failed to retrieve the latest events modifications, please check your network connection"),
    FAILED_UPDATE_STATUS("Failed to update the status, please use the start date/time, end date/time values"),
    FILE_ALREADY_EXISTS("The image that you are uploading already exists in the system. Please load another one!"),
    COPY_FAILED("Failed to save the image into the application memory"),
    FAILED_LOAD_USERS("Failed to load users in the system. Please check your connection!"),
    FAILED_TO_SAVE_IMAGES(" Failed to create the ticket images!Try again.");




    private String value;

    public String getValue() {
        return value;
    }

    ErrorCode(String value) {
        this.value = value;
    }
}
