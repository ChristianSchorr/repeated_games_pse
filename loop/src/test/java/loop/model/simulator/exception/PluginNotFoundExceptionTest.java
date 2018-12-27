package loop.model.simulator.exception;

import static org.junit.Assert.assertEquals;

import org.junit.Test;


/**
 * This class contains unit tests for the
 * {@link PluginNotFoundExceptionTest}-Class
 * 
 * @author Christian Schorr
 *
 */
public class PluginNotFoundExceptionTest {
	
	@Test
	public void testGetPluginName() {
		ExceptionGenerator generator = new ExceptionGenerator();
		try {
			generator.generateException("CustomPlugin");
		} catch (PluginNotFoundException e) {
			assertEquals("CustomPlugin", e.getPluginName());
		}
	}

	private class ExceptionGenerator {
		public void generateException(String name) throws PluginNotFoundException{
			throw new PluginNotFoundException(name);
		}
	}
}
