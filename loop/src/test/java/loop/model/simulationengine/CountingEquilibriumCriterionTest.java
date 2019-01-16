package loop.model.simulationengine;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import loop.model.simulationengine.strategies.PureStrategy;
import loop.model.simulationengine.strategies.Strategy;

/**
 * This class holds tests for implementations of the {@link CountingEquilibriumCriterion} class.
 * 
 * @author Sebastian Feurer
 *
 */
public class CountingEquilibriumCriterionTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testIsEquilibrium() {
		SimulationHistoryTable simulationHistoryTable = new SimulationHistoryTable();
        List<Agent> agents = new ArrayList<Agent>();
        Strategy titForTat = PureStrategy.titForTat();
        Strategy grim = PureStrategy.grim();
        Strategy nevercooperate = PureStrategy.neverCooperate();
        agents.add(new Agent(0,titForTat, 1));
        agents.add(new Agent(0,grim, 1));
        agents.add(new Agent(0,grim, 1));
        agents.add(new Agent(0,nevercooperate, 1));
        
        GameResult result1 = new GameResult(agents.get(0), agents.get(1), true, true, 5, 5);
        simulationHistoryTable.addResult(result1);
          
        GameResult result2 = new GameResult(agents.get(2), agents.get(3), true, false, 6, 0); 
        simulationHistoryTable.addResult(result2);
	}
}
