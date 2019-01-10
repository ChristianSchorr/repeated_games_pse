package loop.model.simulationengine;

import java.util.ArrayList;
import java.util.List;

import loop.model.plugin.Parameter;
import loop.model.plugin.ParameterValidator;
import loop.model.plugin.Plugin;
import loop.model.plugin.PluginRenderer;
import loop.model.simulationengine.distributions.UniformFiniteDistribution;

/**
 * Realises the random pairing of agents.
 * 
 * @author Peter Koepernik
 *
 */
public class RandomPairBuilder implements PairBuilder {
    
    /**
     * The name of this pair builder.
     */
    public static final String NAME = "Random Pair Builder";
    private static final String DESCRIPTION = "Randomly builds pairs of agents until all agents are matched.";

    @Override
    public List<AgentPair> buildPairs(final List<Agent> agents, final SimulationHistory history) {
        List<AgentPair> pairs = new ArrayList<AgentPair>(agents.size() / 2);
        UniformFiniteDistribution<Agent> agentDist = new UniformFiniteDistribution<Agent>(agents);
        
        while (2 * pairs.size() < agents.size() - 1) {
            pairs.add(new ConcreteAgentPair(agentDist.pickAndRemove(), agentDist.pickAndRemove()));
        }
        return pairs;
    }
    
    /**
     * Returns a {@link Plugin} instance wrapping this implementation of the {@link PairBuilder} interface.
     * 
     * @return a plugin instance.
     */
    public static Plugin<PairBuilder> getPlugin() {
        if (plugin == null) {
            plugin = new RandomPairBuilderPlugin();
        }
        return plugin;
    }
    
    private static RandomPairBuilderPlugin plugin;
    
    private static class RandomPairBuilderPlugin extends Plugin<PairBuilder> {
        
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
        public PairBuilder getNewInstance(List<Double> params) {
            if (!ParameterValidator.areValuesValid(params, parameters)) {
                throw new IllegalArgumentException("Invalid parameters given for the creation of a 'random pair builder' object");
            }
            return new RandomPairBuilder();
        }
    }
    
}
