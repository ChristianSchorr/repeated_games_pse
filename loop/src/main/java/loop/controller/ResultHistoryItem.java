package loop.controller;


import loop.model.simulator.SimulationResult;
import loop.model.simulator.SimulationStatus;

import java.util.function.Consumer;

public class ResultHistoryItem {

    private SimulationResult result;

    private long startTime = -1;
    private long finishTime = -1;
    private long lastTimeUpdated = -1;

    private Consumer<ResultHistoryItem> cancleHandler;

    /**
     * Creates a new Instance of this class with a given {@link SimulationResult}
     * @param result the {@link SimulationResult} to store in the history
     */
    public ResultHistoryItem(SimulationResult result, Consumer<ResultHistoryItem> handler) {
        this.result = result;
        statusChanged(result.getStatus());
        result.registerSimulationStatusChangedHandler((res, stat) -> statusChanged(stat));
        result.registerIterationFinished((res, iter) -> lastTimeUpdated = System.currentTimeMillis());
        this.cancleHandler = handler;
    }

    public void cancleSimulation() {
        if (result.getStatus().equals(SimulationStatus.RUNNING) || result.getStatus().equals(SimulationStatus.QUEUED))
           cancleHandler.accept(this);
    }

    /**
     * Returns the SimulationResult of this item
     * @return the SimulationResult of this item
     */
    public SimulationResult getResult() {
        return result;
    }

    /**
     * Returns the start time of this Simulation
     * @return the start time of the Simulation or -1 when it hasn't started yet
     */
    public long getStartTime() {
        return startTime;
    }

    /**
     * Returns the finish time of this Simulation     *
     * @return the finish time of this Simulation or -1 when it hasn't finished yet
     */
    public long getFinishTime() {
        return finishTime;
    }

    /**
     * Returns the last time an iteration result has been added to this Simulation Result
     * @return the last update time
     */
    public long getLastTimeUpdated() {
        return lastTimeUpdated;
    }

    private void statusChanged(SimulationStatus newStatus) {
        if (newStatus == SimulationStatus.RUNNING) {
            startTime = System.currentTimeMillis();
        }
        else if (newStatus == SimulationStatus.FINISHED) {
            finishTime = System.currentTimeMillis();
        }
    }
}