package loop.model.simulationengine;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.alg.interfaces.MatchingAlgorithm;
import org.jgrapht.alg.matching.PathGrowingWeightedMatching;
import org.jgrapht.graph.SimpleWeightedGraph;

/**
 * Realises the "Paarbildung nach Wunsch" pairing mechanism as described in the specification.
 * 
 * @author Peter Koepernik
 *
 */
public class CooperationConsideringPairBuilder implements PairBuilder {
    
    private PairBuilder randomPairBuilder = new RandomPairBuilder();
    
    @Override
    public List<AgentPair> buildPairs(List<Agent> agents, SimulationHistory history) {
        SimpleWeightedGraph<Agent, AgentPair> graph = new SimpleWeightedGraph<Agent, AgentPair>(null);
        for (Agent a1: agents) {
            for (Agent a2: agents) {
                if (a1 == a2) continue;
                if (!graph.containsVertex(a1)) graph.addVertex(a1);
                if (!graph.containsVertex(a2)) graph.addVertex(a2);
                
                if (!graph.containsEdge(a1, a2)) {
                    AgentPair agentPair = new ConcreteAgentPair(a1, a2);
                    graph.addEdge(a1, a2, agentPair);
                    double weight = a1.getStrategy().getCooperationProbability(a1, a2, history)
                            + a2.getStrategy().getCooperationProbability(a2, a1, history);
                    graph.setEdgeWeight(agentPair, weight);
                }
            }
        }
        
        MatchingAlgorithm.Matching<Agent, AgentPair> matching = new PathGrowingWeightedMatching<Agent, AgentPair>(graph, false).getMatching();
        List<AgentPair> pairs = new ArrayList<AgentPair>(matching.getEdges());
        
        if (2 * pairs.size() != agents.size()) {
            System.out.println("Hello");
            List<Agent> pairedAgents = new ArrayList<Agent>();
            for (AgentPair pair: pairs) {
                pairedAgents.add(pair.getFirstAgent());
                pairedAgents.add(pair.getSecondAgent());
            }
            
            List<Agent> missedAgents = new ArrayList<Agent>(agents);
            missedAgents.removeAll(pairedAgents);
            pairs.addAll(randomPairBuilder.buildPairs(missedAgents, history));
        }
        
        return pairs;
    }

}
