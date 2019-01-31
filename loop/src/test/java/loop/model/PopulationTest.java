package loop.model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the methods of the {@link Population} class.
 * 
 * @author Luc Mercatoris
 * 
 */
public class PopulationTest {
	Population population;
	List<Group> groups = new ArrayList<Group>();
	List<Integer> groupSizes = new ArrayList<Integer>();
	Group group1;
	Group group2;
	
	//variables for group1
	List<Segment> segments1 = new ArrayList<Segment>();
	List<Double> segmentSizes1 = new ArrayList<Double>();
	List<Double> binomialDistributionParameters = new ArrayList<Double>();	
	List<String> strategyNames1 = new ArrayList<String>();	
	Segment segment1;
	
	//variables for group2
	List<Segment> segments2 = new ArrayList<Segment>();
	List<Double> segmentSizes2 = new ArrayList<Double>();
	List<Double> poissonDistributionParameters = new ArrayList<Double>();
	List<String> strategyNames2 = new ArrayList<String>();
	Segment segment2;
	
	@Before
	public void setUp() throws Exception {
		//setup group1
		binomialDistributionParameters.add(0.0);
		binomialDistributionParameters.add(50.0);
		binomialDistributionParameters.add(0.8);
		strategyNames1.add("titForTat");
		strategyNames1.add("never");
		segmentSizes1.add(1.0);		
		segment1 = new Segment("Binomial Distribution", binomialDistributionParameters, strategyNames1);
		segments1.add(segment1);
		group1 = new Group("group1", "group1 description", segments1, segmentSizes1, true);
		
		//setup group2
		poissonDistributionParameters.add(1.0);
		strategyNames2.add("grim");
		segmentSizes2.add(1.0);
		segment2 = new Segment("Poisson Distribution", poissonDistributionParameters, strategyNames2);
		segments2.add(segment2);
		group2 = new Group("group2", "group2 description", segments2, segmentSizes2, false);
				
		//setup population
		groups.add(group1);
		groups.add(group2);
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Tests a valid constructor of the {@link Population} class
	 */
	@Test
	public void testValidConstructor() {
		groupSizes.add(20);
		groupSizes.add(30);	
		testPopulation(population);
	}
	
	/**
	 * Tests a non valid constructor of the {@link Population} class. The number of groups is different to
	 * the number of group sizes
	 */
	@Test
	public void testNonValidNumberOfGroupSizes() {
		try {
			groupSizes.add(20);
			groupSizes.add(30);	
			groupSizes.add(10);	
			testPopulation(population);
		} catch (IllegalArgumentException e) {
			assertEquals(e.getMessage(), "Invalid parameters in creation of new population: more or less groups "
					+ "given then group sizes.");
		}		
	}
	
	/**
	 * Tests a non valid constructor of the {@link Population} class. One group does not have a size bigger than 0.
	 */
	@Test
	public void testIllegalGroupSize() {
		try {
			groupSizes.add(-5);
			groupSizes.add(30);	
			testPopulation(population);
		} catch (IllegalArgumentException e) {
			assertEquals(e.getMessage(), "Invalid parameters in creation of new population: groups must have sizes larger than zero.");
		}		
	}
	
	/**
	 * Tests a non valid constructor of the {@link Population} class. The sum of all group sizes is not even.
	 */
	@Test
	public void testOddGroupSizeSum() {
		try {
			groupSizes.add(25);
			groupSizes.add(30);	
			testPopulation(population);
		} catch (IllegalArgumentException e) {
			assertEquals(e.getMessage(), "Invalid parameters in creation of new population: agent count must be even.");
		}		
	}
	
	/**
	 * Tests the getGroupSize method
	 */
	@Test
	public void getGroupSize() {
		groupSizes.add(50);
		groupSizes.add(100);	
		population = new Population("population", "population description", groups.stream().map(g -> g.getName()).collect(Collectors.toList()), groupSizes);
		assertTrue(population.getGroupSize(group1.getName()) == 50);
		assertTrue(population.getGroupSize(group2.getName()) == 100);
		population.getGroupNames().remove(1);
		assertTrue(population.getGroupSize(group2.getName()) == 0);
	}
	
	private void testPopulation(Population population) {
		population = new Population("population", "population description", groups.stream().map(g -> g.getName()).collect(Collectors.toList()), groupSizes);
		assertEquals("population", population.getName());
		assertEquals("population description", population.getDescription());
		assertEquals(groups.stream().map(g -> g.getName()).collect(Collectors.toList()), population.getGroupNames());
		assertEquals(groupSizes, population.getGroupSizes());	
	}

}
