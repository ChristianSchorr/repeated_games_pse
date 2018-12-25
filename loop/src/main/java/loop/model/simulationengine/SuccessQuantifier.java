package loop.model.simulationengine;

import java.util.List;

/**
 * This interface represents a success quantification (ger.: “Erfolgsquantifizierung”). An implementation
 * takes a set of agents as well as the history of the current adaptation step as {@code SimulationHistory}
 * and creates a ranking of the agents based on their success in the past rounds.
 * 
 * @author Peter Koepernik
 *
 */
public interface SuccessQuantifier {
    
    /**
     * Creates and returns a ranking of the given agents.
     * 
     * @param agents the agents that shall be ranked
     * @param history the history of the current adaption step
     * @return the created ranking
     */
    List<Agent> createRanking(final List<Agent> agents, final SimulationHistory history);
}
