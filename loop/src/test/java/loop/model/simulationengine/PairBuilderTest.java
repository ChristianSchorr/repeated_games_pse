package loop.model.simulationengine;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import loop.model.simulationengine.distributions.DiscreteDistribution;
import loop.model.simulationengine.distributions.DiscreteUniformDistribution;
import loop.model.simulationengine.distributions.UniformFiniteDistribution;
import loop.model.simulationengine.strategies.PureStrategy;
import loop.model.simulationengine.strategies.Strategy;

/**
 * This class holds tests for implementations of the {@link PairBuilder} interface.
 * 
 * @author Peter Koepernik
 *
 */
public class PairBuilderTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }
    
    /**
     * Tests the {@link RandomPairBuilder}
     */
    @Test
    public void testRandomPairBuilder() {
        PairBuilder randomPairBuilder = new RandomPairBuilder();
        testPairBuilder(randomPairBuilder);
    }
    
    private void testPairBuilder(PairBuilder pairBuilder) {
        //initialise agents
        int agentCount = 1000;
        DiscreteDistribution capitalDistribution = new DiscreteUniformDistribution(0, 10);
        UniformFiniteDistribution<Strategy> strategyDistribution = new UniformFiniteDistribution<Strategy>();
        Strategy coop = new PureStrategy("", "", (AgentPair pair, SimulationHistory history) -> true);
        strategyDistribution.addObject(coop);
        EngineSegment segment = new EngineSegment(agentCount, -1, capitalDistribution, strategyDistribution);
        List<Agent> agents = new AgentInitialiser().initialiseAgents(segment, false);
        
        //create pairs
        List<AgentPair> pairs = pairBuilder.buildPairs(agents, new SimulationHistoryTable());
        
      //test pairing
        Map<Agent, Boolean> agentsContained = new HashMap<Agent, Boolean>();
        for (Agent agent: agents) {
            agentsContained.put(agent, false);
        }
        
        for (AgentPair pair: pairs) {
            //agents paired twice?
            if (agentsContained.get(pair.getFirstAgent()) == true)
                fail("Agent paired twice.");
            if (agentsContained.get(pair.getSecondAgent()) == true)
                fail("Agent paired twice.");
            
            agentsContained.put(pair.getFirstAgent(), true);
            agentsContained.put(pair.getSecondAgent(), true);
        }
        
        //every agent paired at least once?
        for (Agent agent: agents) {
            if (agentsContained.get(agent) == false)
                fail("Agent not paired.");
        }
    }
}
