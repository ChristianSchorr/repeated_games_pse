package loop.model.simulator;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import loop.model.UserConfiguration;
import loop.model.simulationengine.Configuration;
import loop.model.simulationengine.IterationResult;
import loop.model.simulationengine.SimulationEngine;
import loop.model.simulator.exception.ConfigurationException;
import loop.model.simulator.exception.SimulationEngineException;

/**
 * An implementation of the {@link Simulator} interface. Executes the iterations
 * in parallel using a thread pool.
 * 
 * @author Christian Schorr
 *
 */
public class ThreadPoolSimulator implements Simulator {

	private static final int DEFAULT_MAX_THREAD_COUNT = 4;

	private ThreadPoolExecutor threadPool;
	private int threadCount;

	private Queue<SimulatorTask> runningSimulations;
	private ArrayList<SimulatorTask> finishingSimulations;
	private ArrayList<SimulationResult> finishedSimulations;
	
	private static int nextSimulationId = 1;

	/**
	 * Creates a new ThreadPoolSimulator .
	 */
	public ThreadPoolSimulator() {
		this(DEFAULT_MAX_THREAD_COUNT);
	}

	/**
	 * Creates a new ThreadPoolSimulator with the given maximum amount of running
	 * threads.
	 * 
	 * @param maxThreads the maximum amount of running threads in the thread pool
	 */
	public ThreadPoolSimulator(int maxThreads) {
		threadCount = maxThreads;
		threadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(threadCount);
		runningSimulations = new LinkedList<ThreadPoolSimulator.SimulatorTask>();

		finishedSimulations = new ArrayList<SimulationResult>();
		finishingSimulations = new ArrayList<SimulatorTask>();
	}

	@Override
	public SimulationResult startSimulation(UserConfiguration config) throws ConfigurationException {
		return startSimulation(config, (res) -> {
		});
	}

	@Override
	public SimulationResult startSimulation(UserConfiguration config, Consumer<SimulationResult> action)
			throws ConfigurationException {
		SimulationResult simResult = new SimulationResult(config, nextSimulationId++);
		ConfigurationBuffer configBuffer = new ConfigurationBuffer(config, threadCount);

		SimulatorTask task = new SimulatorTask(simResult, configBuffer, action);
		runningSimulations.add(task);
		scheduleNextIteration();
		return simResult;
	}

	private void scheduleNextIteration() throws ConfigurationException {
		// update finishing tasks
		SimulatorTask finishedSimulation = finishingSimulations.stream()
				.filter(tsk -> tsk.runningIterations.size() == 0).findFirst().orElse(null);
		if (finishedSimulation != null) {
			finishingSimulations.remove(finishedSimulation);
			finishedSimulations.add(finishedSimulation.simResult);
			finishedSimulation.finishedHandler.accept(finishedSimulation.simResult);
		}

		// update running tasks
		SimulatorTask runningTask = runningSimulations.peek();
		while (runningTask != null && runningTask.totalIterationsLeft == 0) {
			finishingSimulations.add(runningTask);
			runningSimulations.remove();
			runningTask = runningSimulations.peek();
		}
		if (runningTask == null)
			return;

		SimulatorTask task = runningSimulations.peek();

		Configuration config = task.getNextConfiguration();
		int index = task.buffer.getIndex(config);

		// start iteration execution
		CompletableFuture<IterationResult> future = CompletableFuture.supplyAsync(() -> {
			SimulationEngine engine = new SimulationEngine();
			return engine.executeIteration(config);
		}, threadPool);

		// handle iteration finished and exceptions
		future.thenAccept((res) -> {
			task.simResult.addIterationResult(res, index);
			task.runningIterations.remove(future);
			task.buffer.addConfiguration(config, index);
			try {
				scheduleNextIteration();
			} catch (ConfigurationException e) {
				e.printStackTrace();
			}
		}).exceptionally((ex) -> {
			task.simResult.addSimulationEngineException(new SimulationEngineException());
			return null;
		});
		task.runningIterations.add(future);
	}

	@Override
	public boolean stopSimulation(SimulationResult sim) {
		SimulatorTask task = runningSimulations.stream().filter((tsk) -> tsk.simResult.equals(sim)).findFirst()
				.orElse(null);
		if (task == null)
			return false;
		for (CompletableFuture<IterationResult> future : task.runningIterations)
			future.cancel(true);
		runningSimulations.remove(task);
		return true;
	}

	@Override
	public boolean stopSimulation(int id) {
		SimulatorTask task = runningSimulations.stream().filter((tsk) -> tsk.simResult.getId() == id).findFirst()
				.orElse(null);
		if (task == null)
			return false;
		return stopSimulation(task.simResult);
	}

	@Override
	public void stopAllSimulations() {
		for (SimulatorTask tsk : runningSimulations)
			stopSimulation(tsk.simResult);
	}

	@Override
	public SimulationResult getSimulation(int id) {
		SimulationResult result = runningSimulations.stream().filter((tsk) -> tsk.simResult.getId() == id)
				.map((tsk) -> tsk.simResult).findFirst().orElse(null);
		return result;
	}

	/**
	 * Returns the amount of currently executed iterations.
	 * 
	 * @return the amount of currently executed iterations
	 */
	public int getRunningIterationCount() {
		int count = 0;
		for (SimulatorTask task : runningSimulations) {
			count += task.runningIterations.size();
		}
		return count;
	}

	/**
	 * Returns the amount of iterations currently waiting for execution.
	 * 
	 * @return the amount of iterations currently waiting for execution
	 */
	public int getQueuedIterationCount() {
		int count = 0;
		for (SimulatorTask task : runningSimulations) {
			count += task.totalIterationsLeft;
		}
		return count;
	}

	private class SimulatorTask {

		private SimulationResult simResult;
		private ConfigurationBuffer buffer;
		private Consumer<SimulationResult> finishedHandler;

		private int totalIterationsLeft;
		private ArrayList<Integer> iterationsLeft;
		private ArrayList<CompletableFuture<IterationResult>> runningIterations;

		public SimulatorTask(SimulationResult result, ConfigurationBuffer buffer,
				Consumer<SimulationResult> finishedHandler) {
			simResult = result;
			this.buffer = buffer;
			this.finishedHandler = finishedHandler;
			runningIterations = new ArrayList<CompletableFuture<IterationResult>>();
			iterationsLeft = new ArrayList<Integer>();
				
			List<Configuration> configs = buffer.peekAllConfigurations();
			for (Configuration config: configs) {
				totalIterationsLeft += config.getTotalIterations();
				iterationsLeft.add(config.getTotalIterations());
			}
		}

		private Configuration getNextConfiguration() throws ConfigurationException {
			if (totalIterationsLeft <= 0)
				return null;
			totalIterationsLeft--;
			for (int i = 0; i < iterationsLeft.size(); i++) {
				if (iterationsLeft.get(i) > 0 && buffer.hasConfiguration(i)) {
					iterationsLeft.set(i, iterationsLeft.get(i) - 1);
					return buffer.getConfiguration(i);					
				}
			}
			for (int i = 0; i < iterationsLeft.size(); i++) {
				if (iterationsLeft.get(i) > 0) {
					iterationsLeft.set(i, iterationsLeft.get(i) - 1);					
					return buffer.getConfiguration(i);
				}
			}
			return null;
		}
	}

	private class ConfigurationBuffer {

		private UserConfiguration config;
		private ArrayList<Queue<Configuration>> buffer;

		public ConfigurationBuffer(UserConfiguration config, int threads) throws ConfigurationException {
			this.config = config;

			// initialize the buffer
			List<Configuration> configs = ConfigurationCreator.generateConfigurations(config);
			for (Configuration engineConfig : configs) {
				Queue<Configuration> configBuffer = new LinkedList<Configuration>();
				configBuffer.add(engineConfig);
				buffer.add(configBuffer);
			}

			// fill the buffer with some copies
			for (int i = 0; i < threads - 1; i++) {
				configs = ConfigurationCreator.generateConfigurations(config);
				for (int j = 0; j < configs.size(); j++) {
					buffer.get(j).add(configs.get(j));
				}
			}
		}

		private void addConfiguration(Configuration engineConfig, int index) {
			buffer.get(index).add(engineConfig);
		}

		private boolean hasConfiguration(int index) {
			return !buffer.get(index).isEmpty();
		}

		private Configuration getConfiguration(int index) throws ConfigurationException {
			// generate new configuration if buffer is empty
			if (buffer.get(index).isEmpty()) {
				List<Configuration> configs = ConfigurationCreator.generateConfigurations(config);
				for (int j = 0; j < configs.size(); j++) {
					buffer.get(j).add(configs.get(j));
				}
			}
			return buffer.get(index).remove();
		}

		private int getIndex(Configuration config) {
			return IntStream.range(0, buffer.size()).filter((i) -> buffer.get(i).peek().equals(config)).findFirst()
					.orElse(-1);
		}
		
		private List<Configuration> peekAllConfigurations() {
			return buffer.stream().map((q) -> q.peek()).collect(Collectors.toList());
		}
	}
}
