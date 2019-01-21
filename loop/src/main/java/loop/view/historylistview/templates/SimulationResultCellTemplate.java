package loop.view.historylistview.templates;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import loop.model.simulator.SimulationResult;
import loop.controller.ResultHistoryItem;

/**
 * This class manges how simulations are displayed in the history list view
 *
 * @author Christian Schorr
 */
public abstract class SimulationResultCellTemplate {

    @FXML
    private HBox container;

    @FXML
    private Label gameNameLabel;

    @FXML
    private Label simulationIdLabel;

    protected void initialize(ResultHistoryItem item) {
        SimulationResult res = item.getResult();
        String gameName = res.getUserConfiguration().getGameName();
        int simulationId = res.getId();

        gameNameLabel.setText(gameName);
        simulationIdLabel.setText(String.format("#%03d", simulationId));
    }

    public HBox getContainer() {
        return container;
    }
}
