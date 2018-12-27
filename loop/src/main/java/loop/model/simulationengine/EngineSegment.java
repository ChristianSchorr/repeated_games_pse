package loop.model.simulationengine;

import loop.model.simulationengine.distributions.DiscreteDistribution;
import loop.model.simulationengine.distributions.UniformFiniteDistribution;
import loop.model.simulationengine.strategies.Strategy;

/**
 * This class represents a segment as used by the {@link AgentInitialiser}. It is characterized
 * by the number of its agents, the id of the group to which its agents belong and its capital
 * and strategy distribution.
 * 
 * @author Peter Koepernik
 *
 */
public class EngineSegment {
    
    private int agentCount;
    private int groupId;
    private DiscreteDistribution capitalDistribution;
    private UniformFiniteDistribution<Strategy> strategyDistribution;
    
    /**
     * Creates a new engine segment with the given parameters.
     * 
     * @param agentCount the amount of agents in this segment
     * @param groupId the id o the group this segments agents belong to (-1 if not cohesive)
     * @param capitalDistribution the capital distribution which is used to initialise the capital
     *                            of this segments agents
     * @param strategyDistribution the strategy distribution which is used to initialise the strategies
     *                            of this segments agents
     */
    public EngineSegment(int agentCount, int groupId, DiscreteDistribution capitalDistribution,
            UniformFiniteDistribution<Strategy> strategyDistribution) {
        this.agentCount = agentCount;
        this.groupId = groupId;
        this.capitalDistribution = capitalDistribution;
        this.strategyDistribution = strategyDistribution;
    }
    
    /**
     * Returns the amount of agents in this segment.
     * 
     * @return the amount of agents in this segment
     */
    public int getAgentCount() {
        return this.agentCount;
    }
    
    /**
     * Returns the id of the group this segment is part of, if it is cohesive, -1 otherwise.
     * 
     * @return the id of the group this segment is part of, if it is cohesive, -1 otherwise
     */
    public int getGroupId() {
        return this.groupId;
    }
    
    /**
     * Returns the capital distribution of this segment.
     * 
     * @return the capital distribution of this segment
     */
    public DiscreteDistribution getCapitalDistribution() {
        return this.capitalDistribution;
    }
    
    /**
     * Returns the strategy distribution of this segment.
     * 
     * @return the strategy
     *  distribution of this segment
     */
    public UniformFiniteDistribution<Strategy> getStrategyDistribution() {
        return this.strategyDistribution;
    }
}
