package loop.model.simulationengine;

import java.util.*;
import java.util.function.Predicate;

public class SimulationHistoryMap implements SimulationHistory {

    private Map<Agent, List<GameResult>> map = new HashMap<>();
    private List<GameResult> allResults = new LinkedList<>();

    @Override
    public void addResult(GameResult result) {
        for (Agent agent : result.getAgents()) {
            List<GameResult> list = map.get(agent);
            if (list == null) list = new LinkedList<>();
            list.add(0, result);
        }
        allResults.add(0, result);
    }

    @Override
    public List<GameResult> getAllResults() {
        return allResults;
    }

    @Override
    public List<GameResult> getResultsByAgent(Agent agent) {
        List<GameResult> results = map.get(agent);
        if (results == null) return new ArrayList<>();
        return map.get(agent);
    }

    @Override
    public List<GameResult> getLatestResults() {
        List<GameResult> results = new ArrayList<>();
        for (Agent agent: map.keySet()) {
            List<GameResult> res = map.get(agent);
            if (res != null) results.add(res.get(0));
        }
        return results;
    }

    @Override
    public GameResult getLatesResultsByAgent(Agent agent) {
        List<GameResult> results = map.get(agent);
        if (results != null) return results.get(0);
        return null;
    }

    @Override
    public List<GameResult> getAllWhere(Predicate<GameResult> condition) {
        List<GameResult> results = new ArrayList<>();
        for (GameResult res : allResults) {
            if (condition.test(res)) results.add(res);
        }
        return results;
    }

    @Override
    public GameResult getLatestWhere(Predicate<GameResult> condition) {
        for (GameResult result: allResults) {
            if (condition.test(result)) return result;
        }
        return null;
    }

    @Override
    public void reset() {
        map = new HashMap<>();
        allResults = new LinkedList<>();
    }
}
