package loop.model.simulationengine;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import loop.model.simulationengine.distributions.BinomialDistribution;
import loop.model.simulationengine.distributions.DiscreteDistribution;
import loop.model.simulationengine.distributions.UniformFiniteDistribution;
import loop.model.simulationengine.strategies.MixedStrategy;
import loop.model.simulationengine.strategies.PureStrategy;
import loop.model.simulationengine.strategies.Strategy;

/**
 * This class holds tests for implementations of the {@link AgentInitialiser} class.
 * 
 * @author Sebastian Feurer
 *
 */
public class AgentInitialiserTest {
	AgentInitialiser agentInitialiser;
	ArrayList<EngineSegment> engineSegmentList = new ArrayList<EngineSegment>();

	/**
	 * Initialize the AgentInitialiser agentInitialiser
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		agentInitialiser = new AgentInitialiser();
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test the method initialiseAgents with the parameters engineSegment and false.
	 * EngineSegment engineSegment is created in this test. 
	 */
	@Test
	public void testInitialiseAgents() {
		int agentcount = 100;
		int groupid = 1;
		DiscreteDistribution binomialdis = new BinomialDistribution(10, 100, 0.5);
		       
        UniformFiniteDistribution<Strategy> strategyDistribution = new UniformFiniteDistribution<Strategy>();
        strategyDistribution.addObject(PureStrategy.alwaysCooperate());
        strategyDistribution.addObject(PureStrategy.neverCooperate());
        strategyDistribution.addObject(PureStrategy.titForTat());
        strategyDistribution.addObject(PureStrategy.grim());
        
		EngineSegment engineSegment = new EngineSegment(agentcount, groupid, binomialdis, strategyDistribution);
		engineSegmentList.add(engineSegment);
		List<Agent> agents = agentInitialiser.initialiseAgents(engineSegmentList, false);
		
		for (Agent agent : agents) {
			assertTrue("The Strategy of the agent should be grim, alwaysCooperate, "
					+ "neverCooperate or titForTat but is:" + agent.getStrategy().getName(),
					agent.getStrategy().getName().equals(PureStrategy.alwaysCooperate().getName()) ||
					agent.getStrategy().getName().equals(PureStrategy.neverCooperate().getName()) ||
					agent.getStrategy().getName().equals(PureStrategy.titForTat().getName()) ||
					agent.getStrategy().getName().equals(PureStrategy.grim().getName()));
			assertTrue("The Initialcapital of the agent should be between 10 and 100 but is:" + agent.getInitialCapital(),
					agent.getInitialCapital() <= 100 && agent.getInitialCapital() >= 10);
			assertTrue("The groupID should be 1. Actual it is" + agent.getGroupId(), agent.getGroupId() == 1);
			assertTrue("The agentcount should be 100. Actual it is" + agents.size(), agents.size() == 100);
		}
	}
	
	/**
	 * Test the method initialiseAgents with the parameters engineSegment and true.
	 * EngineSegment engineSegment is created in this test. 
	 */
	@Test
	public void testInitialiseAgentsWithMixedStrategies() {
		int agentcount = 150;
		int groupid = 2;
		DiscreteDistribution binomialdis = new BinomialDistribution(-25, 0, 0.6);
		       
        UniformFiniteDistribution<Strategy> strategyDistribution = new UniformFiniteDistribution<Strategy>();
        strategyDistribution.addObject(PureStrategy.alwaysCooperate());
        strategyDistribution.addObject(PureStrategy.neverCooperate());
        
		EngineSegment engineSegment = new EngineSegment(agentcount, groupid, binomialdis, strategyDistribution);
		engineSegmentList.add(engineSegment);
		List<Agent> agents = agentInitialiser.initialiseAgents(engineSegmentList, true);
		
		for (Agent agent : agents) {
			MixedStrategy mixedStrategy = (MixedStrategy) agent.getStrategy();
			assertTrue("The Strategy of the agent should always be mix of alwaysCooperate and "
					+ "neverCooperate but is:" + mixedStrategy.getComponentStrategies().get(0).getName() +
					mixedStrategy.getComponentStrategies().get(1).getName(),
					(mixedStrategy.getComponentStrategies().get(0).getName().equals(PureStrategy.alwaysCooperate().getName()) ||
					mixedStrategy.getComponentStrategies().get(0).getName().equals(PureStrategy.neverCooperate().getName())) &&
					(mixedStrategy.getComponentStrategies().get(1).getName().equals(PureStrategy.alwaysCooperate().getName()) ||
					mixedStrategy.getComponentStrategies().get(1).getName().equals(PureStrategy.neverCooperate().getName())));
			assertTrue("The Initialcapital of the agent should be between -25 and 0 but is:" + agent.getInitialCapital(),
					agent.getInitialCapital() <= 0 && agent.getInitialCapital() >= -25);
			assertTrue("The groupID should be 1. Actual it is" + agent.getGroupId(), agent.getGroupId() == 2);
			assertTrue("The agentcount should be 150. Actual it is" + agents.size(), agents.size() == 150);
		}
	}

}
