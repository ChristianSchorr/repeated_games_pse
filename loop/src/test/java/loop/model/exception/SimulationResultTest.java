package loop.model.exception;

import static org.junit.Assert.*;

import java.util.function.Consumer;

import org.junit.Test;

import loop.model.UserConfiguration;
import loop.model.simulationengine.IterationResult;
import loop.model.simulator.SimulationResult;
import loop.model.simulator.Simulator;
import loop.model.simulator.exception.ConfigurationException;

/**
 * This class contains unit tests for the
 * {@link SimulationResult}-Class
 * 
 * @author Christian Schorr
 *
 */
public class SimulationResultTest {
	
	@Test
	public void testGetId() {
		SimulationResult result1 = new SimulationResult(null, 1);
		SimulationResult result2 = new SimulationResult(null, 123);
		SimulationResult result3 = new SimulationResult(null, -21);
		
		assertEquals(1, result1.getId());
		assertEquals(123, result2.getId());
		assertEquals(-21, result3.getId());
	}
	
	@Test
	public void testGetConfig() {
		UserConfiguration defaultConfig = UserConfiguration.getDefaultConfiguration();
		SimulationResult result = new SimulationResult(defaultConfig, 1);
		
		assertEquals(defaultConfig, result.getUserConfiguration());
	}
	
	@Test
	public void testGetConfigurationCount() {
		fail();
	}
	
	@Test
	public void testGetIterationResults() {
		fail();
	}
	
	@Test
	public void testRegisterExceptionHandler() {
		fail();
	}
	
	@Test
	public void testRegisterIterationFinished() {
		fail();
	}
	
}
