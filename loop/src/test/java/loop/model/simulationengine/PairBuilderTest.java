package loop.model.simulationengine;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
     * Tests the standard implementations of the {@link PairBuilder}, namely the {@link RandomPairBuilder},
     * the {@link CooperationConsideringPairBuilder} and the {@link RandomCooperationConsideringPairBuilder}. 
     */
    @Test
    public void testStandardImplementations() {
        //random pair builder
        PairBuilder randomPairBuilder = new RandomPairBuilder();
        testPairingValidOnEmptyHistory(randomPairBuilder);
        testPairingValidOnNonEmptyHistory(randomPairBuilder);
        
        //cooperation considering pair builder
        PairBuilder coopConsideringPairBuilder = new CooperationConsideringPairBuilder();
        testPairingValidOnEmptyHistory(coopConsideringPairBuilder);
        testPairingValidOnNonEmptyHistory(coopConsideringPairBuilder);
        
        //random cooperation considering pair builder
        double randomness = 0.5;
        PairBuilder randomCoopConsideringPairBuilder = new RandomCooperationConsideringPairBuilder(randomness);
        testPairingValidOnEmptyHistory(randomCoopConsideringPairBuilder);
        testPairingValidOnNonEmptyHistory(randomCoopConsideringPairBuilder);
    }
    
    private void testPairingValidOnEmptyHistory(PairBuilder pairBuilder) {
        //initialise agents
        int agentCount = 1000;
        List<Agent> agents = TestUtility.getStandardAgents(agentCount, false);
        
        //create pairs
        List<AgentPair> pairs = pairBuilder.buildPairs(agents, new SimulationHistoryTable());
        
        //test pairing
        Map<Agent, Boolean> agentsContained = new HashMap<Agent, Boolean>();
        for (Agent agent: agents) {
            agentsContained.put(agent, false);
        }
        
        for (AgentPair pair: pairs) {
            //agents paired twice?
            assertFalse(agentsContained.get(pair.getFirstAgent()));
            assertFalse(agentsContained.get(pair.getSecondAgent()));
            
            agentsContained.put(pair.getFirstAgent(), true);
            agentsContained.put(pair.getSecondAgent(), true);
        }
        
        //every agent paired at least once?
        for (Agent agent: agents) {
            assertTrue(agentsContained.get(agent));
        }
    }
    
    private void testPairingValidOnNonEmptyHistory(PairBuilder pairBuilder) {
        //initialise agents
        int agentCount = 1000;
        int rounds = 100;
        List<Agent> agents = TestUtility.getStandardAgents(agentCount, false);
        SimulationHistory history = TestUtility.getHistory(agents, rounds);
        
        //create pairs
        List<AgentPair> pairs = pairBuilder.buildPairs(agents, history);
        
        //test pairing
        Map<Agent, Boolean> agentsContained = new HashMap<Agent, Boolean>();
        for (Agent agent: agents) {
            agentsContained.put(agent, false);
        }
        
        for (AgentPair pair: pairs) {
            //agents paired twice?
            assertFalse(agentsContained.get(pair.getFirstAgent()));
            assertFalse(agentsContained.get(pair.getSecondAgent()));
            
            agentsContained.put(pair.getFirstAgent(), true);
            agentsContained.put(pair.getSecondAgent(), true);
        }
        
        //every agent paired at least once?
        for (Agent agent: agents) {
            assertTrue(agentsContained.get(agent));
        }
    }
}
