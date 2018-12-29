package loop.model.simulationengine.distributions;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

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
}
