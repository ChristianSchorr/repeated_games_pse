package loop.model.simulationengine.strategies;

import java.util.function.BiPredicate;

import loop.model.simulationengine.Agent;
import loop.model.simulationengine.AgentPair;
import loop.model.simulationengine.ConcreteAgentPair;
import loop.model.simulationengine.SimulationHistory;

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

}
