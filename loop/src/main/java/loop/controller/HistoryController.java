package loop.controller;

import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import loop.model.simulator.SimulationResult;
import loop.view.historylistview.HistoryListCell;
import loop.view.historylistview.RefreshSkin;

import java.io.IOException;
import java.util.List;
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

    private RefreshSkin<ResultHistoryItem> listViewSkin;

    /**
     * Initializes the history controller
     */
    public void initialize() {
        history = FXCollections.observableArrayList();
        historyList.setItems(history);
        historyList.getSelectionModel().selectedItemProperty().addListener((ChangeListener<ResultHistoryItem>)
                (observable, oldValue, newValue) -> {
            selectedItem = newValue;
            outputViewController.setDisplayedResult(selectedItem.getResult());
        });
        historyList.setCellFactory(param -> new HistoryListCell());

        //listViewSkin = new RefreshSkin<>(historyList);
        //historyList.setSkin(listViewSkin);
    }

    /**
     * Adds a new simulation to the controller
     * @param simulationResult the {@link SimulationResult} object of the simulation that shall be added
     */
    public void addSimulation(SimulationResult simulationResult) {
        ResultHistoryItem item = new ResultHistoryItem(simulationResult);
        history.add(item);
        item.getResult().registerSimulationStatusChangedHandler((res, stat)-> historyList.refresh());
    }

    /**
     * Returns the currently selected simulation in the list
     * @return the currently selected simulation
     */
    public SimulationResult getSelectedSimulation() {
        if (selectedItem == null) return null;
        return selectedItem.getResult();
    }

    /**
     * Returns all simulations in the simulation list
     * @return a list of all simulations in the list
     */
    public List<SimulationResult> getAllSimulations() {
        return history.stream().map(ResultHistoryItem::getResult).collect(Collectors.toList());
    }
}
