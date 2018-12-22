package loop.model.simulationengine.distributions;

/**
 * This interface represents a probability distribution over a set of objects of type E. An implementation
 * returns the probability of a given object upon request, and provides a {@link Picker<E>} to randomly pick
 * objects from the probability distribution.
 * 
 * @author Peter Koepernik
 *
 * @param <E> The type of objects in this distribution 
 */
public interface Distribution<E> {
    
    /**
     * Returns the probability of the given object in this distribution.
     * @param object the object whose probability shall be returned
     * @return the probability of the given object in this distribution
     */
    double getProbability(final E object);
    
    /**
     * Returns a {@link Picker<E>} for this probability distribution.
     * @return a {@link Picker<E>} for this probability distribution
     */
    Picker<E> getPicker();
}
