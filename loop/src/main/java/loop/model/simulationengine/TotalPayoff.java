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
    private static final String DESCRIPTION = "Uses the total received payoff of each agent as success quantification, i.e. the sum of all payoffs"
            + " received in any games played so far, without the initial capital.";

    @Override
    public List<Agent> createRanking(final List<Agent> agents, final SimulationHistory history) {
        agents.sort((a1, a2) -> ((a2.getCapital() - a2.getInitialCapital()) - (a1.getCapital() - a1.getInitialCapital())));
        return agents;
    }
    
    /**
     * Returns a {@link Plugin} instance wrapping this implementation of the {@link SuccessQuantifier} interface.
     * 
     * @return a plugin instance.
     */
    public static Plugin<SuccessQuantifier> getPlugin() {
        if (plugin == null) {
            plugin = new TotalPayoffPlugin();
        }
        return plugin;
    }
    
    private static TotalPayoffPlugin plugin;
    
    private static class TotalPayoffPlugin extends Plugin<SuccessQuantifier> {
        
        private List<Parameter> parameters = new ArrayList<Parameter>();
        
        @Override
        public String getName() {
            return NAME;
        }

        @Override
        public String getDescription() {
            return DESCRIPTION;
        }

        @Override
        public List<Parameter> getParameters() {
            return parameters;
        }

        @Override
        public SuccessQuantifier getNewInstance(List<Double> params) {
            if (!ParameterValidator.areValuesValid(params, parameters)) {
                throw new IllegalArgumentException("Invalid parameters given for the creation of a 'total capital' object");
            }
            return new TotalPayoff();
        }
    }

}
