package loop.model.simulator;

/**
 * The status of a Simulation that has been started on a Simulator
 */
public enum SimulationStatus {

    /**
     * The Simulation is currently queued in a Simulator
     */
    QUEUED,

    /**
     * The Simulation is currently running in a Simulator
     */
    RUNNING,

    /**
     * The Simulation is allready finished
     */
    FINISHED,

    /**
     * The Simulation has been canceled
     */
    CANCELED
}
