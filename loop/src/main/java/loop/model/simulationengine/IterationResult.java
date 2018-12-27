package loop.model.simulationengine;

import java.util.List;

/**
 * In an instance of this class, the results of a nished iteration are stored:
 * - the agents, sorted by final rank
 * - the {@link SimulationHistory} of the last adaption step
 * - whether an equilibrium was reached
 * - the efficiency of the final state
 * - the number of adaption steps performed
 * 
 * @author Peter Koepernik
 *
 */
public class IterationResult {
    
    private List<Agent> agents;
    private SimulationHistory history;
    private boolean equilibriumReached;
    private double efficiency;
    private int adapts;
    
    /**
     * Creates a new iteration result.
     * 
     * @param agents the agents, sorted by final rank
     * @param history the history of the last adaption step
     * @param equilibriumReached whether an equilibrium was reached
     * @param efficiency the efficiency of the final state
     * @param adapts the number of performed adaption steps
     */
    public IterationResult(List<Agent> agents, SimulationHistory history, boolean equilibriumReached,
            double efficiency, int adapts) {
        this.agents = agents;
        this.history = history;
        this.equilibriumReached = equilibriumReached;
        this.efficiency = efficiency;
        this.adapts = adapts;
    }
    
    /**
     * Returns a list of all agents, sorted by final rank.
     * 
     * @return a list of all agents, sorted by final rank
     */
    public List<Agent> getAgents() {
        return this.agents;
    }
    
    /**
     * Returns the {@link SimulationHistory} of the final adaption step.
     * 
     * @return the {@link SimulationHistory} of the final adaption step
     */
    public SimulationHistory getHistory() {
        return this.history;
    }
    
    /**
     * Returns, whether an equilibrium was reached.
     * 
     * @return {@code true}, if an equilibrium was reached, {@code false} otherwise.
     */
    public boolean equilibriumReached() {
        return this.equilibriumReached;
    }
    
    /**
     * Returns the efficiency of the final state.
     * 
     * @return the efficiency of the final state
     */
    public double getEfficiency() {
        return this.efficiency;
    }
    
    /**
     * Returns the amount of performed adaption steps.
     * 
     * @return the amount of performed adaption steps
     */
    public int getAdapts() {
        return this.adapts;
    }
}
