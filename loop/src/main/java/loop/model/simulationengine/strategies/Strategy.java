package loop.model.simulationengine.strategies;

import loop.model.Nameable;
import loop.model.simulationengine.Agent;
import loop.model.simulationengine.SimulationHistory;

/**
 * Represents a game theoretic strategy.
 * 
 * @author Peter Koepernik
 *
 */
public interface Strategy extends Nameable {
    
    /**
     * Returns whether agent {@code player} would cooperate in a game against agent {@code opponent}
     * using this strategy, given the {@link SimulationHistory} of the current adaption step. If
     * the strategy is non-deterministic, the result may be random.
     * 
     * @param player the player agent
     * @param opponent the opponent agent
     * @param history the history of the current adaption step
     * @return whether agent {@code player} cooperates
     */
    boolean isCooperative(Agent player, Agent opponent, SimulationHistory history);
    
    /**
     * Returns the probability with which agent {@code player} would cooperate in a game against
     * agent {@code opponent} using this strategy, given the {@link SimulationHistory} of the current
     * adaption step.
     * 
     * @param player the player agent
     * @param opponent the opponent agent
     * @param history the history of the current adaption step
     * @return the probability with which agent {@code player} would cooperate in a game against
     * agent {@code opponent} using this strategy, given the {@link SimulationHistory} of the current
     * adaption step
     */
    double getCooperationProbability(Agent player, Agent opponent, SimulationHistory history);
}
