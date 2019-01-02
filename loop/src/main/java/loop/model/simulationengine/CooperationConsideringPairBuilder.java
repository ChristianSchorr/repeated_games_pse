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
    private SimpleWeightedGraph<Agent, AgentPair> graph;
    private List<Agent> allAgents;
    private List<AgentPair> allPairs;
    
    @Override
    public List<AgentPair> buildPairs(List<Agent> agents, SimulationHistory history) {
        //initialise graph etc. on first call with new agents
        if (this.allAgents == null || this.allAgents.size() != agents.size() || !this.allAgents.containsAll(agents)) {
            init(agents);
        }
        
        //set edge weights
        for (AgentPair pair: allPairs) {
            Agent a1 = pair.getFirstAgent();
            Agent a2 = pair.getSecondAgent();
            double weight = a1.getStrategy().getCooperationProbability(a1, a2, history)
                    + a2.getStrategy().getCooperationProbability(a2, a1, history);
            graph.setEdgeWeight(pair, weight);
        }
        
        //create matching
        MatchingAlgorithm.Matching<Agent, AgentPair> matching = new PathGrowingWeightedMatching<Agent, AgentPair>(graph, false).getMatching();
        List<AgentPair> pairs = new ArrayList<AgentPair>(matching.getEdges());
        
        //pair unmatched agents
        if (2 * pairs.size() != agents.size()) {
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
    
    private void init(List<Agent> agents) {
        this.allAgents = new ArrayList<Agent>();
        this.allPairs = new ArrayList<AgentPair>();
        graph = new SimpleWeightedGraph<Agent, AgentPair>(null);
        
        for (Agent a1: agents) {
            this.allAgents.add(a1);
            for (Agent a2: agents) {
                if (a1 == a2) continue;
                if (!graph.containsVertex(a1)) graph.addVertex(a1);
                if (!graph.containsVertex(a2)) graph.addVertex(a2);
                
                if (!graph.containsEdge(a1, a2)) {
                    AgentPair agentPair = new ConcreteAgentPair(a1, a2);
                    this.allPairs.add(agentPair);
                    graph.addEdge(a1, a2, agentPair);
                }
            }
        }
    }

}
