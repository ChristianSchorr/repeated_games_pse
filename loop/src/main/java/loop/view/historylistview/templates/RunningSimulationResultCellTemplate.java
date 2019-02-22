package loop.view.historylistview.templates;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import loop.model.simulator.SimulationResult;
import loop.model.simulator.SimulationStatus;

import java.io.IOException;
import java.time.Duration;

/**
 * This class manges the SimulationResultCellTemplate for running Simulations
 *
 * @author Christian Schorr
 */
public class RunningSimulationResultCellTemplate extends SimulationResultCellTemplate {

    private static final String FXML_NAME = "/view/listViewTemplates/runningSimulationCell.fxml";
    private static final String QUEUED_LABEL = "queued";
    private SimulationResult item;


    @FXML
    private Label progressLabel;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Label durationLeft;

    private ListView listView;

    public RunningSimulationResultCellTemplate(SimulationResult item, ListView listView) {
        this.item = item;
        this.listView = listView;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(FXML_NAME));
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void initialize() {
        super.initialize(item);
        double progress = (double) item.getFinishedIterations() / (double) item.getTotalIterations();
        progressBar.setProgress(progress);

        String progressText = QUEUED_LABEL;
        if (item.getStatus() == SimulationStatus.RUNNING)
            progressText = item.getFinishedIterations() + "/" + item.getTotalIterations();
        progressLabel.setText(progressText);

        if (item.getLastTimeUpdated() > 0) {
            double timeRun = (item.getLastTimeUpdated() - item.getStartTime());
            double timeLeft = (timeRun / progress) -timeRun;
            long updatedTimeLeft = (long)timeLeft - (System.currentTimeMillis() - item.getLastTimeUpdated());
            if (updatedTimeLeft < 0) updatedTimeLeft = (long)(2*timeLeft) - (System.currentTimeMillis() - item.getLastTimeUpdated());
            final Duration duration = Duration.ofMillis(updatedTimeLeft);
            durationLeft.setText(formatDuration(duration));
        }
    }

    private static String formatDuration(Duration duration) {
        long seconds = duration.getSeconds();
        long absSeconds = Math.abs(seconds);
        String positive = String.format(
                "%d:%02d:%02d",
                absSeconds / 3600,
                (absSeconds % 3600) / 60,
                absSeconds % 60);
        return seconds < 0 ? "-" + positive : positive;
    }
}
