package loop.model.simulator.exception;

/**
 * The SimulationEngineException is a type of exception that is thrown whenever an error
 * within the executeIteration method of a {@link SimulationEngine} has occured. The error gets
 * caught by the simulator which then creates an instance of this class and writes it to the
 * corresponding {@link SimulationResult} object.
 * 
 * @author Christian Schorr
 *
 */
public class SimulationEngineException  extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
