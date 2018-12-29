package loop.model.simulationengine;

import java.util.List;

import loop.model.simulationengine.distributions.DiscreteDistribution;
import loop.model.simulationengine.distributions.DiscreteUniformDistribution;
import loop.model.simulationengine.distributions.UniformFiniteDistribution;
import loop.model.simulationengine.strategies.PureStrategy;
import loop.model.simulationengine.strategies.Strategy;

/**
 * Provides some auxiliary methods helpful to test classes related to the simulation engine package, for example
 * sample initialisations of agents or simulation of a few rounds for a given set of agents.
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
    public static List<Agent> getStandardAgents(final int agentCount, final boolean mixedStrategies) {
        DiscreteDistribution capitalDistribution = new DiscreteUniformDistribution(0, 0);
        
        UniformFiniteDistribution<Strategy> strategyDistribution = new UniformFiniteDistribution<Strategy>();
        Strategy coop = new PureStrategy("", "", (AgentPair pair, SimulationHistory history) -> true);
        strategyDistribution.addObject(coop);
        
        EngineSegment segment = new EngineSegment(agentCount, -1, capitalDistribution, strategyDistribution);
        List<Agent> agents = new AgentInitialiser().initialiseAgents(segment, mixedStrategies);
        return agents;
    }
    
    /**
     * Creates a history, simulates a specified amount of rounds using the prisoners dilemma and
     * the random pair builder.
     * 
     * @param agents the agents the history shall be created for
     * @param rounds the amount of rounds that shall be simulated
     * @return the history of the simulated rounds
     */
    public static SimulationHistory getHistory(final List<Agent> agents, final int rounds) {
        SimulationHistory history = new SimulationHistoryTable();
        PairBuilder pairBuilder = new RandomPairBuilder();
        Game game = ConcreteGame.prisonersDilemma();
        
      //execute rounds
        for (int round = 0; round < rounds; round++) {
            List<AgentPair> agentPairs = pairBuilder.buildPairs(agents, history);
            for (AgentPair pair: agentPairs) {
                Agent p1 = pair.getFirstAgent();
                Agent p2 = pair.getSecondAgent();
                boolean p1Cooperates = p1.getStrategy().isCooperative(p1, p2, history);
                boolean p2Cooperates = p2.getStrategy().isCooperative(p2, p1, history);
                history.addResult(game.play(p1, p2, p1Cooperates, p2Cooperates));
            }
        }
        
        return history;
    }
    
}
