package loop.model.simulationengine.distributions;

import java.math.BigInteger;

/**
 * Represents a Poisson distribution.
 * 
 * @author Peter Koepernik
 *
 */
public class PoissonDistribution implements DiscreteDistribution {

    private double lambda;
    
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
    }
    
    @Override
    public double getProbability(final Integer object) {
        if (object < 0) return 0;
        return Math.exp(-lambda) * Math.pow(lambda, object) / factorial(BigInteger.valueOf(object)).doubleValue();
    }

    @Override
    public Picker<Integer> getPicker() {
        return DiscreteDistributionUtility.getPicker(this);
    }

    @Override
    public int getSupportMin(final double q) {
        return DiscreteDistributionUtility.getSupportMin(this, q, (int) Math.round(lambda));
    }

    @Override
    public int getSupportMax(final double q) {
        return DiscreteDistributionUtility.getSupportMax(this, q, (int) Math.round(lambda));
    }
    
    private BigInteger factorial(BigInteger number) {
        BigInteger result = BigInteger.valueOf(1);

        for (long factor = 2; factor <= number.longValue(); factor++) {
            result = result.multiply(BigInteger.valueOf(factor));
        }

        return result;
    }
}
