package loop.model.simulationengine.strategies;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.stream.Stream;

import loop.model.simulationengine.Agent;
import loop.model.simulationengine.AgentPair;
import loop.model.simulationengine.ConcreteAgentPair;
import loop.model.simulationengine.GameResult;
import loop.model.simulationengine.SimulationHistory;

/**
 * This class represents a pure strategy. It is uniquely determined by a condition on the
 * enemy agent based on the history of the simulation, represented as
 * {@link BiPredicate<AgentPair ,SimulationHistory>}.
 * 
 * It also provides static access to some standard strategies such as grim, tit-for-tat,
 * always cooperate or never cooperate
 * 
 * @author Peter Koepernik
 *
 */
public class PureStrategy implements Strategy, java.io.Serializable {
    
    private String name;
    private String description;
    private BiPredicate<AgentPair, SimulationHistory> condition;
    
    public PureStrategy(final String name, final String description, final BiPredicate<AgentPair, SimulationHistory> condition) {
        this.name = name;
        this.description = description;
        this.condition = condition;
    }
    
    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public boolean isCooperative(Agent player, Agent opponent, SimulationHistory history) {
        return condition.test(new ConcreteAgentPair(player, opponent), history);
    }

    @Override
    public double getCooperationProbability(Agent player, Agent opponent, SimulationHistory history) {
        return (condition.test(new ConcreteAgentPair(player, opponent), history)) ? 1 : 0;
    }
    
    /**
     * Returns an instance of the {@link PureStrategy} class representing the tit-for-tat strategy.
     * 
     * @return an instance of the {@link PureStrategy} class representing the tit-for-tat strategy
     */
    public static PureStrategy titForTat() {
        
        return new PureStrategy(
                "tit-for-tat", "-", (pair, history) ->
                toStream(history.getLatestWhere((result) -> result.hasAgent(pair.getFirstAgent()) && result.hasAgent(pair.getSecondAgent())))
                .allMatch((result) -> result == null || result.hasCooperated(pair.getSecondAgent()))
                );
    }
    
    /**
     * Returns an instance of the {@link PureStrategy} class representing the tit-for-tat strategy, where instead
     * of looking at the last game between the player and the opponent the last game between the opponent and an
     * agent of the same (cohesive) group as the player is considered. if the player is part of a non-cohesive group,
     * this strategy leads to the same results as the common tit-for-tat strategy.
     * 
     * @return an instance of the {@link PureStrategy} class representing the group tit-for-tat strategy
     */
    public static PureStrategy groupTitForTat() {
        BiPredicate<AgentPair, GameResult> relevantResult = (pair, result) -> 
                result.hasAgent(pair.getSecondAgent())
                && pair.getFirstAgent().isGroupAffiliated(result.getOtherAgent(pair.getSecondAgent()));
        
        return new PureStrategy(
                "group tit-for-tat", "-", (pair, history) ->
                toStream(history.getLatestWhere((result) -> relevantResult.test(pair, result)))
                .allMatch((result) -> result == null || result.hasCooperated(pair.getSecondAgent()))
                );
    }
    
    /**
     * Returns an instance of the {@link PureStrategy} class representing the grim strategy.
     * 
     * @return an instance of the {@link PureStrategy} class representing the grim strategy
     */
    public static PureStrategy grim() {
        return new PureStrategy(
                "grim", "-", (pair, history) ->
                history.getAllWhere((result) -> result.hasAgent(pair.getFirstAgent()) && result.hasAgent(pair.getSecondAgent()))
                .stream().allMatch((result) -> result.hasCooperated(pair.getSecondAgent()))
                );
    }
    
    /**
     * Returns an instance of the {@link PureStrategy} class representing the grim strategy, where instead
     * of looking at the last game between the player and the opponent the last game between the opponent and an
     * agent of the same (cohesive) group as the player is considered. if the player is part of a non-cohesive group,
     * this strategy leads to the same results as the common grim strategy.
     * 
     * @return an instance of the {@link PureStrategy} class representing the group grim strategy
     */
    public static PureStrategy groupGrim() {
        BiPredicate<AgentPair, GameResult> relevantResult = (pair, result) -> 
                result.hasAgent(pair.getSecondAgent())
                && pair.getFirstAgent().isGroupAffiliated(result.getOtherAgent(pair.getSecondAgent()));
        
        return new PureStrategy(
                "group grim", "-", (pair, history) ->
                history.getAllWhere((result) -> relevantResult.test(pair, result))
                .stream().allMatch((result) -> result.hasCooperated(pair.getSecondAgent()))
                );
    }
    
    /**
     * Returns an instance of the {@link PureStrategy} class representing the "always cooperate" strategy.
     * 
     * @return an instance of the {@link PureStrategy} class representing the "always cooperate" strategy
     */
    public static PureStrategy alwaysCooperate() {
        return new PureStrategy("always cooperate", "-", (pair, history) -> true);
    }
    
    /**
     * Returns an instance of the {@link PureStrategy} class representing the "never cooperate" strategy.
     * 
     * @return an instance of the {@link PureStrategy} class representing the "never cooperate" strategy
     */
    public static PureStrategy neverCooperate() {
        return new PureStrategy("never cooperate", "-", (pair, history) -> false);
    }
    
    private static <T> Stream<T> toStream(T t) {
        List<T> list = new ArrayList<T>();
        list.add(t);
        return list.stream();
    }
    
}
