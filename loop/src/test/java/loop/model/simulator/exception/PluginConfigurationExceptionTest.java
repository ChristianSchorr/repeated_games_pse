package loop.model.simulator.exception;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

/**
 * This class contains unit tests for the
 * {@link PluginConfigurationException}-Class
 * 
 * @author Christian Schorr
 *
 */
public class PluginConfigurationExceptionTest {
	
	@Test
	public void testGetParameter() {
		List<Double> params = new LinkedList<Double>();
		ExceptionGenerator generator = new ExceptionGenerator();
		try {
			generator.generateException("CustomPlugin", params);
		} catch (PluginConfigurationException e) {
			assertEquals(params, e.getParameter());
		}
	}

	@Test
	public void testGetPluginName() {
		ExceptionGenerator generator = new ExceptionGenerator();
		try {
			generator.generateException("CustomPlugin", null);
		} catch (PluginConfigurationException e) {
			assertEquals("CustomPlugin", e.getPluginName());
		}
	}

	private class ExceptionGenerator {
		public void generateException(String name, List<Double> params) throws PluginConfigurationException{
			throw new PluginConfigurationException(name, params);
		}
	}
}
