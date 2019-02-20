package loop.model.simulationengine;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

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
public class IterationResult implements Serializable {
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    private boolean equilibriumReached;
    private double efficiency;
    private int adapts;
    private List<String> strategyNames;
    
    //strategy chart    
    private List<double[]> strategyPortions;
    
    //capital chart
    Map<String, List<Integer>> groupCapitals;
    
    
    /**
     * Creates a new iteration result.
     * 
     * @param agents the agents, sorted by final rank
     * @param history the history of the last adaption step
     * @param equilibriumReached whether an equilibrium was reached
     * @param efficiency the efficiency of the final state
     * @param adapts the number of performed adaption steps
     */
    public IterationResult(boolean equilibriumReached, double efficiency, int adapts, List<String> strategyNames, List<double[]> strategyPortions,
            Map<String, List<Integer>> groupCapitals) {
        this.equilibriumReached = equilibriumReached;
        this.efficiency = efficiency;
        this.adapts = adapts;
        this.strategyPortions = strategyPortions;
        this.strategyNames = strategyNames;
        this.groupCapitals = groupCapitals;
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
    
    /**
     * Return the portions of all strategies throughout all adaption steps.
     * 
     * @return the portions of all strategies throughout all adaption steps
     */
    public List<double[]> getStrategyPortions() {
        return this.strategyPortions;
    }
    
    /**
     * Returns the names of all pure strategies that were available in this iteration, in the same
     * order as their portions are stored in the arrays returned by {@link #getStrategyPortions()}.
     * 
     * @return the names of all strategies
     */
    public List<String> getStrategyNames() {
        return this.strategyNames;
    }
    
    /**
     * Returns the capitals of all agents, partitioned into their groups.
     * 
     * @return the capitals of all agents
     */
    public Map<String, List<Integer>> getGroupCapitals() {
        return this.groupCapitals;
    }
}
