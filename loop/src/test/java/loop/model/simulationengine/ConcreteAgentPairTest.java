package loop.model.simulationengine;

import static org.junit.Assert.*;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import loop.model.simulationengine.strategies.PureStrategy;

/**
 * This class holds tests for implementations of the {@link ConcreteAgentPair} class.
 * 
 * @author Sebastian Feurer
 *
 */
public class ConcreteAgentPairTest {
	ConcreteAgentPair testAgentPair;
	Agent player1;
	Agent player2;

	/**
	 * Initialize the Agents player1 and player2 and the ConcreteAgentPair testAgentPair
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		player1 = new Agent(50, PureStrategy.grim(), 1);
		player2 = new Agent(101, PureStrategy.titForTat(), 0);
		testAgentPair = new ConcreteAgentPair(player1, player2);
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Create a new ConcreteAgentPair and test the parameter of his Agents
	 */
	@Test
	public void testConcreteAgentPair() {
		Agent testPlayer1 = new Agent(99, PureStrategy.groupTitForTat(), 2);
		Agent testPlayer2 = new Agent(-5, PureStrategy.neverCooperate(), 5);
		AgentPair agentPair = new ConcreteAgentPair(testPlayer1, testPlayer2);
		assertTrue(agentPair.getFirstAgent().getInitialCapital() == 99);
		assertTrue(agentPair.getFirstAgent().getCapital() == 99);
		assertTrue(agentPair.getFirstAgent().getStrategy().getName().equals("group tit-for-tat"));
		assertTrue(agentPair.getFirstAgent().getGroupId() == 2);
		assertTrue(agentPair.getSecondAgent().getInitialCapital() == -5);
		assertTrue(agentPair.getSecondAgent().getCapital() == -5);
		assertTrue(agentPair.getSecondAgent().getStrategy().getName().equals("never cooperate"));
		assertTrue(agentPair.getSecondAgent().getGroupId() == 5);
	}

	/**
	 * Test the implementation of the method getFirstAgent method
	 */
	@Test
	public void testGetFirstAgent() {
		assertTrue(testAgentPair.getFirstAgent().getInitialCapital() == 50);
		assertTrue(testAgentPair.getFirstAgent().getCapital() == 50);
		assertTrue(testAgentPair.getFirstAgent().getStrategy().getName().equals("grim"));
		assertTrue(testAgentPair.getFirstAgent().getGroupId() == 1);
	}

	/**
	 * Test the implementation of the method getSecondAgent method
	 */
	@Test
	public void testGetSecondAgent() {
		assertTrue(testAgentPair.getSecondAgent().getInitialCapital() == 101);
		assertTrue(testAgentPair.getSecondAgent().getCapital() == 101);
		assertTrue(testAgentPair.getSecondAgent().getStrategy().getName().equals("tit-for-tat"));
		assertTrue(testAgentPair.getSecondAgent().getGroupId() == 0);
	}

	/**
	 * Test the implementation of the method getAgents method
	 */
	@Test
	public void testGetAgents() {
		List<Agent> agents = testAgentPair.getAgents();
		assertTrue(agents.get(0).getInitialCapital() == 50);
		assertTrue(agents.get(0).getCapital() == 50);
		assertTrue(agents.get(0).getStrategy().getName().equals("grim"));
		assertTrue(agents.get(0).getGroupId() == 1);
		assertTrue(agents.get(1).getInitialCapital() == 101);
		assertTrue(agents.get(1).getCapital() == 101);
		assertTrue(agents.get(1).getStrategy().getName().equals("tit-for-tat"));
		assertTrue(agents.get(1).getGroupId() == 0);
	}

}
