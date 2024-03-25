package view.components.loadingComponent;

public enum LoadingActions {
    SAVING("Performing operation"),
    SUCCES("Operation succeeded "),
    FAIL("Operation failed");

    LoadingActions(String actionValue) {
        this.actionValue = actionValue;
    }

    public String getActionValue() {
        return actionValue;
    }

    private final String actionValue ;
}
