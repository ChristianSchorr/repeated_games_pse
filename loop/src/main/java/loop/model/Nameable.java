package loop.model;


/**
 * Implementations of this interface have a name and a description, which can be queried
 * as {@code String}s.
 * 
 * @author Peter Koepernik
 *
 */
public interface Nameable {
    
    /**
     * Returns the name of the object.
     * @return the name of the object
     */
    String getName();
    
    /**
     * Returns the description of the object.
     * @return the description of the object
     */
    String getDescription();
}
