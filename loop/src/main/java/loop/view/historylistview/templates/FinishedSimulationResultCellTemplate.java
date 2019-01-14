package loop.view.historylistview.templates;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import loop.controller.ResultHistoryItem;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class FinishedSimulationResultCellTemplate extends SimulationResultCellTemplate {

    private static final String FXML_NAME = "finishedSimulationCell.fxml";
    private ResultHistoryItem item;

    private DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

    @FXML
    private Label totalDurationLabel;

    @FXML
    private Label finishedTimeLabel;

    public FinishedSimulationResultCellTemplate(ResultHistoryItem item) {
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
        super.initialize(item);

        final long duration = item.getFinishTime() - item.getStartTime();
        totalDurationLabel.setText(timeFormat.format(duration));
        finishedTimeLabel.setText(timeFormat.format(item.getFinishTime()));
    }


}
