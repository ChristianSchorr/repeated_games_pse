package loop.model.repository;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

/**
 * This class tests the {@link HashMapRepository}-class
 * 
 * @author Christian Schorr
 *
 */
public class HashMapRepositoryTest {
	
	@Test
	public void testAddGetEntity() {
		HashMapRepository<Integer> repo = new HashMapRepository<Integer>();
	 	assertTrue(repo.addEntity("1", 1));
	 	assertTrue(repo.addEntity("2", 2));
	 	assertTrue(repo.addEntity("3", 3));
	 	assertFalse(repo.addEntity("1", 3));
	 	
	 	Integer i1 = repo.getEntityByName("1");
	 	Integer i2 = repo.getEntityByName("2");
	 	Integer i3 = repo.getEntityByName("3");
	 	
	 	assertEquals(1, i1.doubleValue(), 0);
	 	assertEquals(2, i2.doubleValue(), 0);
	 	assertEquals(3, i3.doubleValue(), 0);
	}
	
	@Test
	public void testContainsEntityName() {
		HashMapRepository<Integer> repo = new HashMapRepository<Integer>();
	 	assertTrue(repo.addEntity("1", 1));
	 	assertTrue(repo.addEntity("2", 2));
	 	assertTrue(repo.addEntity("3", 3));
	 	assertFalse(repo.addEntity("1", 3));
	 	
	 	assertTrue(repo.containsEntityName("1"));
	 	assertTrue(repo.containsEntityName("2"));
	 	assertTrue(repo.containsEntityName("3"));
	 	assertFalse(repo.containsEntityName("4"));
	}

	@Test
	public void testGetAllEntityNames() {
		HashMapRepository<Integer> repo = new HashMapRepository<Integer>();
	 	assertTrue(repo.addEntity("1", 1));
	 	assertTrue(repo.addEntity("2", 2));
	 	assertTrue(repo.addEntity("3", 3));
	 	
	 	List<String> entityNames = repo.getAllEntityNames();
	 	List<String> expected = Arrays.asList("1", "2", "3");
	 	assertThat(entityNames, is(expected));
	}
}
