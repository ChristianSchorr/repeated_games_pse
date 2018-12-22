package loop.model.simulationengine.distributions;

/**
 * Represents a discrete uniform distribution.
 * 
 * @author Peter Koepernik
 *
 */
public class DiscreteUniformDistribution implements DiscreteDistribution {

    int min;
    int max;
    
    /**
     * Creates a new discrete uniform distribution with given lower and upper bounds.
     * @param min the lower bound of this distribution
     * @param max the upper bound of this distribution
     */
    public DiscreteUniformDistribution(int min, int max) {
        this.min = min;
        this.max = max;
    }
    
    @Override
    public double getProbability(Integer object) {
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

}
