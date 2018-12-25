package loop.model.simulationengine;

import java.util.List;

/**
 * This interface represents a pairing algorithm (ger.: “Paarbildungsalgorithmus”). An implementation
 * takes a set of agents and the history of the current adaptation step as {@link SimulationHistory}
 * and returns a matching of the agents.
 * 
 * @author Peter Koepernik
 *
 */
public interface PairBuilder {
    
    /**
     * Builds pairs of the given agents and returns them as {@code List<AgentPair>}.
     * 
     * @param agents the agents that shall be paired
     * @param history the history of the current adaption step
     * @return the built pairs
     */
    List<AgentPair> buildPairs(final List<Agent> agents, final SimulationHistory history);
}
