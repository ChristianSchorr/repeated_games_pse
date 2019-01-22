package loop.model.repository;

import static org.junit.Assert.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

import loop.model.UserConfiguration;
import loop.model.simulationengine.strategies.PureStrategy;
import loop.model.simulator.SimulationResult;

/**
 * This class tests the functionality of the FileIO.
 * 
 * @author Pierre Toussing
 *
 */
public class FileIOTest {
	private PureStrategy p1, p2, p3, p4, p5, test;
	private File file1, file2, file3, file4, file5;
	private SimulationResult result;
	
	/**
	 * Set up some PureStrategys (which are serializable)
	 */
	@Before
	public void setUp() {
		p1 = new PureStrategy("test1", "strategy1", null);
		p2 = new PureStrategy("test2", "strategy2", null);
		p3 = new PureStrategy("test3", "strategy3", null);
		p4 = new PureStrategy("test4", "strategy4", null);
		p5 = new PureStrategy("test5", "strategy5", null);
		test = null;
		file1 = new File("./src/test/resources/JavaIOTest/teststrategy1");
		file2 = new File("./src/test/resources/JavaIOTest/test1/teststrategy2");
		file3 = new File("./src/test/resources/JavaIOTest/test1/teststrategy3");
		file4 = new File("./src/test/resources/JavaIOTest/test2/teststrategy4");
		file5 = new File("./src/test/resources/JavaIOTest/test1/test/teststrategy");
		result = new SimulationResult(UserConfiguration.getDefaultConfiguration(), 17);
	}

	/**
	 * Tests saving and loading a serializable
	 */
	@Test
	public void testSaveAndLoad() {
		try {
			FileIO.saveEntity(file1, p1);
			test = (PureStrategy) FileIO.loadEntity(file1);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		assertTrue(p1.getName().equals(test.getName()));
		assertTrue(p1.getDescription().equals(test.getDescription()));
	}
	
	/**
	 * Tests the functionalitiy to find all files and get their objects
	 */
	@Test
	public void testLoadAll() {
		try {
			FileIO.saveEntity(file1, p1);
			FileIO.saveEntity(file2, p2);
			FileIO.saveEntity(file3, p3);
			FileIO.saveEntity(file4, p4);
			FileIO.saveEntity(file5, p5);
		} catch (IOException e) {
			e.printStackTrace();
		}	
		List<Object> list = null;
		try {
			list = FileIO.loadAllEntities(new File("./src/test/resources/JavaIOTest"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		assertTrue(list.size() == 5);		//All files found
				
		ArrayList<String> nameList = new ArrayList<String>();
		for (int k = 0; k < 5; k++) {
			nameList.add(((PureStrategy) list.get(k)).getName());
		}
		assertTrue(nameList.contains(p1.getName()));
		assertTrue(nameList.contains(p2.getName()));
		assertTrue(nameList.contains(p3.getName()));
		assertTrue(nameList.contains(p4.getName()));
		assertTrue(nameList.contains(p5.getName()));
	}
	
	@Test
	public void testSavingResults() {
		FileIO.saveResult(result, new File("./src/test/resources/JavaIOResult"));
		SimulationResult test = FileIO.loadResult(new File("./src/test/resources/JavaIOResult"));
		assert(result.getId() == test.getId());
		assert(result.getFinishedIterations() == test.getFinishedIterations());
		assert(result.getTotalIterations() == test.getTotalIterations());
	}
}
