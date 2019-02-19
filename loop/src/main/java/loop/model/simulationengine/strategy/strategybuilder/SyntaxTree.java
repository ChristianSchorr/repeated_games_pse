package loop.model.simulationengine.strategy.strategybuilder;

import loop.model.simulationengine.strategies.Strategy;

import java.util.List;

/**
 * This class is an implementation of the @link Tree<SyntaxNode> interface using SyntaxNode s to create
 * logical combinations of strategies. Additionally it provides functionality to validate the
 * syntactical correctness of the syntax tree and to get the smallest faulty sub tree in case
 * of a syntactical error.
 */
public class SyntaxTree implements Tree<SyntaxNode> {

    private SyntaxNode root;

    /**
     * Creates a new SyntaxTree with the given node as root.
     *
     * @param root the node that shall be the root of the tree
     */
    public SyntaxTree(SyntaxNode root) {
        this.root = root;
    }

    /**
     * Checks whether the expression represented by this tree is syntactically correct.
     *
     * @return {@code true} if the syntax is correct, {@code false} otherwise
     */
    public boolean checkSyntax() {
        return check(root);
    }

    /**
     * This method returns the root of the smallest sub tree with incorrect syntax
     *
     * @return the root of the smallest faulty sub tree
     */
    public SyntaxNode getFaultyNode() {
        if (checkSyntax()) return null;
        else {
            SyntaxNode node = root;
            while (!check(node)) {
                List<TreeNode<Strategy>> children = node.getChildren();
                if (children.size() != node.getOperator().getOperandCount()) return node;
                for (TreeNode<Strategy> child: children) {
                    if (!check((SyntaxNode) child)) {
                        node = (SyntaxNode) child;
                        break;
                    }                       
                }
            }
            return node;
        }
    }

    private boolean check(SyntaxNode node) {
        if (!node.isInnerNode() && node.getContent() != null) return true;
        else {
            List<TreeNode<Strategy>> children = node.getChildren();
            if (children.size() != node.getOperator().getOperandCount()) return false;
            for (TreeNode<Strategy> child: children) {
                if (check((SyntaxNode)child) == false) {
                    return false;
                };
            }
        }
        return true;
    }

    @Override
    public SyntaxNode getRoot() {
        return root;
    }
}
