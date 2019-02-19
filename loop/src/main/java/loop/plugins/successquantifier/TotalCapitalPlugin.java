package loop.plugins.successquantifier;

import loop.model.plugin.Parameter;
import loop.model.plugin.ParameterValidator;
import loop.model.plugin.Plugin;
import loop.model.simulationengine.SuccessQuantifier;
import loop.model.simulationengine.TotalCapital;

import java.util.ArrayList;
import java.util.List;

public class TotalCapitalPlugin extends Plugin<SuccessQuantifier> {

    private static final String NAME = "Total Capital";
    private static final String DESCRIPTION = "Uses the total capital of each agent as success quantification, i.e. the sum of all payoffs"
            + " received in any games played so far plus the initial capital.";

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
        return new TotalCapital();
    }
}
