package loop.model.simulationengine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import loop.model.plugin.Parameter;
import loop.model.plugin.ParameterValidator;
import loop.model.plugin.Plugin;

/**
 * Realises the "Absolutkapital" success quantification.
 * 
 * @author Peter Koepernik
 *
 */
public class PayoffInLastAdapt implements SuccessQuantifier {
    
    public static final String NAME = "Payoff in the Last Adaption Step";
    private static final String DESCRIPTION = "Uses the total payoff received within the games "
            + "of the elapsed adaption step as success quantification for each agent.";
    
    @Override
    public List<Agent> createRanking(List<Agent> agents, SimulationHistory history) {
        Map<Agent, Double> payoffs = new HashMap<Agent, Double>();
        for (Agent a: agents) {
            payoffs.put(a, 0.0);
        }
        for (GameResult game: history.getAllResults()) {
            Agent a1 = game.getAgents().get(0);
            Agent a2 = game.getOtherAgent(a1);
            double p1 = game.getPayoff(a1);
            double p2 = game.getPayoff(a2);
            payoffs.replace(a1, payoffs.get(a1) + p1);
            payoffs.replace(a2, payoffs.get(a2) + p2);
        }
        agents.sort((a1, a2) -> (payoffs.get(a2) - payoffs.get(a1) < 0) ? -1 : ((payoffs.get(a2) - payoffs.get(a1) > 0) ? 1 : 0));
        return agents;
    }
}
