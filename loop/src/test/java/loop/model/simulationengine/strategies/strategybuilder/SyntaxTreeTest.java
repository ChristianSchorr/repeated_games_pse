package loop.model.simulationengine.strategies.strategybuilder;

import static org.junit.Assert.*;

import javax.swing.tree.TreeNode;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import loop.model.simulationengine.strategies.PureStrategy;
import loop.model.simulationengine.strategy.strategybuilder.ConcreteOperator;
import loop.model.simulationengine.strategy.strategybuilder.SyntaxNode;
import loop.model.simulationengine.strategy.strategybuilder.SyntaxTree;

public class SyntaxTreeTest {
    private SyntaxNode root;
    private SyntaxTree syntaxTree;
    private SyntaxNode node1;
    private SyntaxNode node2;
    
    @Before
    public void setUp() throws Exception {
        root = new SyntaxNode(null, ConcreteOperator.AND());
        syntaxTree = new SyntaxTree(root);       
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testConstructor() {
        assertEquals(root, syntaxTree.getRoot());
        assertFalse(syntaxTree.checkSyntax());
    }
    
    /**
     * Tests a valid SyntaxTree
     */
    @Test
    public void testValidSyntaxTree() {
        node1 = new SyntaxNode(PureStrategy.grim(), null);
        node2 = new SyntaxNode(PureStrategy.alwaysCooperate(), null);
        root.insertNode(node1);
        root.insertNode(node2);
        assertTrue(syntaxTree.checkSyntax());
        assertNull(syntaxTree.getFaultyNode());
    }
    
    /**
     * Tests a non valid SyntaxTree
     */
    @Test
    public void testNonValidSyntaxTree() {
        node1 = new SyntaxNode(null, ConcreteOperator.OR());
        node2 = new SyntaxNode(null, ConcreteOperator.NOT());
        
        root.insertNode(node1); 
        
        assertFalse(syntaxTree.checkSyntax());
        assertEquals(root, syntaxTree.getFaultyNode());
        
        root.insertNode(node2);
        
        assertFalse(syntaxTree.checkSyntax());
        assertEquals(node1, syntaxTree.getFaultyNode());
        
        node1.insertNode(new SyntaxNode(PureStrategy.alwaysCooperate(), null));
        node1.insertNode(new SyntaxNode(PureStrategy.grim(), null));
        assertEquals(node2, syntaxTree.getFaultyNode());
        
        node2.insertNode(new SyntaxNode(PureStrategy.groupGrim(), null));
        assertTrue(syntaxTree.checkSyntax());
    }

}
