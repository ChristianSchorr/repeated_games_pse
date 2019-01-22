package loop.model.simulationengine.distributions;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import loop.model.simulationengine.strategies.PureStrategy;
import loop.model.simulationengine.strategies.Strategy;

/**
 * 
 * Contains test cases for finite distributions over an arbitrary type of objects.
 * 
 * @author Luc Mercatoris
 *
 */

public class FiniteDistributionTest {
	private UniformFiniteDistribution<Strategy> uniformFiniteDistribution = new UniformFiniteDistribution<Strategy>();
	private ArrayList<Strategy> strategyArray = new ArrayList<Strategy>();
	private ArrayList<Strategy> strategiesToAdd = new ArrayList<Strategy>();
	Strategy never;
	Strategy grim;
	Strategy groupTitForTat;
	Strategy always;

	@Before
	public void setUp() throws Exception {
		never = PureStrategy.neverCooperate();		
        grim = PureStrategy.grim();
        groupTitForTat = PureStrategy.groupTitForTat();
        always = PureStrategy.alwaysCooperate();
        
        strategiesToAdd.add(grim);
        strategiesToAdd.add(always);
    }

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testConstructors() {
		
	}
	
	/**
	 * Tests both constructors for a UniformFiniteDistribution over the type Strategy
	 */
	@Test
	public void testUniformFiniteDistribution() {
		initializeEmptyList();
		initializeList(strategyArray);      
	}
	
	/**
	 * Tests the methods addObject, addObjects and removeObject
	 */
	@Test
	public void testAddRemove() {
		initializeList(strategyArray);
		
		//test addObject
		strategyArray.add(groupTitForTat);
		uniformFiniteDistribution.addObject(groupTitForTat);
		assertEquals(uniformFiniteDistribution.getSupport(), this.strategyArray);
		
		//test removeObject
		strategyArray.remove(grim);
		uniformFiniteDistribution.removeObject(grim);
		assertEquals(uniformFiniteDistribution.getSupport(), this.strategyArray);
		
		//test addObjects
		strategyArray.add(grim);
		strategyArray.add(always);
		uniformFiniteDistribution.addObjects(strategiesToAdd);
		assertEquals(uniformFiniteDistribution.getSupport(), strategyArray);
	}
	
	/**
	 * Test the pickAndRemove method
	 */
	@Test
	public void testPickAndRemove() {
		initializeList(strategyArray);
		Strategy pickedStrategy = uniformFiniteDistribution.pickAndRemove();
		assertTrue(strategyArray.contains(pickedStrategy));
		assertTrue(!(uniformFiniteDistribution.getSupport().contains(pickedStrategy)));
		assertTrue(strategyArray.size() == uniformFiniteDistribution.getSupport().size() + 1);
	}
	
	/**
	 * Test the getProbability method
	 */
	@Test
	public void testGetProbability() {
		initializeList(strategyArray);
		uniformFiniteDistribution.addObjects(strategiesToAdd);
		for (Strategy s: uniformFiniteDistribution.getSupport()) {
			assertTrue(uniformFiniteDistribution.getProbability(s) == 0.25);
		}		
	}
		
	/**
	 * Test the Picker of the {@link UniformFiniteDistribution} class.
	 */
	@Test
	public void testPicker() {
		initializeList(strategyArray);
		Picker<Strategy> uniformPicker = uniformFiniteDistribution.getPicker();
		Strategy pickedStrategy = uniformPicker.pickOne();
		assertTrue(strategyArray.contains(pickedStrategy));			
		int size = 20;
		assertTrue(uniformPicker.pickMany(size).size() == size);
	}
	
	private void initializeEmptyList() {
		uniformFiniteDistribution = new UniformFiniteDistribution<Strategy>();
		assertEquals(uniformFiniteDistribution.getSupport(), strategyArray);
		assertTrue(uniformFiniteDistribution.getSupport().isEmpty() == true);
	}
	
	private void initializeList(ArrayList<Strategy> strategyArray) {
		strategyArray.add(never);
		strategyArray.add(grim);
		uniformFiniteDistribution = new UniformFiniteDistribution<Strategy>(strategyArray);
		assertEquals(uniformFiniteDistribution.getSupport(), this.strategyArray);
	}	
	

}
