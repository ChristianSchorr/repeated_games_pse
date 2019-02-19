package loop.plugins.successquantifier;

import loop.model.plugin.Parameter;
import loop.model.plugin.ParameterValidator;
import loop.model.plugin.Plugin;
import loop.model.simulationengine.PayoffInLastAdapt;
import loop.model.simulationengine.SuccessQuantifier;

import java.util.ArrayList;
import java.util.List;

public class PayoffInLastAdaptPlugin extends Plugin<SuccessQuantifier> {

    private static final String NAME = "Payoff in the Last Adaption Step";
    private static final String DESCRIPTION = "Uses the total payoff received within the games "
            + "of the elapsed adaption step as success quantification for each agent.";

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
