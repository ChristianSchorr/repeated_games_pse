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
	}

	/**
	 * Tests the implementation of the method getGame 
	 */
	@Test
	public void testGetGame() {
	}

	/**
	 * Tests the implementation of the method getRoundCount 
	 */
	@Test
	public void testGetRoundCount() {
	}

	/**
	 * Tests the implementation of the method allowsMixedStrategies
	 */
	@Test
	public void testAllowsMixedStrategies() {
	}

	/**
	 * Tests the implementation of the method getSegments
	 */
	@Test
	public void testGetSegments() {
	}

	/**
	 * Tests the implementation of the method getPairBuilder 
	 */
	@Test
	public void testGetPairBuilder() {
	}

	/**
	 * Tests the implementation of the method getSuccessQuantifier 
	 */
	@Test
	public void testGetSuccessQuantifier() {
	}

	/**
	 * Tests the implementation of the method getStrategyAdjuster
	 */
	@Test
	public void testGetStrategyAdjuster() {
	}

	/**
	 * Tests the implementation of the method getEquilibriumCriterion 
	 */
	@Test
	public void testGetEquilibriumCriterion() {
	}

	/**
	 * Tests the implementation of the method getMaxAdapts 
	 */
	@Test
	public void testGetMaxAdapts() {
	}

	/**
	 * Tests the implementation of the method getTotalIterations
	 */
	@Test
	public void testGetTotalIterations() {
	}

}
