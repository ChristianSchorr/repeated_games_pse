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
 * This class holds tests for implementations of the {@link CountingEquilibriumCriterion} class.
 * 
 * @author Sebastian Feurer
 *
 */
public class CountingEquilibriumCriterionTest {
	SimulationHistoryTable simulationHistoryTable;
	List<Agent> agents;
	Strategy titForTat;
	Strategy grim;
	Strategy nevercooperate;
	SuccessQuantifier successQuantifier;

	/**
	 * Initialize the simulationHistoryTable, the List agents (with four agents) and the Strategies titForTat, grim and nevercooperate
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		simulationHistoryTable = new SimulationHistoryTable();
        agents = new ArrayList<Agent>();
        titForTat = PureStrategy.titForTat();
        grim = PureStrategy.grim();
        nevercooperate = PureStrategy.neverCooperate();
        agents.add(new Agent(0,titForTat, 1));
        agents.add(new Agent(0,grim, 1));
        agents.add(new Agent(0,grim, 1));
        agents.add(new Agent(0,nevercooperate, 1));
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Tests the method isEquilibrium with a RankingEquilibrium
	 * After 3 Rounds the Adaptionstep finish and the EquilibriumCriterion check for an Equilibrium.
	 * In this example the parameter alpha is 0.3 and the minRounds parameter is 4.
	 * It means by an amount of 4 agents that the sum of the differences in rank should be 4 rounds smaller than 2.4.
	 * In the second Adaptionstep the sum of the difference is 6 > 2.4.
	 */
	@Test
	public void testIsRankingEquilibrium1() {
		CountingEquilibriumCriterion rankingEquilibrium = new RankingEquilibrium(0.3, 4);
		successQuantifier = new TotalCapital();
        
        GameResult result1 = new GameResult(agents.get(0), agents.get(1), true, true, 5, 5);
        agents.get(0).addCapital(5);
        agents.get(1).addCapital(5);
        simulationHistoryTable.addResult(result1);
        GameResult result2 = new GameResult(agents.get(2), agents.get(3), true, false, 0, 6);
        agents.get(2).addCapital(0);
        agents.get(3).addCapital(6);
        simulationHistoryTable.addResult(result2);
        result1 = new GameResult(agents.get(2), agents.get(0), true, true, 5, 5);
        agents.get(2).addCapital(5);
        agents.get(0).addCapital(5);
        simulationHistoryTable.addResult(result1);
        result2 = new GameResult(agents.get(1), agents.get(3), true, false, 0, 6); 
        agents.get(1).addCapital(0);
        agents.get(3).addCapital(6);
        simulationHistoryTable.addResult(result2);
        result1 = new GameResult(agents.get(2), agents.get(1), true, true, 5, 5);
        agents.get(2).addCapital(5);
        agents.get(1).addCapital(5);
        simulationHistoryTable.addResult(result1);
        result2 = new GameResult(agents.get(0), agents.get(3), true, false, 0, 6); 
        agents.get(0).addCapital(0);
        agents.get(3).addCapital(6);
        simulationHistoryTable.addResult(result2);
        
        List<Agent> ranking = copyAgents(agents);
        ranking = successQuantifier.createRanking(ranking, simulationHistoryTable);
        
        assertFalse(rankingEquilibrium.isEquilibrium(ranking, simulationHistoryTable));
        
        result1 = new GameResult(agents.get(0), agents.get(1), true, true, 5, 5);
        agents.get(0).addCapital(5);
        agents.get(1).addCapital(5);
        simulationHistoryTable.addResult(result1);
        result2 = new GameResult(agents.get(2), agents.get(3), false, false, 0, 0);
        agents.get(2).addCapital(0);
        agents.get(3).addCapital(0);
        simulationHistoryTable.addResult(result2);
        result1 = new GameResult(agents.get(2), agents.get(0), true, true, 5, 5);
        agents.get(2).addCapital(5);
        agents.get(0).addCapital(5);
        simulationHistoryTable.addResult(result1);
        result2 = new GameResult(agents.get(1), agents.get(3), false, false, 0, 0); 
        agents.get(1).addCapital(0);
        agents.get(3).addCapital(0);
        simulationHistoryTable.addResult(result2);
        result1 = new GameResult(agents.get(2), agents.get(1), true, true, 5, 5);
        agents.get(2).addCapital(5);
        agents.get(1).addCapital(5);
        simulationHistoryTable.addResult(result1);
        result2 = new GameResult(agents.get(0), agents.get(3), false, false, 0, 0);
        agents.get(0).addCapital(0);
        agents.get(3).addCapital(0);
        simulationHistoryTable.addResult(result2);
        ranking = successQuantifier.createRanking(agents, simulationHistoryTable);
        
        assertFalse(rankingEquilibrium.isEquilibrium(ranking, simulationHistoryTable));
        
        result1 = new GameResult(agents.get(0), agents.get(1), true, true, 5, 5);
        agents.get(0).addCapital(5);
        agents.get(1).addCapital(5);
        simulationHistoryTable.addResult(result1);
        result2 = new GameResult(agents.get(2), agents.get(3), false, false, 0, 0);
        agents.get(2).addCapital(0);
        agents.get(3).addCapital(0);
        simulationHistoryTable.addResult(result2);
        result1 = new GameResult(agents.get(2), agents.get(0), true, true, 5, 5);
        agents.get(2).addCapital(5);
        agents.get(0).addCapital(5);
        simulationHistoryTable.addResult(result1);
        result2 = new GameResult(agents.get(1), agents.get(3), false, false, 0, 0); 
        agents.get(1).addCapital(0);
        agents.get(3).addCapital(0);
        simulationHistoryTable.addResult(result2);
        result1 = new GameResult(agents.get(2), agents.get(1), true, true, 5, 5);
        agents.get(2).addCapital(5);
        agents.get(1).addCapital(5);
        simulationHistoryTable.addResult(result1);
        result2 = new GameResult(agents.get(0), agents.get(3), false, false, 0, 0);
        agents.get(0).addCapital(0);
        agents.get(3).addCapital(0);
        simulationHistoryTable.addResult(result2);
        
        ranking = copyAgents(agents);
        ranking = successQuantifier.createRanking(ranking, simulationHistoryTable);
        
        assertFalse(rankingEquilibrium.isEquilibrium(ranking, simulationHistoryTable));
	
        result1 = new GameResult(agents.get(0), agents.get(1), true, true, 5, 5);
        agents.get(0).addCapital(5);
        agents.get(1).addCapital(5);
        simulationHistoryTable.addResult(result1);
        result2 = new GameResult(agents.get(2), agents.get(3), false, false, 0, 0);
        agents.get(2).addCapital(0);
        agents.get(3).addCapital(0);
        simulationHistoryTable.addResult(result2);
        result1 = new GameResult(agents.get(2), agents.get(0), true, true, 5, 5);
        agents.get(2).addCapital(5);
        agents.get(0).addCapital(5);
        simulationHistoryTable.addResult(result1);
        result2 = new GameResult(agents.get(1), agents.get(3), false, false, 0, 0); 
        agents.get(1).addCapital(0);
        agents.get(3).addCapital(0);
        simulationHistoryTable.addResult(result2);
        result1 = new GameResult(agents.get(2), agents.get(1), true, true, 5, 5);
        agents.get(2).addCapital(5);
        agents.get(1).addCapital(5);
        simulationHistoryTable.addResult(result1);
        result2 = new GameResult(agents.get(0), agents.get(3), false, false, 0, 0);
        agents.get(0).addCapital(0);
        agents.get(3).addCapital(0);
        simulationHistoryTable.addResult(result2);
        
        ranking = copyAgents(agents);
        ranking = successQuantifier.createRanking(ranking, simulationHistoryTable);
        
        assertFalse(rankingEquilibrium.isEquilibrium(ranking, simulationHistoryTable));
        
        result1 = new GameResult(agents.get(0), agents.get(1), true, true, 5, 5);
        agents.get(0).addCapital(5);
        agents.get(1).addCapital(5);
        simulationHistoryTable.addResult(result1);
        result2 = new GameResult(agents.get(2), agents.get(3), false, false, 0, 0);
        agents.get(2).addCapital(0);
        agents.get(3).addCapital(0);
        simulationHistoryTable.addResult(result2);
        result1 = new GameResult(agents.get(2), agents.get(0), true, true, 5, 5);
        agents.get(2).addCapital(5);
        agents.get(0).addCapital(5);
        simulationHistoryTable.addResult(result1);
        result2 = new GameResult(agents.get(1), agents.get(3), false, false, 0, 0); 
        agents.get(1).addCapital(0);
        agents.get(3).addCapital(0);
        simulationHistoryTable.addResult(result2);
        result1 = new GameResult(agents.get(2), agents.get(1), true, true, 5, 5);
        agents.get(2).addCapital(5);
        agents.get(1).addCapital(5);
        simulationHistoryTable.addResult(result1);
        result2 = new GameResult(agents.get(0), agents.get(3), false, false, 0, 0);
        agents.get(0).addCapital(0);
        agents.get(3).addCapital(0);
        simulationHistoryTable.addResult(result2);
        
        ranking = copyAgents(agents);
        ranking = successQuantifier.createRanking(ranking, simulationHistoryTable);
        
        assertFalse(rankingEquilibrium.isEquilibrium(ranking, simulationHistoryTable));
        
        result1 = new GameResult(agents.get(0), agents.get(1), true, true, 5, 5);
        agents.get(0).addCapital(5);
        agents.get(1).addCapital(5);
        simulationHistoryTable.addResult(result1);
        result2 = new GameResult(agents.get(2), agents.get(3), false, false, 0, 0);
        agents.get(2).addCapital(0);
        agents.get(3).addCapital(0);
        simulationHistoryTable.addResult(result2);
        result1 = new GameResult(agents.get(2), agents.get(0), true, true, 5, 5);
        agents.get(2).addCapital(5);
        agents.get(0).addCapital(5);
        simulationHistoryTable.addResult(result1);
        result2 = new GameResult(agents.get(1), agents.get(3), false, false, 0, 0); 
        agents.get(1).addCapital(0);
        agents.get(3).addCapital(0);
        simulationHistoryTable.addResult(result2);
        result1 = new GameResult(agents.get(2), agents.get(1), true, true, 5, 5);
        agents.get(2).addCapital(5);
        agents.get(1).addCapital(5);
        simulationHistoryTable.addResult(result1);
        result2 = new GameResult(agents.get(0), agents.get(3), false, false, 0, 0);
        agents.get(0).addCapital(0);
        agents.get(3).addCapital(0);
        simulationHistoryTable.addResult(result2);
        
        ranking = copyAgents(agents);
        ranking = successQuantifier.createRanking(ranking, simulationHistoryTable);
        
        assertTrue(rankingEquilibrium.isEquilibrium(ranking, simulationHistoryTable));
	}
	
	/**
	 * Tests the method isEquilibrium with a RankingEquilibrium
	 * After 3 Rounds the Adaptionstep finish and the EquilibriumCriterion check for an Equilibrium.
	 * In this example the parameter alpha is 1 and the minRounds parameter is 5.
	 * It means by an amount of 4 agents that the sum of the differences in rank should be 5 rounds smaller than 8.
	 */
	@Test
	public void testIsRankingEquilibrium2() {
		CountingEquilibriumCriterion rankingEquilibrium = new RankingEquilibrium(1, 5);
		successQuantifier = new TotalCapital();
		Game prisonDilemma = ConcreteGame.prisonersDilemma();
		
		GameResult result1 = prisonDilemma.play(agents.get(0), agents.get(1), true, true);
		simulationHistoryTable.addResult(result1);
		GameResult result2 = prisonDilemma.play(agents.get(2), agents.get(3), true, false);
		simulationHistoryTable.addResult(result2);
		result1 = prisonDilemma.play(agents.get(0), agents.get(2), true, true);
		simulationHistoryTable.addResult(result1);
		result2 = prisonDilemma.play(agents.get(1), agents.get(3), true, false);
		simulationHistoryTable.addResult(result2);
		result1 = prisonDilemma.play(agents.get(2), agents.get(1), true, true);
		simulationHistoryTable.addResult(result1);
		result2 = prisonDilemma.play(agents.get(0), agents.get(3), true, false);
		simulationHistoryTable.addResult(result2);
		
		List<Agent> ranking = copyAgents(agents);
		ranking = successQuantifier.createRanking(ranking, simulationHistoryTable);
		
		assertFalse(rankingEquilibrium.isEquilibrium(ranking, simulationHistoryTable));
		
		result1 = prisonDilemma.play(agents.get(0), agents.get(1), true, true);
		simulationHistoryTable.addResult(result1);
		result2 = prisonDilemma.play(agents.get(2), agents.get(3), false, false);
		simulationHistoryTable.addResult(result2);
		result1 = prisonDilemma.play(agents.get(0), agents.get(2), true, true);
		simulationHistoryTable.addResult(result1);
		result2 = prisonDilemma.play(agents.get(1), agents.get(3), false, false);
		simulationHistoryTable.addResult(result2);
		result1 = prisonDilemma.play(agents.get(2), agents.get(1), true, true);
		simulationHistoryTable.addResult(result1);
		result2 = prisonDilemma.play(agents.get(0), agents.get(3), false, false);
		simulationHistoryTable.addResult(result2);
		
		ranking = copyAgents(agents);
		ranking = successQuantifier.createRanking(ranking, simulationHistoryTable);
		
		assertFalse(rankingEquilibrium.isEquilibrium(ranking, simulationHistoryTable));
		
		result1 = prisonDilemma.play(agents.get(0), agents.get(1), true, true);
		simulationHistoryTable.addResult(result1);
		result2 = prisonDilemma.play(agents.get(2), agents.get(3), false, false);
		simulationHistoryTable.addResult(result2);
		result1 = prisonDilemma.play(agents.get(0), agents.get(2), true, true);
		simulationHistoryTable.addResult(result1);
		result2 = prisonDilemma.play(agents.get(1), agents.get(3), false, false);
		simulationHistoryTable.addResult(result2);
		result1 = prisonDilemma.play(agents.get(2), agents.get(1), true, true);
		simulationHistoryTable.addResult(result1);
		result2 = prisonDilemma.play(agents.get(0), agents.get(3), false, false);
		simulationHistoryTable.addResult(result2);
		
		ranking = copyAgents(agents);
		ranking = successQuantifier.createRanking(ranking, simulationHistoryTable);
		
		assertFalse(rankingEquilibrium.isEquilibrium(ranking, simulationHistoryTable));
		
		result1 = prisonDilemma.play(agents.get(0), agents.get(1), true, true);
		simulationHistoryTable.addResult(result1);
		result2 = prisonDilemma.play(agents.get(2), agents.get(3), false, false);
		simulationHistoryTable.addResult(result2);
		result1 = prisonDilemma.play(agents.get(0), agents.get(2), true, true);
		simulationHistoryTable.addResult(result1);
		result2 = prisonDilemma.play(agents.get(1), agents.get(3), false, false);
		simulationHistoryTable.addResult(result2);
		result1 = prisonDilemma.play(agents.get(2), agents.get(1), true, true);
		simulationHistoryTable.addResult(result1);
		result2 = prisonDilemma.play(agents.get(0), agents.get(3), false, false);
		simulationHistoryTable.addResult(result2);
		
		ranking = copyAgents(agents);
		ranking = successQuantifier.createRanking(ranking, simulationHistoryTable);
		
		assertFalse(rankingEquilibrium.isEquilibrium(ranking, simulationHistoryTable));
		
		result1 = prisonDilemma.play(agents.get(0), agents.get(1), true, true);
		simulationHistoryTable.addResult(result1);
		result2 = prisonDilemma.play(agents.get(2), agents.get(3), false, false);
		simulationHistoryTable.addResult(result2);
		result1 = prisonDilemma.play(agents.get(0), agents.get(2), true, true);
		simulationHistoryTable.addResult(result1);
		result2 = prisonDilemma.play(agents.get(1), agents.get(3), false, false);
		simulationHistoryTable.addResult(result2);
		result1 = prisonDilemma.play(agents.get(2), agents.get(1), true, true);
		simulationHistoryTable.addResult(result1);
		result2 = prisonDilemma.play(agents.get(0), agents.get(3), false, false);
		simulationHistoryTable.addResult(result2);
		
		ranking = copyAgents(agents);
		ranking = successQuantifier.createRanking(ranking, simulationHistoryTable);
		
		assertFalse(rankingEquilibrium.isEquilibrium(ranking, simulationHistoryTable));
		
		result1 = prisonDilemma.play(agents.get(0), agents.get(1), true, true);
		simulationHistoryTable.addResult(result1);
		result2 = prisonDilemma.play(agents.get(2), agents.get(3), false, false);
		simulationHistoryTable.addResult(result2);
		result1 = prisonDilemma.play(agents.get(0), agents.get(2), true, true);
		simulationHistoryTable.addResult(result1);
		result2 = prisonDilemma.play(agents.get(1), agents.get(3), false, false);
		simulationHistoryTable.addResult(result2);
		result1 = prisonDilemma.play(agents.get(2), agents.get(1), true, true);
		simulationHistoryTable.addResult(result1);
		result2 = prisonDilemma.play(agents.get(0), agents.get(3), false, false);
		simulationHistoryTable.addResult(result2);
		
		ranking = copyAgents(agents);
		ranking = successQuantifier.createRanking(ranking, simulationHistoryTable);

		assertTrue(rankingEquilibrium.isEquilibrium(ranking, simulationHistoryTable));
	
	}
	
	/**
	 * Tests the method isEquilibrium with a StrategyEquilibrium
	 * After 3 Rounds the Adaptionstep finish and the EquilibriumCriterion check for an Equilibrium.
	 * In this example the parameter alpha is 0.5 and the minRounds parameter is 3.
	 * It means by an amount of 4 agents that 1 agents are allowed to change their strategy.
	 */
	@Test
	public void testIsStrategyEquilibrium1() {
		CountingEquilibriumCriterion strategyEquilibrium = new StrategyEquilibrium(0.5, 3);
		successQuantifier = new TotalCapital();
		Game prisonDilemma = ConcreteGame.prisonersDilemma();
		
		GameResult result1 = prisonDilemma.play(agents.get(0), agents.get(1), true, true);
		simulationHistoryTable.addResult(result1);
		GameResult result2 = prisonDilemma.play(agents.get(2), agents.get(3), true, false);
		simulationHistoryTable.addResult(result2);
		result1 = prisonDilemma.play(agents.get(0), agents.get(2), true, true);
		simulationHistoryTable.addResult(result1);
		result2 = prisonDilemma.play(agents.get(1), agents.get(3), true, false);
		simulationHistoryTable.addResult(result2);
		result1 = prisonDilemma.play(agents.get(2), agents.get(1), true, true);
		simulationHistoryTable.addResult(result1);
		result2 = prisonDilemma.play(agents.get(0), agents.get(3), true, false);
		simulationHistoryTable.addResult(result2);
		
		List<Agent> ranking = copyAgents(agents);
		ranking = successQuantifier.createRanking(ranking, simulationHistoryTable);
		
		assertFalse(strategyEquilibrium.isEquilibrium(ranking, simulationHistoryTable));
		
		result1 = prisonDilemma.play(agents.get(0), agents.get(1), true, true);
		simulationHistoryTable.addResult(result1);
		result2 = prisonDilemma.play(agents.get(2), agents.get(3), false, false);
		simulationHistoryTable.addResult(result2);
		result1 = prisonDilemma.play(agents.get(0), agents.get(2), true, true);
		simulationHistoryTable.addResult(result1);
		result2 = prisonDilemma.play(agents.get(1), agents.get(3), false, false);
		simulationHistoryTable.addResult(result2);
		result1 = prisonDilemma.play(agents.get(2), agents.get(1), true, true);
		simulationHistoryTable.addResult(result1);
		result2 = prisonDilemma.play(agents.get(0), agents.get(3), false, false);
		simulationHistoryTable.addResult(result2);
		
		ranking = copyAgents(agents);
		ranking = successQuantifier.createRanking(ranking, simulationHistoryTable);
		
		assertFalse(strategyEquilibrium.isEquilibrium(ranking, simulationHistoryTable));
		
		result1 = prisonDilemma.play(agents.get(0), agents.get(1), true, true);
		simulationHistoryTable.addResult(result1);
		result2 = prisonDilemma.play(agents.get(2), agents.get(3), false, false);
		simulationHistoryTable.addResult(result2);
		result1 = prisonDilemma.play(agents.get(0), agents.get(2), true, true);
		simulationHistoryTable.addResult(result1);
		result2 = prisonDilemma.play(agents.get(1), agents.get(3), false, false);
		simulationHistoryTable.addResult(result2);
		result1 = prisonDilemma.play(agents.get(2), agents.get(1), true, true);
		simulationHistoryTable.addResult(result1);
		result2 = prisonDilemma.play(agents.get(0), agents.get(3), false, false);
		simulationHistoryTable.addResult(result2);
		agents.get(0).setStrategy(grim);
		agents.get(2).setStrategy(titForTat);
		
		ranking = copyAgents(agents);
		ranking = successQuantifier.createRanking(ranking, simulationHistoryTable);
		
		assertFalse(strategyEquilibrium.isEquilibrium(ranking, simulationHistoryTable));
		
		result1 = prisonDilemma.play(agents.get(0), agents.get(1), true, true);
		simulationHistoryTable.addResult(result1);
		result2 = prisonDilemma.play(agents.get(2), agents.get(3), false, false);
		simulationHistoryTable.addResult(result2);
		result1 = prisonDilemma.play(agents.get(0), agents.get(2), true, true);
		simulationHistoryTable.addResult(result1);
		result2 = prisonDilemma.play(agents.get(1), agents.get(3), false, false);
		simulationHistoryTable.addResult(result2);
		result1 = prisonDilemma.play(agents.get(2), agents.get(1), true, true);
		simulationHistoryTable.addResult(result1);
		result2 = prisonDilemma.play(agents.get(0), agents.get(3), false, false);
		simulationHistoryTable.addResult(result2);
		agents.get(1).setStrategy(titForTat);
		
		ranking = copyAgents(agents);
		ranking = successQuantifier.createRanking(ranking, simulationHistoryTable);
		
		assertFalse(strategyEquilibrium.isEquilibrium(ranking, simulationHistoryTable));
		
		result1 = prisonDilemma.play(agents.get(0), agents.get(1), true, true);
		simulationHistoryTable.addResult(result1);
		result2 = prisonDilemma.play(agents.get(2), agents.get(3), false, false);
		simulationHistoryTable.addResult(result2);
		result1 = prisonDilemma.play(agents.get(0), agents.get(2), true, true);
		simulationHistoryTable.addResult(result1);
		result2 = prisonDilemma.play(agents.get(1), agents.get(3), false, false);
		simulationHistoryTable.addResult(result2);
		result1 = prisonDilemma.play(agents.get(2), agents.get(1), true, true);
		simulationHistoryTable.addResult(result1);
		result2 = prisonDilemma.play(agents.get(0), agents.get(3), false, false);
		simulationHistoryTable.addResult(result2);
		
		ranking = copyAgents(agents);
		ranking = successQuantifier.createRanking(ranking, simulationHistoryTable);
		
		assertFalse(strategyEquilibrium.isEquilibrium(ranking, simulationHistoryTable));
		
		result1 = prisonDilemma.play(agents.get(0), agents.get(1), true, true);
		simulationHistoryTable.addResult(result1);
		result2 = prisonDilemma.play(agents.get(2), agents.get(3), false, false);
		simulationHistoryTable.addResult(result2);
		result1 = prisonDilemma.play(agents.get(0), agents.get(2), true, true);
		simulationHistoryTable.addResult(result1);
		result2 = prisonDilemma.play(agents.get(1), agents.get(3), false, false);
		simulationHistoryTable.addResult(result2);
		result1 = prisonDilemma.play(agents.get(2), agents.get(1), true, true);
		simulationHistoryTable.addResult(result1);
		result2 = prisonDilemma.play(agents.get(0), agents.get(3), false, false);
		simulationHistoryTable.addResult(result2);
		agents.get(3).setStrategy(titForTat);
		
		ranking = copyAgents(agents);
		ranking = successQuantifier.createRanking(ranking, simulationHistoryTable);

		assertTrue(strategyEquilibrium.isEquilibrium(ranking, simulationHistoryTable));
	}
	
	/**
	 * Tests the method isEquilibrium with a StrategyEquilibrium
	 * After 3 Rounds the Adaptionstep finish and the EquilibriumCriterion check for an Equilibrium.
	 * In this example the parameter alpha is 0.8 and the minRounds parameter is 4.
	 * It means by an amount of 4 agents that 3 agents are allowed to change their strategy.
	 */
	@Test
	public void testIsStrategyEquilibrium2() {
		CountingEquilibriumCriterion strategyEquilibrium = new StrategyEquilibrium(0.8, 4);
		successQuantifier = new TotalCapital();
		Game prisonDilemma = ConcreteGame.prisonersDilemma();
		
		GameResult result1 = prisonDilemma.play(agents.get(0), agents.get(1), true, true);
		simulationHistoryTable.addResult(result1);
		GameResult result2 = prisonDilemma.play(agents.get(2), agents.get(3), true, false);
		simulationHistoryTable.addResult(result2);
		result1 = prisonDilemma.play(agents.get(0), agents.get(2), true, true);
		simulationHistoryTable.addResult(result1);
		result2 = prisonDilemma.play(agents.get(1), agents.get(3), true, false);
		simulationHistoryTable.addResult(result2);
		result1 = prisonDilemma.play(agents.get(2), agents.get(1), true, true);
		simulationHistoryTable.addResult(result1);
		result2 = prisonDilemma.play(agents.get(0), agents.get(3), true, false);
		simulationHistoryTable.addResult(result2);
		
		List<Agent> ranking = copyAgents(agents);
		ranking = successQuantifier.createRanking(ranking, simulationHistoryTable);
		
		assertFalse(strategyEquilibrium.isEquilibrium(ranking, simulationHistoryTable));
		
		result1 = prisonDilemma.play(agents.get(0), agents.get(1), true, true);
		simulationHistoryTable.addResult(result1);
		result2 = prisonDilemma.play(agents.get(2), agents.get(3), false, false);
		simulationHistoryTable.addResult(result2);
		result1 = prisonDilemma.play(agents.get(0), agents.get(2), true, true);
		simulationHistoryTable.addResult(result1);
		result2 = prisonDilemma.play(agents.get(1), agents.get(3), false, false);
		simulationHistoryTable.addResult(result2);
		result1 = prisonDilemma.play(agents.get(2), agents.get(1), true, true);
		simulationHistoryTable.addResult(result1);
		result2 = prisonDilemma.play(agents.get(0), agents.get(3), false, false);
		simulationHistoryTable.addResult(result2);
		agents.get(0).setStrategy(grim);
		agents.get(1).setStrategy(titForTat);
		agents.get(2).setStrategy(titForTat);
		agents.get(3).setStrategy(grim);
		
		ranking = copyAgents(agents);
		ranking = successQuantifier.createRanking(ranking, simulationHistoryTable);
		
		assertFalse(strategyEquilibrium.isEquilibrium(ranking, simulationHistoryTable));
		
		result1 = prisonDilemma.play(agents.get(0), agents.get(1), true, true);
		simulationHistoryTable.addResult(result1);
		result2 = prisonDilemma.play(agents.get(2), agents.get(3), false, false);
		simulationHistoryTable.addResult(result2);
		result1 = prisonDilemma.play(agents.get(0), agents.get(2), true, true);
		simulationHistoryTable.addResult(result1);
		result2 = prisonDilemma.play(agents.get(1), agents.get(3), false, false);
		simulationHistoryTable.addResult(result2);
		result1 = prisonDilemma.play(agents.get(2), agents.get(1), true, true);
		simulationHistoryTable.addResult(result1);
		result2 = prisonDilemma.play(agents.get(0), agents.get(3), false, false);
		simulationHistoryTable.addResult(result2);
		agents.get(0).setStrategy(titForTat);
		agents.get(1).setStrategy(grim);
		agents.get(2).setStrategy(grim);
		
		ranking = copyAgents(agents);
		ranking = successQuantifier.createRanking(ranking, simulationHistoryTable);
		
		assertFalse(strategyEquilibrium.isEquilibrium(ranking, simulationHistoryTable));
		
		result1 = prisonDilemma.play(agents.get(0), agents.get(1), true, true);
		simulationHistoryTable.addResult(result1);
		result2 = prisonDilemma.play(agents.get(2), agents.get(3), false, false);
		simulationHistoryTable.addResult(result2);
		result1 = prisonDilemma.play(agents.get(0), agents.get(2), true, true);
		simulationHistoryTable.addResult(result1);
		result2 = prisonDilemma.play(agents.get(1), agents.get(3), false, false);
		simulationHistoryTable.addResult(result2);
		result1 = prisonDilemma.play(agents.get(2), agents.get(1), true, true);
		simulationHistoryTable.addResult(result1);
		result2 = prisonDilemma.play(agents.get(0), agents.get(3), false, false);
		simulationHistoryTable.addResult(result2);
		agents.get(1).setStrategy(titForTat);
		
		ranking = copyAgents(agents);
		ranking = successQuantifier.createRanking(ranking, simulationHistoryTable);
		
		assertFalse(strategyEquilibrium.isEquilibrium(ranking, simulationHistoryTable));
		
		result1 = prisonDilemma.play(agents.get(0), agents.get(1), true, true);
		simulationHistoryTable.addResult(result1);
		result2 = prisonDilemma.play(agents.get(2), agents.get(3), false, false);
		simulationHistoryTable.addResult(result2);
		result1 = prisonDilemma.play(agents.get(0), agents.get(2), true, true);
		simulationHistoryTable.addResult(result1);
		result2 = prisonDilemma.play(agents.get(1), agents.get(3), false, false);
		simulationHistoryTable.addResult(result2);
		result1 = prisonDilemma.play(agents.get(2), agents.get(1), true, true);
		simulationHistoryTable.addResult(result1);
		result2 = prisonDilemma.play(agents.get(0), agents.get(3), false, false);
		simulationHistoryTable.addResult(result2);
		agents.get(0).setStrategy(grim);
		
		ranking = copyAgents(agents);
		ranking = successQuantifier.createRanking(ranking, simulationHistoryTable);
		
		assertFalse(strategyEquilibrium.isEquilibrium(ranking, simulationHistoryTable));
		
		result1 = prisonDilemma.play(agents.get(0), agents.get(1), true, true);
		simulationHistoryTable.addResult(result1);
		result2 = prisonDilemma.play(agents.get(2), agents.get(3), false, false);
		simulationHistoryTable.addResult(result2);
		result1 = prisonDilemma.play(agents.get(0), agents.get(2), true, true);
		simulationHistoryTable.addResult(result1);
		result2 = prisonDilemma.play(agents.get(1), agents.get(3), false, false);
		simulationHistoryTable.addResult(result2);
		result1 = prisonDilemma.play(agents.get(2), agents.get(1), true, true);
		simulationHistoryTable.addResult(result1);
		result2 = prisonDilemma.play(agents.get(0), agents.get(3), false, false);
		simulationHistoryTable.addResult(result2);
		agents.get(3).setStrategy(titForTat);
		agents.get(0).setStrategy(titForTat);
		agents.get(2).setStrategy(titForTat);
		
		ranking = copyAgents(agents);
		ranking = successQuantifier.createRanking(ranking, simulationHistoryTable);

		assertTrue(strategyEquilibrium.isEquilibrium(ranking, simulationHistoryTable));
	}
	
	/**
	 * Creates an identical copy of the List of Agents
	 * @param agents List to copy
	 * @return The copy of the List
	 */
	private List<Agent> copyAgents(List<Agent> agents){
		List<Agent> copy = new ArrayList<Agent>();
		for(Agent agent : agents) {
			copy.add(agent);
		}
		return copy;
	}
}
