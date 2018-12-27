/**
 * This package contains exception classes to categorise the different kinds of errors that
 * can occur in the process of starting and executing simulations. The exceptions are roughly
 * distinguished in two types. A {@link ConfigurationException} is thrown when the given configuration 
 * is faulty, for example if some parameters are invalid or a given plugin cannot be
 * found. A {@link SimulationEngineException} is thrown when the given configuration was valid
 * and the simulation was started, but an error occured within the execution of some iteration
 * (by an instance of the {@link SimulationEngine} class). This could happen for example if
 * one of the integrated plugins used in the simulation is bugged.
 */
/**
 * @author Christian Schorr
 *
 */
package loop.model.simulator.exception;