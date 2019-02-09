package loop.model.simulationengine.strategies.strategybuilder;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import loop.model.simulationengine.strategies.PureStrategy;
import loop.model.simulationengine.strategies.Strategy;
import loop.model.simulationengine.strategy.strategybuilder.ConcreteOperator;
import loop.model.simulationengine.strategy.strategybuilder.Operator;
import loop.model.simulationengine.strategy.strategybuilder.SyntaxNode;
import loop.model.simulationengine.strategy.strategybuilder.TreeNode;

public class SyntaxNodeTest {
    private Operator operator = ConcreteOperator.AND();
    private List<TreeNode<Strategy>> children = new ArrayList<>();
    private SyntaxNode syntaxNode;

    @Before
    public void setUp() throws Exception {
        syntaxNode = new SyntaxNode(null, operator);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testConstructor() {
        assertEquals(operator, syntaxNode.getOperator());
        assertEquals(null, syntaxNode.getContent());
        assertTrue(syntaxNode.isInnerNode());
    }
    
    /**
     * Tests the insertNode and the removeNode methods
     */
    @Test
    public void testInsertAndRemove() {
        SyntaxNode node1 = new SyntaxNode(PureStrategy.grim(), null);
        SyntaxNode node2 = new SyntaxNode(PureStrategy.alwaysCooperate(), null);
        syntaxNode.insertNode(node1);
        syntaxNode.insertNode(node2);
        children.add(node1);
        children.add(node2);
        
        assertEquals(children, syntaxNode.getChildren());
        
        syntaxNode.removeNode(node2);
        children.remove(1);
        
        assertEquals(children, syntaxNode.getChildren()); 
        assertFalse(node1.isInnerNode());
    }

}
