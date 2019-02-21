package loop.model.simulationengine;

import java.util.List;
import java.util.function.Predicate;

/**
 * An implementation of this interface always stores the results of all games of the current
 * adaptation step. This is necessary because, for example, strategies such as tit-for-tat
 * or grim can refer to the behaviour of the other agents in previous games.
 * 
 * @author Peter Koepernik
 *
 */
public interface SimulationHistory {
    
    /**
     * Adds the given game result to the history.
     * 
     * @param result the result that shall be added to the history
     */
    void addResult(GameResult result);
    
    /**
     * Returns a list of the results of all games of the current adaption step.
     * 
     * @return a list of the results of all games of the current adaption step
     */
    List<GameResult> getAllResults();
    
    /**
     * Returns a list of the results of all games of the given agent.
     * 
     * @param agent the agent whose game results shall be returned
     * @return a list of the results of all games of the given agent
     */
    List<GameResult> getResultsByAgent(Agent agent);

    /**
     * Returns a list of the results of all games of the given agents group members
     * including the agent himself;
     *
     * @param agent the agents whose group shall be considered
     * @return a list of all relevant results
     */
    List<GameResult> getResultsByGroup(Agent agent);
    
    /**
     * Returns the results of the most recent games result of every agent.
     * 
     * @return the results of the most recent games result of every agent
     */
    List<GameResult> getLatestResults();
    
    /**
     * Returns the result of the most recent game of the given agent.
     * 
     * @param agent the agent whose latest game result shall be returned
     * @return the result of the most recent game of the given agent
     */
    GameResult getLatesResultsByAgent(Agent agent);

    /**
     * Returns the results of the most recent games of the agent's group members
     * including the agent himself;
     *
     * @param agent the agent whose group shall be considered
     * @return returns all relevant results
     */
    List<GameResult> getLatestResultsByGroup(Agent agent);
    
    /**
     * Filters all game results with the given condition and returns them.
     * 
     * @param condition the filter condition as {@link Predicate<GameResult>}
     * @return the filtered game results
     */
    List<GameResult> getAllWhere(Predicate<GameResult> condition);
    
    /**
     * Returns the latest game result that meets the given condition.
     * @param condition the filter condition as {@link Predicate<GameResult>}
     * @return the latest game result that meets the given condition
     */
    GameResult getLatestWhere(Predicate<GameResult> condition);

    /**
     * Returns a list of all agents
     * @return a list of all agents
     */
    List<Agent> getAgents();
    
    /**
     * Resets the history;
     */
    void reset();
}
