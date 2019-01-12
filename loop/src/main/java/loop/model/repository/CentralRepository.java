package loop.model.repository;

import java.io.File;
import java.io.FileNotFoundException;

import loop.model.Group;
import loop.model.Population;
import loop.model.plugin.Plugin;
import loop.model.simulationengine.ConcreteGame;
import loop.model.simulationengine.CooperationConsideringPairBuilder;
import loop.model.simulationengine.EquilibriumCriterion;
import loop.model.simulationengine.Game;
import loop.model.simulationengine.PairBuilder;
import loop.model.simulationengine.PayoffInLastAdapt;
import loop.model.simulationengine.PreferentialAdaption;
import loop.model.simulationengine.RandomCooperationConsideringPairBuilder;
import loop.model.simulationengine.RandomPairBuilder;
import loop.model.simulationengine.RankingEquilibrium;
import loop.model.simulationengine.ReplicatorDynamic;
import loop.model.simulationengine.SlidingMean;
import loop.model.simulationengine.StrategyAdjuster;
import loop.model.simulationengine.StrategyEquilibrium;
import loop.model.simulationengine.SuccessQuantifier;
import loop.model.simulationengine.TotalCapital;
import loop.model.simulationengine.TotalPayoff;
import loop.model.simulationengine.distributions.BinomialDistribution;
import loop.model.simulationengine.distributions.DiscreteDistribution;
import loop.model.simulationengine.distributions.DiscreteUniformDistribution;
import loop.model.simulationengine.distributions.PoissonDistribution;
import loop.model.simulationengine.strategies.PureStrategy;
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
	private Repository<Plugin<EquilibriumCriterion>> equilibriumCriterionRepo;
	private Repository<Plugin<SuccessQuantifier>> successQuantifierRepo;
	private Repository<Plugin<StrategyAdjuster>> strategyAdjusterRepo;
	private Repository<Plugin<PairBuilder>> pairBuilderRepo;
	private Repository<Plugin<DiscreteDistribution>> discreteDistributionRepo;
	
	
	private CentralRepository() {
		stratRepo = new HashMapRepository<Strategy>();
		gameRepo = new HashMapRepository<Game>();
		populationRepo = new HashMapRepository<Population>();
		groupRepo = new HashMapRepository<Group>();
		equilibriumCriterionRepo = new HashMapRepository<Plugin<EquilibriumCriterion>>();
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
		return equilibriumCriterionRepo;
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
	    //strategies
	    this.stratRepo.addEntity(PureStrategy.alwaysCooperate().getName(), PureStrategy.alwaysCooperate());
	    this.stratRepo.addEntity(PureStrategy.neverCooperate().getName(), PureStrategy.neverCooperate());
	    this.stratRepo.addEntity(PureStrategy.grim().getName(), PureStrategy.grim());
	    this.stratRepo.addEntity(PureStrategy.groupGrim().getName(), PureStrategy.groupGrim());
	    this.stratRepo.addEntity(PureStrategy.titForTat().getName(), PureStrategy.titForTat());
	    this.stratRepo.addEntity(PureStrategy.groupTitForTat().getName(), PureStrategy.groupTitForTat());
	    
		//pair builders
	    this.pairBuilderRepo.addEntity(RandomPairBuilder.getPlugin().getName(), RandomPairBuilder.getPlugin());
	    this.pairBuilderRepo.addEntity(CooperationConsideringPairBuilder.getPlugin().getName(), CooperationConsideringPairBuilder.getPlugin());
	    this.pairBuilderRepo.addEntity(RandomCooperationConsideringPairBuilder.getPlugin().getName(),
	            RandomCooperationConsideringPairBuilder.getPlugin());
	    
	    //success quantifiers
	    this.successQuantifierRepo.addEntity(PayoffInLastAdapt.getPlugin().getName(), PayoffInLastAdapt.getPlugin());
	    this.successQuantifierRepo.addEntity(SlidingMean.getPlugin().getName(), SlidingMean.getPlugin());
	    this.successQuantifierRepo.addEntity(TotalPayoff.getPlugin().getName(), TotalPayoff.getPlugin());
	    this.successQuantifierRepo.addEntity(TotalCapital.getPlugin().getName(), TotalCapital.getPlugin());
	    
	    //strategy adjusters
	    this.strategyAdjusterRepo.addEntity(ReplicatorDynamic.getPlugin().getName(), ReplicatorDynamic.getPlugin());
	    this.strategyAdjusterRepo.addEntity(PreferentialAdaption.getPlugin().getName(), PreferentialAdaption.getPlugin());
	    
	    //equilibrium criteria
	    this.equilibriumCriterionRepo.addEntity(StrategyEquilibrium.getPlugin().getName(), StrategyEquilibrium.getPlugin());
	    this.equilibriumCriterionRepo.addEntity(RankingEquilibrium.getPlugin().getName(), RankingEquilibrium.getPlugin());
	    
	    //discrete distributions
	    this.discreteDistributionRepo.addEntity(PoissonDistribution.getPlugin().getName(), PoissonDistribution.getPlugin());
	    this.discreteDistributionRepo.addEntity(BinomialDistribution.getPlugin().getName(), BinomialDistribution.getPlugin());
	    this.discreteDistributionRepo.addEntity(DiscreteUniformDistribution.getPlugin().getName(), DiscreteUniformDistribution.getPlugin());
	    
	    //games
	    this.gameRepo.addEntity(ConcreteGame.prisonersDilemma().getName(), ConcreteGame.prisonersDilemma());
	    
	    //load from files
	    try {
			for(Object p: FileIO.loadAllEntities(new File("./src/main/resources/personallib/Strategies"))) {
				if(p.getClass().equals(PureStrategy.class)) {
					stratRepo.addEntity(((PureStrategy) p).getName(), (PureStrategy) p);
				}
			};
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	    try {
			for(Object p: FileIO.loadAllEntities(new File("./src/main/resources/personallib/Games"))) {
				if(p.getClass().equals(Game.class)) {
					gameRepo.addEntity(((Game) p).getName(), (Game) p);
				}
			};
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	    try {
			for(Object p: FileIO.loadAllEntities(new File("./src/main/resources/personallib/Groups"))) {
				if(p.getClass().equals(Group.class)) {
					groupRepo.addEntity(((Group) p).getName(), (Group) p);
				}
			};
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	    try {
			for(Object p: FileIO.loadAllEntities(new File("./src/main/resources/personallib/Populations"))) {
				if(p.getClass().equals(Population.class)) {
					populationRepo.addEntity(((Population) p).getName(), (Population) p);
				}
			};
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	    
	    //load plugins
	    //TODO
	}
}
