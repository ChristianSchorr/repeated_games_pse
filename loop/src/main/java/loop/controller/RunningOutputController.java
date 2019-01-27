package loop.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.GridPane;
import loop.model.simulator.SimulationResult;
import loop.model.simulator.SimulationStatus;

import java.time.Duration;
import java.util.Timer;
import java.util.TimerTask;


/**
 * This class is a controller for displaying meta-data of a running simulation
 */
public class RunningOutputController {

    @FXML
    private Node container;

    @FXML
    private Label gameNameLabel;

    @FXML
    private Label gameIdLabel;

    @FXML
    private Button cancleButton;

    @FXML
    private Label dotLabel;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Label progressLabel;

    @FXML
    private Label durationLeft;

    private ResultHistoryItem item;
    private SimulationResult result;

    /**
     * Creates a new output controller for a running simulation
     * @param resultItem the running simulation to display
     */
    public RunningOutputController(ResultHistoryItem resultItem) {
        this.result = resultItem.getResult();
        this.item = resultItem;
    }

    @FXML
    private void initialize() {
        gameNameLabel.setText(result.getUserConfiguration().getGameName());
        gameIdLabel.setText(String.format("#%03d", result.getId()));
        cancleButton.setOnAction((e) -> cancleSimulation());

        if (result.getStatus() == SimulationStatus.CANCELED) return;
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            int dots = 0;

            @Override
            public void run() {
                dots++;
                String text = "";
                for(int i = 0; i < dots % 4; i++)
                    text += ".";
                final String lableText = text;
                Platform.runLater(() -> dotLabel.setText(lableText));
                Platform.runLater(() -> updateDuration());
            }
        }, 1000, 1000);

        updateProgress();
        result.registerIterationFinished((res, iter) ->  Platform.runLater(() -> updateProgress()));
    }

    private void updateProgress() {
        double progress = (double) result.getFinishedIterations() / (double) result.getTotalIterations();
        progressBar.setProgress(progress);
        progressLabel.setText(result.getFinishedIterations() + "/" + result.getTotalIterations());
    }

    private void updateDuration() {
        if (item.getLastTimeUpdated() > 0) {
            double progress = (double) result.getFinishedIterations() / (double) result.getTotalIterations();
            double timeRun = (item.getLastTimeUpdated() - item.getStartTime());
            double timeLeft = (timeRun / progress) -timeRun;
            final Duration duration = Duration.ofMillis((long)timeLeft - (System.currentTimeMillis() - item.getLastTimeUpdated()));
            durationLeft.setText(formatDuration(duration));
        }
    }

    @FXML
    private void cancleSimulation() {
        item.cancleSimulation();
    }

    /**
     * Returns the root Node of this controller's view
     * @return the root Node of the view
     */
    public Node getContainer() {
        return container;
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
