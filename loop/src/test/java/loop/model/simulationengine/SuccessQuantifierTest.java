package loop.model.simulationengine;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests implementations of the {@link SuccessQuantifier} interface.
 * 
 * @author Peter Koepernik
 *
 */
public class SuccessQuantifierTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }
    
    /**
     * Tests the standard implementations of the {@link SuccessQuantifier} interface, namely
     * {@link TotalCapital}, {@link TotalPayoff}, {@link PayoffInLastAdapt} and {@link SlidingMean}.
     */
    @Test
    public void testStandardImplementations() {
        //total capital
        SuccessQuantifier totalCapital = new TotalCapital();
        testRankingValidOnEmptyHistory(totalCapital);
        testRankingValidOnNonEmptyHistory(totalCapital);
        
        //total payoff
        SuccessQuantifier totalPayoff = new TotalPayoff();
        testRankingValidOnEmptyHistory(totalPayoff);
        testRankingValidOnNonEmptyHistory(totalPayoff);
        
        //payoff in last adapt
        SuccessQuantifier payoffInLastAdapt = new PayoffInLastAdapt();
        testRankingValidOnEmptyHistory(payoffInLastAdapt);
        testRankingValidOnNonEmptyHistory(payoffInLastAdapt);
        
        //sliding mean
        int w = 10;
        SuccessQuantifier slidingMean = new SlidingMean(w);
        testRankingValidOnEmptyHistory(slidingMean);
        testRankingValidOnNonEmptyHistory(slidingMean);
    }
    
    private void testRankingValidOnEmptyHistory(SuccessQuantifier successQuantifier) {
        //initialise agents
        int agentCount = 1000;
        List<Agent> agents = TestUtility.getStandardAgents(agentCount);
        
        //create ranking
        List<Agent> rankedAgents = successQuantifier.createRanking(agents, new SimulationHistoryTable());
        
        //test ranking
        assertTrue(agents.size() == rankedAgents.size());
        
        for (Agent agent: agents) {
            assertTrue(rankedAgents.contains(agent));
        }
    }
    
    private void testRankingValidOnNonEmptyHistory(SuccessQuantifier successQuantifier) {
        //initialise agents
        int agentCount = 1000;
        int rounds = 100;
        List<Agent> agents = TestUtility.getStandardAgents(agentCount);
        SimulationHistory history = TestUtility.getHistory(agents, rounds);
        
        //create ranking
        List<Agent> rankedAgents = successQuantifier.createRanking(agents, history);
        
        //test ranking
        assertTrue(agents.size() == rankedAgents.size());
        
        for (Agent agent: agents) {
            assertTrue(rankedAgents.contains(agent));
        }
    }
}
