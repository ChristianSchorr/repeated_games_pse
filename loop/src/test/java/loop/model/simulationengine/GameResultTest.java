package loop.model.simulationengine;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import loop.model.simulationengine.strategies.PureStrategy;
import loop.model.simulationengine.strategies.Strategy;

/**
 * This class holds tests for implementations of the {@link GameResult} class.
 * 
 * @author Sebastian Feurer
 *
 */
public class GameResultTest {
	GameResult gameresult;
	Strategy titForTat;
	Strategy grim;
	Agent agent1;
	Agent agent2;

	/**
	 * Initialize the gameResult
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		titForTat = PureStrategy.titForTat();
		grim = PureStrategy.grim();
		agent1 = new Agent(20, titForTat, 2);
		agent2 = new Agent(-10, grim, 0);
		gameresult = new GameResult(agent1, agent2, true, true, 3, 3);
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Tests the constructor of the class GameResult and the initialized attributs
	 */
	@Test
	public void testGameResult() {
		Strategy grouptitForTat = PureStrategy.groupTitForTat();
		Strategy neverCooperate = PureStrategy.neverCooperate();
		Agent testAgent1 = new Agent(50, grouptitForTat, 5);
		Agent testAgent2 = new Agent(42, neverCooperate, 3);
		GameResult testGameresult = new GameResult(testAgent1, testAgent2, true, false, 5, -2);
		
		assertTrue(testGameresult.hasAgent(testAgent1));
		assertTrue(testGameresult.hasAgent(testAgent2));
		assertEquals(5, testGameresult.getPayoff(testAgent1));
		assertEquals(-2, testGameresult.getPayoff(testAgent2));
		assertTrue(testGameresult.hasCooperated(testAgent1));
		assertFalse(testGameresult.hasCooperated(testAgent2));
		List<Agent> agents = testGameresult.getAgents();
		assertTrue(testAgent1.equals(agents.get(0)));
		assertTrue(testAgent2.equals(agents.get(1)));
		assertTrue(testAgent2.equals(testGameresult.getOtherAgent(testAgent1)));
		assertTrue(testAgent1.equals(testGameresult.getOtherAgent(testAgent2)));
	}

	/**
	 * Tests the implementation of the method hasAgent
	 */
	@Test
	public void testHasAgent() {
		assertTrue(gameresult.hasAgent(agent1));
		assertTrue(gameresult.hasAgent(agent2));
		Agent agent3 = agent1.getCopy();
		Agent agent4 = new Agent(0, titForTat, 1);
		assertFalse(gameresult.hasAgent(agent3));
		assertFalse(gameresult.hasAgent(agent4));
	}

	/**
	 * Tests the implementation of the method getPayoff
	 */
	@Test
	public void testGetPayoff() {
		assertEquals(3, gameresult.getPayoff(agent1));
		assertEquals(3, gameresult.getPayoff(agent2));
		Agent agent3 = agent1.getCopy();
		Agent agent4 = new Agent(0, titForTat, 1);
		assertEquals(0, gameresult.getPayoff(agent3));
		assertEquals(0, gameresult.getPayoff(agent4));
	}

	/**
	 * Tests the implementation of the method hasCooperated
	 */
	@Test
	public void testHasCooperated() {
		assertTrue(gameresult.hasCooperated(agent1));
		assertTrue(gameresult.hasCooperated(agent2));
		Agent agent3 = agent1.getCopy();
		Agent agent4 = new Agent(0, titForTat, 1);
		assertFalse(gameresult.hasCooperated(agent3));
		assertFalse(gameresult.hasCooperated(agent4));
	}

	/**
	 * Tests the implementation of the method getAgents
	 */
	@Test
	public void testGetAgents() {
		List<Agent> agents = gameresult.getAgents();
		assertTrue(agent1.equals(agents.get(0)));
		assertTrue(agent2.equals(agents.get(1)));
	}

	/**
	 * Tests the implementation of the method getOtherAgent
	 */
	@Test
	public void testGetOtherAgent() {
		assertTrue(agent2.equals(gameresult.getOtherAgent(agent1)));
		assertTrue(agent1.equals(gameresult.getOtherAgent(agent2)));
		Agent agent3 = agent1.getCopy();
		Agent agent4 = new Agent(0, titForTat, 1);
		assertNull(gameresult.getOtherAgent(agent3));
		assertNull(gameresult.getOtherAgent(agent4));
	}

}
