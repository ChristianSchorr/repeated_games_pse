package loop.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.GridPane;
import loop.model.simulator.SimulationResult;

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

    private SimulationResult result;

    /**
     * Creates a new output controller for a running simulation
     * @param result the running simulation to display
     */
    public RunningOutputController(SimulationResult result) {
        this.result = result;
    }

    @FXML
    private void initialize() {
        gameNameLabel.setText(result.getUserConfiguration().getGameName());
        gameIdLabel.setText(String.format("%d", result.getId()));
        cancleButton.setOnAction((e) -> cancleSimulation());

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
            }
        }, 500, 1000);

        updateProgress();
        result.registerIterationFinished((res, iter) ->  Platform.runLater(() -> updateProgress()));
    }

    private void updateProgress() {
        double progress = (double) result.getFinishedIterations() / (double) result.getTotalIterations();
        progressBar.setProgress(progress);
        progressLabel.setText(result.getFinishedIterations() + "/" + result.getTotalIterations());
    }

    @FXML
    private void cancleSimulation() {
        System.out.println("CANCELED");
    }

    /**
     * Returns the root Node of this controller's view
     * @return the root Node of the view
     */
    public Node getContainer() {
        return container;
    }
}
