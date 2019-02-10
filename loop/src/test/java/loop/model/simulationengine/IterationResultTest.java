package loop.model.simulationengine;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import loop.model.simulationengine.strategies.PureStrategy;
import loop.model.simulationengine.strategies.Strategy;

/**
 * This class holds tests for implementations of the {@link IterationResult} class.
 * 
 * @author Sebastian Feurer
 *
 */
public class IterationResultTest {
	IterationResult iterationResult;
	int adapts;
	double efficiency;
	boolean equilibriumReached;
	SimulationHistory history;
	List<Agent> agents = new ArrayList<Agent>();
	List<String> strategyNames = new ArrayList<String>();
	List<double[]> strategyPortions = new ArrayList<>();

	/**
	 * Initialize the IterationResult
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		Strategy titForTat = PureStrategy.titForTat();
		Strategy grim = PureStrategy.grim();
		
		strategyNames.add(titForTat.getName());
		strategyNames.add(grim.getName());

		agents.add(new Agent(10, titForTat, 2));
		agents.add(new Agent(12, grim, 2));
		agents.add(new Agent(50, grim, 1));
		agents.add(new Agent(100, titForTat, 3));
		
		//just for testing, is not valid with the given simulationHistory
		double[] portion1 = {0.5, 0.75}; 
		double[] portion2 = {0.5, 0.25};
		strategyPortions.add(portion1);
		strategyPortions.add(portion2);
		
		equilibriumReached = true;
		efficiency = 0.5;
		adapts = 20;
		
		history = new SimulationHistoryTable();
		GameResult result1 = new GameResult(agents.get(0), agents.get(1), true, true, 3, 3);
        GameResult result2 = new GameResult(agents.get(2), agents.get(3), false, false, 0, 0); 
        history.addResult(result1);       
        history.addResult(result2);
        
		iterationResult = new IterationResult(agents, history, equilibriumReached, efficiency, adapts, strategyPortions, strategyNames);
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Tests the constructor of the class IterationResult and the initialized attributs
	 */
	@Test
	public void testIterationResult() {
	    testGetAgents(iterationResult);
	    testGetHistory(iterationResult);
	    testEquilibriumReached(iterationResult);
	    testGetEfficiency(iterationResult);
	    testGetAdapts(iterationResult);
	    testGetStrategyPortions(iterationResult);
	    testGetStrategyNames(iterationResult);
	}

	/**
	 * Tests the implementation of the method getAgents
	 */
	public void testGetAgents(IterationResult result) {
		assertEquals(4, result.getAgents().size());
		assertEquals(agents.get(0), result.getAgents().get(0));
		assertEquals(agents.get(1), result.getAgents().get(1));
		assertEquals(agents.get(2), result.getAgents().get(2));
		assertEquals(agents.get(3), result.getAgents().get(3));
	}

	/**
	 * Tests the implementation of the method getHistory
	 */
	public void testGetHistory(IterationResult result) {
		assertTrue(history.equals(result.getHistory()));
	}

	/**
	 * Tests the implementation of the method equilibriumReached
	 */
	public void testEquilibriumReached(IterationResult result) {
		assertTrue(result.equilibriumReached());
	}

	/**
	 * Tests the implementation of the method getEfficiency
	 */
	public void testGetEfficiency(IterationResult result) {
		assertTrue(0.5 == result.getEfficiency());
	}

	/**
	 * Tests the implementation of the method getAdapts
	 */
	public void testGetAdapts(IterationResult result) {
		assertEquals(20, result.getAdapts());
	}
	
	/**
     * Tests the implementation of the method getStrategyPortions
     */
    public void testGetStrategyPortions(IterationResult result) {
        assertEquals(strategyPortions, result.getStrategyPortions());
    }
    
    /**
     * Tests the implementation of the method getStrategyNames
     */
    public void testGetStrategyNames(IterationResult result) {
        assertEquals(strategyNames, result.getStrategyNames());
    }

}
