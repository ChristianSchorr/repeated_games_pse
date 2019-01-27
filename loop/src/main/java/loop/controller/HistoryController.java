package loop.controller;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import loop.model.UserConfiguration;
import loop.model.simulator.SimulationResult;
import loop.model.simulator.SimulationStatus;
import loop.view.historylistview.HistoryListCell;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * This controller manages the list of all running and finsihed simulations in the main win-
 * dow. It stores a list of the {@link SimulationResult} objects of all started simulations.
 *
 * @author Christian Schorr
 */
public class HistoryController {

    @FXML
    ListView<ResultHistoryItem> historyList;

    @FXML
    Parent outputView;

    @FXML
    OutputController outputViewController;

    private ResultHistoryItem selectedItem;
    private ObservableList<ResultHistoryItem> history;

    private List<Consumer<UserConfiguration>> configImportHandlers = new ArrayList<Consumer<UserConfiguration>>();

    /**
     * Initializes the history controller
     */
    public void initialize() {
        history = FXCollections.observableArrayList();
        historyList.setItems(history);

        historyList.getSelectionModel().selectedItemProperty().addListener((ChangeListener<ResultHistoryItem>)
                (observable, oldValue, newValue) -> {
                    selectedItem = newValue;
                    if (selectedItem != null)
                        Platform.runLater(() -> outputViewController.setDisplayedResult(selectedItem.getResult()));
                });
        historyList.setCellFactory(param -> new HistoryListCell());
        outputViewController.registerImportUserConfiguration(config -> configImportHandlers.forEach(c -> c.accept(config)));
    }

    /**
     * Adds a new simulation to the controller
     *
     * @param simulationResult the {@link SimulationResult} object of the simulation that shall be added
     */
    public void addSimulation(SimulationResult simulationResult) {
        ResultHistoryItem item = new ResultHistoryItem(simulationResult);
        item.getResult().registerSimulationStatusChangedHandler((res, stat) -> {
            Platform.runLater(() -> historyList.refresh());
            if (stat != SimulationStatus.FINISHED || selectedItem == null || res != selectedItem.getResult()) return;
            int index = historyList.getSelectionModel().getSelectedIndex();
            historyList.getSelectionModel().clearSelection();
            historyList.getSelectionModel().select(index);
        });
        item.getResult().registerIterationFinished((res, iter) -> Platform.runLater(() -> historyList.refresh()));
        history.add(item);
    }

    /**
     * Returns the currently selected simulation in the list
     *
     * @return the currently selected simulation
     */
    public SimulationResult getSelectedSimulation() {
        if (selectedItem == null) return null;
        return selectedItem.getResult();
    }

    /**
     * Returns all simulations in the simulation list
     *
     * @return a list of all simulations in the list
     */
    public List<SimulationResult> getAllSimulations() {
        return history.stream().map(ResultHistoryItem::getResult).collect(Collectors.toList());
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
}
