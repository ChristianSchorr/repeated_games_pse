package loop.model.simulationengine.distributions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Represents a uniform distribution over objects of type E with finite support. Implementations
 * provide methods for adding and removing objects.
 * 
 * @author Peter Koepernik
 *
 * @param <E> the type of objects in this distribution
 */
public class UniformFiniteDistribution<E> implements FiniteDistribution<E> {
    
    private List<E> support;
    
    /**
     * Creates a new uniform finite distribution with empty support.
     */
    public UniformFiniteDistribution() {
        support = new ArrayList<E>();
    }
    
    /**
     * Creates a new uniform finite distribution with the given objects as support.
     * @param objects the objects the support of this distribution shall consist of
     */
    public UniformFiniteDistribution(Collection<E> objects) {
        this.support = new ArrayList<E>(objects);
    }
    
    /**
     * Adds the given object to the support.
     * @param object the object that shall be added
     */
    void addObject(E object) {
        support.add(object);
    }
    
    /**
     * Adds the given objects to the support.
     * @param objects the objects that shall be added
     */
    void addObjects(Collection<E> objects) {
        support.addAll(objects);
    }
    
    /**
     * Removes the given object from the support, if contained.
     * @param object the object that shall be removed
     * @return {@code true} if the given object was contained and successfully removed, {@code false} otherwise
     */
    boolean removeObject(E object) {
        if (!support.contains(object)) return false;
        support.remove(object);
        return true;
    }
    
    @Override
    public double getProbability(E object) {
        return 1 / support.size();
    }

    @Override
    public Picker<E> getPicker() {
        return new Picker<E>() {

            @Override
            public E pickOne() {
                int i = new Random().nextInt(support.size());
                return support.get(i);
            }

            @Override
            public List<E> pickMany(int i) {
                List<E> res = new ArrayList<E>();
                for (int j = 0; j < i; j++) {
                    res.add(pickOne());
                }
                return res;
            }
        };
    }

    @Override
    public Collection<E> getSupport() {
        return support;
    }

}
