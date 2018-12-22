package loop.model.simulationengine;

import loop.model.simulationengine.strategies.Strategy;

/**
 * This class represents an agent. It provides getters for its group afliation, its current and
 * initial capital and its current strategy.
 * 
 * @author Peter Koepernik
 *
 */
public class Agent {
    private int capital;
    private int initialCapital;
    private Strategy strategy;
    private int groupId;
    
    /**
     * Creates a new agent with given initial capital, given strategy and given group affiliation.
     * 
     * @param initialCapital the initial capital of the agent
     * @param initialStrategy the initial strategy of the agent
     * @param groupId the {@code id} of the group this agent belongs to, if it is cohesive; {@code 0} otherwise
     */
    public Agent(final int initialCapital, final Strategy initialStrategy, final int groupId) {
        this.capital = initialCapital;
        this.initialCapital = initialCapital;
        this.strategy = initialStrategy;
        this.groupId = groupId;
    }
    
    /**
     * Returns the current capital of this agent.
     * @return the current capital of this agent
     */
    public int getCapital() {
        return this.capital;
    }
    
    /**
     * Returns the initial capital of this agent.
     * @return the initial capital of this agent
     */
    public int getInitialCapital() {
        return this.initialCapital;
    }
    
    /**
     * Increases the capital of this agent by the given amount.
     * 
     * @param capital the amount by which the agents capital shall be increased
     */
    public void addCapital(final int capital) {
        this.capital += capital;
    }
    
    /**
     * Returns the current strategy of this agent.
     * @return the current strategy of this agent
     */
    public Strategy getStrategy() {
        return this.strategy;
    }
    
    /**
     * Sets the strategy of this agent to the given one.
     * 
     * @param strategy the strategy this agent shall use
     */
    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }
    
    /**
     * Returns the id of the group this agent belongs to, if it is cohesive, {@code -1} otherwise.
     * @return the id of the group this agent belongs to, if it is cohesive, {@code -1} otherwise
     */
    public int getGroupId() {
        return this.groupId;
    }
}
