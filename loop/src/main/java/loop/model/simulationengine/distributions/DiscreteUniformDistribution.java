package loop.model.simulationengine.distributions;

/**
 * Represents a discrete uniform distribution.
 * 
 * @author Peter Koepernik
 *
 */
public class DiscreteUniformDistribution implements DiscreteDistribution {
    
    public static final String NAME = "Discrete Unfiorm Distribution";
    public static final String DESCRIPTION = "This is a discrete uniform distribution with specifiable lower and upper bound.";
    
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
        return 1.0 / (max - min + 1);
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
