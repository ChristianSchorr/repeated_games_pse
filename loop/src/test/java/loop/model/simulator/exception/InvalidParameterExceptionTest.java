package loop.model.simulator.exception;

import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

import org.junit.Rule;

/**
 * This class contains unit tests for the
 * {@link InvalidParameterException}-Class
 * 
 * @author Christian Schorr
 *
 */
public class InvalidParameterExceptionTest {

	@Test
	public void testGetParameterValue() {
		ExceptionGenerator generator = new ExceptionGenerator();
		try {
			generator.generateException(100, "agentCount");
		} catch (InvalidParameterException e) {
			assertEquals(100.0, e.getParameterValue(), 0);
		}
	}

	@Test
	public void testGetParameterName() {
		ExceptionGenerator generator = new ExceptionGenerator();
		try {
			generator.generateException(100, "agentCount");
		} catch (InvalidParameterException e) {
			assertEquals("agentCount", e.getParameterName());
		}
	}

	private class ExceptionGenerator {
		public void generateException(double value, String name) throws InvalidParameterException{
			throw new InvalidParameterException(value, name);
		}
	}
}
