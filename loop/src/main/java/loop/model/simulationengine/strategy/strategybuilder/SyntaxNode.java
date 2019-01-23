package loop.model.simulationengine.strategy.strategybuilder;

import loop.model.simulationengine.strategies.Strategy;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a tree node in a syntax tree for combined strategies. In case of a
 * leaf node it stores the strategy that shall be combined, in case of an inner node it stores
 * an operator.
 *
 * @author Christian Schorr
 */
public class SyntaxNode implements TreeNode<Strategy> {

    private Strategy strat;
    private Operator operator;

    private List<TreeNode<Strategy>> children;
    /**
     * Creates a new SyntaxNode .
     * @param strat the strategy that shall be stored in this node
     * @param op the operator that shall be stored in this node
     */
    public SyntaxNode(Strategy strat, Operator op) {
        this.strat = strat;
        operator = op;
        children = new ArrayList<>();
    }

    /**
     * This method returns whether this node is an inner node.
     * @return {@code true} if this node is an inner node, otherwise {@code false}
     */
    public boolean isInnerNode() {
        return operator != null;
    }

    public Operator getOperator() {
        return operator;
    }

    @Override
    public Strategy getContent() {
        return strat;
    }

    @Override
    public List<TreeNode<Strategy>> getChildren() {
        return children;
    }

    @Override
    public void insertNode(TreeNode<Strategy> node) {
        children.add(node);
    }

    @Override
    public boolean removeNode(TreeNode<Strategy> node) {
        return children.remove(node);
    }
}
