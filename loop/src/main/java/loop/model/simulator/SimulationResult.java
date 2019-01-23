package loop.model.simulator;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import loop.model.UserConfiguration;
import loop.model.simulationengine.IterationResult;
import loop.model.simulator.exception.SimulationEngineException;

/**
 * A SimulationResult object contains information about a started simulation,
 * such as its configuration, execution status, id and, if available, the
 * results of the iterations. It is generated by a Simulator when a simulation
 * is started. The class does not distinguish between multiconfigurations and
 * non-multiconfigurations, so it generally assumes that several elementary
 * configurations underlie.
 * 
 * @author Christian Schorr
 *
 */
public class SimulationResult {

	private UserConfiguration configuration;
	private int id;
	private SimulationStatus status;
	private int totalIterations;
	private int finishedIterations = 0;

	private List<List<IterationResult>> iterationResults;
	private List<SimulationEngineException> exceptions;

	private List<BiConsumer<SimulationResult, IterationResult>> resultHandlers;
	private List<BiConsumer<SimulationResult, SimulationEngineException>> exceptionHandlers;
	private List<BiConsumer<SimulationResult, SimulationStatus>> statusChangedHandler;


	/**
	 * Creates a new simulation result to a simulation with given configuration and
	 * id.
	 * 
	 * @param config the configuration of the simulation
	 * @param id     the id of the simulation
	 */
	public SimulationResult(UserConfiguration config, int id) {
		configuration = config;
		this.id = id;
		iterationResults = new ArrayList<>();
		exceptions = new ArrayList<>();
		status = SimulationStatus.QUEUED;

		resultHandlers = new ArrayList<>();
		exceptionHandlers = new ArrayList<>();
		statusChangedHandler = new ArrayList<>();
	}

	/**
	 * adds an {@link IterationResult} to the i-th elementary configuration and
	 * triggers all registered actions.
	 * 
	 * @param result the iteration result that shall be added
	 * @param i      the elementary configuration to which the given result shall be
	 *               added
	 */
	public void addIterationResult(IterationResult result, int i) {
		while ((i + 1) > iterationResults.size()) {
			iterationResults.add(new ArrayList<IterationResult>());
		}
		iterationResults.get(i).add(result);
		finishedIterations++;

		// notify listeners
		for (BiConsumer<SimulationResult, IterationResult> handler : resultHandlers) {
			handler.accept(this, result);
		}
	}

	/**
	 * Adds a {@link SimulationEngineException} to this Simulaton and triggers all
	 * registered exception handlers
	 * 
	 * @param ex the exception that shall be added
	 */
	public void addSimulationEngineException(SimulationEngineException ex) {
		exceptions.add(ex);

		// notify listeners
		for (BiConsumer<SimulationResult, SimulationEngineException> handler : exceptionHandlers) {
			handler.accept(this, ex);
		}
	}

	/**
	 * Registers an action that will be executed every time an iteration of this
	 * simulation is finished. The IterationResult of the iteration as well as this
	 * instance will be passed as an argument to the action.
	 * 
	 * @param action the action that shall be executed whenever an iteration
	 *               finishes
	 */
	public void registerIterationFinished(BiConsumer<SimulationResult, IterationResult> action) {
		resultHandlers.add(action);
	}

	/**
	 * Registers a handler that will be executed every time an exception
	 * corresponding to this simulation occurs.
	 * 
	 * @param handler the handler that shall be executed whenever an exception
	 *                occurs
	 */
	public void registerExceptionHandler(BiConsumer<SimulationResult, SimulationEngineException> handler) {
		exceptionHandlers.add(handler);
	}

	/**
	 * Returns a list of all yet available results of iterations with the i-th
	 * elementary configuration.
	 * 
	 * @param i the elementary configuration whose finished iterations shall be
	 *          returned
	 * @return a list of all yet available results of iterations with the i -th
	 *         elementary configuration
	 */
	public List<IterationResult> getIterationResults(int i) {
		if (i < 0 || i > iterationResults.size() - 1)
			return null;
		iterationResults.get(i).sort((it1, it2) -> Double.compare(it1.getEfficiency(), it2.getEfficiency()));
		return iterationResults.get(i);
	}
	
	/**
	 * Returns the list of all yet available iterations of all elementary configurations.
	 * 
	 * @return all yet available iteration results
	 */
	public List<List<IterationResult>> getAllIterationResults() {
	    return iterationResults;
	}

	/**
	 * Returns the {@link UserConfiguration} of this simulation.
	 * 
	 * @return the {@link UserConfiguration} of this simulation
	 */
	public UserConfiguration getUserConfiguration() {
		return configuration;
	}

	/**
	 * Returns the amount of elementary configurations of this simulation (1 if this
	 * is not a multiconfiguration).
	 * 
	 * @return the amount of elementary configurations of this simulation
	 */
	public int getConfigurationCount() {
		return iterationResults.size();
	}

	/**
	 * Returns the id of this simulation.
	 * 
	 * @return the id of this simulation
	 */
	public int getId() {
		return id;
	}


	/**
	 * Returns the current status of this simulation
	 *
	 * @return the current status of this simulation
	 */
	public SimulationStatus getStatus() {
		return status;
	}

	/**
	 * Register a handler that will be executed every time the status of this Simulation changes
	 *
	 * @param handler the handler that shall be executed whenever the simulation's status changes
	 */
	public void registerSimulationStatusChangedHandler(BiConsumer<SimulationResult, SimulationStatus> handler) {
		statusChangedHandler.add(handler);
	}

	protected void setStatus(SimulationStatus status) {
		this.status = status;
		for(BiConsumer<SimulationResult, SimulationStatus> handler : statusChangedHandler) {
			handler.accept(this, status);
		}
		
		if (status.equals(SimulationStatus.FINISHED)) {
		    clearHandlers();
		}
	}

	protected void setTotalIterations(int totalIterations) {
		this.totalIterations = totalIterations;
	}

	/**
	 * Returns the total number of iterations in this simulation
	 * @return the total number of iterations in this simulation
	 */
	public int getTotalIterations() {
		return totalIterations;
	}

	/**
	 * Returns the number of finished iterations in this simulation
	 * @return the number of finished iterations in this simulation
	 */
	public int getFinishedIterations() {
		return finishedIterations;
	}
	
	private void clearHandlers() {
	    exceptionHandlers.clear();
	    resultHandlers.clear();
	    statusChangedHandler.clear();
	}
}
