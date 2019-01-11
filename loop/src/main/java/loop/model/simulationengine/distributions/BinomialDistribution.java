package loop.model.simulationengine.distributions;

import java.util.ArrayList;
import java.util.List;

import loop.model.plugin.Parameter;
import loop.model.plugin.ParameterValidator;
import loop.model.plugin.Plugin;

/**
 * Represents a binomial distribution.
 * 
 * @author Peter Koepernik
 *
 */
public class BinomialDistribution implements DiscreteDistribution {
    
    public static final String NAME = "Binomial Distribution";
    private static final String DESCRIPTION = "This is a binomial distribution that is shifted such that instead of taking values between"
            + " 0 and n, it takes values between specifiable bounds a and b, where a and b must be non-negative integers.";
    
    private int min;
    private int max;
    private double p;
    private int n;
    private org.apache.commons.math3.distribution.BinomialDistribution dist;
    
    /**
     * Creates a new binomial distribution with the given parameters.
     * 
     * @param min the lower bound of the distribution
     * @param max the upper bound of the distribution
     * @param p the probability parameter of the distribution
     */
    public BinomialDistribution(int min, int max, double p) {
        if (max < min || p < 0 || p > 1) {
            throw new IllegalArgumentException("Illegal arguments for creation of binomial distribution");
        }
        this.min = min;
        this.max = max;
        this.p = p;
        this.n = max - min;
        
        this.dist = new org.apache.commons.math3.distribution.BinomialDistribution(n, p);
    }
    
    @Override
    public double getProbability(Integer object) {
        if (object < min || object > max) return 0;
        int k = object - min;
        return this.dist.probability(k);
    }

    @Override
    public Picker<Integer> getPicker() {
        return new Picker<Integer>() {
            
            @Override
            public Integer pickOne() {
                return min + dist.sample();
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
    public int getSupportMin(double q) {
        return DiscreteDistributionUtility.getSupportMin(this, q, (int) Math.round(n * p));
    }

    @Override
    public int getSupportMax(double q) {
        return DiscreteDistributionUtility.getSupportMax(this, q, (int) Math.round(n * p));
    }
    
    /**
     * Returns a {@link Plugin} instance wrapping this implementation of the {@link DiscreteDistribution} interface.
     * 
     * @return a plugin instance.
     */
    public static Plugin<DiscreteDistribution> getPlugin() {
        if (plugin == null) {
            plugin = new BinomialDistributionPlugin();
        }
        return plugin;
    }
    
    private static BinomialDistributionPlugin plugin;
    
    private static class BinomialDistributionPlugin extends Plugin<DiscreteDistribution> {
        
        private List<Parameter> parameters = new ArrayList<Parameter>();
        
        public BinomialDistributionPlugin() {
            Parameter minParameter = new Parameter(0.0, 500.0, 1.0, "lower bound", "The lower bound of the distribution.");
            Parameter maxParameter = new Parameter(0.0, 500.0, 1.0, "upper bound", "The upper bound of the distribution.");
            Parameter probParameter = new Parameter(0.0, 1.0, "probability", "The probability of success, characterizing the bernoulli chain"
                    + " modelled by this distribution.");
            parameters.add(minParameter);
            parameters.add(maxParameter);
            parameters.add(probParameter);
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
                throw new IllegalArgumentException("Invalid parameters given for the creation of a 'binomial distribution' object");
            }
            return new BinomialDistribution(params.get(0).intValue(), params.get(1).intValue(), params.get(2));
        }
    }
    
}
