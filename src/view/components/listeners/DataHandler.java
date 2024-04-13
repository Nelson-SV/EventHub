package view.components.listeners;

import javafx.collections.ObservableList;

import java.util.List;

public interface DataHandler<T> {
    ObservableList<T> getResultData();
    void performSearchOperation();
    void undoSearchOperation();
}
