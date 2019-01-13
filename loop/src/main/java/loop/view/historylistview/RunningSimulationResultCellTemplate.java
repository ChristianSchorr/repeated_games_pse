package loop.view.historylistview;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import loop.model.simulator.SimulationResult;

public class RunningSimulationResultCellTemplate extends SimulationResultCellTemplate {

    @FXML
    private Label progressLabel;

    @FXML
    private ProgressBar progressBar;


    public RunningSimulationResultCellTemplate(SimulationResult res) {
        this.initialize(res);
    }
}
