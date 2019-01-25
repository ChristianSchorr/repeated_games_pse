package loop.model.simulationengine.strategies;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import loop.model.simulationengine.Agent;
import loop.model.simulationengine.GameResult;
import loop.model.simulationengine.SimulationHistory;
import loop.model.simulationengine.SimulationHistoryTable;
/**
 * This class holds tests for implementations of the {@link PureStrategy} class.
 * 
 * @author Sebastian Feurer
 *
 */
public class PureStrategyTest {
	PureStrategy testPureStrategy;
	Agent player;
	Agent inSameGroupAsPlayer;
	Agent opponentOtherGroup;
	Agent opponentOtherGroup2;
	Agent opponentSameGroup;
	Agent opponentSameGroup2;
	SimulationHistory history;
	
	/**
	 * Initialize the PureStrategy testPureStrategy, the Simulationhistory history and some Agents
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		testPureStrategy = PureStrategy.titForTat();
		player = new Agent(0, PureStrategy.titForTat(), 1);
		inSameGroupAsPlayer = new Agent(0, PureStrategy.titForTat(), 1);
		opponentOtherGroup = new Agent(10, PureStrategy.neverCooperate(), 2);
		opponentOtherGroup2 = new Agent(10, PureStrategy.neverCooperate(), 0);
		opponentSameGroup = new Agent(20, PureStrategy.alwaysCooperate(), 3);
		opponentSameGroup2 = new Agent(20, PureStrategy.alwaysCooperate(), 3);
		history = new SimulationHistoryTable();
	}

	/**
	 * Create the PureStrategy grim, tests the attributes and the cooperativity of the player with this PureStrategy
	 */
	@Test
	public void testPureStrategy() {
		testPureStrategy = PureStrategy.grim();
		player.setStrategy(PureStrategy.grim());
		assertTrue(testPureStrategy.getName().equals("grim"));
		assertTrue(testPureStrategy.getDescription().equals("-"));
		
		assertTrue("The first time player with Strategy grim plays against opponent should cooperate be true",
				testPureStrategy.isCooperative(player, opponentOtherGroup, history));
		assertEquals(1, testPureStrategy.getCooperationProbability(player, opponentOtherGroup, history), 0);
		assertTrue("The first time player with Strategy grim plays against opponent should cooperate be true",
				testPureStrategy.isCooperative(player, opponentSameGroup, history));
		assertEquals(1, testPureStrategy.getCooperationProbability(player, opponentSameGroup, history), 0);
		
		GameResult result1 = new GameResult(player, opponentOtherGroup, true, true, 5, 5);
        history.addResult(result1);
        assertTrue("The player with Strategy grim should cooperate if the opponent has always cooperated",
        		testPureStrategy.isCooperative(player, opponentOtherGroup, history));
        assertEquals(1, testPureStrategy.getCooperationProbability(player, opponentOtherGroup, history), 0);
        
        result1 = new GameResult(player, opponentSameGroup, true, false, 0, 8);
        history.addResult(result1);        
        assertFalse("The player with Strategy grim shouldn't cooperate if the opponent hasn't cooperated once",
        		testPureStrategy.isCooperative(player, opponentSameGroup, history));
        assertEquals(0, testPureStrategy.getCooperationProbability(player, opponentSameGroup, history), 0);
        
        result1 = new GameResult(player, opponentOtherGroup, true, true, 5, 5);
        history.addResult(result1);
        assertTrue("The player with Strategy grim should cooperate if the opponent has always cooperated",
        		testPureStrategy.isCooperative(player, opponentOtherGroup, history));
        assertEquals(1, testPureStrategy.getCooperationProbability(player, opponentOtherGroup, history), 0);
        
        result1 = new GameResult(player, opponentOtherGroup, true, false, 0, 8);
        history.addResult(result1);        
        assertFalse("The player with Strategy grim shouldn't cooperate if the opponent hasn't cooperated once",
        		testPureStrategy.isCooperative(player, opponentOtherGroup, history));
        assertEquals(0, testPureStrategy.getCooperationProbability(player, opponentOtherGroup, history), 0);
        
        result1 = new GameResult(player, opponentOtherGroup, false, true, 8, 0);
        history.addResult(result1);        
        assertFalse("The player with Strategy grim shouldn't cooperate if the opponent hasn't cooperated once",
        		testPureStrategy.isCooperative(player, opponentOtherGroup, history));
        assertEquals(0, testPureStrategy.getCooperationProbability(player, opponentOtherGroup, history), 0);
        
        result1 = new GameResult(player, opponentSameGroup, false, true, 8, 0);
        history.addResult(result1);        
        assertFalse("The player with Strategy grim shouldn't cooperate if the opponent hasn't cooperated once",
        		testPureStrategy.isCooperative(player, opponentSameGroup, history));
        assertEquals(0, testPureStrategy.getCooperationProbability(player, opponentSameGroup, history), 0);
        
        result1 = new GameResult(player, opponentSameGroup, false, true, 8, 0);
        history.addResult(result1);        
        assertFalse("The player with Strategy grim shouldn't cooperate if the opponent hasn't cooperated once",
        		testPureStrategy.isCooperative(player, opponentSameGroup, history));
        assertEquals(0, testPureStrategy.getCooperationProbability(player, opponentSameGroup, history), 0);
	}

	/**
	 * Tests the implementation of the method getName with the Strategy tit-for-tat
	*/
	@Test
	public void testGetName() {
		assertTrue(testPureStrategy.getName().equals("tit-for-tat"));
	}

	/**
	 * Tests the implementation of the method getDescription with the Strategy tit-for-tat
	*/
	@Test
	public void testGetDescription() {
		assertTrue(testPureStrategy.getDescription().equals("-"));
	}

	/**
	 * Tests the implementation of the method isCooperative with the Strategy tit-for-tat
	*/
	@Test
	public void testIsCooperative() {
		assertTrue("The first time player with Strategy tit-for-tat plays against opponent should cooperate be true",
				testPureStrategy.isCooperative(player, opponentOtherGroup, history));
		assertTrue("The first time player with Strategy tit-for-tat plays against opponent should cooperate be true",
				testPureStrategy.isCooperative(player, opponentSameGroup, history));
		GameResult result1 = new GameResult(player, opponentOtherGroup, true, true, 5, 5);
        history.addResult(result1);
        assertTrue("The player with Strategy tit-for-tat should cooperate if the opponent cooperate the last time they play against each other",
        		testPureStrategy.isCooperative(player, opponentOtherGroup, history));
        result1 = new GameResult(player, opponentOtherGroup, true, false, 0, 8);
        history.addResult(result1);        
        assertFalse("The player with Strategy tit-for-tat shouldn't cooperate if the opponent hasn't cooperate the last time they play against each other",
        		testPureStrategy.isCooperative(player, opponentOtherGroup, history));
        result1 = new GameResult(player, opponentOtherGroup, false, true, 8, 0);
        history.addResult(result1);        
        assertTrue("The player with Strategy tit-for-tat should cooperate if the opponent cooperate the last time they play against each other",
        		testPureStrategy.isCooperative(player, opponentOtherGroup, history));
        result1 = new GameResult(player, opponentSameGroup, true, false, 0, 8);
        history.addResult(result1);        
        assertFalse("The player with Strategy tit-for-tat shouldn't cooperate if the opponent hasn't cooperate the last time they play against each other",
        		testPureStrategy.isCooperative(player, opponentSameGroup, history));
        result1 = new GameResult(player, opponentOtherGroup, true, false, 0, 8);
        history.addResult(result1);        
        assertFalse("The player with Strategy tit-for-tat shouldn't cooperate if the opponent hasn't cooperate the last time they play against each other",
        		testPureStrategy.isCooperative(player, opponentOtherGroup, history));
        result1 = new GameResult(player, opponentOtherGroup, false, true, 8, 0);
        history.addResult(result1);        
        assertTrue("The player with Strategy tit-for-tat should cooperate if the opponent cooperate the last time they play against each other",
        		testPureStrategy.isCooperative(player, opponentOtherGroup, history));
        result1 = new GameResult(player, opponentSameGroup, false, true, 8, 0);
        history.addResult(result1);        
        assertTrue("The player with Strategy tit-for-tat should cooperate if the opponent cooperate the last time they play against each other",
        		testPureStrategy.isCooperative(player, opponentSameGroup, history));
        result1 = new GameResult(player, opponentSameGroup, false, true, 8, 0);
        history.addResult(result1);        
        assertTrue("The player with Strategy tit-for-tat should cooperate if the opponent cooperate the last time they play against each other",
        		testPureStrategy.isCooperative(player, opponentSameGroup, history));
	}

	/**
	 * Tests the implementation of the method getCooperationProbability with the PureStrategy tit-for-tat
	*/
	@Test
	public void testGetCooperationProbability() {
		assertEquals(1, testPureStrategy.getCooperationProbability(player, opponentOtherGroup, history), 0);
		assertEquals(1, testPureStrategy.getCooperationProbability(player, opponentSameGroup, history), 0);
		GameResult result1 = new GameResult(player, opponentOtherGroup, true, true, 5, 5);
        history.addResult(result1);
        assertEquals(1, testPureStrategy.getCooperationProbability(player, opponentOtherGroup, history), 0);
        result1 = new GameResult(player, opponentOtherGroup, true, false, 0, 8);
        history.addResult(result1);        
        assertEquals(0, testPureStrategy.getCooperationProbability(player, opponentOtherGroup, history), 0);
        result1 = new GameResult(player, opponentOtherGroup, false, true, 8, 0);
        history.addResult(result1);        
        assertEquals(1, testPureStrategy.getCooperationProbability(player, opponentOtherGroup, history), 0);
        result1 = new GameResult(player, opponentSameGroup, true, false, 0, 8);
        history.addResult(result1);        
        assertEquals(0, testPureStrategy.getCooperationProbability(player, opponentSameGroup, history), 0);
        result1 = new GameResult(player, opponentOtherGroup, true, false, 0, 8);
        history.addResult(result1);        
        assertEquals(0, testPureStrategy.getCooperationProbability(player, opponentOtherGroup, history), 0);
        result1 = new GameResult(player, opponentOtherGroup, false, true, 8, 0);
        history.addResult(result1);        
        assertEquals(1, testPureStrategy.getCooperationProbability(player, opponentOtherGroup, history), 0);
        result1 = new GameResult(player, opponentSameGroup, false, true, 8, 0);
        history.addResult(result1);        
        assertEquals(1, testPureStrategy.getCooperationProbability(player, opponentSameGroup, history), 0);
        result1 = new GameResult(player, opponentSameGroup, false, true, 8, 0);
        history.addResult(result1);        
        assertEquals(1, testPureStrategy.getCooperationProbability(player, opponentSameGroup, history), 0);
	}

	/**
	 * Create the PureStrategy group tit-for-tat, tests the attributes and the cooperativity of the player with this PureStrategy
	 */
	@Test
	public void testGroupTitForTat() {
		testPureStrategy = PureStrategy.groupTitForTat();
		player.setStrategy(PureStrategy.groupTitForTat());
		assertTrue(testPureStrategy.getName().equals("group tit-for-tat"));
		assertTrue(testPureStrategy.getDescription().equals("-"));
		
		assertTrue("The first time player with Strategy group-tit-for-tat plays against opponent-group should cooperate be true",
				testPureStrategy.isCooperative(player, opponentOtherGroup, history));
		assertEquals(1, testPureStrategy.getCooperationProbability(player, opponentOtherGroup, history), 0);
		assertTrue("The first time player with Strategy group-tit-for-tat plays against opponent-group should cooperate be true",
				testPureStrategy.isCooperative(player, opponentOtherGroup2, history));
		assertEquals(1, testPureStrategy.getCooperationProbability(player, opponentOtherGroup2, history), 0);
		assertTrue("The first time player with Strategy group-tit-for-tat plays against opponent-group should cooperate be true",
				testPureStrategy.isCooperative(player, opponentSameGroup, history));
		assertEquals(1, testPureStrategy.getCooperationProbability(player, opponentSameGroup, history), 0);
		
		GameResult result1 = new GameResult(player, opponentOtherGroup, true, true, 5, 5);
        history.addResult(result1);
        assertTrue("The player with Strategy group-tit-for-tat should cooperate if the opponent has cooperated with the last player of the group",
        		testPureStrategy.isCooperative(player, opponentOtherGroup, history));
        assertEquals(1, testPureStrategy.getCooperationProbability(player, opponentOtherGroup, history), 0);
        
        result1 = new GameResult(inSameGroupAsPlayer, opponentOtherGroup, true, false, 0, 8);
        history.addResult(result1);
        assertFalse("The player with Strategy group-tit-for-tat shouldn't cooperate if the opponent hasn't cooperated with the last player of the group",
        		testPureStrategy.isCooperative(player, opponentOtherGroup, history));
        assertEquals(0, testPureStrategy.getCooperationProbability(player, opponentOtherGroup, history), 0);
        
        result1 = new GameResult(player, opponentOtherGroup2, true, false, 0, 8);
        history.addResult(result1);
        assertFalse("The player with Strategy group-tit-for-tat shouldn't cooperate if the opponent hasn't cooperated with the last player of the group",
        		testPureStrategy.isCooperative(player, opponentOtherGroup2, history));
        assertEquals(0, testPureStrategy.getCooperationProbability(player, opponentOtherGroup2, history), 0);
        
        result1 = new GameResult(player, opponentOtherGroup, false, true, 8, 0);
        history.addResult(result1);
        assertTrue("The player with Strategy group-tit-for-tat should cooperate if the opponent has cooperated with the last player of the group",
        		testPureStrategy.isCooperative(player, opponentOtherGroup, history));
        assertEquals(1, testPureStrategy.getCooperationProbability(player, opponentOtherGroup, history), 0);
        
        result1 = new GameResult(inSameGroupAsPlayer, opponentOtherGroup2, false, true, 8, 0);
        history.addResult(result1);
        assertTrue("The player with Strategy group-tit-for-tat should cooperate if the opponent has cooperated with the last player of the group",
        		testPureStrategy.isCooperative(player, opponentOtherGroup2, history));
        assertEquals(1, testPureStrategy.getCooperationProbability(player, opponentOtherGroup2, history), 0);
        
        result1 = new GameResult(player, opponentOtherGroup2, true, false, 0, 8);
        history.addResult(result1);
        assertFalse("The player with Strategy group-tit-for-tat shouldn't cooperate if the opponent hasn't cooperated with the last player of the group",
        		testPureStrategy.isCooperative(player, opponentOtherGroup2, history));
        assertEquals(0, testPureStrategy.getCooperationProbability(player, opponentOtherGroup2, history), 0);
	}

	/**
	 * Create the PureStrategy group grim, tests the attributes and the cooperativity of the player with this PureStrategy
	 */
	@Test
	public void testGroupGrim() {
		testPureStrategy = PureStrategy.groupGrim();
		player.setStrategy(PureStrategy.groupGrim());
		assertTrue(testPureStrategy.getName().equals("group grim"));
		assertTrue(testPureStrategy.getDescription().equals("-"));
		
		assertTrue("The first time player with Strategy group-grim plays against opponent-group should cooperate be true",
				testPureStrategy.isCooperative(player, opponentOtherGroup, history));
		assertEquals(1, testPureStrategy.getCooperationProbability(player, opponentOtherGroup, history), 0);
		assertTrue("The first time player with Strategy group-grim plays against opponent-group should cooperate be true",
				testPureStrategy.isCooperative(player, opponentOtherGroup2, history));
		assertEquals(1, testPureStrategy.getCooperationProbability(player, opponentOtherGroup2, history), 0);
		assertTrue("The first time player with Strategy group-grim plays against opponent-group should cooperate be true",
				testPureStrategy.isCooperative(player, opponentSameGroup, history));
		assertEquals(1, testPureStrategy.getCooperationProbability(player, opponentSameGroup, history), 0);
		
		GameResult result1 = new GameResult(player, opponentOtherGroup, true, true, 5, 5);
        history.addResult(result1);
        assertTrue("The player with Strategy group-grim should cooperate if the opponent has always cooperated with the players of this group",
        		testPureStrategy.isCooperative(player, opponentOtherGroup, history));
        assertEquals(1, testPureStrategy.getCooperationProbability(player, opponentOtherGroup, history), 0);
        
        result1 = new GameResult(inSameGroupAsPlayer, opponentSameGroup, true, false, 0, 8);
        history.addResult(result1);        
        assertFalse("The player with Strategy group-grim shouldn't cooperate if the opponent hasn't cooperated once against the players group",
        		testPureStrategy.isCooperative(player, opponentSameGroup, history));
        assertEquals(0, testPureStrategy.getCooperationProbability(player, opponentSameGroup, history), 0);
        
        result1 = new GameResult(player, opponentSameGroup2, false, true, 8, 0);
        history.addResult(result1);        
        assertTrue("The player with Strategy group-grim should cooperate if the opponent has always cooperated with the players of this group",
        		testPureStrategy.isCooperative(player, opponentSameGroup2, history));
        assertEquals(1, testPureStrategy.getCooperationProbability(player, opponentSameGroup2, history), 0);
        
        result1 = new GameResult(player, opponentSameGroup, false, true, 8, 0);
        history.addResult(result1);        
        assertFalse("The player with Strategy group-grim shouldn't cooperate if the opponent hasn't cooperated once against the players group",
        		testPureStrategy.isCooperative(player, opponentSameGroup, history));
        assertEquals(0, testPureStrategy.getCooperationProbability(player, opponentSameGroup, history), 0);
        
        result1 = new GameResult(player, opponentOtherGroup2, true, true, 5, 5);
        history.addResult(result1);
        assertTrue("The player with Strategy group-grim should cooperate if the opponent has always cooperated with the players of this group",
        		testPureStrategy.isCooperative(player, opponentOtherGroup2, history));
        assertEquals(1, testPureStrategy.getCooperationProbability(player, opponentOtherGroup2, history), 0);
        
        result1 = new GameResult(player, opponentOtherGroup2, true, false, 0, 8);
        history.addResult(result1);
        assertFalse("The player with Strategy group-grim shouldn't cooperate if the opponent hasn't cooperated once against the players group",
        		testPureStrategy.isCooperative(player, opponentOtherGroup2, history));
        assertEquals(0, testPureStrategy.getCooperationProbability(player, opponentOtherGroup2, history), 0);
        
        result1 = new GameResult(player, opponentOtherGroup, false, true, 8, 0);
        history.addResult(result1);
        assertTrue("The player with Strategy group-grim should cooperate if the opponent has always cooperated with the players of this group",
        		testPureStrategy.isCooperative(player, opponentOtherGroup, history));
        assertEquals(1, testPureStrategy.getCooperationProbability(player, opponentOtherGroup, history), 0);
        
        result1 = new GameResult(inSameGroupAsPlayer, opponentOtherGroup, false, false, 0, 0);
        history.addResult(result1);
        assertFalse("The player with Strategy group-grim shouldn't cooperate if the opponent hasn't cooperated once against the players group", 
        		testPureStrategy.isCooperative(player, opponentOtherGroup, history));
        assertEquals(0, testPureStrategy.getCooperationProbability(player, opponentOtherGroup, history), 0);
	}

	/**
	 * Create the PureStrategy always cooperate, tests the attributes and the cooperativity of the player with this PureStrategy
	 */
	@Test
	public void testAlwaysCooperate() {
		testPureStrategy = PureStrategy.alwaysCooperate();
		player.setStrategy(PureStrategy.alwaysCooperate());
		assertTrue(testPureStrategy.getName().equals("always cooperate"));
		assertTrue(testPureStrategy.getDescription().equals("-"));
		
		assertTrue("The player should be always cooperative",testPureStrategy.isCooperative(player, opponentOtherGroup, history));
		assertEquals(1, testPureStrategy.getCooperationProbability(player, opponentOtherGroup, history), 0);
		assertTrue("The player should be always cooperative", testPureStrategy.isCooperative(player, opponentSameGroup, history));
		assertEquals(1, testPureStrategy.getCooperationProbability(player, opponentSameGroup, history), 0);
		
		GameResult result1 = new GameResult(player, opponentOtherGroup, true, true, 5, 5);
        history.addResult(result1);
        assertTrue("The player should be always cooperative", testPureStrategy.isCooperative(player, opponentOtherGroup, history));
        assertEquals(1, testPureStrategy.getCooperationProbability(player, opponentOtherGroup, history), 0);
        
        result1 = new GameResult(player, opponentOtherGroup2, true, false, 0, 8);
        history.addResult(result1);
        assertTrue("The player should be always cooperative", testPureStrategy.isCooperative(player, opponentOtherGroup, history));
        assertEquals(1, testPureStrategy.getCooperationProbability(player, opponentOtherGroup, history), 0);
        
        result1 = new GameResult(player, opponentOtherGroup, false, true, 8, 0);
        history.addResult(result1);
        assertTrue("The player should be always cooperative", testPureStrategy.isCooperative(player, opponentOtherGroup, history));
        assertEquals(1, testPureStrategy.getCooperationProbability(player, opponentOtherGroup, history), 0);
        
        result1 = new GameResult(player, opponentOtherGroup, false, false, 0, 0);
        history.addResult(result1);
        assertTrue("The player should be always cooperative", testPureStrategy.isCooperative(player, opponentOtherGroup, history));
        assertEquals(1, testPureStrategy.getCooperationProbability(player, opponentOtherGroup, history), 0);
	}

	/**
	 * Create the PureStrategy never cooperate, tests the attributes and the cooperativity of the player with this PureStrategy
	 */
	@Test
	public void testNeverCooperate() {
		testPureStrategy = PureStrategy.neverCooperate();
		player.setStrategy(PureStrategy.neverCooperate());
		assertTrue(testPureStrategy.getName().equals("never cooperate"));
		assertTrue(testPureStrategy.getDescription().equals("-"));
		
		assertFalse("The player should be never cooperative",testPureStrategy.isCooperative(player, opponentOtherGroup, history));
		assertEquals(0, testPureStrategy.getCooperationProbability(player, opponentOtherGroup, history), 0);
		assertFalse("The player should be never cooperative", testPureStrategy.isCooperative(player, opponentSameGroup, history));
		assertEquals(0, testPureStrategy.getCooperationProbability(player, opponentSameGroup, history), 0);
		
		GameResult result1 = new GameResult(player, opponentOtherGroup, true, true, 5, 5);
        history.addResult(result1);
        assertFalse("The player should be never cooperative", testPureStrategy.isCooperative(player, opponentOtherGroup, history));
        assertEquals(0, testPureStrategy.getCooperationProbability(player, opponentOtherGroup, history), 0);
        
        result1 = new GameResult(player, opponentOtherGroup2, true, false, 0, 8);
        history.addResult(result1);
        assertFalse("The player should be never cooperative", testPureStrategy.isCooperative(player, opponentOtherGroup, history));
        assertEquals(0, testPureStrategy.getCooperationProbability(player, opponentOtherGroup, history), 0);
        
        result1 = new GameResult(player, opponentOtherGroup, false, true, 8, 0);
        history.addResult(result1);
        assertFalse("The player should be never cooperative", testPureStrategy.isCooperative(player, opponentOtherGroup, history));
        assertEquals(0, testPureStrategy.getCooperationProbability(player, opponentOtherGroup, history), 0);
        
        result1 = new GameResult(player, opponentOtherGroup, false, false, 0, 0);
        history.addResult(result1);
        assertFalse("The player should be never cooperative", testPureStrategy.isCooperative(player, opponentOtherGroup, history));
        assertEquals(0, testPureStrategy.getCooperationProbability(player, opponentOtherGroup, history), 0);
	}
}
