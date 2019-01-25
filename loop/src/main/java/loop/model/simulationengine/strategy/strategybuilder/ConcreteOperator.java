package loop.model.simulationengine.strategy.strategybuilder;

import loop.model.simulationengine.strategies.PureStrategy;
import loop.model.simulationengine.strategies.Strategy;

import java.util.List;
import java.util.function.Function;

public class ConcreteOperator implements Operator {

    private Function<List<Strategy>, Strategy> operator;
    private int operandCount;

    private String name;
    private String description;

    public ConcreteOperator(Function<List<Strategy>, Strategy> operator, int operandCount, String name, String description) {
        this.operator = operator;
        this.operandCount = operandCount;
        this.name = name;
        this.description = description;
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

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public static Operator AND() {
        Function<List<Strategy>, Strategy> op = (strats) -> new PureStrategy("","",
                (pair, hist) -> strats.get(0).isCooperative(pair.getFirstAgent(), pair.getSecondAgent(), hist) &&
                                strats.get(1).isCooperative(pair.getFirstAgent(), pair.getSecondAgent(), hist));
        return new ConcreteOperator(op,2, "AND", "");
    }

    public static Operator OR() {
        Function<List<Strategy>, Strategy> op = (strats) -> new PureStrategy("","",
                (pair, hist) -> strats.get(0).isCooperative(pair.getFirstAgent(), pair.getSecondAgent(), hist) ||
                        strats.get(1).isCooperative(pair.getFirstAgent(), pair.getSecondAgent(), hist));
        return new ConcreteOperator(op,2, "OR", "");
    }

    public static Operator NAND() {
        Function<List<Strategy>, Strategy> op = (strats) -> new PureStrategy("","",
                (pair, hist) -> !(strats.get(0).isCooperative(pair.getFirstAgent(), pair.getSecondAgent(), hist) &&
                        strats.get(1).isCooperative(pair.getFirstAgent(), pair.getSecondAgent(), hist)));
        return new ConcreteOperator(op,2, "NAND", "");
    }

    public static Operator NOR() {
        Function<List<Strategy>, Strategy> op = (strats) -> new PureStrategy("","",
                (pair, hist) -> !(strats.get(0).isCooperative(pair.getFirstAgent(), pair.getSecondAgent(), hist) ||
                        strats.get(1).isCooperative(pair.getFirstAgent(), pair.getSecondAgent(), hist)));
        return new ConcreteOperator(op,2, "NOR", "");
    }

    public static Operator XOR() {
        Function<List<Strategy>, Strategy> op = (strats) -> new PureStrategy("","",
                (pair, hist) -> strats.get(0).isCooperative(pair.getFirstAgent(), pair.getSecondAgent(), hist) ^
                        strats.get(1).isCooperative(pair.getFirstAgent(), pair.getSecondAgent(), hist));
        return new ConcreteOperator(op,2, "XOR", "");
    }

    public static Operator NOT() {
        Function<List<Strategy>, Strategy> op = (strats) -> new PureStrategy("","",
                (pair, hist) -> !strats.get(0).isCooperative(pair.getFirstAgent(), pair.getSecondAgent(), hist));
        return new ConcreteOperator(op,1, "NOT", "");
    }

    public static Operator IMPLIES() {
        Function<List<Strategy>, Strategy> op = (strats) -> new PureStrategy("","",
                (pair, hist) -> (!strats.get(0).isCooperative(pair.getFirstAgent(), pair.getSecondAgent(), hist)) ||
                                  strats.get(1).isCooperative(pair.getFirstAgent(), pair.getSecondAgent(), hist));
        return new ConcreteOperator(op,2, "IMPLIES", "");
    }
}
