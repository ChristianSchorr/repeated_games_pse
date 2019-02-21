package loop.model.simulationengine;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import loop.model.simulationengine.strategies.PureStrategy;

/**
 * This class holds tests for implementations of the {@link ConcreteGame} class.
 * 
 * @author Sebastian Feurer
 *
 */
public class ConcreteGameTest {
	Agent player1;
	Agent player2;
	ConcreteGame actualGame;

	/**
	 * Initialize the ConcreteGame testGame, PureStrategies grim and neverCooperate and
	 * the Agents player1 and player2
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		player1 = new Agent(50, PureStrategy.grim(), 0);
		player2 = new Agent(100, PureStrategy.neverCooperate(), 1);
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testPrisonersDilemma() {
	    actualGame = (ConcreteGame) ConcreteGame.prisonersDilemma();
	    testName("Prisoner's Dilemma");
	    testDescription("Two prisoners are accused of a crime. Each one can either "
                + "confess or not, but doesn't know the other ones decision. Depending on both decisions, the two "
                + "prisoners get higher or lower, and not necessarily the same prison sentence. Paradoxically, when "
                + "each prisoner pursues his self-interest, both end up worse off than they would have been had when "
                + "acting otherwise.");
	    testPayoffs(-1, -3, 0, -2, -1, 0, -3, -2);
	}
	
	@Test
    public void testStagHunt() {
        actualGame = (ConcreteGame) ConcreteGame.stagHunt();
        testName("Stag hunt");
        testDescription("Two mates go out on a hunt. Each one can individually choose between hunting "
                + "a stag and hunting a hare, but is not aware of the other one's choice. If one hunts a stag, he must "
                + "have the cooperation of his mate in order to succeed. One can get a hare by itself, but a hare is "
                + "worth less than a stag.");
        testPayoffs(4, 0, 3, 3, 4, 3, 0, 3);
    }
	
	@Test
    public void testChickenGame() {
        actualGame = (ConcreteGame) ConcreteGame.ChickenGame();
        testName("Chicken game");
        testDescription("In a test of courage, two drivers drive very fastly towards each other. One "
                + "must swerve, or both may die in the crash. However, if one driver swerves and the other does not, the one who "
                + "swerved will be called a \"chicken\", meaning a coward.");
        testPayoffs(4, 2, 6, 0, 4, 6, 2, 0);
    }
	
	private void testName(String name) {
	    assertEquals(name, actualGame.getName());
	}
	
	private void testDescription(String description) {
        assertEquals(description, actualGame.getDescription());
    }
	
	private void testPayoffs(int cc1, int cn1, int nc1, int nn1, int cc2, int cn2, int nc2, int nn2) {
	    assertEquals(cc1, actualGame.getCC1());
	    assertEquals(cn1, actualGame.getCN1());
	    assertEquals(nc1, actualGame.getNC1());
        assertEquals(nn1, actualGame.getNN1());
        assertEquals(cc2, actualGame.getCC2());
        assertEquals(cn2, actualGame.getCN2());
        assertEquals(nc2, actualGame.getNC2());
        assertEquals(nn1, actualGame.getNN2());
	}
	

	/**
	 * Test the implementation of the method play for the game Prisoners Dilemma
	 */
	@Test
	public void testPlay() {
		GameResult gameResult = ConcreteGame.prisonersDilemma().play(player1, player2, true, true);
		assertTrue(gameResult.getAgents().get(0).getCapital() == 49);
		assertTrue(gameResult.getAgents().get(1).getCapital() == 99);
		
		gameResult = ConcreteGame.prisonersDilemma().play(player1, player2, true, false);
		assertTrue(gameResult.getAgents().get(0).getCapital() == 46);
		assertTrue(gameResult.getAgents().get(1).getCapital() == 99);
		
		gameResult = ConcreteGame.prisonersDilemma().play(player1, player2, false, true);
		assertTrue(gameResult.getAgents().get(0).getCapital() == 46);
		assertTrue(gameResult.getAgents().get(1).getCapital() == 96);
		
		gameResult = ConcreteGame.prisonersDilemma().play(player1, player2, false, false);
		assertTrue(gameResult.getAgents().get(0).getCapital() == 44);
		assertTrue(gameResult.getAgents().get(1).getCapital() == 94);
	}
}
