package loop.model.simulationengine;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import loop.model.simulationengine.strategies.PureStrategy;

public class SimulationHistoryMapTest {
    private Map<Agent, List<GameResult>> map = new HashMap<>();
    private List<GameResult> allResults = new LinkedList<>();
    private SimulationHistoryMap simulationHistoryMap = new SimulationHistoryMap();
    private Agent player1 = new Agent(0, PureStrategy.alwaysCooperate(), 1);
    private Agent player2 = new Agent(0, PureStrategy.titForTat(), 1);
    private Agent player3 = new Agent(0, PureStrategy.grim(), 2);
    
    @Before
    public void setUp() throws Exception {
        simulationHistoryMap.addResult(new GameResult(player1, player2, true, true, 2, 2));
        simulationHistoryMap.addResult(new GameResult(player1, player3, true, false, 2, 1));
        simulationHistoryMap.addResult(new GameResult(player3, player2, false, false, 2, 2));
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testSimulationHistoryMap() {
        List<GameResult> relevantResults = simulationHistoryMap.getAllResults();
        assertTrue(relevantResults.size() == 3);
        
        relevantResults = simulationHistoryMap.getResultsByAgent(player1);
        assertTrue(relevantResults.size() == 2);
        
        relevantResults = simulationHistoryMap.getResultsByGroup(player1);
        //agent1 and agent2 play 2 games
        assertTrue(relevantResults.size() == 4);
        
        GameResult newGameResult = new GameResult(player1, player2, true, false, 2, 0);
        simulationHistoryMap.addResult(newGameResult);
        relevantResults = simulationHistoryMap.getLatestResults();
        assertTrue(relevantResults.size() == 2);
        
        GameResult gameResult = simulationHistoryMap.getLatesResultsByAgent(player1);
        assertEquals(gameResult, newGameResult);
        
        relevantResults = simulationHistoryMap.getLatestResultsByGroup(player1);
        assertTrue(relevantResults.size() == 2);
        
        relevantResults = simulationHistoryMap.getAllWhere(result -> result.hasCooperated(player1));
        assertTrue(relevantResults.size() == 3);
        
        gameResult = simulationHistoryMap.getLatestWhere(result -> result.hasCooperated(player1));
        assertEquals(gameResult, newGameResult);
        
        simulationHistoryMap.reset();
        relevantResults = simulationHistoryMap.getAllResults();
        assertTrue(relevantResults.size() == 0);
    }

}
