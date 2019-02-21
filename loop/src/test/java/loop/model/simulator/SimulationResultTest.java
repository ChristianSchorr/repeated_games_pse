package loop.model.simulator;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import loop.model.UserConfiguration;
import loop.model.simulationengine.IterationResult;
import loop.model.simulator.SimulationResult;
import loop.model.simulator.exception.SimulationEngineException;

/**
 * This class contains unit tests for the
 * {@link SimulationResult}-Class
 * 
 * @author Christian Schorr
 *
 */
public class SimulationResultTest {
	
	private SimulationResult testSimResult;
	private int testCount = 0;
	
	private SimulationEngineException testException;
	private IterationResult testIterationResult;
	
	
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
		testSimResult = new SimulationResult(null, -1);
		testIterationResult = new IterationResult(false, 0, 0, null, null, null);
		
		testSimResult.addIterationResult(testIterationResult, 0);
		testSimResult.addIterationResult(testIterationResult, 1);
		testSimResult.addIterationResult(testIterationResult, 2);
		testSimResult.addIterationResult(testIterationResult, 1);
		
		int configCount = testSimResult.getConfigurationCount();
		assertEquals(configCount, 3);		
	}
	
	@Test
	public void testGetIterationResults() {
		testSimResult = new SimulationResult(null, -1);
		testIterationResult = new IterationResult(false, 0, 0, null, null, null);
		
		testSimResult.addIterationResult(testIterationResult, 0);
		testSimResult.addIterationResult(testIterationResult, 1);
		testSimResult.addIterationResult(testIterationResult, 1);
		testSimResult.addIterationResult(testIterationResult, 1);
		testSimResult.addIterationResult(testIterationResult, 1);
		testSimResult.addIterationResult(testIterationResult, 1);
		testSimResult.addIterationResult(testIterationResult, 0);
		List<IterationResult> results1 = testSimResult.getIterationResults(0);
		List<IterationResult> results2 = testSimResult.getIterationResults(1);
		assertEquals(results1.size(), 2);
		assertEquals(results2.size(), 5);
		for (IterationResult res : results1) {
			assertEquals(res, testIterationResult);
		}
		for (IterationResult res : results2) {
			assertEquals(res, testIterationResult);
		}
	}
	
	@Test
	public void testRegisterExceptionHandler() {
		testCount = 0;
		testSimResult = new SimulationResult(null, -1);
		testException = new SimulationEngineException();
		
		testSimResult.registerExceptionHandler((res, e) -> testExceptionHandler(res, e));
		testSimResult.addSimulationEngineException(testException);
		testSimResult.addSimulationEngineException(testException);
		testSimResult.addSimulationEngineException(testException);
		testSimResult.addSimulationEngineException(testException);
		assertEquals(testCount, 4);		
	}
	
	@Test
	public void testRegisterIterationFinished() {
		testCount = 0;
		testSimResult = new SimulationResult(null, -1);
		testIterationResult = new IterationResult(false, 0, 0, null, null, null);
		
		testSimResult.registerIterationFinished((res, iterRes) -> testFinishedHandler(res, iterRes));
		testSimResult.addIterationResult(testIterationResult, 0);
		testSimResult.addIterationResult(testIterationResult, 0);
		testSimResult.addIterationResult(testIterationResult, 0);
		testSimResult.addIterationResult(testIterationResult, 0);
		assertEquals(testCount, 4);		
	}
	
	private void testExceptionHandler(SimulationResult result, SimulationEngineException ex) {
		assertEquals(testSimResult, result);
		assertEquals(testException, ex);
		testCount++;
	}
	
	private void testFinishedHandler(SimulationResult result, IterationResult iterRes) {
		assertEquals(testSimResult, result);
		assertEquals(testIterationResult, iterRes);
		testCount++;
	}
}
