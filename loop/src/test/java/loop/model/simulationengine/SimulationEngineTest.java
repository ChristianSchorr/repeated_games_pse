package loop.model.simulationengine;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import loop.model.simulationengine.distributions.DiscreteDistribution;
import loop.model.simulationengine.distributions.DiscreteUniformDistribution;
import loop.model.simulationengine.distributions.UniformFiniteDistribution;
import loop.model.simulationengine.strategies.PureStrategy;
import loop.model.simulationengine.strategies.Strategy;

/**
 * This class holds tests for the simulaiton engine.
 * 
 * @author Peter Koepernik
 *
 */
public class SimulationEngineTest {
    
    private SimulationEngine engine;
    
    @Before
    public void setUp() throws Exception {
        this.engine = new SimulationEngine();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() {
        int agentCount = 50;
        int roundCount  = 500;
        int maxAdapts = 1000;
        PairBuilder pairBuilder = new RandomPairBuilder();
        SuccessQuantifier successQuantifier = new PayoffInLastAdapt();
        StrategyAdjuster strategyAdjuster = new ReplicatorDynamic(0.5, 0.5);
        EquilibriumCriterion equilibriumCriterion = new StrategyEquilibrium(0.01, 50);
        Game game = ConcreteGame.prisonersDilemma();
        boolean mixedStrategies = false;
        
        //engine segments
        DiscreteDistribution capitalDistribution = new DiscreteUniformDistribution(0, 0);
        
        UniformFiniteDistribution<Strategy> strategyDistribution = new UniformFiniteDistribution<Strategy>();
        Strategy coop = new PureStrategy("coop", "", (pair, history) -> true);
        Strategy notCoop = new PureStrategy("notcoop", "", (pair, history) -> false);
        strategyDistribution.addObject(coop);
        strategyDistribution.addObject(notCoop);
        
        EngineSegment segment = new EngineSegment(agentCount, -1, capitalDistribution, strategyDistribution);
        List<EngineSegment> segments = new ArrayList<EngineSegment>();
        segments.add(segment);
        
        Configuration configuration = new Configuration(game, roundCount, mixedStrategies, segments, pairBuilder,
                successQuantifier, strategyAdjuster, equilibriumCriterion, maxAdapts);
        
        //execute
        IterationResult result = this.engine.executeIteration(configuration);
    }
    
}
