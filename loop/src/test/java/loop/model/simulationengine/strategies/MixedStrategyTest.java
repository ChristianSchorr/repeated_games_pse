package loop.model.simulationengine.strategies;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import loop.model.simulationengine.Agent;
import loop.model.simulationengine.GameResult;
import loop.model.simulationengine.SimulationHistoryTable;

/**
 * This class holds tests for implementations of the {@link MixedStrategy} class.
 * 
 * @author Sebastian Feurer
 *
 */
public class MixedStrategyTest {
	MixedStrategy testMixedStrategy;
	List<Strategy> strategies;
	List<Double> probabilities;
	Strategy titfortat;
	Strategy grim;
	Strategy neverCooperate;
	
	/**
	 * Initialize the MixedStrategy testMixedStrategy
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		titfortat = PureStrategy.titForTat();
		grim = PureStrategy.grim();
		neverCooperate = PureStrategy.neverCooperate();
		strategies = new ArrayList<Strategy>();
		probabilities = new ArrayList<Double>();
		strategies.add(titfortat);
		strategies.add(grim);
		strategies.add(neverCooperate);
		probabilities.add(0.5);
		probabilities.add(0.3);
		probabilities.add(0.2);
		testMixedStrategy = new MixedStrategy("tit-for-tat/grim/neverCooperate",
				"This MixedStrategy exist only for test reasons.", strategies, probabilities);
	}
	
	/**
	 * Tests the constructor of the class MixedStrategy and the attributes of the created object
	 */
	@Test
	public void testMixedStrategy() {
		Strategy grouptitfortat = PureStrategy.groupTitForTat();
		Strategy groupgrim = PureStrategy.groupGrim();
		Strategy alwaysCooperate = PureStrategy.alwaysCooperate();
		strategies.add(grouptitfortat);
		strategies.add(groupgrim);
		strategies.add(alwaysCooperate);
		probabilities.add(0.3);
		probabilities.add(0.15);
		probabilities.add(0.1);
		probabilities.set(0, 0.2);
		probabilities.set(1, 0.15);
		probabilities.set(2, 0.1);
		MixedStrategy mixedStrategy = new MixedStrategy("tit-for-tat/grim/neverCooperate/grouptitfortat/groupgrim/alwlaysCooperate",
				"This MixedStrategy exist also only for test reasons.", strategies, probabilities);
		assertTrue(mixedStrategy.getName().equals("tit-for-tat/grim/neverCooperate/grouptitfortat/groupgrim/alwlaysCooperate"));
		assertTrue(mixedStrategy.getDescription().equals("This MixedStrategy exist also only for test reasons."));
		assertEquals(6, mixedStrategy.getSize());
		
		List<Strategy> componentsstrategies = mixedStrategy.getComponentStrategies();
		assertTrue(componentsstrategies.get(0).equals(titfortat));
		assertTrue(componentsstrategies.get(1).equals(grim));
		assertTrue(componentsstrategies.get(2).equals(neverCooperate));
		assertTrue(componentsstrategies.get(3).equals(grouptitfortat));
		assertTrue(componentsstrategies.get(4).equals(groupgrim));
		assertTrue(componentsstrategies.get(5).equals(alwaysCooperate));
		List<Double> components = mixedStrategy.getComponents();
		assertEquals(0.2, components.get(0), 0);
		assertEquals(0.15, components.get(1), 0);
		assertEquals(0.1, components.get(2), 0);
		assertEquals(0.3, components.get(3), 0);
		assertEquals(0.15, components.get(4), 0);
		assertEquals(0.1, components.get(5), 0);
		
		assertEquals(Math.sqrt(0.2*0.2+0.3*0.3+2*0.1*0.1+2*0.15*0.15), mixedStrategy.getEuclideanNorm(),0);

		assertEquals(1, mixedStrategy.getSumNorm(), 0);
	
		mixedStrategy.mutliplyBy(2);
		components = mixedStrategy.getComponents();
		assertEquals(0.4, components.get(0), 0);
		assertEquals(0.3, components.get(1), 0);
		assertEquals(0.2, components.get(2), 0);
		assertEquals(0.6, components.get(3), 0);
		assertEquals(0.3, components.get(4), 0);
		assertEquals(0.2, components.get(5), 0);
	}

	/**
	 * Tests the constructor of the class MixedStrategy with illegal arguments(4 strategies and 3 probabilities)
	 */
	@Test (expected = IllegalArgumentException.class)
	public void testMixedStrategy2() {
		List <Strategy> falsestrategies = new ArrayList <Strategy>();
		falsestrategies.addAll(strategies);
		falsestrategies.add(neverCooperate);
		
		MixedStrategy mixedStrategy = new MixedStrategy("tit-for-tat/grim/neverCooperate/alwlaysCooperate",
				"This MixedStrategy exist also only for test reasons.", falsestrategies, probabilities);
	}

	/**
	 * Tests the constructor of the class MixedStrategy with illegal arguments(probability > 1)
	 */
	@Test (expected = IllegalArgumentException.class)
	public void testMixedStrategy3() {
		List <Double> falseprobabilities = new ArrayList <Double>();
		falseprobabilities.addAll(probabilities);
		falseprobabilities.set(0, 1.1);
		MixedStrategy mixedStrategy = new MixedStrategy("tit-for-tat/grim/alwlaysCooperate",
				"This MixedStrategy exist also only for test reasons.", strategies, falseprobabilities);
	}
	
	/**
	 * Tests the constructor of the class MixedStrategy with illegal arguments(sum of probabilities > 1)
	 */
	@Test (expected = IllegalArgumentException.class)
	public void testMixedStrategy4() {
		List <Double> falseprobabilities = new ArrayList <Double>();
		falseprobabilities.addAll(probabilities);
		falseprobabilities.set(0, 0.9);
		falseprobabilities.set(1, 0.7);
		MixedStrategy mixedStrategy = new MixedStrategy("tit-for-tat/grim/alwlaysCooperate",
				"This MixedStrategy exist also only for test reasons.", strategies, falseprobabilities);
	}
	
	/**
	 * Tests the implementation of the method getName
	 */
	@Test
	public void testGetName() {
		assertTrue(testMixedStrategy.getName().equals("tit-for-tat/grim/neverCooperate"));
	}

	/**
	 * Tests the implementation of the method getDescription
	 */
	@Test
	public void testGetDescription() {
		assertTrue(testMixedStrategy.getDescription().equals("This MixedStrategy exist only for test reasons."));
	}

	/**
	 * Tests the implementation of the method getSize
	 */
	@Test
	public void testGetSize() {
		assertEquals(3, testMixedStrategy.getSize());
	}

	/**
	 * Tests the implementation of the method getComponent
	 */
	@Test
	public void testGetComponent() {
		assertEquals(0.5, testMixedStrategy.getComponent(0), 0);
		assertEquals(0.3, testMixedStrategy.getComponent(1), 0);
		assertEquals(0.2, testMixedStrategy.getComponent(2), 0);
	}

	/**
	 * Tests the implementation of the method setComponent
	 */
	@Test
	public void testSetComponent() {
		testMixedStrategy.setComponent(0, 0.2);
		testMixedStrategy.setComponent(1, 0.333);
		testMixedStrategy.setComponent(2, 0.467);
		assertEquals(0.2, testMixedStrategy.getComponent(0), 0);
		assertEquals(0.333, testMixedStrategy.getComponent(1), 0);
		assertEquals(0.467, testMixedStrategy.getComponent(2), 0);
	}

	/**
	 * Tests the implementation of the method getComponents
	 */
	@Test
	public void testGetComponents() {
		List<Double> components = testMixedStrategy.getComponents();
		assertEquals(0.5, components.get(0), 0);
		assertEquals(0.3, components.get(1), 0);
		assertEquals(0.2, components.get(2), 0);
	}

	/**
	 * Tests the implementation of the method getComponentStrategies
	 */
	@Test
	public void testGetComponentStrategies() {
		List<Strategy> components = testMixedStrategy.getComponentStrategies();
		assertTrue(components.get(0).equals(titfortat));
		assertTrue(components.get(1).equals(grim));
		assertTrue(components.get(2).equals(neverCooperate));
	}

	/**
	 * Tests the implementation of the method clone
	 */
	@Test
	public void testClone() {
		MixedStrategy cloneMixedStrategy = (MixedStrategy) testMixedStrategy.clone();
		List<Strategy> componentsstrategies = cloneMixedStrategy.getComponentStrategies();
		assertTrue(componentsstrategies.get(0).equals(titfortat));
		assertTrue(componentsstrategies.get(1).equals(grim));
		assertTrue(componentsstrategies.get(2).equals(neverCooperate));
		List<Double> components = cloneMixedStrategy.getComponents();
		assertEquals(0.5, components.get(0), 0);
		assertEquals(0.3, components.get(1), 0);
		assertEquals(0.2, components.get(2), 0);
		assertTrue(cloneMixedStrategy.getName().equals("tit-for-tat/grim/neverCooperate"));
		assertTrue(cloneMixedStrategy.getDescription().equals("This MixedStrategy exist only for test reasons."));
		assertEquals(3, cloneMixedStrategy.getSize());
	}

	/**
	 * Tests the implementation of the method getEuclideanNorm
	 */
	@Test
	public void testGetEuclideanNorm() {
		assertEquals(Math.sqrt(0.5*0.5+0.3*0.3+0.2*0.2), testMixedStrategy.getEuclideanNorm(),0);
	}

	/**
	 * Tests the implementation of the method getSumNorm
	 */
	@Test
	public void testGetSumNorm() {
		assertEquals(1, testMixedStrategy.getSumNorm(), 0);
	}

	/**
	 * Tests the implementation of the method multliplyBy
	 */
	@Test
	public void testMutliplyBy() {
		testMixedStrategy.mutliplyBy(2.5);
		List<Double> components = testMixedStrategy.getComponents();
		assertEquals(1.25, components.get(0), 0);
		assertEquals(0.75, components.get(1), 0);
		assertEquals(0.5, components.get(2), 0);		
	}

	/**
	 * Tests the implementation of the method add
	 */
	@Test
	public void testAdd() {
		List<Double> addprobabilities = new ArrayList<Double>();
		addprobabilities.add(0.23);
		addprobabilities.add(0.74);
		addprobabilities.add(0.03);
		MixedStrategy addMixedStrategy = new MixedStrategy("addMixedStrategy", "This MixedStrategy is to test the method add",
				strategies, addprobabilities);
		testMixedStrategy.add(addMixedStrategy);
		List<Double> components = testMixedStrategy.getComponents();
		assertEquals(0.73, components.get(0), 0);
		assertEquals(1.04, components.get(1), 0);
		assertEquals(0.23, components.get(2), 0);
	}
	
	/**
	 * Tests the implementation of the method add
	 */
	@Test(expected = IllegalArgumentException.class) 
	public void testAdd2() { 
		Strategy alwaysCooperate = PureStrategy.alwaysCooperate();
		strategies.add(alwaysCooperate);
		List<Double> addprobabilities = new ArrayList<Double>();
		addprobabilities.add(0.23);
		addprobabilities.add(0.74);
		addprobabilities.add(0.03);
		addprobabilities.add(0.0);
		MixedStrategy addMixedStrategy = new MixedStrategy("addMixedStrategy", "This MixedStrategy is to test the method add",
				strategies, addprobabilities);
		testMixedStrategy.add(addMixedStrategy);
	}

	/**
	 * Tests the implementation of the method isCooperative
	 */
	@Test
	public void testIsCooperative() {
		Agent player = new Agent(0, testMixedStrategy, 1);
		Agent opponent = new Agent(30, testMixedStrategy, 2);
		SimulationHistoryTable history = new SimulationHistoryTable();
		
		testMixedStrategy.setComponent(1, 0.5);
		testMixedStrategy.setComponent(2, 0.0);
		
		assertTrue(testMixedStrategy.isCooperative(player, opponent, history));
		
		GameResult result = new GameResult(player, opponent, true, false, 0, 8);
		history.addResult(result);
		
		assertFalse(testMixedStrategy.isCooperative(player, opponent, history));
		
		result = new GameResult(player, opponent, false, true, 8, 0);
		history.addResult(result);
		
		testMixedStrategy.setComponent(0, 1.0);
		testMixedStrategy.setComponent(1, 0.0);
		
		assertTrue(testMixedStrategy.isCooperative(player, opponent, history));
		
		testMixedStrategy.setComponent(2, 1.0);
		testMixedStrategy.setComponent(0, 0.0);
		
		assertFalse(testMixedStrategy.isCooperative(player, opponent, history));
	}

	/**
	 * Tests the implementation of the method getCooperationProbability
	 */
	@Test
	public void testGetCooperationProbability() {
		Agent player = new Agent(0, testMixedStrategy, 1);
		Agent opponent = new Agent(30, testMixedStrategy, 2);
		SimulationHistoryTable history = new SimulationHistoryTable();
		
		assertEquals(0.8, testMixedStrategy.getCooperationProbability(player, opponent, history), 0);
		assertEquals(0.8, testMixedStrategy.getCooperationProbability(opponent, player, history), 0);
		
		GameResult result = new GameResult(player, opponent, true, false, 0, 8);
		history.addResult(result);
		
		assertEquals(0, testMixedStrategy.getCooperationProbability(player, opponent, history), 0);
		assertEquals(0.8, testMixedStrategy.getCooperationProbability(opponent, player, history), 0);
		
		result = new GameResult(player, opponent, false, true, 8, 0);
		history.addResult(result);
		
		assertEquals(0.5, testMixedStrategy.getCooperationProbability(player, opponent, history), 0);
		assertEquals(0, testMixedStrategy.getCooperationProbability(opponent, player, history), 0);
	}
}
