package loop.model.repository;

import java.util.List;

/**
 * A repository stores an association of names and entities and makes them
 * available on request.
 * 
 * @author Christian Schorr
 *
 * @param <T> the type of the entities that shall be stored in this repository
 */
public interface Repository<T> {

	/**
	 * Returns the entity with the given name, if currently stored, {@code null}
	 * otherwise.
	 * 
	 * @param name the name of the entity that shall be returned
	 * @return the entity with the given name if stored, {@code null} otherwise
	 */
	public T getEntityByName(String name);
	
	/**
	 * Returns the entities with the given names in the order the names were given,
	 * where {@code null} is used for each name not corresponding to a stored entity.
	 * 
	 * @param names the names of the desired entities
	 * @return the entities with the given names
	 */
	public List<T> getEntitiesByNames(List<String> names);
	
	/**
	 * Removes the entity with the given name, if currently stored.
	 * 
	 * @param name the name of the entity that shall be removed
	 */
	public void removeEntity(String name);
	
	/**
	 * Returns all entities in this repository
	 *
	 * @return all entities in this repository
	 */
	public List<T> getAllEntities();
	
	/**
	 * Adds a new entity with the given name to the repository, if no entity with
	 * the given name is already stored.
	 * 
	 * @param name   the name of the entity that shall be added
	 * @param entity the entity that shall be added
	 * @return {@code true} if the entity was successfully added, {@code false}
	 *         otherwise
	 */
	public boolean addEntity(String name, T entity);

	/**
	 * Returns whether this repository contains an entity with the given name.
	 * 
	 * @param name the name that shall be checked
	 * @return {@code true} if an entity with the given name is stored,
	 *         {@code false} otherwise
	 */
	public boolean containsEntityName(String name);

	/**
	 * Returns all entity keys used in this repository
	 * 
	 * @return a list of all entity keys
	 */
	public List<String> getAllEntityNames();
}
