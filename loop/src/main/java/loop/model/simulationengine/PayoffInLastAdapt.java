package loop.model.simulationengine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import loop.model.plugin.Parameter;
import loop.model.plugin.ParameterValidator;
import loop.model.plugin.Plugin;

/**
 * Realises the "Absolutkapital" success quantification.
 * 
 * @author Peter Koepernik
 *
 */
public class PayoffInLastAdapt implements SuccessQuantifier {
    
    public static final String NAME = "Payoff in the Last Adaption Step";
    private static final String DESCRIPTION = "Uses the total payoff received within the games "
            + "of the elapsed adaption step as success quantification for each agent.";
    
    @Override
    public List<Agent> createRanking(List<Agent> agents, SimulationHistory history) {
        Map<Agent, Double> payoffs = new HashMap<Agent, Double>();
        for (Agent a: agents) {
            payoffs.put(a, 0.0);
        }
        for (GameResult game: history.getAllResults()) {
            Agent a1 = game.getAgents().get(0);
            Agent a2 = game.getOtherAgent(a1);
            double p1 = game.getPayoff(a1);
            double p2 = game.getPayoff(a2);
            payoffs.replace(a1, payoffs.get(a1) + p1);
            payoffs.replace(a2, payoffs.get(a2) + p2);
        }
        agents.sort((a1, a2) -> (payoffs.get(a2) - payoffs.get(a1) < 0) ? -1 : ((payoffs.get(a2) - payoffs.get(a1) > 0) ? 1 : 0));
        return agents;
    }
    
    /**
     * Returns a {@link Plugin} instance wrapping this implementation of the {@link SuccessQuantifier} interface.
     * 
     * @return a plugin instance.
     */
    public static Plugin<SuccessQuantifier> getPlugin() {
        if (plugin == null) {
            plugin = new PayoffInLastAdaptPlugin();
        }
        return plugin;
    }
    
    private static PayoffInLastAdaptPlugin plugin;
    
    private static class PayoffInLastAdaptPlugin extends Plugin<SuccessQuantifier> {
        
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
                throw new IllegalArgumentException("Invalid parameters given for the creation of a 'payoff in last adapt' object");
            }
            return new PayoffInLastAdapt();
        }
    }

}
