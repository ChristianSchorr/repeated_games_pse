package loop.model.simulationengine.strategies.strategybuilder;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import loop.model.simulationengine.Agent;
import loop.model.simulationengine.GameResult;
import loop.model.simulationengine.SimulationHistory;
import loop.model.simulationengine.SimulationHistoryTable;
import loop.model.simulationengine.strategies.PureStrategy;
import loop.model.simulationengine.strategies.Strategy;
import loop.model.simulationengine.strategy.strategybuilder.ConcreteOperator;
import loop.model.simulationengine.strategy.strategybuilder.Operator;

public class ConcreteOperatorTest {

    private Operator concreteOperator;
    private PureStrategy strategy1 = PureStrategy.titForTat();
    private PureStrategy strategy2 = PureStrategy.neverCooperate();
    private ArrayList<Strategy> strategies1 = new ArrayList<Strategy>();
    private PureStrategy strategy3 = PureStrategy.grim();
    private PureStrategy strategy4 = PureStrategy.alwaysCooperate();
    private ArrayList<Strategy> strategies2 = new ArrayList<Strategy>(); 
    private Agent player;
    private Agent opponent;
    private SimulationHistory history;
    private Strategy newStrategy1;
    private Strategy newStrategy2;

    @Before
    public void setUp() throws Exception {
        strategies1.add(strategy1);
        strategies1.add(strategy2);
        strategies2.add(strategy3);
        strategies2.add(strategy4);
        history = new SimulationHistoryTable();
        opponent = new Agent(0, PureStrategy.titForTat(), 2);
        history.addResult(new GameResult(player, opponent, true, true, 3, 3));
    }

    @After
    public void tearDown() throws Exception {
    }
    
    /**
     * Tests the AND operator
     */
    @Test
    public void testAND() {
        //Test the combineStrategies method with an invalid strategy List
        strategies1.remove(1);
        concreteOperator = ConcreteOperator.AND();
        assertNull(concreteOperator.combineStrategies(strategies1));
        
        strategies1.add(PureStrategy.neverCooperate());
        testOperator("AND", 2);
        assertFalse(newStrategy1.isCooperative(player, opponent, history));
        assertTrue(newStrategy2.isCooperative(player, opponent, history));
    }
    
    /**
     * Tests the OR operator
     */
    @Test
    public void testOR() {
        concreteOperator = ConcreteOperator.OR();
        testOperator("OR", 2);
        assertTrue(newStrategy1.isCooperative(player, opponent, history));
        assertTrue(newStrategy2.isCooperative(player, opponent, history));
    }
    
    /**
     * Tests the NOR operator
     */
    @Test
    public void testNOR() {
        concreteOperator = ConcreteOperator.NOR();
        testOperator("NOR", 2);
        assertFalse(newStrategy1.isCooperative(player, opponent, history)); 
        assertFalse(newStrategy2.isCooperative(player, opponent, history));
    }
    
    /**
     * Tests the NAND operator
     */
    @Test
    public void testNAND() {
        concreteOperator = ConcreteOperator.NAND();
        testOperator("NAND", 2);
        assertTrue(newStrategy1.isCooperative(player, opponent, history));
        assertFalse(newStrategy2.isCooperative(player, opponent, history));
    }
    
    /**
     * Tests the XOR operator
     */
    @Test
    public void testXOR() {
        concreteOperator = ConcreteOperator.XOR();
        testOperator("XOR", 2);
        assertTrue(newStrategy1.isCooperative(player, opponent, history));
        assertFalse(newStrategy2.isCooperative(player, opponent, history));
    }
    
    /**
     * Tests the NOT operator
     */
    @Test
    public void testNOT() {
        concreteOperator = ConcreteOperator.NOT();
        testOperator("NOT", 1);
        assertFalse(newStrategy1.isCooperative(player, opponent, history)); 
        assertFalse(newStrategy2.isCooperative(player, opponent, history));
    }
    
    /**
     * Tests the IMPLIES operator
     */
    @Test
    public void testIMPLIES() {
        concreteOperator = ConcreteOperator.IMPLIES();
        testOperator("IMPLIES", 2);
        assertFalse(newStrategy1.isCooperative(player, opponent, history)); 
        assertTrue(newStrategy2.isCooperative(player, opponent, history));
    }
    
    public void testOperator(String operatorName, int operandCound) {
        assertEquals(operandCound, concreteOperator.getOperandCount());
        assertEquals(operatorName, concreteOperator.getName());
        assertEquals("", concreteOperator.getDescription());
        newStrategy1 = concreteOperator.combineStrategies(strategies1);
        newStrategy2 = concreteOperator.combineStrategies(strategies2);
    }
    
}
