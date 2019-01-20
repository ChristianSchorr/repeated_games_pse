package loop.model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

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
	private String parameterName;
    private ArrayList<Double> integerValues = new ArrayList<Double>();
    private ArrayList<Double> floatValues = new ArrayList<Double>();
    private double startValue;
    private double endValue;
    private double stepSize;

	@Before
	public void setUp() throws Exception {
		integerValues.add(10.0);
		integerValues.add(15.0);
		integerValues.add(20.0);
		integerValues.add(25.0);
		integerValues.add(30.0);
		floatValues.add(0.4);
		floatValues.add(0.5);
		floatValues.add(0.6);
	}

	@After
	public void tearDown() throws Exception {
	}
	
	/**
	 * Tests the multiconfiguration parameter 'amount of rounds'
	 */
	@Test
	public void testIterationCount() {
		type = MulticonfigurationParameterType.ROUND_COUNT;
		testConstructor1(type);
		try {	
			testConstructor3();
		} catch (IllegalArgumentException e) {
			assertEquals(e.getMessage(), "Wrong constructor used.");
		}
	}
	
	/**
	 * Tests a constructor for the types ROUND_COUNT, ITERATION_COUNT and MAX_ADAPTS
	 */
	private void testConstructor1(MulticonfigurationParameterType type) {
		multiconfigurationParameter = new MulticonfigurationParameter(type, 10, 30, 5);
		assertEquals(type, multiconfigurationParameter.getType());
		assertEquals(integerValues, multiconfigurationParameter.getParameterValues());
		assertEquals(type.getDescriptionFormat(), multiconfigurationParameter.getParameterName());
	}
	
	/**
	 * Tests a constructor for the types PB_PARAM, SA_PARAM, SQ_PARAM and EC_PARAM
	 */
	private void testConstructor2(MulticonfigurationParameterType type) {
		multiconfigurationParameter = new MulticonfigurationParameter(type, 0.4, 0.6, 0.1, "name");
		assertEquals(type, multiconfigurationParameter.getType());
		assertEquals(floatValues, multiconfigurationParameter.getParameterValues());
		assertEquals("name", multiconfigurationParameter.getParameterName());
	}
	
	/**
	 * Tests a constructor for the type GROUP_SIZE
	 */
	private void testConstructor3() {
		multiconfigurationParameter = new MulticonfigurationParameter(10, 30, 5, "group name");
		assertEquals(MulticonfigurationParameterType.GROUP_SIZE, multiconfigurationParameter.getType());
		assertEquals(integerValues, multiconfigurationParameter.getParameterValues());
		assertEquals("group name", multiconfigurationParameter.getGroupName());
	}

}
