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

public class SimulationHistoryTableClass {
	
	private List<GameResult> results = new ArrayList<GameResult>();
	
	/**
	 * Tests the addResult method
	 */
	@Test
	public void testAddResult() {
		SimulationHistoryTable simulationHistoryTable = new SimulationHistoryTable();
		int agentCount = 1000;
        int rounds = 100;
        List<Agent> agents = TestUtility.getStandardAgents(agentCount, false);
        GameResult result1 = new GameResult(agents.get(0), agents.get(1), true, true, 3, 3);     
        this.results.add(result1);
        simulationHistoryTable.addResult(result1);
        assertEquals(simulationHistoryTable.getAllResults(), this.results);
        GameResult result2 = new GameResult(agents.get(2), agents.get(3), true, false, 3, 0); 
        this.results.add(0, result2);
        assertNotEquals(simulationHistoryTable.getAllResults(), this.results);
        simulationHistoryTable.addResult(result2);
        assertEquals(simulationHistoryTable.getAllResults(), this.results);    
	}
	
	
	

}
