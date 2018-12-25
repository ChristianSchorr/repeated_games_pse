package loop.model.simulationengine.strategies;

/**
 * This interface represents a vector of xed size holding entities of type {@code T}. An implementation
 * allows retrieving and overwriting vector components and provides a cloning method.
 * @author Peter Koepernik
 *
 * @param <T> the type of the components of this vector
 */
public interface Vector<T> {
    
    /**
     * Returns the size of the vector.
     * 
     * @return the size of the vector
     */
    int getSize();
    
    /**
     * Returns the component at the given index.
     * 
     * @param index the index of the component that shall be returned
     * @return the component at the given index
     */
    T getComponent(int index);
    
    /**
     * Set the component at the given index to the given value.
     * 
     * @param index the index of the component that shall be set to the given value
     * @param value the value that the component at given index shall be set to
     */
    void setComponent(int index, T value);
    
    /**
     * Returns a copy of the vector.
     * 
     * @return a copy of the vector
     */
    Vector<T> clone();
}
