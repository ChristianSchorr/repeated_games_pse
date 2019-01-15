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
	Group group;
	Segment segment1;
	Segment segment2;

	@Before
	public void setUp() throws Exception {
		binomialDistributionParameters.add(10.0);
		binomialDistributionParameters.add(20.0);
		binomialDistributionParameters.add(0.7);
		strategyNames.add("titForTat");
		strategyNames.add("grim");
		segment1 = new Segment("Binomial Distribution", binomialDistributionParameters, strategyNames);
		segments.add(segment1);
		poissonDistributionParameters.add(3.0);
		segment2 = new Segment("Poisson Distribution", poissonDistributionParameters, strategyNames);
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
		testGroup(group);
	}
	
	/**
	 * Tests a non valid constructor of the Group class. The number of segments is different to
	 * the number of segmentSizes
	 */
	@Test
	public void testNonValidSegmentSizes() {
		try {
			segmentSizes.remove(1);
			testGroup(group);
		} catch (IllegalArgumentException e) {
			assertEquals(e.getMessage(), "Invalid parameters in creation of new group: more or less segments "
					+ "given then segment sizes.");
		}		
	}
	
	/**
	 * Tests a non valid constructor of the Group class. One segmentSize is not between 0 and 1
	 */
	@Test
	public void testIllegalSegmentSize() {
		try {
			segmentSizes.remove(1);
			segmentSizes.add(1.5);
			testGroup(group);
		} catch (IllegalArgumentException e) {
			assertEquals(e.getMessage(), "Invalid parameters in creation of new group: segment size not"
					+ " between zero and one.");
		}		
	}
	
	/**
	 * Tests a non valid constructor of the Group class. The sum of all segment sizes is not equal 1.
	 */
	@Test
	public void testIllegalSegmentSizeSum() {
		try {
			segmentSizes.remove(1);
			segmentSizes.add(0.8);
			testGroup(group);
		} catch (IllegalArgumentException e) {
			assertEquals(e.getMessage(), "Invalid parameters in creation of new group: segment sizes do not "
					+ "sum up to one.");
		}		
	}
	
	/**
	 * Tests the accuracy of the sum of the segment sizes
	 */
	@Test 
	public void testSumSizeAccuracy() {
		segmentSizes.remove(1);
		segmentSizes.add(0.7 + Math.pow(10, -8));	
		testValidConstructor();
		try {
			segmentSizes.remove(1);
			segmentSizes.add(0.7 + Math.pow(10, -6));	
			testGroup(group);
		} catch (IllegalArgumentException e) {
			assertEquals(e.getMessage(), "Invalid parameters in creation of new group: segment sizes do not "
					+ "sum up to one.");
		}		
	}
	
	/**
	 * Tests the getSegmentSize method
	 */
	@Test
	public void testGetSegmentSize() {
		group = new Group("group1", "group1 description", segments, segmentSizes, true);	
		assertTrue(group.getSegmentSize(segment1) == 0.3);
		assertTrue(group.getSegmentSize(segment2) == 0.7);
		group.getSegments().remove(1);
		assertTrue(group.getSegmentSize(segment2) == 0);
	}
	
	
	private void testGroup(Group group) {
		group = new Group("group1", "group1 description", segments, segmentSizes, true);	
		assertEquals("group1", group.getName());
		assertEquals("group1 description", group.getDescription());
		assertEquals(segments, group.getSegments());
		assertEquals(segmentSizes, group.getSegmentSizes());	
		assertEquals(true, group.isCohesive());
	}
}
