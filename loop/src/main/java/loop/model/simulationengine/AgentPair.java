package loop.model.simulationengine;

import java.util.List;

/**
 * This interface represents a pair of agents
 * 
 * @author Peter Koepernik
 *
 */
public interface AgentPair {
    
    /**
     * Returns the first of both agents.
     * 
     * @return the first of both agents
     */
    Agent getFirstAgent();
    
    /**
     * Returns the second of both agents.
     * 
     * @return the second of both agents
     */
    Agent getSecondAgent();
    
    /**
     * Returns both agents as list.
     * 
     * @return both agents as list
     */
    List<Agent> getAgents();
}
