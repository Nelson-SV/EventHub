package view.components.eventsPage.searchDataHandler;
import be.EventStatus;
import javafx.collections.ObservableList;
import view.components.listeners.DataHandler;
import view.components.main.Model;

public class EventSearchHandler  implements DataHandler<EventStatus> {

    private Model model;

    public EventSearchHandler(Model model) {
        this.model = model;
    }

    @Override
    public ObservableList<EventStatus> getResultData(String filter) {
        return model.getSearchEventsResults(filter);
    }

    @Override
    public void performSelectSearchOperation(int entityId) {
        model.performSearchEventSelectOperation(entityId);
    }

    @Override
    public void undoSearchOperation() {
       model.undoSearchOperation();
    }
}
