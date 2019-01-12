package loop.model.simulationengine;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * This class holds tests for the {@link SimulationHistoryTable} class.
 *  
 * @author Luc Mercatoris
 *
 */

public class SimulationHistoryTableTest {
	
	private List<GameResult> results = new ArrayList<GameResult>();
	private List<GameResult> resultsOfFirstAgent = new ArrayList<GameResult>();
	private List<GameResult> latestResults = new ArrayList<GameResult>();

	
	/**
	 * Tests the methods of the SimulationHistoryTable class
	 */
	@Test
	public void testResultList() {
		SimulationHistoryTable simulationHistoryTable = new SimulationHistoryTable();
		int agentCount = 3;
        List<Agent> agents = TestUtility.getStandardAgents(agentCount, false);
        
        GameResult result1 = new GameResult(agents.get(0), agents.get(1), true, true, 3, 3);     
        this.results.add(0, result1);
        this.resultsOfFirstAgent.add(0, result1);
        simulationHistoryTable.addResult(result1);
        assertEquals(simulationHistoryTable.getAllResults(), this.results);
          
        GameResult result2 = new GameResult(agents.get(0), agents.get(2), true, false, 3, 0); 
        this.results.add(0, result2);
        this.resultsOfFirstAgent.add(0, result2);
        this.latestResults.add(0, result2);
        assertNotEquals(simulationHistoryTable.getAllResults(), this.results);
        simulationHistoryTable.addResult(result2);
        assertEquals(simulationHistoryTable.getAllResults(), this.results);  
        assertEquals(simulationHistoryTable.getResultsByAgent(agents.get(0)), resultsOfFirstAgent);
        
        GameResult result3 = new GameResult(agents.get(1), agents.get(2), false, false, 2, 2); 
        this.results.add(0, result3);
        this.latestResults.add(0, result3);
        simulationHistoryTable.addResult(result3);
        assertEquals(simulationHistoryTable.getLatestResults(), latestResults);
        assertEquals(simulationHistoryTable.getLatesResultsByAgent(agents.get(2)), result3);
        assertNotEquals(simulationHistoryTable.getLatesResultsByAgent(agents.get(2)), result2);
           
        assertEquals(simulationHistoryTable.getAllWhere((result) -> result.hasAgent(agents.get(2))), latestResults);
        assertEquals(simulationHistoryTable.getLatestWhere((result) -> result.hasCooperated(agents.get(1))), result1); 
        assertEquals(simulationHistoryTable.getLatestWhere((result) -> result.getPayoff(agents.get(0)) > 2), result2); 
                
	}
	
		

}
