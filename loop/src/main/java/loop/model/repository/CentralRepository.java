package loop.model.repository;

import loop.model.Group;
import loop.model.Population;
import loop.model.plugin.Plugin;
import loop.model.simulationengine.EquilibriumCriterion;
import loop.model.simulationengine.Game;
import loop.model.simulationengine.PairBuilder;
import loop.model.simulationengine.StrategyAdjuster;
import loop.model.simulationengine.SuccessQuantifier;
import loop.model.simulationengine.distributions.DiscreteDistribution;
import loop.model.simulationengine.strategies.Strategy;

/**
 * This class provides central access to all loaded plugins as well as all
 * available strategies, games, populations and groups. It provides getter
 * methods for the {@link Repository<T>} instances for all those types. It is
 * implemented as a singleton.
 * 
 * @author Christian Schorr
 *
 */
public class CentralRepository {

	private static CentralRepository instance;
	
	private Repository<Strategy> stratRepo;
	private Repository<Game> gameRepo;
	private Repository<Population> populationRepo;
	private Repository<Group> groupRepo;
	private Repository<Plugin<EquilibriumCriterion>> equilibriumCriterinoRepo;
	private Repository<Plugin<SuccessQuantifier>> successQuantifierRepo;
	private Repository<Plugin<StrategyAdjuster>> strategyAdjusterRepo;
	private Repository<Plugin<PairBuilder>> pairBuilderRepo;
	private Repository<Plugin<DiscreteDistribution>> discreteDistributionRepo;
	
	
	private CentralRepository() {
		stratRepo = new HashMapRepository<Strategy>();
		gameRepo = new HashMapRepository<Game>();
		populationRepo = new HashMapRepository<Population>();
		groupRepo = new HashMapRepository<Group>();
		equilibriumCriterinoRepo = new HashMapRepository<Plugin<EquilibriumCriterion>>();
		successQuantifierRepo = new HashMapRepository<Plugin<SuccessQuantifier>>();
		strategyAdjusterRepo = new HashMapRepository<Plugin<StrategyAdjuster>>();
		pairBuilderRepo = new HashMapRepository<Plugin<PairBuilder>>();
		discreteDistributionRepo = new HashMapRepository<Plugin<DiscreteDistribution>>();		
	}

	/**
	 * Returns the singleton instance
	 * 
	 * @return the singleton instance
	 */
	public static CentralRepository getInstance() {
		if (instance == null) {
			instance = new CentralRepository();
			instance.initialize();
		}
		return instance;
	}

	/**
	 * Returns the repository containing all currently available strategies
	 * 
	 * @return the strategy repository
	 */
	public Repository<Strategy> getStrategyRepository() {
		return stratRepo;
	}
	
	/**
	 * Returns the repository containing all currently available games
	 * 
	 * @return the game repository
	 */
	public Repository<Game> getGameRepository() {
		return gameRepo;
	}

	/**
	 * Returns the repository containing all currently available populations
	 * 
	 * @return the population repository
	 */
	public Repository<Population> getPopulationRepository() {
		return populationRepo;
	}
	
	/**
	 * Returns the repository containing all currently available groups
	 * 
	 * @return the group repository
	 */
	public Repository<Group> getGroupRepository() {
		return groupRepo;
	}
	
	/**
	 * Returns the repository containing all currently available {@link EquilibriumCriterion}-plugins
	 * 
	 * @return the equilibrium criterion repository
	 */
	public Repository<Plugin<EquilibriumCriterion>> getEquilibriumCriterionRepository() {
		return equilibriumCriterinoRepo;
	}
	
	/**
	 * Returns the repository containing all currently available {@link SuccessQuantifier}-plugins
	 * 
	 * @return the success quantifier repository
	 */
	public Repository<Plugin<SuccessQuantifier>> getSuccessQuantifiernRepository() {
		return successQuantifierRepo;
	}
	
	/**
	 * Returns the repository containing all currently available {@link StrategyAdjuster}-plugins
	 * 
	 * @return the strategy adjuster repository
	 */
	public Repository<Plugin<StrategyAdjuster>> getStrategyAdjusterRepository() {
		return strategyAdjusterRepo;
	}
	
	/**
	 * Returns the repository containing all currently available {@link PairBuilder}-plugins
	 * 
	 * @return the pair builder repository
	 */
	public Repository<Plugin<PairBuilder>> getPairBuilderRepository() {
		return pairBuilderRepo;
	}
	
	/**
	 * Returns the repository containing all currently available {@link DiscreteDistribution}-plugins
	 * 
	 * @return the discrete distribution repository
	 */
	public Repository<Plugin<DiscreteDistribution>> getDiscreteDistributionRepository() {
		return discreteDistributionRepo;
	}
	
	private void initialize() {
		
	}
}
