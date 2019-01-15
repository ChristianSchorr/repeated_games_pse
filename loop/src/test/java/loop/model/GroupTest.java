package loop.model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests the constructors of the {@link Group} class.
 * 
 * @author Luc Mercatoris
 * 
 */

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class GroupTest {
	
	List<Segment> segments = new ArrayList<Segment>();
	List<Double> segmentSizes = new ArrayList<Double>();
	List<Double> binomialDistributionParameters = new ArrayList<Double>();
	List<Double> poissonDistributionParameters = new ArrayList<Double>();
	List<String> strategyNames = new ArrayList<String>();

	@Before
	public void setUp() throws Exception {
		binomialDistributionParameters.add(10.0);
		binomialDistributionParameters.add(20.0);
		binomialDistributionParameters.add(0.7);
		strategyNames.add("titForTat");
		strategyNames.add("grim");
		Segment segment1 = new Segment("Binomial Distribution", binomialDistributionParameters, strategyNames);
		segments.add(segment1);
		poissonDistributionParameters.add(3.0);
		Segment segment2 = new Segment("Poisson Distribution", poissonDistributionParameters, strategyNames);
		segments.add(segment2);
		segmentSizes.add(0.3);
		segmentSizes.add(0.7);
	}

	@After
	public void tearDown() throws Exception {
	}
	
	
	/**
	 * Tests a valid constructor of the Group class
	 */
	@Test
	public void testValidConstructor() {
		Group group = new Group("group1", "group1 description", segments, segmentSizes, true);	
		assertEquals("group1", group.getName());
		assertEquals("group1 description", group.getDescription());
		assertEquals(segments, group.getSegments());
		assertEquals(segmentSizes, group.getSegmentSizes());	
		assertEquals(true, group.isCohesive());
	}
	
	/**
	 * Tests a non valid constructor of the Group class
	 */
	@Test
	public void testNonValidConstructor() {
		segmentSizes.remove(1);
		Group group = new Group("group1", "group1 description", segments, segmentSizes, true);
	}
}
