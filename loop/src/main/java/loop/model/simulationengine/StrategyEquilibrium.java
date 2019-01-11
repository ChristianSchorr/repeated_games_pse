package loop.model.simulationengine;

import java.util.ArrayList;
import java.util.List;

import loop.model.plugin.Parameter;
import loop.model.plugin.ParameterValidator;
import loop.model.plugin.Plugin;
import loop.model.simulationengine.strategies.MixedStrategy;
import loop.model.simulationengine.strategies.Strategy;

/**
 * Realises the "Strategiegleichgewicht" described in the specification.
 * 
 * @author Peter Koepernik
 *
 */
public class StrategyEquilibrium extends CountingEquilibriumCriterion {
    
    public static final String NAME = "Strategy Equilibrium";
    private static final String DESCRIPTION = "After every adaption step, calculates a measure for the amount of change in the agents strategies."
            + " In case of pure strategies that is just the proportion of agents who changed their strategy; in case of mixed strategies, the"
            + " mean absolute distance of each agents strategy to his preceeding strategy is used, where the mixed strategies are regarded as real"
            + " vectors with entries between 0 and 1. This is normalized such that both measures coincide when regarding a pure strategy as (a rather"
            + " trivial) mixed strategy. Then the determined amount of change is compared with a configurable 'strictness', and if it"
            + " is lower than that strictness for a configurable amount of consecutive adaption steps, an equilibrium is reached.";
    
    private double alpha;
    private int G;
    
    /**
     * Creates a new strategy equilibrium instance with the given parameters.
     * 
     * @param alpha the strictness of the equilibrium (between zero and one)
     * @param minRounds the minimum amount of consecutive condition-meeting adaption steps
     */
    public StrategyEquilibrium(double alpha, int minRounds) {
        if (alpha < 0 || alpha > 1 || minRounds < 0) {
            throw new IllegalArgumentException("Invalid parameters for intialisation of strategy equilibrium.");
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
        
        List<Strategy> oldStrategies = new ArrayList<Strategy>();
        List<Strategy> newStrategies = new ArrayList<Strategy>();
        for (Agent a: agents) {
            oldStrategies.add(lastRoundAgentsDeepCopy.get(lastRoundAgentsFlatCopy.indexOf(a)).getStrategy());
            newStrategies.add(a.getStrategy());
        }
        
        //Check whether all strategies are mixed
        boolean allMixed = true;
        for (Strategy s: oldStrategies) {
            if (!(s instanceof MixedStrategy)) allMixed = false;
        }
        for (Strategy s: newStrategies) {
            if (!(s instanceof MixedStrategy)) allMixed = false;
        }
        
        double diff = 0;
        for (int i = 0; i < agents.size(); i++) {
            if (allMixed) {
                diff += ((MixedStrategy) newStrategies.get(i)).clone().add(((MixedStrategy) oldStrategies.get(i)).clone().mutliplyBy(-1)).getSumNorm(); 
            } else {
                diff += (newStrategies.get(i) != oldStrategies.get(i)) ? 2 : 0;
            }
        }
        diff *= 0.5;
        
        return (diff < this.alpha * agents.size());
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
            plugin = new StrategyEquilibriumPlugin();
        }
        return plugin;
    }
    
    private static StrategyEquilibriumPlugin plugin;
    
    private static class StrategyEquilibriumPlugin extends Plugin<EquilibriumCriterion> {
        
        private List<Parameter> parameters = new ArrayList<Parameter>();
        
        public StrategyEquilibriumPlugin() {
            Parameter alphaParameter = new Parameter(0.0, 1.0, "strictness", "Indicates the strictness of the equilibrium. 0 means that"
                    + " strategies may not change at all, 1 means strategies may change arbitrarily much (obviously, both of these extremes"
                    + " should be avoided, values of around 0.005 proved to produce reasonable results).");
            Parameter GParameter = new Parameter(0.0, 500.0, 1.0, "consecutive steps", "Indicates in how many consecutive adaption steps"
                    + " the bound for strategy changes determined by the strictness must be undermatched in order for an equilibrium to"
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
                throw new IllegalArgumentException("Invalid parameters given for the creation of a 'strategy equilibrium' object");
            }
            return new StrategyEquilibrium(params.get(0), params.get(1).intValue());
        }
    }

}
