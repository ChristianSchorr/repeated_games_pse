package loop.controller;


import loop.model.simulator.SimulationResult;
import loop.model.simulator.SimulationStatus;

public class ResultHistoryItem {

    private SimulationResult result;
    private long startTime = -1;
    private long finishTime = -1;

    /**
     * Creates a new Instance of this class with a given {@link SimulationResult}
     * @param result the {@link SimulationResult} to store in the history
     */
    public ResultHistoryItem(SimulationResult result) {
        this.result = result;
        result.registerSimulationStatusChangedHandler((res, stat) -> statusChanged(stat));
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

    private void statusChanged(SimulationStatus newStatus) {
        if (newStatus == SimulationStatus.RUNNING)
            startTime = System.currentTimeMillis();
        else if (newStatus == SimulationStatus.FINISHED)
            finishTime = System.currentTimeMillis();
    }
}