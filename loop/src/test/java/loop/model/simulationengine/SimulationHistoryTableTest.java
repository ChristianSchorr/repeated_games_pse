package loop.model.simulationengine;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
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
	SimulationHistoryTable simulationHistoryTable;
	List<Agent> agents;
	GameResult result1;
	GameResult result2;
	GameResult result3;
	
	@Before
	public void setUp() throws Exception {
		simulationHistoryTable = new SimulationHistoryTable();
		int agentCount = 3;
        agents = TestUtility.getStandardAgents(agentCount, false);
        result1 = new GameResult(agents.get(0), agents.get(1), true, false, 3, 3);
        result2 = new GameResult(agents.get(1), agents.get(2), true, false, 3, 0); 
        result3 = new GameResult(agents.get(0), agents.get(2), false, false, 2, 2); 
        this.results.add(0, result1);   
        this.results.add(0, result2); 
        this.results.add(0, result3); 
        simulationHistoryTable.addResult(result1);       
        simulationHistoryTable.addResult(result2);
        simulationHistoryTable.addResult(result3);
	}
	
	/**
	 * Tests the addResult method
	 */
	@Test
	public void testAddResult() { 
		assertEquals(simulationHistoryTable.getAllResults(), this.results);
	}
	
	/**
	 * Tests the getResultsByAgent method
	 */
	@Test
	public void testGetResultsByAgent() {  
        resultsOfFirstAgent.add(0, result1);
        resultsOfFirstAgent.add(0, result3);
        assertEquals(simulationHistoryTable.getResultsByAgent(agents.get(0)), resultsOfFirstAgent);
	}
	
	/**
	 * Tests the getLatestResults method and the getLatesResultsByAgent method
	 */
	@Test
	public void testLatestResults() {
		latestResults.add(0, result2);
        latestResults.add(0, result3);
        assertEquals(simulationHistoryTable.getLatestResults(), latestResults);
        assertEquals(simulationHistoryTable.getLatesResultsByAgent(agents.get(0)), result3);
        assertEquals(simulationHistoryTable.getLatesResultsByAgent(agents.get(1)), result2);
        assertEquals(simulationHistoryTable.getLatesResultsByAgent(agents.get(2)), result3);
	}
		
	/**
	 * Tests the getAllWhere method and the getLatestWhere method
	 */
	@Test
	public void testWhere() {  	
		//List of all results where agent 1 participated
		List<GameResult> allResultsIncludingAgent1 = new ArrayList<GameResult>(); 
		allResultsIncludingAgent1.add(0, result1);
		allResultsIncludingAgent1.add(0, result3);
		
		//List of all results where agent 3 has cooperated (is empty)
		List<GameResult> allWhereAgent3Cooperated = new ArrayList<GameResult>();
        assertEquals(simulationHistoryTable.getAllWhere((result) -> result.hasAgent(agents.get(0))), allResultsIncludingAgent1);
        assertEquals(simulationHistoryTable.getAllWhere((result) -> result.hasCooperated(agents.get(2))), allWhereAgent3Cooperated);
        assertEquals(simulationHistoryTable.getLatestWhere((result) -> result.hasCooperated(agents.get(1))), result2); 
        assertEquals(simulationHistoryTable.getLatestWhere((result) -> result.getPayoff(agents.get(0)) > 2), result1);  
	}
	
	@Test
	public void testGetResultsByGroup() {
	    List<GameResult> resultsByGroup = simulationHistoryTable.getResultsByGroup(agents.get(0));
	    assertTrue(resultsByGroup.size() == 2);
	    	    
	    List<GameResult> latestResultsByGroup = simulationHistoryTable.getLatestResultsByGroup(agents.get(0));
	    assertTrue(latestResultsByGroup.size() == 1);
	}
	
		

}
