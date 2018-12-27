package loop.model.simulationengine;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * This class is an implementation of the {@link SimulationHistory} interface that uses a list to store
 * all game results. The returned lists will always contain the results in ascending age.
 * 
 * @author Peter Koepernik
 *
 */
public class SimulationHistoryTable implements SimulationHistory {
    
    private List<GameResult> results = new ArrayList<GameResult>();
    private List<Agent> agents = new ArrayList<Agent>();
    
    @Override
    public void addResult(GameResult result) {
        this.results.add(0, result);
        for (Agent agent: result.getAgents()) {
            if (!agents.contains(agent))
                agents.add(agent);
        }
    }

    @Override
    public List<GameResult> getAllResults() {
        return this.results;
    }

    @Override
    public List<GameResult> getResultsByAgent(Agent agent) {
        List<GameResult> queriedResults = new ArrayList<GameResult>();
        for (GameResult result: results) {
            if (result.hasAgent(agent)) {
                queriedResults.add(result);
            }
        }
        return queriedResults;
    }

    @Override
    public List<GameResult> getLatestResults() {
        List<GameResult> queriedResults = new ArrayList<GameResult>();
        
        //the agents whose latest results haven't been found yet
        List<Agent> unhandledAgents = new ArrayList<Agent>(agents);
        
        //search for the queried results
        for (GameResult result: results) {
            if (unhandledAgents.contains(result.getAgents().get(0)) || unhandledAgents.contains(result.getAgents().get(1))) {
                queriedResults.add(result);
                unhandledAgents.removeAll(result.getAgents());
            }
            if (unhandledAgents.isEmpty())
                break;
        }
        
        return queriedResults;
    }

    @Override
    public GameResult getLatesResultsByAgent(Agent agent) {
        for (GameResult result: results) {
            if (result.hasAgent(agent)) return result;
        }
        return null;
    }

    @Override
    public List<GameResult> getAllWhere(Predicate<GameResult> condition) {
        List<GameResult> queriedResults = new ArrayList<GameResult>();
        
        for (GameResult result: results) {
            if (condition.test(result)) {
                queriedResults.add(result);
            }
        }
        
        return null;
    }

    @Override
    public GameResult getLatestWhere(Predicate<GameResult> condition) {
        for (GameResult result: results) {
            if (condition.test(result)) return result;
        }
        return null;
    }

    @Override
    public void reset() {
        results = new ArrayList<GameResult>();
        agents = new ArrayList<Agent>();
    }

}
