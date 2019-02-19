package loop.plugins.equilibriumcriterion;

import loop.model.plugin.Parameter;
import loop.model.plugin.ParameterValidator;
import loop.model.plugin.Plugin;
import loop.model.simulationengine.EquilibriumCriterion;
import loop.model.simulationengine.StrategyEquilibrium;

import java.util.ArrayList;
import java.util.List;

public class StrategyEquilibriumPlugin extends Plugin<EquilibriumCriterion> {

    public static final String NAME = "Strategy Equilibrium";
    private static final String DESCRIPTION = "After every adaption step, calculates a measure for the amount of change in the agents strategies."
            + " In case of pure strategies that is just the proportion of agents who changed their strategy; in case of mixed strategies, the"
            + " mean absolute distance of each agents strategy to his preceeding strategy is used, where the mixed strategies are regarded as real"
            + " vectors with entries between 0 and 1. This is normalized such that both measures coincide when regarding a pure strategy as (a rather"
            + " trivial) mixed strategy. Then the determined amount of change is compared with a configurable 'strictness', and if it"
            + " is lower than that strictness for a configurable amount of consecutive adaption steps, an equilibrium is reached.";

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
