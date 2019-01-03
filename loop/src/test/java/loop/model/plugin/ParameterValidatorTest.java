package loop.model.plugin;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests the {@link ParameterValidator}
 * 
 * @author Pierre Toussing
 *
 */
public class ParameterValidatorTest {
	public Parameter p1, p2;
	final static double tolerance = 0.1e-8;	//floating point precision
	
	/**
	 * Initialize two parameters used in the tests
	 */
	@Before
	public void setUp() {
		p1 = new Parameter(1, 7, 0.1, "test", "test");
		p2 = new Parameter(0, 2, "test", "test");
	}
	
	/**
	 * Several test cases for the isValueValid() method
	 */
	@Test
	public void testIsValid() {
		//Tests for p1
		assertTrue(ParameterValidator.isValueValid(6.8, p1));
		assertTrue(!ParameterValidator.isValueValid(4.55, p1));
		assertTrue(!ParameterValidator.isValueValid(10, p1));
		
		//Tests for p2
		assertTrue(!ParameterValidator.isValueValid(6.8, p2));
		assertTrue(ParameterValidator.isValueValid(1.55, p2));
		assertTrue(ParameterValidator.isValueValid(0, p2));
	}

	/**
	 * Several test cases for the getClosestValue() method
	 */
	@Test
	public void testGetClosest() {
		//Tests for p1
		assertTrue(Math.abs(ParameterValidator.getClosestValid(5.5, p1) - 5.5) < tolerance);
		assertTrue(Math.abs(ParameterValidator.getClosestValid(8, p1) - 7) < tolerance);
		assertTrue(Math.abs(ParameterValidator.getClosestValid(2.378, p1) - 2.4) < tolerance);
		assertTrue(Math.abs(ParameterValidator.getClosestValid(2.35, p1) - 2.3) < tolerance);
		
		//Tests for p2
		assertTrue(Math.abs(ParameterValidator.getClosestValid(8, p2) - 2) < tolerance);
		assertTrue(Math.abs(ParameterValidator.getClosestValid(-14, p2) - 0) < tolerance);
		assertTrue(Math.abs(ParameterValidator.getClosestValid(1.378, p2) - 1.378) < tolerance);		
	}
}
