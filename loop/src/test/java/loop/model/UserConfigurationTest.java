package loop.model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import loop.model.simulationengine.ConcreteGame;
import loop.model.simulationengine.PayoffInLastAdapt;
import loop.model.simulationengine.RandomPairBuilder;
import loop.model.simulationengine.ReplicatorDynamic;
import loop.model.simulationengine.StrategyEquilibrium;

/**
 * This class holds tests for implementations of the {@link UserConfiguration} class.
 * 
 * @author Sebastian Feurer
 *
 */
public class UserConfigurationTest {
	List<Double> pairBuilderParameters;
	List<Double> successQuantifierParameters;
	List<Double> strategyAdjusterParameters;
	List<Double> equilibriumCriterionParameters;
	MulticonfigurationParameter multiconfigurationParameter;
	UserConfiguration testUserConfiguration;

	/**
	 * Initialize the UserConfiguration testUserConfiguration
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		pairBuilderParameters = new ArrayList<Double>();
		successQuantifierParameters = new ArrayList<Double>();
		strategyAdjusterParameters = new ArrayList<Double>();
		strategyAdjusterParameters.add(0.7);
		strategyAdjusterParameters.add(0.3);
		equilibriumCriterionParameters = new ArrayList<Double>();
		equilibriumCriterionParameters.add(0.1);
		equilibriumCriterionParameters.add(100.0);
		multiconfigurationParameter = new MulticonfigurationParameter(MulticonfigurationParameterType.ROUND_COUNT, 10, 30, 5);
		testUserConfiguration = new UserConfiguration("testUserConfiguration", 200, 16, true, "testPopulation",
				"Random Pair Builder", pairBuilderParameters, "Total Capital", successQuantifierParameters, "Replicator Dynamic",
				strategyAdjusterParameters, "Ranking Equilibrium", equilibriumCriterionParameters, 500, true, multiconfigurationParameter);
	}

	/**
	 * Tests the constructor of the class UserConfiguration and the attributes of the created object
	 */
	@Test
	public void testUserConfiguration() {
		List<Double> examplePairBuilderParameters = new ArrayList<Double>();
		List<Double> exampleSuccessQuantifierParameters = new ArrayList<Double>();
		List<Double> exampleStrategyAdjusterParameters = new ArrayList<Double>();
		exampleStrategyAdjusterParameters.add(0.345);
		exampleStrategyAdjusterParameters.add(0.283);
		List<Double> exampleEquilibriumCriterionParameters = new ArrayList<Double>();
		exampleEquilibriumCriterionParameters.add(0.2);
		exampleEquilibriumCriterionParameters.add(12.0);
		testUserConfiguration = new UserConfiguration("exampleUserConfiguration", 10, 32, false, 
				"examplePopulation", "Cooperation Considering Pair Builder", examplePairBuilderParameters, "Total Payoff", 
				exampleSuccessQuantifierParameters, "Preferential Adaption", exampleStrategyAdjusterParameters, 
				"Strategy Equilibrium", exampleEquilibriumCriterionParameters, 5000, false, null);
		assertTrue(testUserConfiguration.getGameName().equals("exampleUserConfiguration"));
		assertEquals(10, testUserConfiguration.getRoundCount());
		assertEquals(32, testUserConfiguration.getIterationCount());
		assertFalse(testUserConfiguration.getMixedAllowed());
		assertTrue(testUserConfiguration.getPopulationName().equals("examplePopulation"));
		assertTrue(testUserConfiguration.getPairBuilderName().equals("Cooperation Considering Pair Builder"));
		List<Double> testPairBuilderParameters = testUserConfiguration.getPairBuilderParameters();
		assertTrue(testPairBuilderParameters.equals(examplePairBuilderParameters));
		assertTrue(testUserConfiguration.getSuccessQuantifierName().equals("Total Payoff"));
		List<Double> testSuccessQuantifierParameters = testUserConfiguration.getSuccessQuantifierParameters();
		assertTrue(testSuccessQuantifierParameters.equals(exampleSuccessQuantifierParameters));
		assertTrue(testUserConfiguration.getStrategyAdjusterName().equals("Preferential Adaption"));
		List<Double> testStrategyAdjusterParameters = testUserConfiguration.getStrategyAdjusterParameters();
		assertEquals(0.345, testStrategyAdjusterParameters.get(0), 0);
		assertEquals(0.283, testStrategyAdjusterParameters.get(1), 0);
		assertTrue(testStrategyAdjusterParameters.equals(exampleStrategyAdjusterParameters));
		assertTrue(testUserConfiguration.getEquilibriumCriterionName().equals("Strategy Equilibrium"));
		List<Double> testEquilibriumCriterionParameters = testUserConfiguration.getEquilibriumCriterionParameters();
		assertEquals(0.2, testEquilibriumCriterionParameters.get(0), 0);
		assertEquals(12.0, testEquilibriumCriterionParameters.get(1), 0);
		assertTrue(testEquilibriumCriterionParameters.equals(exampleEquilibriumCriterionParameters));
		assertEquals(5000, testUserConfiguration.getMaxAdapts());
		assertFalse(testUserConfiguration.isMulticonfiguration());
		assertNull(testUserConfiguration.getMulticonfigurationParameter());
		assertEquals("", testUserConfiguration.getVariableParameterName());
		assertNull(testUserConfiguration.getParameterValues());
	}

	/**
	 * Tests the DefaultConfiguration and her attributes
	 */
	@Test
	public void testGetDefaultConfiguration() {
		testUserConfiguration = UserConfiguration.getDefaultConfiguration();
		assertTrue(testUserConfiguration.getGameName().equals(ConcreteGame.prisonersDilemma().getName()));
		assertEquals(200, testUserConfiguration.getRoundCount());
		assertEquals(8, testUserConfiguration.getIterationCount());
		assertTrue(testUserConfiguration.getMixedAllowed());
		assertTrue(testUserConfiguration.getPairBuilderName().equals(RandomPairBuilder.NAME));
		List<Double> testPairBuilderParameters = testUserConfiguration.getPairBuilderParameters();
		assertEquals(0, testPairBuilderParameters.size());
		assertEquals(PayoffInLastAdapt.NAME ,testUserConfiguration.getSuccessQuantifierName());
		List<Double> testSuccessQuantifierParameters = testUserConfiguration.getSuccessQuantifierParameters();
		assertEquals(0, testSuccessQuantifierParameters.size());
		assertEquals(ReplicatorDynamic.NAME ,testUserConfiguration.getStrategyAdjusterName());
		List<Double> testStrategyAdjusterParameters = testUserConfiguration.getStrategyAdjusterParameters();
		assertEquals(0.5, testStrategyAdjusterParameters.get(0), 0);
		assertEquals(0.5, testStrategyAdjusterParameters.get(1), 0);
		assertEquals(2, testStrategyAdjusterParameters.size());
		assertEquals(StrategyEquilibrium.NAME, testUserConfiguration.getEquilibriumCriterionName());
		List<Double> testEquilibriumCriterionParameters = testUserConfiguration.getEquilibriumCriterionParameters();
		assertEquals(0.005, testEquilibriumCriterionParameters.get(0), 0);
		assertEquals(50.0, testEquilibriumCriterionParameters.get(1), 0);
		assertEquals(2, testEquilibriumCriterionParameters.size());
		assertEquals(500, testUserConfiguration.getMaxAdapts());
		assertFalse(testUserConfiguration.isMulticonfiguration());
		assertNull(testUserConfiguration.getMulticonfigurationParameter());
		assertEquals("", testUserConfiguration.getVariableParameterName());
		assertNull(testUserConfiguration.getParameterValues());
	}

	/**
	 * Tests the implementation of the method getGameName
	 */
	@Test
	public void testGetGameName() {
		assertTrue(testUserConfiguration.getGameName().equals("testUserConfiguration"));
	}

	/**
	 * Tests the implementation of the method getRoundCount
	 */
	@Test
	public void testGetRoundCount() {
		assertEquals(200, testUserConfiguration.getRoundCount());
	}

	/**
	 * Tests the implementation of the method getIterationCount
	 */
	@Test
	public void testGetIterationCount() {
		assertEquals(16, testUserConfiguration.getIterationCount());
	}

	/**
	 * Tests the implementation of the method getMixedAllowed
	 */
	@Test
	public void testGetMixedAllowed() {
		assertTrue(testUserConfiguration.getMixedAllowed());
	}

	/**
	 * Tests the implementation of the method getPopulationName
	 */
	@Test
	public void testGetPopulationName() {
		assertTrue(testUserConfiguration.getPopulationName().equals("testPopulation"));
	}

	/**
	 * Tests the implementation of the method getPairBuilderName
	 */
	@Test
	public void testGetPairBuilderName() {
		assertTrue(testUserConfiguration.getPairBuilderName().equals("Random Pair Builder"));
	}

	/**
	 * Tests the implementation of the method getPairBuilderParameters
	 */
	@Test
	public void testGetPairBuilderParameters() {
		List<Double> testPairBuilderParameters = testUserConfiguration.getPairBuilderParameters();
		assertTrue(testPairBuilderParameters.equals(pairBuilderParameters));
	}

	/**
	 * Tests the implementation of the method getSuccessQuantifierName
	 */
	@Test
	public void testGetSuccessQuantifierName() {
		assertTrue(testUserConfiguration.getSuccessQuantifierName().equals("Total Capital"));
	}

	/**
	 * Tests the implementation of the method getSuccessQuantifierParameters
	 */
	@Test
	public void testGetSuccessQuantifierParameters() {
		List<Double> testSuccessQuantifierParameters = testUserConfiguration.getSuccessQuantifierParameters();
		assertTrue(testSuccessQuantifierParameters.equals(successQuantifierParameters));
	}

	/**
	 * Tests the implementation of the method getStrategyAdjusterName
	 */
	@Test
	public void testGetStrategyAdjusterName() {
		assertTrue(testUserConfiguration.getStrategyAdjusterName().equals("Replicator Dynamic"));
	}

	/**
	 * Tests the implementation of the method getStrategyAdjusterParameters
	 */
	@Test
	public void testGetStrategyAdjusterParameters() {
		List<Double> testStrategyAdjusterParameters = testUserConfiguration.getStrategyAdjusterParameters();
		assertEquals(0.7, testStrategyAdjusterParameters.get(0), 0);
		assertEquals(0.3, testStrategyAdjusterParameters.get(1), 0);
		assertTrue(testStrategyAdjusterParameters.equals(strategyAdjusterParameters));
	}

	/**
	 * Tests the implementation of the method getEquilibriumCriterionName
	 */
	@Test
	public void testGetEquilibriumCriterionName() {
		assertTrue(testUserConfiguration.getEquilibriumCriterionName().equals("Ranking Equilibrium"));
	}

	/**
	 * Tests the implementation of the method getEquilibriumCriterionParameters
	 */
	@Test
	public void testGetEquilibriumCriterionParameters() {
		List<Double> testEquilibriumCriterionParameters = testUserConfiguration.getEquilibriumCriterionParameters();
		assertEquals(0.1, testEquilibriumCriterionParameters.get(0), 0);
		assertEquals(100.0, testEquilibriumCriterionParameters.get(1), 0);
		assertTrue(testEquilibriumCriterionParameters.equals(equilibriumCriterionParameters));
	}

	/**
	 * Tests the implementation of the method getMaxAdapts
	 */
	@Test
	public void testGetMaxAdapts() {
		assertEquals(500, testUserConfiguration.getMaxAdapts());
	}

	/**
	 * Tests the implementation of the method isMulticonfiguration
	 */
	@Test
	public void testIsMulticonfiguration() {
		assertTrue(testUserConfiguration.isMulticonfiguration());
	}

	/**
	 * Tests the implementation of the method getMulticonfigurationParameter
	 */
	@Test
	public void testGetMulticonfigurationParameter() {
		assertEquals(multiconfigurationParameter, testUserConfiguration.getMulticonfigurationParameter());
	}

	/**
	 * Tests the implementation of the method getVariableParameterName
	 */
	@Test
	public void testGetVariableParameterName() {
		assertEquals(multiconfigurationParameter.getParameterName() ,testUserConfiguration.getVariableParameterName());
	}

	/**
	 * Tests the implementation of the method getParameterValues
	 */
	@Test
	public void testGetParameterValues() {
		assertEquals(multiconfigurationParameter.getParameterValues() ,testUserConfiguration.getParameterValues());
	}
}
