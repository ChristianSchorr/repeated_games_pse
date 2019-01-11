package loop.model.simulator;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import loop.model.Group;
import loop.model.MulticonfigurationParameter;
import loop.model.MulticonfigurationParameterType;
import loop.model.Population;
import loop.model.Segment;
import loop.model.UserConfiguration;
import loop.model.repository.CentralRepository;
import loop.model.simulationengine.ConcreteGame;
import loop.model.simulationengine.Configuration;
import loop.model.simulationengine.EngineSegment;
import loop.model.simulationengine.RandomPairBuilder;
import loop.model.simulationengine.ReplicatorDynamic;
import loop.model.simulationengine.StrategyAdjuster;
import loop.model.simulationengine.StrategyEquilibrium;
import loop.model.simulationengine.TotalPayoff;
import loop.model.simulationengine.distributions.BinomialDistribution;
import loop.model.simulationengine.distributions.DiscreteDistribution;
import loop.model.simulationengine.distributions.PoissonDistribution;
import loop.model.simulationengine.strategies.PureStrategy;
import loop.model.simulator.exception.ConfigurationException;

/**
 * This class holds test for the {@link ConfigurationCreator}.
 * 
 * @author Peter Koepernik
 *
 */
public class ConfigurationCreatorTest {
    
    private Population population;
    private Group group1; //2 segments
    private Group group2; //1 segments
    
    private String gameName = ConcreteGame.prisonersDilemma().getName();
    private int roundCount = 200;
    private int iterationCount = 10;
    private boolean mixedAllowed = true;
    private String populationName;
    private String pairBuilderName = RandomPairBuilder.NAME;
    private List<Double> pairBuilderParameters = new ArrayList<Double>();
    private String successQuantifierName = TotalPayoff.NAME;
    private List<Double> successQuantifierParameters = new ArrayList<Double>();
    private String strategyAdjusterName = ReplicatorDynamic.NAME;
    private List<Double> strategyAdjusterParameters = toList(0.5, 0.5);
    private String equilibriumCriterionName = StrategyEquilibrium.NAME;
    private List<Double> equilibriumCriterionParameters = toList(0.005, 50.0);
    private int maxAdapts = 100000;
    
    @Before
    public void setUp() throws Exception {
        //create and add a population
        Segment g1seg1 = new Segment(PoissonDistribution.NAME, toList(10.0), toList(PureStrategy.alwaysCooperate().getName()));
        Segment g1seg2 = new Segment(PoissonDistribution.NAME, toList(10.0), toList(PureStrategy.neverCooperate().getName()));
        group1 = new Group("Group 1", "This is Group 1", toList(g1seg1, g1seg2), toList(0.5, 0.5), false);
        
        Segment g2seg1 = new Segment(BinomialDistribution.NAME, toList(0.0, 20.0, 0.5),
                toList(PureStrategy.groupGrim().getName(), PureStrategy.groupTitForTat().getName()));
        group2 = new Group("Group 2", "This is Group 2", toList(g2seg1), toList(1.0), true);
        
        population = new Population("Population", "This is a population", toList(group1, group2), toList(100, 100));
        populationName = population.getName();
        CentralRepository.getInstance().getPopulationRepository().addEntity(populationName, population);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testNoMulti() {
        UserConfiguration userConfig = new UserConfiguration(gameName, roundCount, iterationCount, mixedAllowed, populationName, pairBuilderName,
                pairBuilderParameters, successQuantifierName, successQuantifierParameters, strategyAdjusterName, strategyAdjusterParameters,
                equilibriumCriterionName, equilibriumCriterionParameters, maxAdapts, false, null);
        
        List<Configuration> configs = null;
        try {
            configs = ConfigurationCreator.generateConfigurations(userConfig);
        } catch (ConfigurationException e) {
            e.printStackTrace();
            fail("ConfigurationException occured.");
        }
        assertNotNull(configs);
        assertEquals(1, configs.size());
        
        //test the configuration
        Configuration config = configs.get(0);
        assertEquals(gameName, config.getGame().getName());
        assertEquals(roundCount, config.getRoundCount());
        assertEquals(mixedAllowed, config.allowsMixedStrategies());
        assertEquals(maxAdapts, config.getMaxAdapts());
        testAlgorithmClasses(config);
        testSegments(config);
    }
    
    @Test
    public void testMultiRoundCount() {
        int start = 100;
        int end = 200;
        int step = 10;
        MulticonfigurationParameter multiParam = new MulticonfigurationParameter(MulticonfigurationParameterType.ROUND_COUNT, start, end, step);
        
        UserConfiguration userConfig = new UserConfiguration(gameName, roundCount, iterationCount, mixedAllowed, populationName, pairBuilderName,
                pairBuilderParameters, successQuantifierName, successQuantifierParameters, strategyAdjusterName, strategyAdjusterParameters,
                equilibriumCriterionName, equilibriumCriterionParameters, maxAdapts, true, multiParam);
        
        List<Configuration> configs = null;
        try {
            configs = ConfigurationCreator.generateConfigurations(userConfig);
        } catch (ConfigurationException e) {
            e.printStackTrace();
            fail("ConfigurationException occured.");
        }
        assertNotNull(configs);
        assertEquals(11, configs.size());
        
        for (int i = 0; i <= 10 ; i++) {
            Configuration config = configs.get(i);
            assertEquals(gameName, config.getGame().getName());
            assertEquals(start + i * step, config.getRoundCount());
            assertEquals(mixedAllowed, config.allowsMixedStrategies());
            assertEquals(maxAdapts, config.getMaxAdapts());
            testAlgorithmClasses(config);
            testSegments(config);
        }
    }
    
    @Test
    public void testMultiMaxAdapts() {
        int start = 1000;
        int end = 5000;
        int step = 200;
        MulticonfigurationParameter multiParam = new MulticonfigurationParameter(MulticonfigurationParameterType.MAX_ADAPTS, start, end, step);
        
        UserConfiguration userConfig = new UserConfiguration(gameName, roundCount, iterationCount, mixedAllowed, populationName, pairBuilderName,
                pairBuilderParameters, successQuantifierName, successQuantifierParameters, strategyAdjusterName, strategyAdjusterParameters,
                equilibriumCriterionName, equilibriumCriterionParameters, maxAdapts, true, multiParam);
        
        List<Configuration> configs = null;
        try {
            configs = ConfigurationCreator.generateConfigurations(userConfig);
        } catch (ConfigurationException e) {
            e.printStackTrace();
            fail("ConfigurationException occured.");
        }
        assertNotNull(configs);
        assertEquals(21, configs.size());
        
        for (int i = 0; i <= 10 ; i++) {
            Configuration config = configs.get(i);
            assertEquals(gameName, config.getGame().getName());
            assertEquals(roundCount, config.getRoundCount());
            assertEquals(mixedAllowed, config.allowsMixedStrategies());
            assertEquals(start + i * step, config.getMaxAdapts());
            testAlgorithmClasses(config);
            testSegments(config);
        }
    }
    
    @Test
    public void testMultiSAParams() {
        double start = 0.0;
        double end = 1.0;
        double step = 0.1;
        //alpha
        MulticonfigurationParameter multiParam = 
            new MulticonfigurationParameter(MulticonfigurationParameterType.SA_PARAM, start, end, step,
                CentralRepository.getInstance().getStrategyAdjusterRepository().getEntityByName(strategyAdjusterName).getParameters().get(0).getName());
        
        UserConfiguration userConfig = new UserConfiguration(gameName, roundCount, iterationCount, mixedAllowed, populationName, pairBuilderName,
                pairBuilderParameters, successQuantifierName, successQuantifierParameters, strategyAdjusterName, strategyAdjusterParameters,
                equilibriumCriterionName, equilibriumCriterionParameters, maxAdapts, true, multiParam);
        
        List<Configuration> configs = null;
        try {
            configs = ConfigurationCreator.generateConfigurations(userConfig);
        } catch (ConfigurationException e) {
            e.printStackTrace();
            fail("ConfigurationException occured.");
        }
        assertNotNull(configs);
        assertEquals(11, configs.size());
        
        for (int i = 0; i <= 10 ; i++) {
            Configuration config = configs.get(i);
            assertEquals(gameName, config.getGame().getName());
            assertEquals(roundCount, config.getRoundCount());
            assertEquals(mixedAllowed, config.allowsMixedStrategies());
            assertEquals(maxAdapts, config.getMaxAdapts());
            testAlgorithmClasses(config);
            testSegments(config);
            
            StrategyAdjuster strategyAdjuster = config.getStrategyAdjuster();
            Class<?> saClass = strategyAdjuster.getClass();
            Field field = null;
            try {
                field = saClass.getDeclaredField("alpha");
            } catch (NoSuchFieldException | SecurityException e) {
                e.printStackTrace();
            }
            field.setAccessible(true);
            double alpha = -1;
            try {
                alpha = field.getDouble(strategyAdjuster);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
            assertEquals(start + i * step, alpha, Math.pow(10, -7));
        }
    }
    
    @Test
    public void testMultiCDParams() {
        double start = 1.0;
        double end = 100.0;
        double step = 5.0;
        //alpha
        MulticonfigurationParameter multiParam = 
            new MulticonfigurationParameter(start, end, step,
                CentralRepository.getInstance().getDiscreteDistributionRepository().getEntityByName(PoissonDistribution.NAME).getParameters().get(0).getName(),
                group1.getName(), 1);
        
        UserConfiguration userConfig = new UserConfiguration(gameName, roundCount, iterationCount, mixedAllowed, populationName, pairBuilderName,
                pairBuilderParameters, successQuantifierName, successQuantifierParameters, strategyAdjusterName, strategyAdjusterParameters,
                equilibriumCriterionName, equilibriumCriterionParameters, maxAdapts, true, multiParam);
        
        List<Configuration> configs = null;
        try {
            configs = ConfigurationCreator.generateConfigurations(userConfig);
        } catch (ConfigurationException e) {
            e.printStackTrace();
            fail("ConfigurationException occured.");
        }
        assertNotNull(configs);
        assertEquals(20, configs.size());
        
        for (int i = 0; i <= 10 ; i++) {
            Configuration config = configs.get(i);
            assertEquals(gameName, config.getGame().getName());
            assertEquals(roundCount, config.getRoundCount());
            assertEquals(mixedAllowed, config.allowsMixedStrategies());
            assertEquals(maxAdapts, config.getMaxAdapts());
            testAlgorithmClasses(config);
            testSegments(config);
            
            EngineSegment g1seg2 = config.getSegments().get(1);
            DiscreteDistribution dist = g1seg2.getCapitalDistribution();
            Class<?> distClass = dist.getClass();
            Field field = null;
            try {
                field = distClass.getDeclaredField("lambda");
            } catch (NoSuchFieldException | SecurityException e) {
                e.printStackTrace();
            }
            field.setAccessible(true);
            double lambda = -1.0;
            try {
                lambda = field.getDouble(dist);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
            assertEquals(start + i * step, lambda, Math.pow(10, -7));
        }
    }
    
    private void testAlgorithmClasses(Configuration config) {
        assertTrue(config.getPairBuilder() instanceof RandomPairBuilder);
        assertTrue(config.getSuccessQuantifier() instanceof TotalPayoff);
        assertTrue(config.getStrategyAdjuster() instanceof ReplicatorDynamic);
        assertTrue(config.getEquilibriumCriterion() instanceof StrategyEquilibrium);
    }
    
    private void testSegments(Configuration config) {
        List<EngineSegment> segments = config.getSegments();
        assertEquals(3, segments.size());
        EngineSegment g1seg1 = segments.get(0);
        EngineSegment g1seg2 = segments.get(1);
        EngineSegment g2seg1 = segments.get(2);
        
        //g1seg1
        assertEquals(50, g1seg1.getAgentCount());
        assertTrue(g1seg1.getCapitalDistribution() instanceof PoissonDistribution);
        assertEquals(1, g1seg1.getStrategyDistribution().getSupport().size());
        assertEquals(PureStrategy.alwaysCooperate().getName(), g1seg1.getStrategyDistribution().getPicker().pickOne().getName());
        assertEquals(-1, g1seg1.getGroupId());
        
        //g1seg2
        assertEquals(50, g1seg2.getAgentCount());
        assertTrue(g1seg2.getCapitalDistribution() instanceof PoissonDistribution);
        assertEquals(1, g1seg2.getStrategyDistribution().getSupport().size());
        assertEquals(PureStrategy.neverCooperate().getName(), g1seg2.getStrategyDistribution().getPicker().pickOne().getName());
        assertEquals(-1, g1seg2.getGroupId());
        
        //g2seg1
        assertEquals(100, g2seg1.getAgentCount());
        assertTrue(g2seg1.getCapitalDistribution() instanceof BinomialDistribution);
        assertEquals(2, g2seg1.getStrategyDistribution().getSupport().size());
        assertTrue(g2seg1.getStrategyDistribution().getSupport().stream().anyMatch(s -> s.getName() == PureStrategy.groupGrim().getName()));
        assertTrue(g2seg1.getStrategyDistribution().getSupport().stream().anyMatch(s -> s.getName() == PureStrategy.groupTitForTat().getName()));
        assertEquals(1, g2seg1.getGroupId());
    }
    
    @SuppressWarnings("unchecked")
    private <T> List<T> toList(T... items) {
        List<T> list = new ArrayList<T>();
        for (int i = 0; i < items.length; i++) {
            list.add(items[i]);
        }
        return list;
    }

}
