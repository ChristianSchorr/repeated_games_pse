package loop.model.simulationengine;

import java.util.ArrayList;
import java.util.List;

import loop.model.plugin.Parameter;
import loop.model.plugin.ParameterValidator;
import loop.model.plugin.Plugin;

/**
 * Realises the “Ranggleichgewicht” described in the specification.
 * 
 * @author Peter Koepernik
 *
 */
public class RankingEquilibrium extends CountingEquilibriumCriterion {
    
    public static final String NAME = "Ranking Equilibrium";
    private static final String DESCRIPTION = "After every adaption step, calculates the mean distance of the agents ranks to their previous ones."
            + " Then the determined amount of change is compared with a configurable 'strictness', and if the change"
            + " in rankings is lower than that strictness for a configurable amount of consecutive adaption steps, an equilibrium is reached.";
    
    private double alpha;
    private int G;
    
    /**
     * Creates a new ranking equilibrium instance with the given parameters.
     * 
     * @param alpha the strictness of the equilibrium (between zero and one)
     * @param minRounds the minimum amount of consecutive condition-meeting adaption steps
     */
    public RankingEquilibrium(double alpha, int minRounds) {
        if (alpha < 0 || alpha > 1 || minRounds < 0) {
            throw new IllegalArgumentException("Invalid parameters for intialisation of ranking equilibrium.");
        }
        
        this.alpha = alpha;
        this.G = minRounds;
    }
    
    @Override
    public boolean hasEquilibriumCondition(final List<Agent> lastRoundAgentsFlatCopy,
            final List<Agent> lastRoundAgentsDeepCopy, final List<Agent> agents) {
        if (lastRoundAgentsFlatCopy.size() != agents.size() || lastRoundAgentsDeepCopy.size() != agents.size()) {
            throw new IllegalArgumentException("Invalid parameters in call of 'hasEquilibriumCondition'");
        }
        
        int[] oldRank = new int[agents.size()];
        
        for (int i = 0; i < agents.size(); i++) {
            oldRank[i] = lastRoundAgentsFlatCopy.indexOf(agents.get(i));
        }
        
        int diff = 0;
        for (int i = 0; i < agents.size(); i++) {
            diff += Math.abs(i - oldRank[i]);
        }

        return (diff < 0.5 * alpha * agents.size() * agents.size());
    }

    @Override
    public boolean longEnough(int steps) {
        return (steps >= this.G);
    }
    
    /**
     * Returns a {@link Plugin} instance wrapping this implementation of the {@link EquilibriumCriterion} interface.
     * 
     * @return a plugin instance.
     */
    public static Plugin<EquilibriumCriterion> getPlugin() {
        if (plugin == null) {
            plugin = new RankingEquilibriumPlugin();
        }
        return plugin;
    }
    
    private static RankingEquilibriumPlugin plugin;
    
    private static class RankingEquilibriumPlugin extends Plugin<EquilibriumCriterion> {
        
        private List<Parameter> parameters = new ArrayList<Parameter>();
        
        public RankingEquilibriumPlugin() {
            Parameter alphaParameter = new Parameter(0.0, 1.0, "strictness", "Indicates the strictness of the equilibrium. 0 means that"
                    + " rankings may not change at all, 1 means rankings may change arbitrarily much.");
            Parameter GParameter = new Parameter(0.0, 500.0, 1.0, "consecutive steps", "Indicates in how many consecutive adaption steps"
                    + " the bound for ranking changes determined by the strictness must be undermatched in order for an equilibrium to"
                    + " be reached.");
            parameters.add(alphaParameter);
            parameters.add(GParameter);
        }
        
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
        public EquilibriumCriterion getNewInstance(List<Double> params) {
            if (!ParameterValidator.areValuesValid(params, parameters)) {
                throw new IllegalArgumentException("Invalid parameters given for the creation of a 'ranking equilibrium' object");
            }
            return new RankingEquilibrium(params.get(0), params.get(1).intValue());
        }
    }
    
}
