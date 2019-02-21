package loop.model.simulationengine;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import loop.model.simulationengine.strategies.PureStrategy;
import loop.model.simulationengine.strategies.Strategy;

/**
 * This class holds tests for implementations of the {@link IterationResult} class.
 * 
 * @author Sebastian Feurer
 *
 */
public class IterationResultTest {
	IterationResult iterationResult;
	int adapts;
	double efficiency;
	boolean equilibriumReached;
	SimulationHistory history;
	List<Agent> agents = new ArrayList<Agent>();
	List<String> strategyNames = new ArrayList<String>();
	Map<String, List <double[]>> strategyPortions = new HashMap<String, List <double[]>>();
	Map<String, List <Integer>> groupCapitals = new HashMap<String, List <Integer>>();

	/**
	 * Initialize the IterationResult
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		Strategy titForTat = PureStrategy.titForTat();
		Strategy grim = PureStrategy.grim();
		
		strategyNames.add(titForTat.getName());
		strategyNames.add(grim.getName());

		//just for testing, is not valid with the given simulationHistory
		double[] portion1 = {0.5, 0.5}; 
		double[] portion2 = {0.75, 0.25};
		List <double[]> testgroupPortion = new ArrayList <double[]>();
		testgroupPortion.add(portion1);
		testgroupPortion.add(portion2);
		List <double[]> normalPeoplePortion = new ArrayList <double[]>();
		double[] portion3 = {0.33, 0.67}; 
		double[] portion4 = {0.1, 0.9};
		normalPeoplePortion.add(portion3);
		normalPeoplePortion.add(portion4);
		strategyPortions.put("Testgroup", testgroupPortion);
		strategyPortions.put("normalPeople", normalPeoplePortion);
		
		List <Integer> testgroupCapital = new ArrayList<Integer>();
		testgroupCapital.add(10);
		testgroupCapital.add(100);
		List <Integer> normalPeopleCapital = new ArrayList<Integer>();
		normalPeopleCapital.add(50);
		normalPeopleCapital.add(12);
		
		groupCapitals.put("Testgroup", testgroupCapital);
		groupCapitals.put("normalPeople", normalPeopleCapital);
		
		equilibriumReached = true;
		efficiency = 0.5;
		adapts = 20;
		
        iterationResult = new IterationResult(equilibriumReached, efficiency, adapts, strategyNames, strategyPortions, groupCapitals);
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Tests the constructor of the class IterationResult and the initialized attributs
	 */
	@Test
	public void testIterationResult() {
	    testEquilibriumReached(iterationResult);
	    testGetEfficiency(iterationResult);
	    testGetAdapts(iterationResult);
	    testGetStrategyPortions(iterationResult);
	    testGetStrategyNames(iterationResult);
	    testGetGroupCapitals(iterationResult);
	    
	}

	
	/**
	 * Tests the implementation of the method equilibriumReached
	 */
	public void testEquilibriumReached(IterationResult result) {
		assertTrue(result.equilibriumReached());
	}

	/**
	 * Tests the implementation of the method getEfficiency
	 */
	public void testGetEfficiency(IterationResult result) {
		assertTrue(0.5 == result.getEfficiency());
	}

	/**
	 * Tests the implementation of the method getAdapts
	 */
	public void testGetAdapts(IterationResult result) {
		assertEquals(20, result.getAdapts());
	}
	
	/**
     * Tests the implementation of the method getStrategyPortions
     */
    public void testGetStrategyPortions(IterationResult result) {
        assertEquals(strategyPortions, result.getStrategyPortions());
    }
    
    /**
     * Tests the implementation of the method getStrategyNames
     */
    public void testGetStrategyNames(IterationResult result) {
        assertEquals(strategyNames, result.getStrategyNames());
    }

    /**
     * Tests the implementation of the method getStrategyNames
     */
    public void testGetGroupCapitals(IterationResult result) {
        assertEquals(groupCapitals, result.getGroupCapitals());
    }
}
