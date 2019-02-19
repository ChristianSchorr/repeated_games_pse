package loop.plugins.strategyadjuster;

import loop.model.plugin.Parameter;
import loop.model.plugin.ParameterValidator;
import loop.model.plugin.Plugin;
import loop.model.simulationengine.ReplicatorDynamic;
import loop.model.simulationengine.StrategyAdjuster;

import java.util.ArrayList;
import java.util.List;

public class ReplicatorDynamicPlugin extends Plugin<StrategyAdjuster> {

    public static final String NAME = "Replicator Dynamic";
    private static final String DESCRIPTION = "At the end of an adaption step, each agent will consider changing his strategy with a certain,"
            + " configurable 'comparing probability'. If an agent, say A, chooses to do so, he randomly picks another agent B. He then compares"
            + " ranks, and if A is ranked higher then B, he keeps his strategy. Otherwise, he takes B's strategy with a probability p that"
            + " is proportional to the configurable 'adjusting probability' and the difference in ranks. If mixed strategies are used,"
            + " A does not simply take B's strategy but interpolates A's and B's strategies with parameter p.";

    private List<Parameter> parameters = new ArrayList<Parameter>();

    public ReplicatorDynamicPlugin() {
        Parameter alphaParameter = new Parameter(0.0, 1.0, "comparing probability",
                "The probability with which an agent considers to adapt his strategy at the end of an adaption step.");
        Parameter betaParameter = new Parameter(0.0, 1.0, "adjusting probability",
                "A factor proportioning the probability (or the interpolation parameter) with which an agent takes the "
                        + "strategy of another agent after comparison.");
        parameters.add(alphaParameter);
        parameters.add(betaParameter);
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
    public StrategyAdjuster getNewInstance(List<Double> params) {
        if (!ParameterValidator.areValuesValid(params, parameters)) {
            throw new IllegalArgumentException("Invalid parameters given for the creation of a 'replicator dynamic' object");
        }
        return new ReplicatorDynamic(params.get(0), params.get(1));
    }
}
