package loop.model.simulationengine;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import loop.model.simulationengine.strategies.PureStrategy;
import loop.model.simulationengine.strategies.Strategy;

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

	@Test
	public void testGameResult() {
		fail("Not yet implemented");
	}

	@Test
	public void testHasAgent() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetPayoff() {
		fail("Not yet implemented");
	}

	@Test
	public void testHasCooperated() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetAgents() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetOtherAgent() {
		fail("Not yet implemented");
	}

}
