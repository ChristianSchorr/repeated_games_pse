package loop.model.simulator;

import loop.model.UserConfiguration;
import loop.model.simulationengine.IterationResult;
import loop.model.simulationengine.SimulationEngine;

/**
 * Implementations of this interface create an instance of the {@link IterationResult} class for a given, finished iteration.
 * 
 * @author Peter Koepernik
 *
 */
public interface IterationResultCreator {
    
    /**
     * Create an iteration result for the last executed iteration of the given simulation engine with the given user configuration.
     * 
     * @param engine the simulation engine that executed the iteration of which an iteration result shall be created
     * @param configuration the user configuration of the finished iteration
     * @return the created iteration result
     */
    public IterationResult createIterationResult(SimulationEngine engine, UserConfiguration configuration);
}
