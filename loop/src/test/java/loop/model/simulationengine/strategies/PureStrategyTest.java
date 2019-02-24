package loop.model.simulationengine.strategies;

import static org.junit.Assert.*;


import org.junit.Before;
import org.junit.Ignore;
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
		opponent = new Agent(5, PureStrategy.neverCooperate(), 2);
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
	    testGetDescription("A player using tit-for-tat will cooperate in the first game. In every following game, he will"
                + " cooperate if and only if the opponent cooperated in the previous game.");
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
        testGetDescription("A player using grim will cooperate in the first game. In the following games, he cooperates if and only if the opponent"
                + " cooperated in all previous games.");
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
        testGetDescription("A player using group tit-for-tat uses the tit-for-tat strategy, where instead of looking "
                + "at the last game between the player and the opponent the last game between the opponent and any agent "
                + "of the same (cohesive) group as the player is considered. If the player is part of a non-cohesive group, "
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
        testGetDescription("A player using group grim uses the grim strategy, where instead of looking at the last game between"
                + " the player and the opponent the last game between the opponent and any agent of the same (cohesive) group"
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
	    testGetDescription("A player using this strategy will cooperate in every game.");
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
        testPureStrategy = PureStrategy.opponentAlwaysCooperated();
        player = new Agent(0, testPureStrategy, 1);
        assertTrue(testPureStrategy.getName().equals("The opponent always cooperated"));
        assertTrue(testPureStrategy.getDescription().equals("A player using this strategy will first cooperate, afterwards he refer to the previous actions of the opponent."
                + " If the opponent previously was always cooperative, the agent is cooperative. If the opponent was at least one time "
                + "not cooperative, the agent is from now on not cooperative to that opponent."));
        boolean[] cooperate = {true, true, false, false, false};
        testIsCooperative(cooperate);
        history.reset();
        testGetCooperationProbability(cooperate);
    } 
    
    /**
     * Tests the opponentAlwaysCooperated strategy
     */
    @Test
    public void testOpponentAlwaysCooperated() {
        testPureStrategy = PureStrategy.opponentAlwaysCooperated();
        player = new Agent(0, testPureStrategy, 1);
        assertTrue(testPureStrategy.getName().equals("The opponent always cooperated"));
        assertTrue(testPureStrategy.getDescription().equals("A player using this strategy will first cooperate, afterwards he refer to the previous actions of the opponent."
                + " If the opponent previously was always cooperative, the agent is cooperative. If the opponent was at least one time "
                + "not cooperative, the agent is from now on not cooperative to that opponent."));
        boolean[] cooperate = {true, true, false, false, false};
        testIsCooperative(cooperate);
        history.reset();
        testGetCooperationProbability(cooperate);
    }
    
    /**
     * Tests the opponentCooperatedAtLeastOnce strategy
     */
    @Test
    public void testOpponentCooperatedAtLeastOnce() {
        testPureStrategy = PureStrategy.opponentCooperatedAtLeastOnce();
        player = new Agent(0, testPureStrategy, 1);
        assertTrue(testPureStrategy.getName().equals("The opponent cooperated at least once"));
        assertTrue(testPureStrategy.getDescription().equals("A player using this strategy will not cooperate first, afterwards he refer to the previous actions of the opponent."
                + " If the opponent previously has one time cooperated with the player, the player is cooperative. If the opponent was never"
                + " cooperative, the player isn't cooperative to that opponent."));
        boolean[] cooperate = {false, true, true, true, true};
        testIsCooperative(cooperate);
        history.reset();
        testGetCooperationProbability(cooperate);
    }
    
    /**
     * Test the opponentCooperatedLastTime strategy
     */
    @Test
    public void testOpponentCooperatedLastTime() {
        testPureStrategy = PureStrategy.opponentCooperatedLastTime();
        player = new Agent(0, testPureStrategy, 1);
        assertTrue(testPureStrategy.getName().equals("The opponent cooperated last time"));
        assertTrue(testPureStrategy.getDescription().equals("A player using this strategy will first cooperate, afterwards he replicate the opponent's previous action."
                + " If the opponent previously was cooperative, the player is cooperative. If the opponent previously wasn't cooperative,"
                + " the player is not cooperative."));
        boolean[] cooperate = {true, true, false, false, true};
        testIsCooperative(cooperate);
        history.reset();
        testGetCooperationProbability(cooperate);
    }
    
    /**
     * Test the opponentCooperatedNever strategy
     */
    @Test
    public void testOpponentCooperatedNever() {
        testPureStrategy = PureStrategy.opponentCooperatedNever();
        player = new Agent(0, testPureStrategy, 1);
        assertTrue(testPureStrategy.getName().equals("The opponent never cooperated"));
        assertTrue(testPureStrategy.getDescription().equals("A player using this strategy will first cooperate, afterwards he refer to the previous actions of the opponent."
                + " If the opponent previously was never cooperative, the player is cooperative. If the opponent was at least one time "
                + " cooperative, the player is from now on not cooperative to this opponent."));
        boolean[] cooperate = {true, false, false, false, false};
        testIsCooperative(cooperate);
        history.reset();
        testGetCooperationProbability(cooperate);
    }
    
    /**
     * Test the opponentHasHigherCapital strategy
     */
    @Test
    public void testOpponentHasHigherCapital() {
        testPureStrategy = PureStrategy.opponentHasHigherCapital();
        player = new Agent(0, testPureStrategy, 1);
        assertTrue(testPureStrategy.getName().equals("The opponent has a higher capital"));
        assertTrue(testPureStrategy.getDescription().equals("A player using this strategy will be cooperative if the opponent "
                + "has a higher capital than the player else he won't be cooperative."));
        boolean[] cooperate = {true, true, false, true, true};
        testIsCooperative(cooperate);
        history.reset();
        
        //Set the initial capital
        player.addCapital(-8);
        opponent.addCapital(-5);
        
        testGetCooperationProbability(cooperate);
    }
    
    /**
     * Test the opponentHasLowerCapital strategy
     */
    @Test
    public void testOpponentHasLowerCapital() {
        testPureStrategy = PureStrategy.opponentHasLowerCapital();
        player = new Agent(0, testPureStrategy, 1);
        assertTrue(testPureStrategy.getName().equals("The opponent has a lower capital"));
        assertTrue(testPureStrategy.getDescription().equals("A player using this strategy will be cooperative if the opponent "
                + "has a lower capital than the player else he won't be cooperative."));
        boolean[] cooperate = {false, false, true, false, false};
        testIsCooperative(cooperate);
        history.reset();
        
        //Set the initial capital
        player.addCapital(-8);
        opponent.addCapital(-5);
        
        testGetCooperationProbability(cooperate);
    }
    
    /**
     * Test the opponentHasSimilarCapital strategy
     */
    @Test
    public void testOpponentHasSimilarCapital() {
        testPureStrategy = PureStrategy.opponentHasSimilarCapital(0.3);
        player = new Agent(0, testPureStrategy, 1);
        assertTrue(testPureStrategy.getName().equals("The opponent has a similar capital"));
        assertTrue(testPureStrategy.getDescription().equals("A player using this strategy will be cooperative if the opponent "
                + "has a similar capital than the player else he won't be cooperative."));
        boolean[] cooperate = {false, false, false, true, true};
        testIsCooperative(cooperate);
        history.reset();
        
        //Set the initial capital
        player.addCapital(-8);
        opponent.addCapital(-5);
        
        testGetCooperationProbability(cooperate);
    }
    
    /**
     * Test the opponentIsInTheSameGroup strategy
     */
    @Test
    public void testOpponentIsInTheSameGroup() {
        testPureStrategy = PureStrategy.opponentIsInTheSameGroup();
        player = new Agent(0, testPureStrategy, 1);
        assertTrue(testPureStrategy.getName().equals("The opponent is in the same group"));
        assertTrue(testPureStrategy.getDescription().equals("A player using this strategy will be cooperative if the opponent "
                + "is in the same group else the player is not cooperative."));
        assertTrue(testPureStrategy.isCooperative(player, inSameGroupAsPlayer, history));
        assertFalse(testPureStrategy.isCooperative(player, opponent, history));
        assertTrue(testPureStrategy.getCooperationProbability(player, inSameGroupAsPlayer, history) == 1);
        assertTrue(testPureStrategy.getCooperationProbability(player, opponent, history) == 0);
    }
    
    /**
     * Test the currAgentAlwaysCooperated strategy
     */
    @Test
    public void testCurrAgentAlwaysCooperated() {
        testPureStrategy = PureStrategy.currAgentAlwaysCooperated();
        player = new Agent(0, testPureStrategy, 1);
        assertTrue(testPureStrategy.getName().equals("I always cooperated"));
        assertTrue(testPureStrategy.getDescription().equals("A player using this strategy will first cooperate, afterwards he refer to his previous actions."
                + " If the player previously was always cooperative to this opponent, the player is cooperative. If the player was at least one time "
                + "not cooperative to this opponent, the player is from now on not cooperative to this opponent."));
        boolean[] cooperate = {true, true, false, false, false};
        testIsCooperative(cooperate);
        history.reset();
        testGetCooperationProbability(cooperate);
    }
    
    /**
     * Test the currAgentCooperatedAtLeastOnce strategy
     */
    @Test
    public void testCurrAgentCooperatedAtLeastOnce() {
        testPureStrategy = PureStrategy.currAgentCooperatedAtLeastOnce();
        player = new Agent(0, testPureStrategy, 1);
        assertTrue(testPureStrategy.getName().equals("I cooperated at least once"));
        assertTrue(testPureStrategy.getDescription().equals("A player using this strategy will not cooperate first, afterwards he refer to his previous actions."
                + " If the player previously was at least one time cooperative to this opponent, the player is cooperative. If the player was never "
                + " cooperative to this opponent, the player is not cooperative to this opponent."));
        boolean[] cooperate = {false, true, true, true, true};
        testIsCooperative(cooperate);
        history.reset();
        testGetCooperationProbability(cooperate);
    }
    
    /**
     * Test the currAgentCooperatedLastTime strategy
     */
    @Test
    public void testCurrAgentCooperatedLastTime() {
        testPureStrategy = PureStrategy.currAgentCooperatedLastTime();
        player = new Agent(0, testPureStrategy, 1);
        assertTrue(testPureStrategy.getName().equals("I cooperated last time"));
        assertTrue(testPureStrategy.getDescription().equals("A player using this strategy will first cooperate, afterwards he refer to his previous action."
                + " If the player previously was the last time cooperative to this opponent, the player is cooperative. If the player was the last time "
                + "  not cooperative to this opponent, the player is not cooperative to this opponent."));
        boolean[] cooperate = {true, true, false, false, true};
        testIsCooperative(cooperate);
        history.reset();
        testGetCooperationProbability(cooperate);
    }
    
    /**
     * Test the currAgentCooperatedNever strategy
     */
    @Test
    public void testCurrAgentCooperatedNever() {
        testPureStrategy = PureStrategy.currAgentCooperatedNever();
        player = new Agent(0, testPureStrategy, 1);
        assertTrue(testPureStrategy.getName().equals("I never cooperated"));
        assertTrue(testPureStrategy.getDescription().equals("A player using this strategy will first cooperate, afterwards he refer to his previous actions."
                + " If the player previously was never cooperative to this opponent, the player is cooperative. If the player was at least one time "
                + " cooperative to this opponent, the player is not cooperative to this opponent."));
        boolean[] cooperate = {true, false, false, false, false};
        testIsCooperative(cooperate);
        history.reset();
        testGetCooperationProbability(cooperate);
    }
    
    /**
     * Tests the stratBuilderStrategy method to generate a new strategy
     */
    @Test
    public void testStratBuilderStrategy() {
        when = TimeAdverb.ATLEASTONCE;
        cooperatedWithWhom = AgentEntity.SAME_GROUP;        
        testPureStrategy = PureStrategy.stratBuilderStrategy(cooperatedWithWhom, when, 0.5); 
        player = new Agent(0, testPureStrategy, 1);
        assertTrue(testPureStrategy.getName().equals("stratbuilder strategy"));
        assertTrue(testPureStrategy.getDescription().equals("opponent cooperated with " + cooperatedWithWhom.toString() + ", " + when.toString()));
        boolean[] cooperate = {false, true, true, true, true};
        testIsCooperative(cooperate);
        history.reset();
        testGetCooperationProbability(cooperate);
        history.reset();
        
        when = TimeAdverb.LASTTIME;
        cooperatedWithWhom = AgentEntity.AGENT;        
        testPureStrategy = PureStrategy.stratBuilderStrategy(cooperatedWithWhom, when, 0.5); 
        player = new Agent(0, testPureStrategy, 1);
        assertTrue(testPureStrategy.getName().equals("stratbuilder strategy"));
        assertTrue(testPureStrategy.getDescription().equals("opponent cooperated with " + cooperatedWithWhom.toString() + ", " + when.toString()));
        boolean[] cooperate2 = {true, true, false, false, true};
        testIsCooperative(cooperate2);
        history.reset();
        testGetCooperationProbability(cooperate2);
        history.reset();
        
        //Set the initial capital       
        player.addCapital(-16);
        opponent.addCapital(-10);
        
        when = TimeAdverb.NEVER;
        cooperatedWithWhom = AgentEntity.SIM_CAPITAL;
        player = new Agent(0, testPureStrategy, 1);
        testPureStrategy = PureStrategy.stratBuilderStrategy(cooperatedWithWhom, when, 0.5); 
        assertTrue(testPureStrategy.getDescription().equals("opponent cooperated with " + cooperatedWithWhom.toString() + ", " + when.toString()));
        boolean[] cooperate3 = {true, true, false, false, false};
        testIsCooperative(cooperate3);        
    }
    
    /**
     * Tests the groupMemberCooperation to generate a new strategy
     */
    @Test
    public void testGroupMemberCooperation() {
        when = TimeAdverb.ALWAYS;
        testPureStrategy = PureStrategy.groupMemberCooperation(when);
        player = new Agent(0, testPureStrategy, 1);
        assertTrue(testPureStrategy.getName().equals("An agent of the same group has cooperated always"));
        assertTrue(testPureStrategy.getDescription().equals(""));
        
        history.addResult(new GameResult(inSameGroupAsPlayer, opponent, true, true, 3, 3));
        assertTrue(testPureStrategy.isCooperative(player, opponent, history));
        history.addResult(new GameResult(inSameGroupAsPlayer, opponent, false, true, 0, 3));
        assertFalse(testPureStrategy.isCooperative(player, opponent, history));
        history.addResult(new GameResult(inSameGroupAsPlayer, opponent, true, true, 3, 3));
        assertFalse(testPureStrategy.isCooperative(player, opponent, history));
        
        history.reset();
        
        when = TimeAdverb.NEVER;
        testPureStrategy = PureStrategy.groupMemberCooperation(when);
        player = new Agent(0, testPureStrategy, 1);
        assertTrue(testPureStrategy.getName().equals("An agent of the same group has cooperated never"));
        assertTrue(testPureStrategy.getDescription().equals(""));
        
        history.addResult(new GameResult(inSameGroupAsPlayer, opponent, false, true, 3, 3));
        assertTrue(testPureStrategy.isCooperative(player, opponent, history));
        history.addResult(new GameResult(inSameGroupAsPlayer, opponent, true, true, 0, 3));
        assertFalse(testPureStrategy.isCooperative(player, opponent, history));
        history.addResult(new GameResult(inSameGroupAsPlayer, opponent, false, true, 3, 3));
        assertFalse(testPureStrategy.isCooperative(player, opponent, history));
        
        history.reset();
        
        when = TimeAdverb.LASTTIME;     
        testPureStrategy = PureStrategy.groupMemberCooperation(when);
        player = new Agent(0, testPureStrategy, 1);
        assertTrue(testPureStrategy.getName().equals("An agent of the same group has cooperated last time"));
        assertTrue(testPureStrategy.getDescription().equals(""));
        
        history.addResult(new GameResult(inSameGroupAsPlayer, opponent, true, true, 3, 3));
        assertTrue(testPureStrategy.isCooperative(player, opponent, history));
        history.addResult(new GameResult(inSameGroupAsPlayer, opponent, false, true, 0, 3));
        assertFalse(testPureStrategy.isCooperative(player, opponent, history));
        
        history.reset();
        
        when = TimeAdverb.ATLEASTONCE;     
        testPureStrategy = PureStrategy.groupMemberCooperation(when);
        player = new Agent(0, testPureStrategy, 1);
        assertTrue(testPureStrategy.getName().equals("An agent of the same group has cooperated at least once"));
        assertTrue(testPureStrategy.getDescription().equals(""));
        
        history.addResult(new GameResult(inSameGroupAsPlayer, opponent, false, true, 3, 3));
        assertFalse(testPureStrategy.isCooperative(player, opponent, history));
        history.addResult(new GameResult(inSameGroupAsPlayer, opponent, true, true, 0, 3));
        assertTrue(testPureStrategy.isCooperative(player, opponent, history));  
        history.addResult(new GameResult(inSameGroupAsPlayer, opponent, false, true, 3, 3));
        assertTrue(testPureStrategy.isCooperative(player, opponent, history));
    }
    
    /**
     * Tests the hasSimilarCapital method for different capital values
     */
    @Test
    public void testHasSimilarCapital() {
        similarCapital(10, 20, 0.5, true);
        similarCapital(20, 10, 0.5, true);
        similarCapital(10, 21, 0.5, false);
        similarCapital(10, 10, 0, true);
        similarCapital(0, 100, 1, true);
        similarCapital(999, 1000, 0, false);
    }
    
    private void similarCapital(int agent1Capital, int agent2capital, double percentage, boolean similar) {
        Agent agent1 = new Agent(agent1Capital, PureStrategy.titForTat(), 1);
        Agent agent2 = new Agent(agent2capital, PureStrategy.titForTat(), 1);
        assertTrue(similar == PureStrategy.hasSimilarCapital(agent1, agent2, percentage));
    }

    private void testGetName(String name) {
        assertTrue(testPureStrategy.getName().equals(name));
    }

    private void testGetDescription(String description) {
        assertTrue(testPureStrategy.getDescription().equals(description));
    }
	
	/**
	 * Tests the implementation of the method isCooperative for the different strategies
	*/
    //The capital additions are not complete and serve only for more variation while testing
	private void testIsCooperative(boolean[] cooperate) {
	    int i = 0;
		assertTrue(cooperate[i++] == testPureStrategy.isCooperative(player, opponent, history));
		
		history.addResult(new GameResult(player, opponent, true, true, 5, 5)); 
        history.addResult(new GameResult(inSameGroupAsPlayer, opponent, true, false, 8, 0)); 
              
        assertTrue(cooperate[i++] == testPureStrategy.isCooperative(player, opponent, history));                
        
        history.addResult(new GameResult(player, opponent, false, false, 8, 0)); 
        player.addCapital(8);
        
        assertTrue(cooperate[i++] == testPureStrategy.isCooperative(player, opponent, history));
        
        history.addResult(new GameResult(inSameGroupAsPlayer, opponent, true, true, 5, 5));
        opponent.addCapital(5);
        
        assertTrue(cooperate[i++] == testPureStrategy.isCooperative(player, opponent, history));
        
        history.addResult(new GameResult(player, opponent, true, true, 5, 5)); 
        
        assertTrue(cooperate[i++] == testPureStrategy.isCooperative(player, opponent, history));
	}

	
	/**
	 * Tests the implementation of the method getCooperationProbability for the different strategies
	*/
	private void testGetCooperationProbability(boolean[] cooperate) {
	    int i = 0;
		assertTrue(((cooperate[i++]) ? 1 : 0) == testPureStrategy.getCooperationProbability(player, opponent, history));
        
        history.addResult(new GameResult(player, opponent, true, true, 0, 8)); 
        history.addResult(new GameResult(inSameGroupAsPlayer, opponent, true, false, 0, 8));
        
        assertTrue(((cooperate[i++]) ? 1 : 0) == testPureStrategy.getCooperationProbability(player, opponent, history));
        
        history.addResult(new GameResult(player, opponent, false, false, 0, 8)); 
        player.addCapital(8);
        
        assertTrue(((cooperate[i++]) ? 1 : 0) == testPureStrategy.getCooperationProbability(player, opponent, history)); 
        
        history.addResult(new GameResult(inSameGroupAsPlayer, opponent, true, true, 8, 0));
        opponent.addCapital(5);
        
        assertTrue(((cooperate[i++]) ? 1 : 0) == testPureStrategy.getCooperationProbability(player, opponent, history)); 
        
        history.addResult(new GameResult(player, opponent, true, true, 5, 5));
        
        assertTrue(((cooperate[i++]) ? 1 : 0) == testPureStrategy.getCooperationProbability(player, opponent, history));
	}
}
