package loop.model.repository;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import loop.model.Group;
import loop.model.Population;
import loop.model.UserConfiguration;
import loop.model.plugin.*;
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
import loop.model.simulationengine.distributions.DiscreteDistribution;
import loop.model.simulationengine.strategies.PureStrategy;
import loop.model.simulationengine.strategies.Strategy;
import loop.plugins.distributions.BinomialDistributionPlugin;
import loop.plugins.distributions.DiscreteUniformDistributionPlugin;
import loop.plugins.distributions.PoissonDistributionPlugin;
import loop.plugins.equilibriumcriterion.RankingEquilibriumPlugin;
import loop.plugins.equilibriumcriterion.StrategyEquilibriumPlugin;
import loop.plugins.pairbuilder.CooperationConsideringPairBuilderPlugin;
import loop.plugins.pairbuilder.RandomCooperationConsideringPairBuilderPlugin;
import loop.plugins.pairbuilder.RandomPairBuilderPlugin;
import loop.plugins.strategyadjuster.PreferentialAdaptionPlugin;
import loop.plugins.strategyadjuster.ReplicatorDynamicPlugin;
import loop.plugins.successquantifier.PayoffInLastAdaptPlugin;
import loop.plugins.successquantifier.SlidingMeanPlugin;
import loop.plugins.successquantifier.TotalCapitalPlugin;
import loop.plugins.successquantifier.TotalPayoffPlugin;

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
		FileIO.initializeDirectories();
	    //strategies
	    this.stratRepo.addEntity(PureStrategy.alwaysCooperate().getName(), PureStrategy.alwaysCooperate());
	    this.stratRepo.addEntity(PureStrategy.neverCooperate().getName(), PureStrategy.neverCooperate());
	    this.stratRepo.addEntity(PureStrategy.grim().getName(), PureStrategy.grim());
	    this.stratRepo.addEntity(PureStrategy.groupGrim().getName(), PureStrategy.groupGrim());
	    this.stratRepo.addEntity(PureStrategy.titForTat().getName(), PureStrategy.titForTat());
	    this.stratRepo.addEntity(PureStrategy.groupTitForTat().getName(), PureStrategy.groupTitForTat());
	    
		//pair builders
        Plugin<PairBuilder> pairBuilderPlugin = new RandomPairBuilderPlugin();
        this.pairBuilderRepo.addEntity(pairBuilderPlugin.getName(), pairBuilderPlugin);
        pairBuilderPlugin = new CooperationConsideringPairBuilderPlugin();
        this.pairBuilderRepo.addEntity(pairBuilderPlugin.getName(), pairBuilderPlugin);
        pairBuilderPlugin = new RandomCooperationConsideringPairBuilderPlugin();
        this.pairBuilderRepo.addEntity(pairBuilderPlugin.getName(), pairBuilderPlugin);

	    //success quantifiers
		Plugin<SuccessQuantifier> successQuantifierPlugin = new PayoffInLastAdaptPlugin();
		this.successQuantifierRepo.addEntity(successQuantifierPlugin.getName(), successQuantifierPlugin);
		successQuantifierPlugin = new SlidingMeanPlugin();
		this.successQuantifierRepo.addEntity(successQuantifierPlugin.getName(), successQuantifierPlugin);
		successQuantifierPlugin = new TotalCapitalPlugin();
		this.successQuantifierRepo.addEntity(successQuantifierPlugin.getName(), successQuantifierPlugin);
		successQuantifierPlugin = new TotalPayoffPlugin();
		this.successQuantifierRepo.addEntity(successQuantifierPlugin.getName(), successQuantifierPlugin);

	    //strategy adjusters
		Plugin<StrategyAdjuster> strategyAdjusterPlugin = new ReplicatorDynamicPlugin();
		this.strategyAdjusterRepo.addEntity(strategyAdjusterPlugin.getName(), strategyAdjusterPlugin);
		strategyAdjusterPlugin = new PreferentialAdaptionPlugin();
		this.strategyAdjusterRepo.addEntity(strategyAdjusterPlugin.getName(), strategyAdjusterPlugin);

	    //equilibrium criteria
		Plugin<EquilibriumCriterion> equilibriumCriterionPlugin = new StrategyEquilibriumPlugin();
		this.equilibriumCriterionRepo.addEntity(equilibriumCriterionPlugin.getName(), equilibriumCriterionPlugin);
		equilibriumCriterionPlugin = new RankingEquilibriumPlugin();
	    this.equilibriumCriterionRepo.addEntity(equilibriumCriterionPlugin.getName(), equilibriumCriterionPlugin);

	    //discrete distributions
		Plugin<DiscreteDistribution> distributionPlugin = new PoissonDistributionPlugin();
	    this.discreteDistributionRepo.addEntity(distributionPlugin.getName(), distributionPlugin);
	    distributionPlugin = new BinomialDistributionPlugin();
		this.discreteDistributionRepo.addEntity(distributionPlugin.getName(), distributionPlugin);
		distributionPlugin = new DiscreteUniformDistributionPlugin();
		this.discreteDistributionRepo.addEntity(distributionPlugin.getName(), distributionPlugin);

	    //games
	    this.gameRepo.addEntity(ConcreteGame.prisonersDilemma().getName(), ConcreteGame.prisonersDilemma());
	    this.gameRepo.addEntity(ConcreteGame.stagHunt().getName(), ConcreteGame.stagHunt());
	    this.gameRepo.addEntity(ConcreteGame.ChickenGame().getName(), ConcreteGame.ChickenGame());

		UserConfiguration configuration = UserConfiguration.getDefaultConfiguration();
	    
	    try {
			for(Object p: FileIO.loadAllEntities(FileIO.STRATEGY_DIR)) {
				if(p instanceof Strategy) {
					stratRepo.addEntity(((PureStrategy) p).getName(), (PureStrategy) p);
				}
			};
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NullPointerException n) {
			//Empty File    
		}
	    try {
			for(Object p: FileIO.loadAllEntities(FileIO.GAME_DIR)) {
				if(p instanceof Game) {
					gameRepo.addEntity(((Game) p).getName(), (Game) p);
				}
			};
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NullPointerException n) {
			//Empty File    
		}
	    try {
			for(Object p: FileIO.loadAllEntities(FileIO.GROUP_DIR)) {
				if(p instanceof Group) {
					groupRepo.addEntity(((Group) p).getName(), (Group) p);
				}
			};
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NullPointerException n) {
			//Empty File    
		}
	    try {
			for(Object p: FileIO.loadAllEntities(FileIO.POPULATION_DIR)) {
				if(p instanceof Population) {
					populationRepo.addEntity(((Population) p).getName(), (Population) p);
				}
			};
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NullPointerException n) {
			//Empty File    
		}

	    loadPlugins();

	    validateGroups();
	    validatePopulations();
	}
	
	private void validateGroups() {
	    List<Group> invalidGroups = new ArrayList<Group>();
	    
	    for (Group group: groupRepo.getAllEntities()) {
	        List<String> unknownStrategies = new ArrayList<String>();
	        List<String> unknownCapitalDistributionNames = new ArrayList<String>();
	        group.getSegments().forEach(seg -> {
	            seg.getStrategyNames().forEach(stratName -> {
	                if (!CentralRepository.getInstance().getStrategyRepository().containsEntityName(stratName))
	                    if (!unknownStrategies.contains(stratName)) unknownStrategies.add(stratName);
	                });
	            String distName = seg.getCapitalDistributionName();
	            if (!CentralRepository.getInstance().getDiscreteDistributionRepository().containsEntityName(distName)) {
	                if (!unknownCapitalDistributionNames.contains(distName)) {
	                    unknownCapitalDistributionNames.add(distName);
	                }
	            }
	        });
	        
	        if (unknownStrategies.isEmpty() && unknownCapitalDistributionNames.isEmpty()) {
	            continue;
	        }
	        
	        invalidGroups.add(group);
	        
	        String errorMsg = "The group '" + group.getName() + "' could not be imported because it contains the following unknown entities:";
	        for (String strat: unknownStrategies) errorMsg += "\n - the strategy '" + strat + "'";
	        for (String dist: unknownCapitalDistributionNames) errorMsg += "\n - the discrete distribution '" + dist + "'";
	        
	        Alert alert = new Alert(AlertType.ERROR, errorMsg, ButtonType.OK);
	        alert.showAndWait();
	    }
	    
	    invalidGroups.forEach(group -> groupRepo.removeEntity(group.getName()));
	}
	
	private void validatePopulations() {
	    List<Population> invalidPopulations = new ArrayList<Population>();
	    
	    for (Population population: populationRepo.getAllEntities()) {
	        List<String> unknownGroups = population.getGroupNames().stream().filter(
	                name -> !CentralRepository.getInstance().getGroupRepository().containsEntityName(name)).collect(Collectors.toList());
	        
	        if (unknownGroups.isEmpty()) continue;
	        
	        String errorMsg = "The population '" + population.getName() + "' could not be imported because it contains the following unknown groups:";
	        for (String name: unknownGroups) errorMsg += "\n - " + name;
	        Alert alert = new Alert(AlertType.ERROR, errorMsg, ButtonType.OK);
	        alert.showAndWait();
	    }
	    
	    invalidPopulations.forEach(pop -> populationRepo.removeEntity(pop.getName()));
	}
	
	private void loadPlugins() {

		List<PairBuilderPlugin> pairBuilderPlugins = PluginLoader.loadPlugins(PairBuilderPlugin.class);
		for (PairBuilderPlugin plugin : pairBuilderPlugins)
			pairBuilderRepo.addEntity(plugin.getName(), plugin);

		List<SuccessQuantifierPlugin> successQuantifierPlugins = PluginLoader.loadPlugins(SuccessQuantifierPlugin.class);
		for (SuccessQuantifierPlugin plugin : successQuantifierPlugins)
			successQuantifierRepo.addEntity(plugin.getName(), plugin);

		List<StrategyAdjusterPlugin> strategyAdjusterPlugins = PluginLoader.loadPlugins(StrategyAdjusterPlugin.class);
		for (StrategyAdjusterPlugin plugin : strategyAdjusterPlugins)
			strategyAdjusterRepo.addEntity(plugin.getName(), plugin);

		List<EquilibriumCriterionPlugin> equilibriumCriterionPlugins = PluginLoader.loadPlugins(EquilibriumCriterionPlugin.class);
		for (EquilibriumCriterionPlugin plugin : equilibriumCriterionPlugins)
			equilibriumCriterionRepo.addEntity(plugin.getName(), plugin);

		List<DiscreteDistributionPlugin> discreteDistributionPlugins = PluginLoader.loadPlugins(DiscreteDistributionPlugin.class);
		for (DiscreteDistributionPlugin plugin : discreteDistributionPlugins)
			discreteDistributionRepo.addEntity(plugin.getName(), plugin);
	}
}
