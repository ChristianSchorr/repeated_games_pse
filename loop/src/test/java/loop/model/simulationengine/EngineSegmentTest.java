package loop.model.simulationengine;

import static org.junit.Assert.*;

import java.util.Collection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import loop.model.simulationengine.distributions.DiscreteDistribution;
import loop.model.simulationengine.distributions.DiscreteUniformDistribution;
import loop.model.simulationengine.distributions.UniformFiniteDistribution;
import loop.model.simulationengine.strategies.PureStrategy;
import loop.model.simulationengine.strategies.Strategy;

/**
 * This class holds tests for implementations of the {@link EngineSegment} class.
 * 
 * @author Sebastian Feurer
 *
 */
public class EngineSegmentTest {
	EngineSegment testEngineSegment;
	Strategy groupTitForTat;
	Strategy grim;

	/**
	 * Creates a new DiscreteDistribution capitalDistribution and a UniformFiniteDistribution<Strategy>
	 * strategyDistribution to initialize the EngineSegment testEngineSegment
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		DiscreteDistribution capitalDistribution = new DiscreteUniformDistribution(0, 50);
	       
        UniformFiniteDistribution<Strategy> strategyDistribution = new UniformFiniteDistribution<Strategy>();
        groupTitForTat = PureStrategy.groupTitForTat();
        grim = PureStrategy.grim();
        strategyDistribution.addObject(groupTitForTat);
        strategyDistribution.addObject(grim);
		
		testEngineSegment = new EngineSegment(32, 0, capitalDistribution, strategyDistribution);
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Tests the constructor of the class EngineSegment and verify the attributs.
	 */
	@Test
	public void testEngineSegment() {
		DiscreteDistribution capDistribution = new DiscreteUniformDistribution(-25, 0);
	       
        UniformFiniteDistribution<Strategy> stratDistribution = new UniformFiniteDistribution<Strategy>();
        Strategy titForTat = PureStrategy.titForTat();
        Strategy neverCooperate = PureStrategy.neverCooperate();
        Strategy alwaysCooperate = PureStrategy.alwaysCooperate();
        stratDistribution.addObject(titForTat);
        stratDistribution.addObject(neverCooperate);
		stratDistribution.addObject(alwaysCooperate);
        
		EngineSegment engineSegment = new EngineSegment(42, 2, capDistribution, stratDistribution);
		assertTrue("Agentcount current: " + engineSegment.getAgentCount() + ".Expected: 42.", engineSegment.getAgentCount() == 42 );
		assertTrue("GroupId current: " + engineSegment.getGroupId() + ".Expected: 2.", engineSegment.getGroupId() == 2);
		DiscreteUniformDistribution capitalDistribution = (DiscreteUniformDistribution) engineSegment.getCapitalDistribution();
		assertTrue("capitalDistribution is not like the initialized", capitalDistribution.getSupportMin(0.0) == -25);
		assertTrue("capitalDistribution is not like the initialized", capitalDistribution.getSupportMax(0.0) == 0);
		Collection<Strategy> strategies = engineSegment.getStrategyDistribution().getSupport();
		assertTrue("The StrategyDistribution is not equal to the inizialized one",
				strategies.contains(titForTat) && strategies.contains(neverCooperate)
						&& strategies.contains(alwaysCooperate));
	
	}

	/**
	 * Tests the method getAgentCount.
	 */
	@Test
	public void testGetAgentCount() {
		assertTrue("Agentcount current: " + testEngineSegment.getAgentCount() + ".Expected: 32.", testEngineSegment.getAgentCount() == 32 );
	}

	/**
	 * Tests the method getGroupId.
	 */
	@Test
	public void testGetGroupId() {
		assertTrue("GroupId current: " + testEngineSegment.getGroupId() + ".Expected: 0.", testEngineSegment.getGroupId() == 0);
	}

	/**
	 * Tests the method getCapitalDistribution.
	 */
	@Test
	public void testGetCapitalDistribution() {
		DiscreteUniformDistribution capitalDistribution = (DiscreteUniformDistribution) testEngineSegment.getCapitalDistribution();
		assertTrue("capitalDistribution is not like the initialized", capitalDistribution.getSupportMin(0.0) == 0);
		assertTrue("capitalDistribution is not like the initialized", capitalDistribution.getSupportMax(0.0) == 50);
	}

	/**
	 * Tests the method getStrategyDistribution.
	 */
	@Test
	public void testGetStrategyDistribution() {
		Collection<Strategy> strategies = testEngineSegment.getStrategyDistribution().getSupport();
		assertTrue("The StrategyDistribution is not equal to the inizialized one",
				strategies.contains(groupTitForTat) && strategies.contains(grim));
	}

}
