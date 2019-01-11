package loop.model.simulationengine.distributions;

import java.util.ArrayList;
import java.util.List;

import loop.model.plugin.Parameter;
import loop.model.plugin.ParameterValidator;
import loop.model.plugin.Plugin;

/**
 * Represents a discrete uniform distribution.
 * 
 * @author Peter Koepernik
 *
 */
public class DiscreteUniformDistribution implements DiscreteDistribution {

    private int min;
    private int max;
    
    /**
     * Creates a new discrete uniform distribution with given lower and upper bounds.
     * @param min the lower bound of this distribution
     * @param max the upper bound of this distribution
     */
    public DiscreteUniformDistribution(int min, int max) {
        if (max < min) {
            throw new IllegalArgumentException("Invalid arguments in creation of discrete uniform distribution.");
        }
        this.min = min;
        this.max = max;
    }
    
    @Override
    public double getProbability(Integer object) {
        if (object < min || object > max) return 0;
        return 1 / (max - min + 1);
    }

    @Override
    public Picker<Integer> getPicker() {
        return DiscreteDistributionUtility.getPicker(this);
    }

    @Override
    public int getSupportMin(double q) {
        return min;
    }

    @Override
    public int getSupportMax(double q) {
        return max;
    }
    
    private static final String NAME = "Discrete Unfiorm Distribution";
    private static final String DESCRIPTION = "This is a discrete uniform distribution with specifiable lower and upper bound.";
    
    /**
     * Returns a {@link Plugin} instance wrapping this implementation of the {@link DiscreteDistribution} interface.
     * 
     * @return a plugin instance.
     */
    public static Plugin<DiscreteDistribution> getPlugin() {
        if (plugin == null) {
            plugin = new DiscreteUniformDistributionPlugin();
        }
        return plugin;
    }
    
    private static DiscreteUniformDistributionPlugin plugin;
    
    private static class DiscreteUniformDistributionPlugin extends Plugin<DiscreteDistribution> {
        
        private List<Parameter> parameters = new ArrayList<Parameter>();
        
        public DiscreteUniformDistributionPlugin() {
            Parameter minParameter = new Parameter(0.0, 500.0, 1.0, "lower bound", "The lower bound of this distribution.");
            Parameter maxParameter = new Parameter(0.0, 500.0, 1.0, "upper bound", "The upper bound of this distribution.");
            parameters.add(minParameter);
            parameters.add(maxParameter);
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
        public DiscreteDistribution getNewInstance(List<Double> params) {
            if (!ParameterValidator.areValuesValid(params, parameters)) {
                throw new IllegalArgumentException("Invalid parameters given for the creation of a 'discrete uniform distribution' object");
            }
            return new DiscreteUniformDistribution(params.get(0).intValue(), params.get(1).intValue());
        }
    }
    
}
