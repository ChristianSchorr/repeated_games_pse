package loop.controller;

import loop.model.Population;
import loop.model.simulationengine.strategies.Strategy;

public class CentralRepository {
    public static CentralRepository getInstance() {
        return null;
    }
    public Repository<Strategy> getStrategyRepository() {
        return null;
    }
    public Repository<Population> getPopulationRepository() {
        return null;
    }
}
