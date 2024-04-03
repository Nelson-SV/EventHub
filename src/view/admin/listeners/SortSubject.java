package view.admin.listeners;

public interface SortSubject {
    void  changeToAll();
    void changeToSort();
    boolean isSelected();
    String getIdentificationId();
    void changePerformedOperationToDefault();
    void changePerformedOperationToSort();
    void setSelected(boolean val);
}
