package loop.model.plugin;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests the constructors of the {@link Parameter} class.
 *  
 * @author Pierre Toussing
 *
 */
public class ParameterTest {

	/**
	 * Tests the constructor with the name and the description as parameters.
	 */
	@Test
	public void testSimpelConstructor() {
		Parameter p = new Parameter("alpha", "size");
		assertTrue(p.getName().equals("alpha"));
		assertTrue(p.getDescription().equals("size"));
		assertTrue(p.getStepSize() == 0);
		assertTrue(p.getMaxValue() == Double.POSITIVE_INFINITY);
		assertTrue(p.getMinValue() == Double.NEGATIVE_INFINITY);
	}

	/**
	 * Tests the constructor with the minValue, the maxValue, the name and the description as parameters.
	 */
	@Test
	public void testExtendedConstructor() {
		Parameter p = new Parameter(0, 5, "alpha", "size");
		assertTrue(p.getName().equals("alpha"));
		assertTrue(p.getDescription().equals("size"));
		assertTrue(p.getStepSize() == 0);
		assertTrue(p.getMaxValue() == 5);
		assertTrue(p.getMinValue() == 0);
	}
	
	/**
	 * Tests the constructor with the minValue, the maxValue, the stepsize, the name and the description as parameters.
	 */
	@Test
	public void testCompleteConstructor() {
		Parameter p = new Parameter(0, 5, 0.2, "alpha", "size");
		assertTrue(p.getName().equals("alpha"));
		assertTrue(p.getDescription().equals("size"));
		assertTrue(p.getStepSize() == 0.2);
		assertTrue(p.getMaxValue() == 5);
		assertTrue(p.getMinValue() == 0);
	}
}
