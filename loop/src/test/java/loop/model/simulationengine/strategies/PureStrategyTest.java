package loop.model.simulationengine.strategies;

import static org.junit.Assert.*;


import org.junit.Before;
import org.junit.Test;
import loop.model.simulationengine.Agent;
import loop.model.simulationengine.GameResult;
import loop.model.simulationengine.SimulationHistory;
import loop.model.simulationengine.SimulationHistoryTable;
import loop.model.simulationengine.strategies.PureStrategy.AgentEntity;
/**
 * This class holds tests for implementations of the {@link PureStrategy} class.
 * 
 * @author Sebastian Feurer
 *
 */
import loop.model.simulationengine.strategies.PureStrategy.TimeAdverb;
public class PureStrategyTest {
	PureStrategy testPureStrategy;
	Agent player;
	Agent inSameGroupAsPlayer;
	Agent opponent;
	SimulationHistory history;
	TimeAdverb when;
	AgentEntity cooperatedWithWhom;
	
	/**
	 * Initialize the PureStrategy testPureStrategy, the Simulationhistory history and some Agents
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		testPureStrategy = PureStrategy.titForTat();
		inSameGroupAsPlayer = new Agent(0, PureStrategy.titForTat(), 1);
		opponent = new Agent(10, PureStrategy.neverCooperate(), 2);
		history = new SimulationHistoryTable();
	}
	
	/**
	 * Tests the tit-for-tat strategy
	 */
	@Test
    public void testTitForTat() {
	    testPureStrategy = PureStrategy.titForTat();
	    player = new Agent(0, testPureStrategy, 1);
	    testGetName("tit-for-tat");
	    testGetDescription("A player using tit-for-tat will first cooperate, afterwards he replicate the opponent's previous action."
                + " If the opponent previously was cooperative, the player is cooperative. If the opponent previously wasn't cooperative,"
                + " the player is not cooperative.");
	    boolean[] cooperate = {true, true, false, false, true};
	    testIsCooperative(cooperate);
	    history.reset();
	    testGetCooperationProbability(cooperate);	    
	}
	
	/**
     * Tests the grim strategy
     */
    @Test
    public void testGrim() {
        testPureStrategy = PureStrategy.grim();
        player = new Agent(0, testPureStrategy, 1);
        testGetName("grim");
        testGetDescription("A player using grim will first cooperate, afterwards he refer to the previous actions of the opponent."
                + " If the opponent previously was always cooperative, the agent is cooperative. If the opponent was at least one time"
                + " not cooperative, the agent is from now on not cooperative to that opponent.");
        boolean[] cooperate = {true, true, false, false, false};
        testIsCooperative(cooperate);
        history.reset();
        testGetCooperationProbability(cooperate);
    }
    
    /**
     * Tests the group tit-for-tat strategy
     */
    @Test
    public void testGroupTitForTat() {
        testPureStrategy = PureStrategy.groupTitForTat();
        player = new Agent(0, testPureStrategy, 1);
        testGetName("group tit-for-tat");
        testGetDescription("A player using group tit-for-tat use the tit-for-tat strategy, where instead of looking"
                + "at the last game between the player and the opponent the last game between the opponent and an agent"
                + "of the same (cohesive) group as the player is considered. If the player is part of a non-cohesive group,"
                + "this strategy leads to the same results as the common tit-for-tat strategy.");
        boolean[] cooperate = {true, false, false, true, true};
        testIsCooperative(cooperate);
        history.reset();
        testGetCooperationProbability(cooperate);
    }
	
	/**
	 * Tests the group grim strategy
	 */
	@Test
    public void testGroupGrim() {
        testPureStrategy = PureStrategy.groupGrim();
        player = new Agent(0, testPureStrategy, 1);
        testGetName("group grim");
        testGetDescription("A player using group grim use the grim strategy, where instead of looking at the last game between"
                + " the player and the opponent the last game between the opponent and an agent of the same (cohesive) group"
                + " as the player is considered. If the player is part of a non-cohesive group, this strategy leads to the same results"
                + " as the common grim strategy.");
        boolean[] cooperate = {true, false, false, false, false};
        testIsCooperative(cooperate);
        history.reset();
        testGetCooperationProbability(cooperate);
	}
	
	/**
	 * Test the always cooperate method
	 */
	@Test
	public void testAlwaysCooperate() {
	    testPureStrategy = PureStrategy.alwaysCooperate();
	    player = new Agent(0, testPureStrategy, 1);
	    testGetName("always cooperate");
	    testGetDescription("A player using always cooperate will be cooperative against all opponents");
	    boolean[] cooperate = {true, true, true, true, true};
        testIsCooperative(cooperate);
        history.reset();
        testGetCooperationProbability(cooperate);
	}
	
	/**
     * Test the never cooperate strategy
     */
    @Test
    public void testNeverCooperate() {
        testPureStrategy = PureStrategy.neverCooperate();
        player = new Agent(0, testPureStrategy, 1);
        assertTrue(testPureStrategy.getName().equals("never cooperate"));
        assertTrue(testPureStrategy.getDescription().equals("A player using never cooperate won't be cooperative "
                + "against any opponent."));
        boolean[] cooperate = {false, false, false, false, false};
        testIsCooperative(cooperate);
        history.reset();
        testGetCooperationProbability(cooperate);
    }    
    
    /**
     * Tests the stratBuilderStrategy method to generate a new strategy
     */
    @Test
    public void testStratBuilderStrategy() {
        when = TimeAdverb.LASTTIME;
        cooperatedWithWhom = AgentEntity.SAME_GROUP;        
        testPureStrategy = PureStrategy.stratBuilderStrategy(cooperatedWithWhom, when, 0.5); 
        player = new Agent(0, testPureStrategy, 1);
        assertTrue(testPureStrategy.getName().equals("stratbuilder strategy"));
        assertTrue(testPureStrategy.getDescription().equals("SAME_GROUP, LASTTIME"));
        boolean[] cooperate = {true, false, false, true, true};
        testIsCooperative(cooperate);
        history.reset();
        testGetCooperationProbability(cooperate);
        
        when = TimeAdverb.ATLEASTONCE;
        cooperatedWithWhom = AgentEntity.AGENT;        
        testPureStrategy = PureStrategy.stratBuilderStrategy(cooperatedWithWhom, when, 0.5); 
        player = new Agent(0, testPureStrategy, 1);
        assertTrue(testPureStrategy.getName().equals("stratbuilder strategy"));
        assertTrue(testPureStrategy.getDescription().equals("AGENT, ATLEASTONCE"));
        boolean[] cooperate2 = {false, true, true, true, true};
        testIsCooperative(cooperate2);
        history.reset();
        testGetCooperationProbability(cooperate2);
    }
    

    public void testGetName(String name) {
        assertTrue(testPureStrategy.getName().equals(name));
    }

    public void testGetDescription(String description) {
        assertTrue(testPureStrategy.getDescription().equals(description));
    }
	
	/**
	 * Tests the implementation of the method isCooperative for the different strategies
	*/
	public void testIsCooperative(boolean[] cooperate) {
	    int i = 0;
		assertTrue(cooperate[i++] == testPureStrategy.isCooperative(player, opponent, history));
		
		history.addResult(new GameResult(player, opponent, true, true, 5, 5)); 
        history.addResult(new GameResult(inSameGroupAsPlayer, opponent, true, false, 0, 8)); 
              
        assertTrue(cooperate[i++] == testPureStrategy.isCooperative(player, opponent, history));                
        
        history.addResult(new GameResult(player, opponent, true, false, 0, 8)); 
        
        assertTrue(cooperate[i++] == testPureStrategy.isCooperative(player, opponent, history));
        
        history.addResult(new GameResult(inSameGroupAsPlayer, opponent, true, true, 5, 5));
        
        assertTrue(cooperate[i++] == testPureStrategy.isCooperative(player, opponent, history));
        
        history.addResult(new GameResult(player, opponent, true, true, 5, 5)); 
        
        assertTrue(cooperate[i++] == testPureStrategy.isCooperative(player, opponent, history));
	}

	
	/**
	 * Tests the implementation of the method getCooperationProbability for the different strategies
	*/
	public void testGetCooperationProbability(boolean[] cooperate) {
	    int i = 0;
		assertTrue(((cooperate[i++]) ? 1 : 0) == testPureStrategy.getCooperationProbability(player, opponent, history));
        
        history.addResult(new GameResult(player, opponent, true, true, 0, 8)); 
        history.addResult(new GameResult(inSameGroupAsPlayer, opponent, true, false, 0, 8));
        
        assertTrue(((cooperate[i++]) ? 1 : 0) == testPureStrategy.getCooperationProbability(player, opponent, history));
        
        history.addResult(new GameResult(player, opponent, true, false, 0, 8)); 
        
        assertTrue(((cooperate[i++]) ? 1 : 0) == testPureStrategy.getCooperationProbability(player, opponent, history)); 
        
        history.addResult(new GameResult(inSameGroupAsPlayer, opponent, true, true, 8, 0));
        
        assertTrue(((cooperate[i++]) ? 1 : 0) == testPureStrategy.getCooperationProbability(player, opponent, history)); 
        
        history.addResult(new GameResult(player, opponent, true, true, 5, 5));
        
        assertTrue(((cooperate[i++]) ? 1 : 0) == testPureStrategy.getCooperationProbability(player, opponent, history));
	}
}
