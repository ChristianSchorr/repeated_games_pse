package loop.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import loop.LoopSettings;
import loop.model.UserConfiguration;
import loop.model.simulator.SimulationResult;
import loop.model.simulator.SimulationStatus;
import loop.view.historylistview.HistoryListCell;
import org.controlsfx.control.Notifications;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

/**
 * This controller manages the list of all running and finsihed simulations in the main win-
 * dow. It stores a list of the {@link SimulationResult} objects of all started simulations.
 *
 * @author Christian Schorr
 */
public class HistoryController {

    @FXML
    ListView<SimulationResult> historyList;

    @FXML
    Parent outputView;

    @FXML
    OutputController outputViewController;

    private SimulationResult selectedItem;
    private ObservableList<SimulationResult> history;

    private List<Consumer<UserConfiguration>> configImportHandlers = new ArrayList<Consumer<UserConfiguration>>();
    private List<Consumer<SimulationResult>> cancleHandlers = new ArrayList<>();

    private MediaPlayer mediaPlayer;
    private final static String soundFile = "/notificationSound.mp3";

    /**
     * Initializes the history controller
     */
    public void initialize() {

        Media sound = new Media(this.getClass().getResource(soundFile).toExternalForm());
        mediaPlayer = new MediaPlayer(sound);

        history = FXCollections.observableArrayList();
        historyList.setItems(history);

        historyList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selectedItem = newValue;
            if (selectedItem != null)
                outputViewController.setDisplayedResult(selectedItem, (i) -> {
                    for (Consumer<SimulationResult> handler : cancleHandlers) handler.accept(i);
                }, res -> {
                    history.remove(res);
                    if (history.isEmpty())
                        outputViewController.setDisplayedResult(null, null, null);
                });
        });
        historyList.setCellFactory(param -> new HistoryListCell());
        outputViewController.registerImportUserConfiguration(config -> configImportHandlers.forEach(c -> c.accept(config)));
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> historyList.refresh());
            }
        }, 1000, 1000);
    }

    /**
     * Adds a new simulation to the controller
     *
     * @param simulationResult the {@link SimulationResult} object of the simulation that shall be added
     */
    public void addSimulation(SimulationResult simulationResult) {
        simulationResult.registerSimulationStatusChangedHandler((res, stat) -> {
            if (!(selectedItem == null || res != selectedItem)) {
                Platform.runLater(() -> outputViewController.setDisplayedResult(selectedItem, (i) -> {
                    for (Consumer<SimulationResult> handler : cancleHandlers) handler.accept(i);
                }, result -> {
                    history.remove(result);
                    if (history.isEmpty())
                        outputViewController.setDisplayedResult(null, null, null);
                }));
            }
            Platform.runLater(() -> historyList.refresh());
            if (res.getStatus() == SimulationStatus.FINISHED && LoopSettings.getInstance().isEnable_notification())
                showFinishNotification(simulationResult);
        });
        simulationResult.registerIterationFinished((res, iter) -> Platform.runLater(() -> historyList.refresh()));
        history.add(simulationResult);
    }

    private void showFinishNotification(SimulationResult result) {
        NotificationController template = new NotificationController(result);
        HBox container = template.getContainer();
        String css = this.getClass().getResource("/view/style.css").toExternalForm();
        container.getStylesheets().add(css);
        Platform.runLater(() -> {
            Notifications.create()
                    .hideCloseButton()
                    .position(Pos.TOP_RIGHT)
                    .graphic(container)
                    .hideAfter(new Duration(3000))
                    .show();

            mediaPlayer.seek(new Duration(0));
            mediaPlayer.play();
        });
    }

    /**
     * Returns the currently selected simulation in the list
     *
     * @return the currently selected simulation
     */
    public SimulationResult getSelectedSimulation() {
        if (selectedItem == null) return null;
        return selectedItem;
    }

    /**
     * Returns all simulations in the simulation list
     *
     * @return a list of all simulations in the list
     */
    public List<SimulationResult> getAllSimulations() {
        return history;
    }

    /**
     * Register an action that will be executed whenever a user configuration shall be imported
     * from a simulation result.
     *
     * @param action the action
     */
    public void registerImportUserConfiguration(Consumer<UserConfiguration> action) {
        configImportHandlers.add(action);
    }

    public void registerCancleRequestHandler(Consumer<SimulationResult> handler) {
        cancleHandlers.add(handler);
    }
}
