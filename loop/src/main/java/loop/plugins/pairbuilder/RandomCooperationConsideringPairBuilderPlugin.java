package loop.plugins.pairbuilder;

import loop.model.plugin.Parameter;
import loop.model.plugin.ParameterValidator;
import loop.model.plugin.Plugin;
import loop.model.simulationengine.PairBuilder;
import loop.model.simulationengine.RandomCooperationConsideringPairBuilder;

import java.util.ArrayList;
import java.util.List;

public class RandomCooperationConsideringPairBuilderPlugin extends Plugin<PairBuilder> {

    private static final String NAME = "Random Cooperation Considering Pair Builder";
    private static final String DESCRIPTION = "Tries to maximize mutual cooperation probability for each builded pair, i.e. builds pairs"
            + " such that the probability of both agents cooperating is as high as possible for each pair. Though with a certain probability,"
            + " given by the randomness factor, pairs the agents completely at random instead.";

    private List<Parameter> parameters = new ArrayList<Parameter>();

    public RandomCooperationConsideringPairBuilderPlugin() {
        Parameter randomnessFactorParameter = new Parameter(0.0, 1.0, "randomness factor",
                "Determines the probability with which pairs are built randomly.");
        parameters.add(randomnessFactorParameter);
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
    public PairBuilder getNewInstance(List<Double> params) {
        if (!ParameterValidator.areValuesValid(params, parameters)) {
            throw new IllegalArgumentException("Invalid parameters given for the creation of a 'random coop considering pair builder' object");
        }
        return new RandomCooperationConsideringPairBuilder(params.get(0));
    }
}
