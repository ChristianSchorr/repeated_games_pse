package loop.model.simulationengine.distributions;

import java.util.List;

/**
 * A {@link Picker<E>} is provided by implementations of the {@link Distribution<E>} interface to pick
 * objects from the associated probability distribution.
 * 
 * @author Peter Koepernik
 *
 * @param <E> the type of objects in the distribution this picker picks from
 */
public interface Picker<E> {
    /**
     * Picks an object out of the probability distribution and returns it.
     * @return the picked object
     */
    E pickOne();
    
    /**
     * Picks multiple elements out of the probability distribution and returns them as {@link List<E>}.
     * @param i the amount of objects that shall be picked
     * @return the picked objects as {@link List<E>}
     */
    List<E> pickMany(final int i);
}
