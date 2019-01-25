package loop.model.simulationengine.strategy.strategybuilder;

import java.util.List;

/**
 * This interface represents a node in a tree-like structure. It stores a generic content and
 * provides functionality to insert and remove children nodes.
 *
 * @param <T> the type of the content that shall be stored in each node
 * @author Christian Schorr
 */
public interface TreeNode<T> {

    /**
     * Returns the content stored in this node
     *
     * @return the node’s content
     */
    public T getContent();

    /**
     * Returns all children of this node
     *
     * @return a list of all children
     */
    public List<TreeNode<T>> getChildren();

    /**
     * Adds the given node as a child of this node
     *
     * @param node the node to add as a child
     */
    public void insertNode(TreeNode<T> node);

    /**
     * Removes the given node from this node’s children
     *
     * @param node the node that shall be removed
     * @return {@code true} if the node was successfully removed, {@code false} otherwise
     */
    public boolean removeNode(TreeNode<T> node);
}
