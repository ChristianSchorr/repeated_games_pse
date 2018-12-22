package loop.model.simulationengine.distributions;

/**
 * Represents a discrete probability distribution on the natural numbers. An implementation
 * calculates an interval I_q = [a, b] for a given value q in (0, 1), such that a
 * randomly picked value from the distribution is in I_q with probability greater or equal
 * to q. The boundary points a and b of this interval can be queried.
 * 
 * @author Peter Koepernik
 *
 */
public interface DiscreteDistribution extends Distribution<Integer> {
    /**
     * Returns the lower bound of I_q.
     * @param q the value for the determination of I_q
     * @return the lower bound of I_q
     */
    int getSupportMin(final double q);
    
    /**
     * Returns the upper bound of I_q.
     * @param q the value for the determination of I_q
     * @return the upper bound of I_q
     */
    int getSupportMax(final double q);
}
