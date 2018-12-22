package loop.model.simulationengine.distributions;

/**
 * Represents a binomial distribution.
 * 
 * @author Peter Koepernik
 *
 */
public class BinomialDistribution implements DiscreteDistribution {

    private int min;
    private int max;
    private double p;
    private int n;
    
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
    }
    
    @Override
    public double getProbability(Integer object) {
        if (object < min || object > max) return 0;
        int k = object - min;
        return binCoeff(n, k) * Math.pow(p, k) * Math.pow(1 - p, n - k);
    }

    @Override
    public Picker<Integer> getPicker() {
        return DiscreteDistributionUtility.getPicker(this);
    }

    @Override
    public int getSupportMin(double q) {
        return DiscreteDistributionUtility.getSupportMin(this, q, (int) Math.round(n * p));
    }

    @Override
    public int getSupportMax(double q) {
        return DiscreteDistributionUtility.getSupportMax(this, q, (int) Math.round(n * p));
    }
    
    private int binCoeff(int n, int k)
    {
        if ((n < 0) || (k < 0) || (k > n))
            throw new IllegalArgumentException(n + ", " + k);
        if (k > n/2) k = n - k;

        int result = 1;

        for (int i = n - k + 1; i <= n; i++)
            result *= i;
        for (int i = 2; i <= k; i++)
            result /= i;

        return result;
    }

}
