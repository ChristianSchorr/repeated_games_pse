package loop.plugins.successquantifier;

import loop.model.plugin.Parameter;
import loop.model.plugin.ParameterValidator;
import loop.model.plugin.Plugin;
import loop.model.simulationengine.SlidingMean;
import loop.model.simulationengine.SuccessQuantifier;

import java.util.ArrayList;
import java.util.List;

public class SlidingMeanPlugin extends Plugin<SuccessQuantifier> {

    private static final String NAME = "Sliding Mean";
    private static final String DESCRIPTION = "Calculates a sliding mean with window size w over the payoffs received in the elapsed"
            + " adaption step for each agent. Details in the specification";

    private List<Parameter> parameters = new ArrayList<Parameter>();

    public SlidingMeanPlugin() {
        Parameter windowSize = new Parameter(1.0, 500.0, 1.0, "window size",
                "the window size used to calculate the sliding mean");
        parameters.add(windowSize);
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
    public SuccessQuantifier getNewInstance(List<Double> params) {
        if (!ParameterValidator.areValuesValid(params, parameters)) {
            throw new IllegalArgumentException("Invalid parameters given for the creation of a 'sliding mean' object");
        }
        return new SlidingMean(params.get(0).intValue());
    }
}
