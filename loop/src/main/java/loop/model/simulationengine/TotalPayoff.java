package loop.model.simulationengine;

import java.util.ArrayList;
import java.util.List;

import loop.model.plugin.Parameter;
import loop.model.plugin.ParameterValidator;
import loop.model.plugin.Plugin;

/**
 * Realises the "Absolutkapital ohne Initialkapital" success quantification.
 * 
 * @author Peter Koepernik
 *
 */
public class TotalPayoff implements SuccessQuantifier {
    
    
    public static final String NAME = "Total Payoff";

    @Override
    public List<Agent> createRanking(final List<Agent> agents, final SimulationHistory history) {
        agents.sort((a1, a2) -> ((a2.getCapital() - a2.getInitialCapital()) - (a1.getCapital() - a1.getInitialCapital())));
        return agents;
    }
}
