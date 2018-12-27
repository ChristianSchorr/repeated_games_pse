package loop.model.simulator.exception;

/**
 * The ConfigurationException is a type of exception that occurs whenever the simulator
 * gets passed a faulty {@link UserConfiguration}. It gets thrown by the startSimulation method
 * of the {@link Simulator} interface.
 * 
 * @author Christian Schorr
 *
 */
public abstract class ConfigurationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
