package loop.model.simulationengine.strategies;

import loop.model.simulationengine.Agent;
import loop.model.simulationengine.AgentPair;

/**
 * An implementation of the {@link AgentPair} interface.
 * 
 * @author Peter Koepernik
 *
 */
public class ConcreteAgentPair implements AgentPair {
    
    private Agent firstAgent;
    private Agent secondAgent;
    
    /**
     * Creates a new agent pair with the given agents.
     * 
     * @param firstAgent the first agent
     * @param secondAgent the second agent
     */
    public ConcreteAgentPair(final Agent firstAgent, final Agent secondAgent) {
        this.firstAgent = firstAgent;
        this.secondAgent = secondAgent;
    }
    
    @Override
    public Agent getFirstAgent() {
        return this.firstAgent;
    }

    @Override
    public Agent getSecondAgent() {
        return this.secondAgent;
    }

}
