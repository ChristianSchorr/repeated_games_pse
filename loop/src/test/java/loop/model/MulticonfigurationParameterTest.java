package loop.model;

import static org.junit.Assert.*;

import java.util.ArrayList;

/**
 * This class holds tests for implementations of the {@link MulticonfigurationParameter} class.
 * 
 * @author Luc Mercatoris
 *
 */

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class MulticonfigurationParameterTest {
	private MulticonfigurationParameter multiconfigurationParameter;
	private MulticonfigurationParameterType type;
    private ArrayList<Double> parameterValues = new ArrayList<Double>();
    private ArrayList<MulticonfigurationParameterType> counters = new ArrayList<MulticonfigurationParameterType>();
    private ArrayList<MulticonfigurationParameterType> others = new ArrayList<MulticonfigurationParameterType>();
    
	@Before
	public void setUp() throws Exception {
		//setup a list of parameter values
		parameterValues.add(10.0);
		parameterValues.add(15.0);
		parameterValues.add(20.0);
		parameterValues.add(25.0);
		parameterValues.add(30.0);	
		
		counters.add(MulticonfigurationParameterType.ITERATION_COUNT);
		counters.add(MulticonfigurationParameterType.ROUND_COUNT);
		counters.add(MulticonfigurationParameterType.MAX_ADAPTS);
		
		others.add(MulticonfigurationParameterType.EC_PARAM);
		others.add(MulticonfigurationParameterType.SA_PARAM);
		others.add(MulticonfigurationParameterType.PB_PARAM);
		others.add(MulticonfigurationParameterType.SQ_PARAM);		
	}

	@After
	public void tearDown() throws Exception {
	}
	
	/**
	 * Tests non valid constructors for the different multiconfiguration parameters
	 */
	@Test
	public void testNonValidConstructors() {
		for (MulticonfigurationParameterType t: MulticonfigurationParameterType.values()) {
			type = t;
			if (counters.contains(type)) {
				try {	
					testConstructor(type);
				} catch (IllegalArgumentException e) {
					assertEquals(e.getMessage(), "Wrong constructor used.");
				}	
			} else if (others.contains(type)) {
				try {
					testCountConstructor(type);
				} catch (IllegalArgumentException e) {
					assertEquals(e.getMessage(), "Wrong constructor used.");
				}	
			}
		}		
	}
	
	/**
	 * Tests valid constructors for the different multiconfiguration parameters
	 */
	@Test
	public void testValidConstrucors() {
		for (MulticonfigurationParameterType t: counters) {
			if (!t.equals(MulticonfigurationParameterType.ITERATION_COUNT)) {
				testCountConstructor(t);
			}			
		}
		
		for (MulticonfigurationParameterType t: others) {
			testConstructor(t);
		}
		
		type = MulticonfigurationParameterType.GROUP_SIZE;
		testGroupSizeConstructor();
		type = MulticonfigurationParameterType.SEGMENT_SIZE;
		testSegmentSizeConstructor();
	}
	
	/**
	 * Tests the getGroupName method for different multiconfiguration parameters
	 */
	@Test
	public void testGetGroupName() {
		for (MulticonfigurationParameterType t: counters) {
			if (!t.equals(MulticonfigurationParameterType.ITERATION_COUNT)) {
				testCountConstructor(t);
				try {
					multiconfigurationParameter.getGroupName();
				} catch (NullPointerException e){					
					assertEquals(e.getMessage(), "group name not defined for multiconfiguration parameter '" + 
						multiconfigurationParameter.getParameterName() + "'.");
				}
			}
		}
		
		for (MulticonfigurationParameterType t: others) {
			testConstructor(t);
			try {
				multiconfigurationParameter.getGroupName();
			} catch (NullPointerException e){					
				assertEquals(e.getMessage(), "group name not defined for multiconfiguration parameter '" + 
					multiconfigurationParameter.getParameterName() + "'.");
			}
		}
		
		assertTrue(multiconfigurationParameter.getGroupName() == "group name");
		
		type = MulticonfigurationParameterType.GROUP_SIZE;
		testGroupSizeConstructor();
		assertTrue(multiconfigurationParameter.getGroupName() == "group name");
		
		type = MulticonfigurationParameterType.SEGMENT_SIZE;
		testSegmentSizeConstructor();
		assertTrue(multiconfigurationParameter.getGroupName() == "group name");		
	}
	
	/**
	 * Tests a constructor for the types ROUND_COUNT, ITERATION_COUNT and MAX_ADAPTS
	 */
	private void testCountConstructor(MulticonfigurationParameterType type) {
		multiconfigurationParameter = new MulticonfigurationParameter(type, 10, 30, 5);
		assertEquals(type, multiconfigurationParameter.getType());
		assertEquals(parameterValues, multiconfigurationParameter.getParameterValues());
		assertEquals(type.getDescriptionFormat(), multiconfigurationParameter.getParameterName());
	}
	
	/**
	 * Tests a constructor for the types PB_PARAM, SA_PARAM, SQ_PARAM and EC_PARAM
	 */
	private void testConstructor(MulticonfigurationParameterType type) {
		multiconfigurationParameter = new MulticonfigurationParameter(type, 10.0, 30.0, 5.0, "parameter name");
		assertEquals(type, multiconfigurationParameter.getType());
		assertEquals(parameterValues, multiconfigurationParameter.getParameterValues());
		assertEquals("parameter name", multiconfigurationParameter.getParameterName());
	}
	
	/**
	 * Tests a constructor for the type GROUP_SIZE
	 */
	private void testGroupSizeConstructor() {
		multiconfigurationParameter = new MulticonfigurationParameter(10, 30, 5, "group name");
		assertEquals(MulticonfigurationParameterType.GROUP_SIZE, multiconfigurationParameter.getType());
		assertEquals(parameterValues, multiconfigurationParameter.getParameterValues());
		assertEquals("group name", multiconfigurationParameter.getGroupName());
		assertEquals(String.format(type.getDescriptionFormat(), "group name"), multiconfigurationParameter.getParameterName());
	}
	
	/**
	 * Tests a constructor for the type GROUP_SIZE
	 */
	private void testSegmentSizeConstructor() {
		multiconfigurationParameter = new MulticonfigurationParameter(10.0, 30.0, 5.0, "group name");
		assertEquals(MulticonfigurationParameterType.SEGMENT_SIZE, multiconfigurationParameter.getType());
		assertEquals(parameterValues, multiconfigurationParameter.getParameterValues());
		assertEquals("group name", multiconfigurationParameter.getGroupName());
		assertEquals(String.format(type.getDescriptionFormat(), "group name"), multiconfigurationParameter.getParameterName());
	}

}
