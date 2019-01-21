package loop.view.historylistview.templates;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import loop.controller.ResultHistoryItem;
import loop.model.simulator.SimulationResult;
import loop.model.simulator.SimulationStatus;

import java.io.IOException;

/**
 * This class manges the SimulationResultCellTemplate for running Simulations
 *
 * @author Christian Schorr
 */
public class RunningSimulationResultCellTemplate extends SimulationResultCellTemplate {

    private static final String FXML_NAME = "/view/listViewTemplates/runningSimulationCell.fxml";
    private static final String QUEUED_LABEL = "eingereiht";
    private ResultHistoryItem item;


    @FXML
    private Label progressLabel;

    @FXML
    private ProgressBar progressBar;


    public RunningSimulationResultCellTemplate(ResultHistoryItem item) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(FXML_NAME));
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.item = item;
    }

    public void initialize() {
        if (item == null) return;
        super.initialize(item);

        SimulationResult result = item.getResult();
        double progress = (double) result.getFinishedIterations() / (double) result.getTotalIterations();
        progressBar.setProgress(progress);

        String progressText = QUEUED_LABEL;
        if (result.getStatus() == SimulationStatus.RUNNING)
            progressText = result.getFinishedIterations() + "/" + result.getTotalIterations();
        progressLabel.setText(progressText);
    }
}
