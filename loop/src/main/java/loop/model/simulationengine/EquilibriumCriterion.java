package loop.model.simulationengine;

import java.util.List;

/**
 * This interface represents an equilibrium criterion (ger.: “Gleichgewichtskriterium”). At the
 * end of an adaptation step, an implementation of this interface receives a list of the agents,
 * ordered by rank, and the SimulationHistory of the current adaptation step, and returns
 * whether or not an equilibrium is reached.
 * 
 * @author Peter Koepernik
 *
 */
public interface EquilibriumCriterion {
    
    /**
     * Returns whether an equilibrium is reached.
     * 
     * @param agents the agents of the currently simulated iteration
     * @param history the history of the current adaption steo
     * @return {@code true} if an equilibrium is reached, {@code false} otherwise
     */
    boolean isEquilibrium(final List<Agent> agents, final SimulationHistory history);
}
