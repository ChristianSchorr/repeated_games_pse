package loop.model.repository;

import static org.junit.Assert.*;

import org.junit.Test;


/**
 * This class test the {@link CentralRepository}-class
 * 
 * @author Christian Schorr
 *
 */
public class CentralRepositoryTest {
	
	@Test
	public void testSingletonMechanism() {
		CentralRepository repo1 = CentralRepository.getInstance();
		assertNotNull(repo1);
		CentralRepository repo2 = CentralRepository.getInstance();
		assertNotNull(repo2);
		assertEquals(repo1, repo2);
	}
}
