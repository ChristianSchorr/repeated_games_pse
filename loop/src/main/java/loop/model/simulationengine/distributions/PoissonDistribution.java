package loop.model.simulationengine.distributions;

import java.util.ArrayList;
import java.util.List;

import loop.model.plugin.Parameter;
import loop.model.plugin.ParameterValidator;
import loop.model.plugin.Plugin;

/**
 * Represents a Poisson distribution.
 * 
 * @author Peter Koepernik
 *
 */
public class PoissonDistribution implements DiscreteDistribution {

    private double lambda;
    private org.apache.commons.math3.distribution.PoissonDistribution dist;
    
    /**
     * Creates a new Poisson distribution with the given mean.
     * 
     * @param lambda the mean of the Poisson distribution
     */
    public PoissonDistribution(final double lambda) {
        if (lambda < 0) {
            throw new IllegalArgumentException("passed a negative value as the mean of a poisson distribution.");
        }
        this.lambda = lambda;
        this.dist = new org.apache.commons.math3.distribution.PoissonDistribution(lambda);
    }
    
    @Override
    public double getProbability(final Integer object) {
        if (object < 0) return 0;
        return dist.probability(object);
    }

    @Override
    public Picker<Integer> getPicker() {
        return new Picker<Integer>() {
            
            @Override
            public Integer pickOne() {
                return dist.sample();
            }
            
            @Override
            public List<Integer> pickMany(final int i) {
                List<Integer> res = new ArrayList<Integer>();
                for (int j = 0; j < i; j++) {
                    res.add(pickOne());
                }
                return res;
            }
        };
    }

    @Override
    public int getSupportMin(final double q) {
        return DiscreteDistributionUtility.getSupportMin(this, q, (int) Math.round(lambda));
    }

    @Override
    public int getSupportMax(final double q) {
        return DiscreteDistributionUtility.getSupportMax(this, q, (int) Math.round(lambda));
    }
    
    private static final String NAME = "Poisson Distribution";
    private static final String DESCRIPTION = "This is a poisson distribution.";
    
    /**
     * Returns a {@link Plugin} instance wrapping this implementation of the {@link DiscreteDistribution} interface.
     * 
     * @return a plugin instance.
     */
    public static Plugin<DiscreteDistribution> getPlugin() {
        if (plugin == null) {
            plugin = new PoissonDistributionPlugin();
        }
        return plugin;
    }
    
    private static PoissonDistributionPlugin plugin;
    
    private static class PoissonDistributionPlugin extends Plugin<DiscreteDistribution> {
        
        private List<Parameter> parameters = new ArrayList<Parameter>();
        
        public PoissonDistributionPlugin() {
            Parameter lambdaParameter = new Parameter(0.0, 500.0, "mean", "The mean of this distribution.");
            parameters.add(lambdaParameter);
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
                throw new IllegalArgumentException("Invalid parameters given for the creation of a 'poisson distribution' object");
            }
            return new PoissonDistribution(params.get(0));
        }
    }
    
}
