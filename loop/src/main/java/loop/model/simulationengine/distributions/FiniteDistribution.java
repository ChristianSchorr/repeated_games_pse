package loop.model.simulationengine.distributions;

import java.util.Collection;

/**
 * Represents a probability distribution over objects of type E with nite support. Upon request,
 * implementations return the support as {@link java.util.Collection<E>}.
 * 
 * @author Peter Koepernik
 *
 * @param <E> the type of objects in this distribution
 */
public interface FiniteDistribution<E> extends Distribution<E> {

    /**
     * Returns the support of this distribution.
     * @return the support of this distribution
     */
    Collection<E> getSupport();
}
