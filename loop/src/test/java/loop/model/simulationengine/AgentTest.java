package loop.model.simulationengine;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import loop.model.simulationengine.strategies.PureStrategy;
import loop.model.simulationengine.strategies.Strategy;

/**
 * This class holds tests for implementations of the {@link Agent} class.
 * 
 * @author Sebastian Feurer
 *
 */
public class AgentTest {
	Strategy titForTat;
	Agent agent;
	
	/**
	 * Initialize the Strategy titForTat and Agent agent
	 */
	@Before
	public void setUp() throws Exception {
		titForTat = PureStrategy.titForTat();
		agent = new Agent(50, titForTat, 1);
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Create a new Agent and test his parameters
	 */
	@Test
	public void testAgent() {
		Strategy grim = PureStrategy.grim();
		Agent testAgent = new Agent(42, grim, 0);
		assertTrue(testAgent.getStrategy().equals(grim));
		assertTrue(testAgent.getCapital() == 42);
		assertTrue(testAgent.getInitialCapital() == 42);
		assertTrue(testAgent.getGroupId() == 0);
	}

	/**
	 * Test the implementation of the method getCapital
	 */
	@Test
	public void testGetCapital() {
		assertTrue(agent.getCapital() == 50);
	}

	/**
	 * Test the implementation of the method getInitialCapital
	 */
	@Test
	public void testGetInitialCapital() {
		assertTrue(agent.getInitialCapital() == 50);
	}

	/**
	 * Test the implementation of the method addCapital
	 */
	@Test
	public void testAddCapital() {
		assertTrue(agent.getCapital() == 50);
		agent.addCapital(5);
		assertTrue(agent.getCapital() == 55);
		agent.addCapital(47);
		assertTrue(agent.getCapital() == 102);
	}

	/**
	 * Test the implementation of the method getStrategy
	 */
	@Test
	public void testGetStrategy() {
		assertTrue(agent.getStrategy().equals(titForTat));
	}

	/**
	 * Test the implementation of the method setStrategy
	 */
	@Test
	public void testSetStrategy() {
		Strategy neverCooperate = PureStrategy.neverCooperate();
		assertTrue(agent.getStrategy().equals(titForTat));
		agent.setStrategy(neverCooperate);
		assertTrue(agent.getStrategy().equals(neverCooperate));
	}

	/**
	 * Test the implementation of the method getGroupId
	 */
	@Test
	public void testGetGroupId() {
		assertTrue(agent.getGroupId() == 1);
	}

	/**
	 * Test the implementation of the method isGroupAffiliated with an agent of the same group
	 */
	@Test
	public void testIsGroupAffiliatedTrue() {
		Strategy neverCooperate = PureStrategy.neverCooperate();
		Agent testAgent = new Agent(101, neverCooperate, 1);
		assertTrue(agent.isGroupAffiliated(testAgent));
	}

	/**
	 * Test the implementation of the method isGroupAffiliated with an agent from another group
	 */
	@Test
	public void testIsGroupAffiliatedFalse() {
		Strategy neverCooperate = PureStrategy.neverCooperate();
		Agent testAgent = new Agent(50, neverCooperate, 0);
		assertFalse(agent.isGroupAffiliated(testAgent));
	}
	
	/**
	 * Test the implementation of the method getCopy
	 */
	@Test
	public void testGetCopy() {
		Agent copy = agent.getCopy();
		assertTrue(copy.getStrategy().equals(titForTat));
		assertTrue(copy.getCapital() == 50);
		assertTrue(copy.getInitialCapital() == 50);
		assertTrue(copy.getGroupId() == 1);
		agent.addCapital(5);
		Strategy grim = PureStrategy.grim();
		agent.setStrategy(grim);
		assertTrue(copy.getStrategy().equals(titForTat));
		assertTrue(copy.getCapital() == 50);
		assertTrue(copy.getInitialCapital() == 50);
		assertTrue(agent.getStrategy().equals(grim));
		assertTrue(agent.getCapital() == 55);
	}

}
