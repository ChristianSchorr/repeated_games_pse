package loop.plugins.pairbuilder;

import loop.model.plugin.Parameter;
import loop.model.plugin.ParameterValidator;
import loop.model.plugin.Plugin;
import loop.model.simulationengine.PairBuilder;
import loop.model.simulationengine.RandomPairBuilder;

import java.util.ArrayList;
import java.util.List;

public class RandomPairBuilderPlugin extends Plugin<PairBuilder> {

    private static final String NAME = "Random Pair Builder";
    private static final String DESCRIPTION = "Randomly builds pairs of agents until all agents are matched.";

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
            throw new IllegalArgumentException("Invalid parameters given for the creation of a 'random pair builder' object");
        }
        return new RandomPairBuilder();
    }
}
