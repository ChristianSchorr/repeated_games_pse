package loop.model.simulator;

import static org.junit.Assert.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import loop.model.UserConfiguration;
import loop.model.simulator.exception.ConfigurationException;

/**
 * This class test the {@link ThreadPoolSimulator}-class
 * 
 * @author Christian Schorr
 *
 */
public class ThreadPoolSimulatorTest {
	
	private SimulationResult result;
	private CountDownLatch lock = new CountDownLatch(16);
	
	@Test
	public void startSimulationTest() {
		ThreadPoolSimulator simulator = new ThreadPoolSimulator(2);
		UserConfiguration config = UserConfiguration.getDefaultConfiguration();
		try {
			result = simulator.startSimulation(config);
			result.registerIterationFinished((res, a) -> simulationFinishedHandler(res));
			result.registerExceptionHandler((r, e)->simulationFinishedHandler(r));
			assertEquals(result.getUserConfiguration(), config);
			try {
				lock.await(2, TimeUnit.MINUTES);
				System.out.println("skipped2");

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
		
	}
	
	private void simulationFinishedHandler(SimulationResult res) {
		assertEquals(result, res);
		System.out.println("hIER!");
		lock.countDown();
	}

}
