package loop.view.historylistview;

import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import loop.controller.ResultHistoryItem;
import loop.model.simulator.SimulationStatus;
import loop.view.historylistview.templates.CanceledSimulationResultCellTemplate;
import loop.view.historylistview.templates.FinishedSimulationResultCellTemplate;
import loop.view.historylistview.templates.RunningSimulationResultCellTemplate;
import loop.view.historylistview.templates.SimulationResultCellTemplate;

public class HistoryListCell extends ListCell<ResultHistoryItem> {

    @Override
    protected void updateItem(ResultHistoryItem item, boolean empty) {
        super.updateItem(item, empty);
        setText(null);
        setGraphic(null);
        if(item != null && !empty) {
            item.getResult().registerSimulationStatusChangedHandler((i, s) -> getListView().refresh());
            HBox container = getTemplate(item).getContainer();
            setGraphic(container);
        }
    }

    private SimulationResultCellTemplate getTemplate(ResultHistoryItem item) {
        if (item.getResult().getStatus() == SimulationStatus.CANCELED)
            return new CanceledSimulationResultCellTemplate(item);
        else if (item.getResult().getStatus() == SimulationStatus.FINISHED)
            return new FinishedSimulationResultCellTemplate(item);
        else if (item.getResult().getStatus() == SimulationStatus.RUNNING)
            return new RunningSimulationResultCellTemplate(item, getListView());
        else return new RunningSimulationResultCellTemplate(item, getListView());
    }
}

