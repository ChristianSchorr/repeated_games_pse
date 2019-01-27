package loop.model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the methods of the {@link Segement} class.
 * 
 * @author Luc Mercatoris
 * 
 */

public class SegmentTest {
	private String capitalDistributionName;
	private List<Double> capitalDistributionParameters = new ArrayList<Double>();
	private List<String> strategyNames = new ArrayList<String>();
	
	@Before
	public void setUp() throws Exception {
		capitalDistributionName = "name";
		capitalDistributionParameters.add(0.7);
		capitalDistributionParameters.add(0.4);
		strategyNames.add("grim");
		strategyNames.add("never");
	}

	@After
	public void tearDown() throws Exception {
	}
	
	/**
	 * Tests the constructor of the {@link Segement} class
	 */
	@Test
	public void testConstructor() {
		Segment segment = new Segment(capitalDistributionName, capitalDistributionParameters, strategyNames);
		assertEquals(capitalDistributionName, segment.getCapitalDistributionName());
		assertEquals(capitalDistributionParameters, segment.getCapitalDistributionParameters());
		assertEquals(strategyNames, segment.getStrategyNames());
	}

}
