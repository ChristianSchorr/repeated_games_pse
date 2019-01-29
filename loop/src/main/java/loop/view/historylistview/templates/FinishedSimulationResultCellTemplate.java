package loop.view.historylistview.templates;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import loop.controller.ResultHistoryItem;
import loop.model.simulator.SimulationResult;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;

/**
 * This class manges the SimulationResultCellTemplate for finished Simulations
 *
 * @author Christian Schorr
 */
public class FinishedSimulationResultCellTemplate extends SimulationResultCellTemplate {

    private static final String FXML_NAME = "/view/listViewTemplates/finishedSimulationCell.fxml";
    private SimulationResult item;

    private DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

    @FXML
    private Label totalDurationLabel;

    @FXML
    private Label finishedTimeLabel;

    public FinishedSimulationResultCellTemplate(SimulationResult item) {
        this.item = item;
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

        final Duration duration = Duration.ofMillis(item.getFinishTime() - item.getStartTime());
        totalDurationLabel.setText(formatDuration(duration));
        finishedTimeLabel.setText(timeFormat.format(item.getFinishTime()));
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
