package loop.model.simulationengine;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public List<Agent> getAgents() {
        List<Agent> agents = new ArrayList<Agent>();
        agents.add(this.firstAgent);
        agents.add(this.secondAgent);
        return agents;
    }

}
