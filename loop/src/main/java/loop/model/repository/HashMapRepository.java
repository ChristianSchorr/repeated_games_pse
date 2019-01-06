package loop.model.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An implementation of the {@link Repository<T>}-interface that stores entities
 * in a HashMap .
 * 
 * @author Christian Schorr
 *
 * @param <T> the type of the entities that shall be stored in this repository
 */
public class HashMapRepository<T> implements Repository<T> {
	
	private Map<String, T> repo = new HashMap<String, T>();

	@Override
	public T getEntityByName(String name) {
		return repo.get(name);
	}

	@Override
    public List<T> getEntitiesByNames(List<String> names) {
        List<T> entities = new ArrayList<T>();
	    names.forEach(name -> entities.add(getEntityByName(name)));
	    return entities;
    }
	
	@Override
	public boolean addEntity(String name, T entity) {
		if (repo.containsKey(name)) return false;
		repo.put(name, entity);
		return true;
	}

	@Override
	public boolean containsEntityName(String name) {
		return repo.containsKey(name);
	}

	@Override
	public List<String> getAllEntityNames() {
		return new ArrayList<String>(repo.keySet());
	}

}
