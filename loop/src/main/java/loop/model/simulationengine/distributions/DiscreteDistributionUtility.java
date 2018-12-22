package loop.model.simulationengine.distributions;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This class provides some utility for implementations of the {@link DiscreteDistribution} interface, such as
 * generic provision of pickers and calculation of supports.
 * 
 * @author Peter Koepernik
 *
 */
public class DiscreteDistributionUtility {
    
    private final static double ACCURACY = Math.pow(10, -10);
    
    /**
     * Provides a generic way of calculating a support as requested by the {@code getSupportMin(double q)} and {@code getSupportMax(double q)}
     * methods in the {@link DiscreteDistribution} interface. Returns the lower bound of the support.
     * @param dist the distribution the support shall be calculated for
     * @param q the value for the calculation of the support
     * @param center a value as close to the mean of the distribution as possible. Will definitely be part of the calculated support
     * @return the lower bound of the support
     */
    public static int getSupportMin(final DiscreteDistribution dist, final double q, final int center) {
        return getSupport(dist, q, center).getMin();
    }
    
    /**
     * Provides a generic way of calculating a support as requested by the {@code getSupportMin(double q)} and {@code getSupportMax(double q)}
     * methods in the {@link DiscreteDistribution} interface. Returns the upper bound of the support.
     * @param dist the distribution the support shall be calculated for
     * @param q the value for the calculation of the support
     * @param center a value as close to the mean of the distribution as possible. Will definitely be part of the calculated support
     * @return the upper bound of the support
     */
    public static int getSupportMax(final DiscreteDistribution dist, final double q, final int center) {
        return getSupport(dist, q, center).getMax();
    }
    
    /**
     * Returns a picker to the given probability distribution.
     * @param dist the distribution a probability picker shall be returned for
     * @return a picker to the given probability distribution
     */
    public static Picker<Integer> getPicker(final DiscreteDistribution dist) {
        return new Picker<Integer>() {
            
            @Override
            public Integer pickOne() {
                int min = dist.getSupportMin(1 - ACCURACY);
                int max = dist.getSupportMax(1 - ACCURACY);
                int n = max - min;
                double[] cumProbs = new double[n + 1];
                cumProbs[min] = dist.getProbability(min);
                for (int i = 0; i < n; i++) {
                    cumProbs[i + 1] = cumProbs[i] + dist.getProbability(i + 1);
                }
                cumProbs[min + n] = 1;
                double r = new Random().nextDouble();
                int res = 0;
                while (r > cumProbs[res]) res++;
                return res;
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
    
    private static Support getSupport(final DiscreteDistribution dist, final double q, final int center) {
        int min = center;
        int max = center;
        double prob = dist.getProbability(center);
        while (prob < q) {
            if (dist.getProbability(max + 1) >= dist.getProbability(min - 1)) {
                prob += dist.getProbability(++max);
                continue;
            }
            prob += dist.getProbability(--min);
        }
        return new Support(min, max);
    }
    
    static class Support {
        private int min;
        private int max;
        
        public Support(int min, int max) {
            this.min = min;
            this.max = max;
        }
        
        public int getMin() {
            return min;
        }
        
        public int getMax() {
            return max;
        }
    }
}
