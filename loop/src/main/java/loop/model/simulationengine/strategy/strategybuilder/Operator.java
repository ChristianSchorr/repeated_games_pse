package loop.model.simulationengine.strategy.strategybuilder;

import loop.model.simulationengine.strategies.Strategy;

import java.util.List;

/**
 * This interface represents a unary or binary logical operator for combining strategies.
 */
public interface Operator {

    /**
     * This method creates a new strategy by logically connecting the given ones
     *
     * @param strats the strategies that shall be combined;
     * @return the new strategy
     */
    public Strategy combineStrategies(List<Strategy> strats);

    /**
     * Returns whether this operator combines one or two operands
     *
     * @return the number of operands of this operator
     */
    public int getOperandCount();
}
