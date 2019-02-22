package loop.view.historylistview.templates;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import loop.model.simulator.SimulationResult;


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

    protected void initialize(SimulationResult item) {
        String gameName = item.getUserConfiguration().getGameName();
        int simulationId = item.getId();

        gameNameLabel.setText(gameName);
        simulationIdLabel.setText(String.format("#%03d", simulationId));
    }


    public HBox getContainer() {
        return container;
    }
}