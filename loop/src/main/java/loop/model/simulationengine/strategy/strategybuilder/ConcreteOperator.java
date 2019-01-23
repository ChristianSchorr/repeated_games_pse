package loop.model.simulationengine.strategy.strategybuilder;

import loop.model.simulationengine.strategies.PureStrategy;
import loop.model.simulationengine.strategies.Strategy;

import java.util.List;
import java.util.function.Function;

public class ConcreteOperator implements Operator {

    private Function<List<Strategy>, Strategy> operator;
    private int operandCount;

    public ConcreteOperator(Function<List<Strategy>, Strategy> operator, int operandCount) {
        this.operator = operator;
        this.operandCount = operandCount;
    }

    @Override
    public Strategy combineStrategies(List<Strategy> strats) {
        if (strats.size() < operandCount) return null;
        else return operator.apply(strats);
    }

    @Override
    public int getOperandCount() {
        return operandCount;
    }

    public static Operator AND() {
        Function<List<Strategy>, Strategy> op = (strats) -> new PureStrategy("","",
                (pair, hist) -> strats.get(0).isCooperative(pair.getFirstAgent(), pair.getSecondAgent(), hist) &&
                                strats.get(1).isCooperative(pair.getFirstAgent(), pair.getSecondAgent(), hist));
        return new ConcreteOperator(op,2);
    }

    public static Operator OR() {
        Function<List<Strategy>, Strategy> op = (strats) -> new PureStrategy("","",
                (pair, hist) -> strats.get(0).isCooperative(pair.getFirstAgent(), pair.getSecondAgent(), hist) ||
                        strats.get(1).isCooperative(pair.getFirstAgent(), pair.getSecondAgent(), hist));
        return new ConcreteOperator(op,2);
    }

    public static Operator NAND() {
        Function<List<Strategy>, Strategy> op = (strats) -> new PureStrategy("","",
                (pair, hist) -> !(strats.get(0).isCooperative(pair.getFirstAgent(), pair.getSecondAgent(), hist) &&
                        strats.get(1).isCooperative(pair.getFirstAgent(), pair.getSecondAgent(), hist)));
        return new ConcreteOperator(op,2);
    }

    public static Operator NOR() {
        Function<List<Strategy>, Strategy> op = (strats) -> new PureStrategy("","",
                (pair, hist) -> !(strats.get(0).isCooperative(pair.getFirstAgent(), pair.getSecondAgent(), hist) ||
                        strats.get(1).isCooperative(pair.getFirstAgent(), pair.getSecondAgent(), hist)));
        return new ConcreteOperator(op,2);
    }

    public static Operator XOR() {
        Function<List<Strategy>, Strategy> op = (strats) -> new PureStrategy("","",
                (pair, hist) -> strats.get(0).isCooperative(pair.getFirstAgent(), pair.getSecondAgent(), hist) ^
                        strats.get(1).isCooperative(pair.getFirstAgent(), pair.getSecondAgent(), hist));
        return new ConcreteOperator(op,2);
    }

    public static Operator NOT() {
        Function<List<Strategy>, Strategy> op = (strats) -> new PureStrategy("","",
                (pair, hist) -> !strats.get(0).isCooperative(pair.getFirstAgent(), pair.getSecondAgent(), hist));
        return new ConcreteOperator(op,1);
    }

    public static Operator IMPLIES() {
        Function<List<Strategy>, Strategy> op = (strats) -> new PureStrategy("","",
                (pair, hist) -> (!strats.get(0).isCooperative(pair.getFirstAgent(), pair.getSecondAgent(), hist)) ||
                                  strats.get(1).isCooperative(pair.getFirstAgent(), pair.getSecondAgent(), hist));
        return new ConcreteOperator(op,2);
    }

}
