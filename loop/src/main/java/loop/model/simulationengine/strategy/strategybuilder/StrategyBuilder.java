package loop.model.simulationengine.strategy.strategybuilder;

import loop.model.simulationengine.AgentPair;
import loop.model.simulationengine.SimulationHistory;
import loop.model.simulationengine.strategies.PureStrategy;
import loop.model.simulationengine.strategies.Strategy;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * This class provides functionality to create a new strategy from a given root {@link SyntaxNode}
 * of a syntax tree by combining the leaf nodes strategies with the inner nodes operators.
 *
 * @author Christian Schorr
 */
public class StrategyBuilder {

    /**
     * This method creates a new strategy from a given syntax tree
     *
     * @param root        the root of the syntax tree the strategy shall be built from
     * @param name        the name of the newly created strategy
     * @param description the description of the newly created strategy
     * @return the newly created strategy or null if there is a syntactical error in the given
     * syntax tree
     */
    public static Strategy creatNewStrategy(SyntaxNode root, String name, String description) {
        Strategy rootStrat = buildStrategy(root);
        BiPredicate<AgentPair, SimulationHistory> condition = (pair, history) ->
            rootStrat.isCooperative(pair.getFirstAgent(), pair.getSecondAgent(), history);
        return new PureStrategy(name, description, condition);
    }

    private static Strategy buildStrategy(SyntaxNode root) {
        if (!root.isInnerNode()) root.getContent();
        else {
            List<TreeNode<Strategy>> children = root.getChildren();
            Operator op = root.getOperator();
            List<Strategy> operands = children.stream().map(node -> buildStrategy((SyntaxNode)node)).collect(Collectors.toList());
            return op.combineStrategies(operands);
        }
        return null;
    }
}
