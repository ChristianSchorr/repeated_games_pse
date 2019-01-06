package loop.model.simulator;

import java.util.ArrayList;
import java.util.List;

import loop.model.UserConfiguration;
import loop.model.simulationengine.ConcreteGame;
import loop.model.simulationengine.Configuration;
import loop.model.simulationengine.EngineSegment;
import loop.model.simulationengine.EquilibriumCriterion;
import loop.model.simulationengine.Game;
import loop.model.simulationengine.PairBuilder;
import loop.model.simulationengine.PayoffInLastAdapt;
import loop.model.simulationengine.RandomPairBuilder;
import loop.model.simulationengine.ReplicatorDynamic;
import loop.model.simulationengine.StrategyAdjuster;
import loop.model.simulationengine.StrategyEquilibrium;
import loop.model.simulationengine.SuccessQuantifier;
import loop.model.simulationengine.distributions.DiscreteDistribution;
import loop.model.simulationengine.distributions.DiscreteUniformDistribution;
import loop.model.simulationengine.distributions.UniformFiniteDistribution;
import loop.model.simulationengine.strategies.PureStrategy;
import loop.model.simulationengine.strategies.Strategy;
import loop.model.simulator.exception.ConfigurationException;

/**
 * This class takes a {@link UserConfiguration} and generates all associated
 * elementary configurations. These are returned as {@link Configuration}s. It
 * also provides functionality to create a deep copy of a {@link Configuration}
 * .
 * 
 * @author Christian Schorr
 *
 */
public class ConfigurationCreator {

	/**
	 * Generates all associated elementary configurations to the given
	 * {@link UserConfiguration} and returns them as {@link Configuration}s.
	 * 
	 * @param config the {@link UserConfiguration} whose associated elementary
	 *               configurations shall be generated
	 * @return all associated elementary configurations of the given
	 *         {@link UserConfiguration} as {@link Configuration}s
	 *
	 * @throws ConfigurationException {@link ConfigurationException} when the
	 *                                provided configuration is faulty
	 */
	public static List<Configuration> generateConfigurations(UserConfiguration config) throws ConfigurationException {
		
		int agentCount = 50;
        int roundCount  = 200;
        int maxAdapts = 100000;
        PairBuilder pairBuilder = new RandomPairBuilder();
        SuccessQuantifier successQuantifier = new PayoffInLastAdapt();
        StrategyAdjuster strategyAdjuster = new ReplicatorDynamic(0.5, 0.5);
        int G = 50;
        double alpha = 0.005;
        EquilibriumCriterion equilibriumCriterion = new StrategyEquilibrium(alpha, G);
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
                successQuantifier, strategyAdjuster, equilibriumCriterion, maxAdapts, 16);
        List<Configuration> list = new ArrayList<Configuration>();
        list.add(configuration);
        
		return list;

	}


	/**
	 * Provides a deep copy of the given configuration.
	 * 
	 * @param config the configuration to copy
	 * @return the newly created deep copy of the given configuration
	 */
	public static Configuration getCopy(Configuration config) {
		return null;
	}
}
