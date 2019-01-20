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
	List<Agent> agents;

	/**
	 * Initialize the IterationResult
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		Strategy titForTat = PureStrategy.titForTat();
		Strategy grim = PureStrategy.grim();
		Strategy neverCooperate = PureStrategy.neverCooperate();
		Strategy groupGrim = PureStrategy.groupGrim();
		agents = new ArrayList<Agent>();
		agents.add(new Agent(10, groupGrim, 2));
		agents.add(new Agent(12, grim, 2));
		agents.add(new Agent(50, neverCooperate, 1));
		agents.add(new Agent(20, neverCooperate, 0));
		agents.add(new Agent(25, titForTat, 0));
		agents.add(new Agent(100, neverCooperate, 3));
		equilibriumReached = true;
		efficiency = 0.5;
		adapts = 103;
		history = new SimulationHistoryTable();
		GameResult result1 = new GameResult(agents.get(0), agents.get(1), true, true, 3, 3);
        GameResult result2 = new GameResult(agents.get(2), agents.get(3), false, false, 0, 0); 
        GameResult result3 = new GameResult(agents.get(4), agents.get(5), true, false, 0, 5); 
        history.addResult(result1);       
        history.addResult(result2);
        history.addResult(result3);
		iterationResult = new IterationResult(agents, history, equilibriumReached, efficiency, adapts);
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Tests the constructor of the class IterationResult and the initialized attributs
	 */
	@Test
	public void testIterationResult() {
		Strategy alwaysCooperate = PureStrategy.alwaysCooperate();
		Strategy grim = PureStrategy.grim();
		Strategy neverCooperate = PureStrategy.neverCooperate();
		List<Agent> testAgents = new ArrayList<Agent>();
		testAgents.add(new Agent(0, alwaysCooperate, 1));
		testAgents.add(new Agent(42, grim, 0));
		testAgents.add(new Agent(0, alwaysCooperate, 1));
		testAgents.add(new Agent(100, neverCooperate, 2));
		testAgents.add(new Agent(42, grim, 0));
		testAgents.add(new Agent(0, alwaysCooperate, 1));
		boolean testEquilibriumReached = false;
		double testEfficiency = 0.833;
		int testAdapts = 200;
		SimulationHistory simulationhistory = new SimulationHistoryTable();
		GameResult result1 = new GameResult(testAgents.get(4), testAgents.get(1), true, true, 6, 6);
        GameResult result2 = new GameResult(testAgents.get(0), testAgents.get(5), true, true, 6, 6); 
        GameResult result3 = new GameResult(testAgents.get(2), testAgents.get(3), true, false, 0, 10); 
        simulationhistory.addResult(result1);       
        simulationhistory.addResult(result2);
        simulationhistory.addResult(result3);
		IterationResult testIterationResult = new IterationResult(testAgents, simulationhistory,
												testEquilibriumReached, testEfficiency, testAdapts);
		
		List<Agent> verifyAgents = testIterationResult.getAgents();
		assertEquals(6, verifyAgents.size());
		assertTrue(verifyAgents.contains(testAgents.get(0)));
		assertTrue(verifyAgents.contains(testAgents.get(1)));
		assertTrue(verifyAgents.contains(testAgents.get(2)));
		assertTrue(verifyAgents.contains(testAgents.get(3)));
		assertTrue(verifyAgents.contains(testAgents.get(4)));
		assertTrue(verifyAgents.contains(testAgents.get(5)));
		
		assertTrue(simulationhistory.equals(testIterationResult.getHistory()));
		assertFalse(testIterationResult.equilibriumReached());
		assertEquals(0.833, testIterationResult.getEfficiency(),0.0);
		assertEquals(200, testIterationResult.getAdapts());
	}

	/**
	 * Tests the implementation of the method getAgents
	 */
	@Test
	public void testGetAgents() {
		List<Agent> testAgent = iterationResult.getAgents();
		assertEquals(6, testAgent.size());
		assertTrue(testAgent.contains(agents.get(0)));
		assertTrue(testAgent.contains(agents.get(1)));
		assertTrue(testAgent.contains(agents.get(2)));
		assertTrue(testAgent.contains(agents.get(3)));
		assertTrue(testAgent.contains(agents.get(4)));
		assertTrue(testAgent.contains(agents.get(5)));
	}

	/**
	 * Tests the implementation of the method getHistory
	 */
	@Test
	public void testGetHistory() {
		assertTrue(history.equals(iterationResult.getHistory()));
	}

	/**
	 * Tests the implementation of the method equilibriumReached
	 */
	@Test
	public void testEquilibriumReached() {
		assertTrue(iterationResult.equilibriumReached());
	}

	/**
	 * Tests the implementation of the method getEfficiency
	 */
	@Test
	public void testGetEfficiency() {
		assertEquals(0.5, iterationResult.getEfficiency(),0.0);
	}

	/**
	 * Tests the implementation of the method getAdapts
	 */
	@Test
	public void testGetAdapts() {
		assertEquals(103, iterationResult.getAdapts());
	}

}
