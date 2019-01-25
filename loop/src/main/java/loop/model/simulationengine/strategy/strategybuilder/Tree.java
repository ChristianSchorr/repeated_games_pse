package loop.model.simulationengine.strategy.strategybuilder;

/**
 * This interface represents a graph with tree structure which consists of TreeNode<T> in-
 * stances.
 *
 * @param <T> the type of the tree’s node’s contents (resp. the generic type of {@link TreeNode<T>} )
 * @author Christian Schorr
 */
public interface Tree<T> {

    /**
     * This method returns the root of the tree
     *
     * @return the root of the tree
     */
    public T getRoot();
}
