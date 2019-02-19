package loop.plugins.equilibriumcriterion;

import loop.model.plugin.Parameter;
import loop.model.plugin.ParameterValidator;
import loop.model.plugin.Plugin;
import loop.model.simulationengine.EquilibriumCriterion;
import loop.model.simulationengine.RankingEquilibrium;

import java.util.ArrayList;
import java.util.List;

public class RankingEquilibriumPlugin extends Plugin<EquilibriumCriterion> {

    private static final String NAME = "Ranking Equilibrium";
    private static final String DESCRIPTION = "After every adaption step, calculates the mean distance of the agents ranks to their previous ones."
            + " Then the determined amount of change is compared with a configurable 'strictness', and if the change"
            + " in rankings is lower than that strictness for a configurable amount of consecutive adaption steps, an equilibrium is reached.";

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
