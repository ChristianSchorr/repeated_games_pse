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
    public void testOneSegment() {
        int agentCount = 70;
        int roundCount  = 200;
        int maxAdapts = 100000;
        PairBuilder pairBuilder = new RandomPairBuilder();
        SuccessQuantifier successQuantifier = new PayoffInLastAdapt();
        StrategyAdjuster strategyAdjuster = new ReplicatorDynamic(0.5, 0.5);
        EquilibriumCriterion equilibriumCriterion = new StrategyEquilibrium(0.005, 50);
        Game game = ConcreteGame.prisonersDilemma();
        boolean mixedStrategies = true;
        
        //engine segments
        DiscreteDistribution capitalDistribution = new DiscreteUniformDistribution(0, 0);
        
        UniformFiniteDistribution<Strategy> strategyDistribution = new UniformFiniteDistribution<Strategy>();
        strategyDistribution.addObject(PureStrategy.alwaysCooperate());
        strategyDistribution.addObject(PureStrategy.neverCooperate());
        strategyDistribution.addObject(PureStrategy.titForTat());
        strategyDistribution.addObject(PureStrategy.grim());
        
        EngineSegment segment = new EngineSegment(agentCount, -1, capitalDistribution, strategyDistribution);
        List<EngineSegment> segments = new ArrayList<EngineSegment>();
        segments.add(segment);
        
        Configuration configuration = new Configuration(game, roundCount, mixedStrategies, segments, pairBuilder,
                successQuantifier, strategyAdjuster, equilibriumCriterion, maxAdapts);
        
        //execute
        IterationResult result = this.engine.executeIteration(configuration);
        System.out.println("efficiency: " + result.getEfficiency());
    }
    
    //@Test
    public void testTwoGroups() {
        int agentCount = 50;
        int roundCount  = 200;
        int maxAdapts = 100000;
        PairBuilder pairBuilder = new RandomPairBuilder();
        SuccessQuantifier successQuantifier = new PayoffInLastAdapt();
        StrategyAdjuster strategyAdjuster = new ReplicatorDynamic(0.5, 0.5);
        EquilibriumCriterion equilibriumCriterion = new StrategyEquilibrium(0.005, 50);
        Game game = ConcreteGame.prisonersDilemma();
        boolean mixedStrategies = true;
        
        //engine segments
        DiscreteDistribution capitalDistribution = new DiscreteUniformDistribution(0, 0);
        
        UniformFiniteDistribution<Strategy> strategyDistribution = new UniformFiniteDistribution<Strategy>();
        strategyDistribution.addObject(PureStrategy.alwaysCooperate());
        strategyDistribution.addObject(PureStrategy.neverCooperate());
        strategyDistribution.addObject(PureStrategy.titForTat());
        strategyDistribution.addObject(PureStrategy.grim());
        
        EngineSegment segment = new EngineSegment(agentCount, -1, capitalDistribution, strategyDistribution);
        List<EngineSegment> segments = new ArrayList<EngineSegment>();
        segments.add(segment);
        
        Configuration configuration = new Configuration(game, roundCount, mixedStrategies, segments, pairBuilder,
                successQuantifier, strategyAdjuster, equilibriumCriterion, maxAdapts);
        
        //execute
        IterationResult result = this.engine.executeIteration(configuration);
        System.out.println("efficiency: " + result.getEfficiency());
    }
    
}
