package loop.model.simulator;

import java.util.function.Consumer;

import loop.model.UserConfiguration;
import loop.model.simulator.exception.ConfigurationException;

/**
 * An implementation of this interface provides means of starting and stopping
 * simulations to given (user) configurations. A SimulationResult to a started
 * simulation will be returned instantly. Each simulation gets a unique id
 * assigned to it upon start.
 * 
 * @author Christian Schorr
 *
 */
public interface Simulator {

	/**
	 * Starts a new simulation with the given configuration and returns a handle to
	 * a {@link SimulationResult} object for the started simulation.
	 * 
	 * @param config the configuration for which a simulation shall be started
	 * @return a handle to a {@link SimulationResult} object for the started
	 *         simulation
	 * @throws ConfigurationException {@link ConfigurationException} when the
	 *                                provided configuration is faulty
	 */
	public SimulationResult startSimulation(UserConfiguration config) throws ConfigurationException;

	/**
	 * Starts a new simulation with the given configuration and returns a handle to
	 * a {@link SimulationResult} object for the started simulation. Executes the
	 * given action with the {@link SimulationResult} object passed as parameter
	 * when the started simulation is finished.
	 * 
	 * @param config the configuration a new simulation shall be started with
	 * @param action the action that shall be executed when the simulation is
	 *               finished
	 * @return a handle to a SimulationResult object for the started simulation
	 * @throws ConfigurationException {@link ConfigurationException} when the
	 *                                provided configuration is faulty
	 */
	public SimulationResult startSimulation(UserConfiguration config, Consumer<SimulationResult> action)
			throws ConfigurationException;

	/**
	 * Stop the execution of the simulation corresponding to the given simulation
	 * result if it is currently running.
	 * 
	 * @param sim the {@link SimulationResult} object of the simulation whose
	 *            execution shall be stopped
	 * @return {@code true} , if the execution of the simulation was successfully
	 *         stopped, {@code false} otherwise
	 */
	public boolean stopSimulation(SimulationResult sim);

	/**
	 * If a simulation with the given id is currently running, stop its execution.
	 * 
	 * @param id the id of the simulation that shall be stopped
	 * @return {@code true} , if the execution of the simulation was successfully
	 *         stopped, {@code false} otherwise
	 */
	public boolean stopSimulation(int id);

	/**
	 * Stops the execution of all running simulations.
	 */
	public void stopAllSimulations();

	/**
	 * Returns the {@link SimulationResult} object of the simulation with the given
	 * id , if existent; {@code null} otherwise.
	 * 
	 * @param id the id of the simulation, whose {@link SimulationResult} object
	 *           shall be returned
	 * @return the {@link SimulationResult}-object of the simulation with the given
	 *         id, if existent; {@code null} otherwise
	 */
	public SimulationResult getSimulation(int id);
}
