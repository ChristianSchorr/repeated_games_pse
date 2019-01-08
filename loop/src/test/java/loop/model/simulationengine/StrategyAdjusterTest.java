package loop.model.simulationengine;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import loop.model.simulationengine.strategies.MixedStrategy;

/**
 * Tests implementations of the {@link StrategyAdjuster} interface.
 * 
 * @author Peter Koepernik
 *
 */
public class StrategyAdjusterTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }
    
    /**
     * Tests standard implementations of the {@link StrategyAdjuster} interface, namely
     * {@link ReplicatorDynamic} and {@link PreferentialAdaption}.
     */
    @Test
    public void testStandardImplementations() {
        //replicator dynamic
        for (double alpha = 0.0; alpha <= 1.0; alpha += 0.2) {
            for (double beta = 0.0; beta <= 1.0; beta += 0.2) {
                StrategyAdjuster replicatorDynamic = new ReplicatorDynamic(alpha, beta);
                testStrategiesValidOnEmptyHistory(replicatorDynamic);
                testStrategiesValidOnNonEmptyHistory(replicatorDynamic);
            }
            //System.out.println("Replicator Dynamic: alpha = " + alpha + " done.");
        }
        
      //preferential adaption
        for (double alpha = 0.0; alpha <= 1.0; alpha += 0.2) {
            for (double beta = 0.0; beta <= 1.0; beta += 0.2) {
                StrategyAdjuster preferentialAdaption = new PreferentialAdaption(alpha, beta);
                testStrategiesValidOnEmptyHistory(preferentialAdaption);
                testStrategiesValidOnNonEmptyHistory(preferentialAdaption);
            }
            //System.out.println("Preferential Adaption: alpha = " + alpha + " done.");
        }        
    }
    
    private void testStrategiesValidOnEmptyHistory(StrategyAdjuster strategyAdjuster) {
        //initialise agents
        int agentCount = 1000;
        List<Agent> agentsPure = TestUtility.getStandardAgents(agentCount, false);
        List<Agent> agentsMixed = TestUtility.getStandardAgents(agentCount, true);
        
        //adjust strategies
        strategyAdjuster.adaptStrategies(agentsPure, new SimulationHistoryTable());
        strategyAdjuster.adaptStrategies(agentsMixed, new SimulationHistoryTable());
        
        //test whether strategies are valid
        for (Agent agent: agentsPure) {
            assertTrue(agent.getStrategy() != null);
            assertFalse(agent.getStrategy() instanceof MixedStrategy);
        }
        
        for (Agent agent: agentsMixed) {
            assertTrue(agent.getStrategy() != null);
            assertTrue(agent.getStrategy() instanceof MixedStrategy);
        }
    }
    
    private void testStrategiesValidOnNonEmptyHistory(StrategyAdjuster strategyAdjuster) {
        //initialise agents
        int agentCount = 1000;
        int rounds = 100;
        List<Agent> agentsPure = TestUtility.getStandardAgents(agentCount, false);
        List<Agent> agentsMixed = TestUtility.getStandardAgents(agentCount, true);
        SimulationHistory historyPure = TestUtility.getHistory(agentsPure, rounds);
        SimulationHistory historyMixed = TestUtility.getHistory(agentsMixed, rounds);
        
        //adjust strategies
        strategyAdjuster.adaptStrategies(agentsPure, historyPure);
        strategyAdjuster.adaptStrategies(agentsMixed, historyMixed);
        
        //test whether strategies are valid
        for (Agent agent: agentsPure) {
            assertTrue(agent.getStrategy() != null);
            assertFalse(agent.getStrategy() instanceof MixedStrategy);
        }
        
        for (Agent agent: agentsMixed) {
            assertTrue(agent.getStrategy() != null);
            assertTrue(agent.getStrategy() instanceof MixedStrategy);
        }
    }

}
