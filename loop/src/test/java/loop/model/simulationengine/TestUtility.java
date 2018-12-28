package loop.model.simulationengine;

import java.util.List;

import loop.model.simulationengine.distributions.DiscreteDistribution;
import loop.model.simulationengine.distributions.DiscreteUniformDistribution;
import loop.model.simulationengine.distributions.UniformFiniteDistribution;
import loop.model.simulationengine.strategies.PureStrategy;
import loop.model.simulationengine.strategies.Strategy;

/**
 * Provides some auxiliary methods helpful to test classes related to the simulation engine package, for example
 * sample initialisations of agents.
 * 
 * @author Peter Koepernik
 *
 */
public class TestUtility {
    
    /**
     * Returns a list of agents initialised with the 'always cooperate' strategy and zero capital.
     * 
     * @param agentCount the amount of agents to be created
     * @return a list of the initialised agents
     */
    public static List<Agent> getStandardAgents(final int agentCount) {
        DiscreteDistribution capitalDistribution = new DiscreteUniformDistribution(0, 0);
        
        UniformFiniteDistribution<Strategy> strategyDistribution = new UniformFiniteDistribution<Strategy>();
        Strategy coop = new PureStrategy("", "", (AgentPair pair, SimulationHistory history) -> true);
        strategyDistribution.addObject(coop);
        
        EngineSegment segment = new EngineSegment(agentCount, -1, capitalDistribution, strategyDistribution);
        List<Agent> agents = new AgentInitialiser().initialiseAgents(segment, false);
        return agents;
    }
}
