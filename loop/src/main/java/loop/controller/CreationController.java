package loop.controller;

import java.util.function.Consumer;

/**
 * This interface gets implemented by all controllers which are associated with creation windows.
 * It provides the possibility to register a callback that is called whenever a new instance
 * is created.
 * @author Peter Koepernik
 *
 * @param <T> the type of the entity created by the controller
 */
public interface CreationController<T> {
    
    /**
     * Registers a callback which gets invoked whenever a new entity has been created
     * and passes the newly created entity to it.
     * 
     * @param action the callback that shall be registered
     */
    void registerElementCreated(Consumer<T> action);
}
