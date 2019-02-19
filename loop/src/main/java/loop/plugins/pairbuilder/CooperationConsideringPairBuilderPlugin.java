package loop.plugins.pairbuilder;

import loop.model.plugin.Parameter;
import loop.model.plugin.ParameterValidator;
import loop.model.plugin.Plugin;
import loop.model.simulationengine.CooperationConsideringPairBuilder;
import loop.model.simulationengine.PairBuilder;

import java.util.ArrayList;
import java.util.List;

public class CooperationConsideringPairBuilderPlugin extends Plugin<PairBuilder> {

    private static final String NAME = "Cooperation Considering Pair Builder";
    private static final String DESCRIPTION = "Tries to maximize mutual cooperation probability for each builded pair, i.e. builds pairs"
            + " such that the probability of both agents cooperating is as high as possible for each pair.";

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
            throw new IllegalArgumentException("Invalid parameters given for the creation of a 'coop considering pair builder' object");
        }
        return new CooperationConsideringPairBuilder();
    }
}
