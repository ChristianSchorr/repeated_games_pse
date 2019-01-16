package loop.model.simulationengine;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import loop.model.simulationengine.distributions.BinomialDistribution;
import loop.model.simulationengine.distributions.DiscreteDistribution;
import loop.model.simulationengine.distributions.DiscreteUniformDistribution;
import loop.model.simulationengine.distributions.PoissonDistribution;
import loop.model.simulationengine.distributions.UniformFiniteDistribution;
import loop.model.simulationengine.strategies.PureStrategy;
import loop.model.simulationengine.strategies.Strategy;

/**
 * This class holds tests for implementations of the {@link Configuration} class.
 * 
 * @author Sebastian Feurer
 *
 */
public class ConfigurationTest {
	Configuration testConfiguration;
	Game game;
	PairBuilder pairBuilder;
	SuccessQuantifier successQuantifier;
	List<EngineSegment> segments;
	StrategyAdjuster strategyAdjuster;
	EquilibriumCriterion equilibriumCriterion;
	
	/**
	 * Initialize the Configuration class testConfiguration
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		game = ConcreteGame.prisonersDilemma();
		pairBuilder = new RandomPairBuilder();
		successQuantifier = new TotalPayoff();
		segments = new ArrayList<EngineSegment>();
		
		DiscreteDistribution capitalDistributionSegment1 = new DiscreteUniformDistribution(0, 50);   
        UniformFiniteDistribution<Strategy> strategyDistributionSegment1 = new UniformFiniteDistribution<Strategy>();
        Strategy groupTitForTat = PureStrategy.groupTitForTat();
        Strategy grim = PureStrategy.grim();
        strategyDistributionSegment1.addObject(groupTitForTat);
        strategyDistributionSegment1.addObject(grim);
		segments.add(new EngineSegment(32, 1, capitalDistributionSegment1, strategyDistributionSegment1));
		
		DiscreteDistribution capitalDistributionSegment2 = new BinomialDistribution(10, 100, 0.8);
		UniformFiniteDistribution<Strategy> strategyDistributionSegment2 = new UniformFiniteDistribution<Strategy>();
        Strategy alwaysCooperate = PureStrategy.alwaysCooperate();
        Strategy titForTat = PureStrategy.titForTat();
        strategyDistributionSegment2.addObject(alwaysCooperate);
        strategyDistributionSegment2.addObject(titForTat);
		segments.add(new EngineSegment(44, 1, capitalDistributionSegment2, strategyDistributionSegment2));
		
		DiscreteDistribution capitalDistributionSegment3 = new PoissonDistribution(50);
		UniformFiniteDistribution<Strategy> strategyDistributionSegment3 = new UniformFiniteDistribution<Strategy>();
		Strategy neverCooperate = PureStrategy.neverCooperate();
		strategyDistributionSegment3.addObject(neverCooperate);
		segments.add(new EngineSegment(60, 0, capitalDistributionSegment3, strategyDistributionSegment3));
		
		strategyAdjuster = new ReplicatorDynamic(0.4, 0.6);
		equilibriumCriterion = new RankingEquilibrium(0.5, 5);
		
		testConfiguration = new Configuration(game, 10, false, segments,
				pairBuilder, successQuantifier, strategyAdjuster, equilibriumCriterion, 100, 10);
	}

	@After
	public void tearDown() throws Exception {
	}
	
	/**
	 * Tests the implementation of the constructor of the class Configuration
	 */
	@Test
	public void testConfiguration() {
		Game testGame = new ConcreteGame("Test Dilemma", "This Dilemma only exist to"
				+ " test the ConcreteGame class.", 5, 4, -1, 2, 5, -1, 4, 2);
		PairBuilder cooperationConsideringPairBuilder = new CooperationConsideringPairBuilder();
		SuccessQuantifier totalCapital = new TotalCapital();
		List<EngineSegment> testSegments = new ArrayList<EngineSegment>();
		
		DiscreteDistribution capitalDistributionSegment1 = new DiscreteUniformDistribution(500, 550);   
        UniformFiniteDistribution<Strategy> strategyDistributionSegment1 = new UniformFiniteDistribution<Strategy>();
        Strategy titForTat = PureStrategy.titForTat();
        Strategy grim = PureStrategy.grim();
        strategyDistributionSegment1.addObject(titForTat);
        strategyDistributionSegment1.addObject(grim);
		testSegments.add(new EngineSegment(10, 5, capitalDistributionSegment1, strategyDistributionSegment1));
		
		DiscreteDistribution capitalDistributionSegment2 = new BinomialDistribution(-50, 30, 0.8);
		UniformFiniteDistribution<Strategy> strategyDistributionSegment2 = new UniformFiniteDistribution<Strategy>();
        Strategy neverCooperate = PureStrategy.neverCooperate();
        strategyDistributionSegment2.addObject(neverCooperate);
		testSegments.add(new EngineSegment(20, 0, capitalDistributionSegment2, strategyDistributionSegment2));
				
		StrategyAdjuster preferentialAdaption = new PreferentialAdaption(0.5, 0.5);
		EquilibriumCriterion strategyEquilibrium = new StrategyEquilibrium(0.4, 10);
		
		Configuration newConfiguration = new Configuration(testGame, 50, true, testSegments,
				cooperationConsideringPairBuilder, totalCapital, preferentialAdaption, strategyEquilibrium, 200, 50);
		
		assertTrue("The current game should be equal to the initialized one in the method testConfiguration",
				testGame.equals(newConfiguration.getGame()));
		assertTrue("RoundCount:" + newConfiguration.getRoundCount() + ". Expected: 50", 
				newConfiguration.getRoundCount() == 50);
		assertTrue("AllowsMixedStrategies:" + newConfiguration.allowsMixedStrategies() + "Espected: true",
				newConfiguration.allowsMixedStrategies());
		assertTrue("The current segment list should be equal to the initialized one in the method testConfiguration",
					testSegments.equals(newConfiguration.getSegments()));
		assertTrue("The current PairBuilder should be equal to the initialized one in the method testConfiguration",
					cooperationConsideringPairBuilder.equals(newConfiguration.getPairBuilder()));
		assertTrue("The current SuccessQuantifier should be equal to the initialized one in the method testConfiguration",
					totalCapital.equals(newConfiguration.getSuccessQuantifier()));
		assertTrue("The current StrategyAdjuster should be equal to the initialized one in the method testConfiguration",
					preferentialAdaption.equals(newConfiguration.getStrategyAdjuster()));
		assertTrue("The current EquilibriumCriterion should be equal to the initialized one in the method testConfiguration",
					strategyEquilibrium.equals(newConfiguration.getEquilibriumCriterion()));
		assertTrue("MaxAdapts:" + newConfiguration.getMaxAdapts() + ". Expected: 200", 
				newConfiguration.getMaxAdapts() == 200);
		assertTrue("TotalIteration:" + newConfiguration.getTotalIterations() + ". Expected: 50", 
				newConfiguration.getTotalIterations() == 50);
	}

	/**
	 * Tests the implementation of the method getGame 
	 */
	@Test
	public void testGetGame() {
		assertTrue("The current game should be equal to the initialized one", game.equals(testConfiguration.getGame()));
	}

	/**
	 * Tests the implementation of the method getRoundCount 
	 */
	@Test
	public void testGetRoundCount() {
		assertTrue("RoundCount:" + testConfiguration.getRoundCount() + ". Expected: 10", 
				testConfiguration.getRoundCount() == 10);
	}

	/**
	 * Tests the implementation of the method allowsMixedStrategies
	 */
	@Test
	public void testAllowsMixedStrategies() {
		assertFalse("AllowsMixedStrategies:" + testConfiguration.allowsMixedStrategies() + "Espected: false",
				testConfiguration.allowsMixedStrategies());
	}

	/**
	 * Tests the implementation of the method getSegments
	 */
	@Test
	public void testGetSegments() {
		assertTrue("The current segment list should be equal to the initialized one", segments.equals(testConfiguration.getSegments()));
	}

	/**
	 * Tests the implementation of the method getPairBuilder 
	 */
	@Test
	public void testGetPairBuilder() {
		assertTrue("The current PairBuilder should be equal to the initialized one", pairBuilder.equals(testConfiguration.getPairBuilder()));
	}

	/**
	 * Tests the implementation of the method getSuccessQuantifier 
	 */
	@Test
	public void testGetSuccessQuantifier() {
		assertTrue("The current SuccessQuantifier should be equal to the initialized one", successQuantifier.equals(testConfiguration.getSuccessQuantifier()));
	}


	/**
	 * Tests the implementation of the method getStrategyAdjuster
	 */
	@Test
	public void testGetStrategyAdjuster() {
		assertTrue("The current StrategyAdjuster should be equal to the initialized one", strategyAdjuster.equals(testConfiguration.getStrategyAdjuster()));
	}

	/**
	 * Tests the implementation of the method getEquilibriumCriterion 
	 */
	@Test
	public void testGetEquilibriumCriterion() {
		assertTrue("The current EquilibriumCriterion should be equal to the initialized one", equilibriumCriterion.equals(testConfiguration.getEquilibriumCriterion()));
	}

	/**
	 * Tests the implementation of the method getMaxAdapts 
	 */
	@Test
	public void testGetMaxAdapts() {
		assertTrue("MaxAdapts:" + testConfiguration.getMaxAdapts() + ". Expected: 100", 
				testConfiguration.getMaxAdapts() == 100);
	}

	/**
	 * Tests the implementation of the method getTotalIterations
	 */
	@Test
	public void testGetTotalIterations() {
		assertTrue("TotalIteration:" + testConfiguration.getTotalIterations() + ". Expected: 10", 
				testConfiguration.getTotalIterations() == 10);
	}
}
