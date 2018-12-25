package loop.model.simulationengine;

import java.util.List;

/**
 * This interface represents an adaptation mechanism (germ.: “Adaptionsmechanismus”).
 * An implementation takes a ranked list of agents and adjusts the agents’ strategies.
 * 
 * @author Peter Koepernik
 *
 */
public interface StrategyAdjuster {
    
    /**
     * Adjusts the strategies of the given agents considering the simulation history of the
     * current adaption step.
     * 
     * @param agents the agents whose strategies shall be adapted, ordered by their success in
     * the current adaption step
     * @param history the history of the current adaption step
     */
    void adaptStrategies(final List<Agent> agents, final SimulationHistory history);
}
