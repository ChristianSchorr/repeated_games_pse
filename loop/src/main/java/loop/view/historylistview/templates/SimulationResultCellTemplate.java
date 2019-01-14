package loop.view.historylistview.templates;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import loop.model.simulator.SimulationResult;
import loop.controller.ResultHistoryItem;

public abstract class SimulationResultCellTemplate {

    @FXML
    private HBox container;

    @FXML
    protected Label nameLabel;

    @FXML
    protected Label idLabel;

    protected void initialize(ResultHistoryItem item) {
        SimulationResult res = item.getResult();
        String gameName = res.getUserConfiguration().getGameName();
        int simulationId = res.getId();

        nameLabel.setText(gameName);
        idLabel.setText(String.format("%03d", simulationId));
    }

    public HBox getContainer() {
        return container;
    }
}
