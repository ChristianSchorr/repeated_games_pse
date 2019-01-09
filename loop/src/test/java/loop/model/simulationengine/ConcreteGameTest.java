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
	ConcreteGame testGame;
	Agent player1;
	Agent player2;

	/**
	 * Initialize the ConcreteGame testGame, PureStrategies grim and neverCooperate and
	 * the Agents player1 and player2
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		testGame = new ConcreteGame("Test Dilemma", "This Dilemma only exist to"
				+ " test the ConcreteGame class.", 5, 4, -1, 2, 5, -1, 4, 2);
		PureStrategy grim = PureStrategy.grim();
		PureStrategy neverCooperate = PureStrategy.neverCooperate();
		player1 = new Agent(50, grim, 0);
		player2 = new Agent(100, neverCooperate, 1);
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Creates a new ConcreteGame hawksGame, test his parameters and player1 and player2
	 * play the game four times.
	 */
	@Test
	public void testConcreteGame() {
		ConcreteGame hawksGame = new ConcreteGame("hawks Dilemma", "Two hawks fly towards each other."
				+ "If a hawk cooperate he slow down. If no hawk cooperate they crash into each other"
				, -2, -2, 5, -10, -2, 5, -2, -10);
		assertTrue(hawksGame.getName().equals("hawks Dilemma"));
		assertTrue(hawksGame.getDescription().equals("Two hawks fly towards each other."
				+ "If a hawk cooperate he slow down. If no hawk cooperate they crash into each other"));
		GameResult gameResult = hawksGame.play(player1, player2, true, true);
		assertTrue(gameResult.getAgents().get(0).getCapital() == 48);
		assertTrue(gameResult.getAgents().get(1).getCapital() == 98);
		gameResult = hawksGame.play(player1, player2, true, false);
		assertTrue(gameResult.getAgents().get(0).getCapital() == 46);
		assertTrue(gameResult.getAgents().get(1).getCapital() == 103);
		gameResult = hawksGame.play(player1, player2, false, true);
		assertTrue(gameResult.getAgents().get(0).getCapital() == 51);
		assertTrue(gameResult.getAgents().get(1).getCapital() == 101);
		gameResult = hawksGame.play(player1, player2, false, false);
		assertTrue(gameResult.getAgents().get(0).getCapital() == 41);
		assertTrue(gameResult.getAgents().get(1).getCapital() == 91);
	}

	/**
	 * Test the implementation of the method getName
	 */
	@Test
	public void testGetName() {
		assertTrue(testGame.getName().equals("Test Dilemma"));
	}

	/**
	 * Test the implementation of the method getDiscription
	 */
	@Test
	public void testGetDescription() {
		assertTrue(testGame.getDescription().equals("This Dilemma only exist to"
				+ " test the ConcreteGame class."));
	}

	/**
	 * Test the implementation of the method play four times
	 */
	@Test
	public void testPlay() {
		GameResult gameResult = testGame.play(player1, player2, true, true);
		assertTrue(gameResult.getAgents().get(0).getCapital() == 55);
		assertTrue(gameResult.getAgents().get(1).getCapital() == 105);
		gameResult = testGame.play(player1, player2, true, false);
		assertTrue(gameResult.getAgents().get(0).getCapital() == 59);
		assertTrue(gameResult.getAgents().get(1).getCapital() == 104);
		gameResult = testGame.play(player1, player2, false, true);
		assertTrue(gameResult.getAgents().get(0).getCapital() == 58);
		assertTrue(gameResult.getAgents().get(1).getCapital() == 108);
		gameResult = testGame.play(player1, player2, false, false);
		assertTrue(gameResult.getAgents().get(0).getCapital() == 60);
		assertTrue(gameResult.getAgents().get(1).getCapital() == 110);
	}

	/**
	 * Creates a new ConcreteGame prisonDilemma, test his parameters and player1 and player2
	 * play the game four times.
	 */
	@Test
	public void testPrisonersDilemma() {
		Game prisonDilemma = ConcreteGame.prisonersDilemma();
		assertTrue(prisonDilemma.getName().equals("Prisoner's Dilemma"));
		assertTrue(prisonDilemma.getDescription().equals("The prisoner's dilemma is a standard example of a game analyzed in game" 
				+ " theory that shows why two completely rational individuals might not cooperate, even if it appears that it is" 
				+ " in their best interests to do so."));
		GameResult gameResult = prisonDilemma.play(player1, player2, true, true);
		assertTrue(gameResult.getAgents().get(0).getCapital() == 49);
		assertTrue(gameResult.getAgents().get(1).getCapital() == 99);
		gameResult = prisonDilemma.play(player1, player2, true, false);
		assertTrue(gameResult.getAgents().get(0).getCapital() == 46);
		assertTrue(gameResult.getAgents().get(1).getCapital() == 99);
		gameResult = prisonDilemma.play(player1, player2, false, true);
		assertTrue(gameResult.getAgents().get(0).getCapital() == 46);
		assertTrue(gameResult.getAgents().get(1).getCapital() == 96);
		gameResult = prisonDilemma.play(player1, player2, false, false);
		assertTrue(gameResult.getAgents().get(0).getCapital() == 44);
		assertTrue(gameResult.getAgents().get(1).getCapital() == 94);
	}

}
