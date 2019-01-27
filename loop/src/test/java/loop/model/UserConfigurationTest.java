package loop.model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class UserConfigurationTest {
	List<Double> pairBuilderParameters;
	List<Double> successQuantifierParameters;
	List<Double> strategyAdjusterParameters;
	List<Double> equilibriumCriterionParameters;
	MulticonfigurationParameter multiconfigurationParameter;
	UserConfiguration testUserConfiguration;

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
		multiconfigurationParameter = new MulticonfigurationParameter(MulticonfigurationParameterType.ITERATION_COUNT, 10, 30, 5);
		testUserConfiguration = new UserConfiguration("testUserConfiguration", 200, 16, true, "testPopulation",
				"Random Pair Builder", pairBuilderParameters, "Total Capital", successQuantifierParameters, "Replicator Dynamic",
				strategyAdjusterParameters, "Ranking Equilibrium", equilibriumCriterionParameters, 500, true, multiconfigurationParameter);
	}

	@Test
	public void testUserConfiguration() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetDefaultConfiguration() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetGameName() {
		assertTrue(testUserConfiguration.getGameName().equals("testUserConfiguration"));
	}

	@Test
	public void testGetRoundCount() {
		assertEquals(200, testUserConfiguration.getRoundCount());
	}

	@Test
	public void testGetIterationCount() {
		assertEquals(16, testUserConfiguration.getIterationCount());
	}

	@Test
	public void testGetMixedAllowed() {
		assertTrue(testUserConfiguration.getMixedAllowed());
	}

	@Test
	public void testGetPopulationName() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetPairBuilderName() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetPairBuilderParameters() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetSuccessQuantifierName() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetSuccessQuantifierParameters() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetStrategyAdjusterName() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetStrategyAdjusterParameters() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetEquilibriumCriterionName() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetEquilibriumCriterionParameters() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetMaxAdapts() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsMulticonfiguration() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetMulticonfigurationParameter() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetVariableParameterName() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetParameterValues() {
		fail("Not yet implemented");
	}

}
