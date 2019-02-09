package loop.model.simulationengine.strategies.strategybuilder;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import loop.model.simulationengine.Agent;
import loop.model.simulationengine.GameResult;
import loop.model.simulationengine.SimulationHistoryTable;
import loop.model.simulationengine.strategies.PureStrategy;
import loop.model.simulationengine.strategy.strategybuilder.ConcreteOperator;
import loop.model.simulationengine.strategy.strategybuilder.StrategyBuilder;
import loop.model.simulationengine.strategy.strategybuilder.SyntaxNode;

public class StrategyBuilderTest {
    private SyntaxNode root;
    private SyntaxNode node1;
    private SyntaxNode node2;
    private PureStrategy newStrategy;
    private Agent player;
    private Agent opponent;
    private SimulationHistoryTable history;
    private StrategyBuilder strategyBuilder = new StrategyBuilder();

    @Before
    public void setUp() throws Exception {
        //build a new valid strategy
        root = new SyntaxNode(null, ConcreteOperator.AND());
        node1 = new SyntaxNode(null, ConcreteOperator.OR());
        node2 = new SyntaxNode(null, ConcreteOperator.NOT());
        
        node1.insertNode(new SyntaxNode(PureStrategy.grim(), null));
        node1.insertNode(new SyntaxNode(PureStrategy.titForTat(), null));
        node2.insertNode(new SyntaxNode(PureStrategy.neverCooperate(), null));
        
        root.insertNode(node1); 
        root.insertNode(node2);
        
        //setup a history
        history = new SimulationHistoryTable();
        opponent = new Agent(0, PureStrategy.titForTat(), 2);
        history.addResult(new GameResult(player, opponent, true, true, 3, 3));
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testCreateNewStrategy() {
        newStrategy = (PureStrategy) StrategyBuilder.creatNewStrategy(root, "", ""); 
        assertTrue(newStrategy.isCooperative(player, opponent, history)); 
        history.addResult(new GameResult(player, opponent, true, false, 4, 0));
        assertFalse(newStrategy.isCooperative(player, opponent, history));
    }

}
