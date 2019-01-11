package loop.model.simulator;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import loop.model.Group;
import loop.model.MulticonfigurationParameter;
import loop.model.MulticonfigurationParameterType;
import loop.model.Population;
import loop.model.Segment;
import loop.model.UserConfiguration;
import loop.model.plugin.Plugin;
import loop.model.repository.CentralRepository;
import loop.model.simulationengine.Configuration;
import loop.model.simulationengine.EngineSegment;
import loop.model.simulationengine.EquilibriumCriterion;
import loop.model.simulationengine.Game;
import loop.model.simulationengine.PairBuilder;
import loop.model.simulationengine.StrategyAdjuster;
import loop.model.simulationengine.SuccessQuantifier;
import loop.model.simulationengine.distributions.DiscreteDistribution;
import loop.model.simulationengine.distributions.UniformFiniteDistribution;
import loop.model.simulationengine.strategies.Strategy;
import loop.model.simulator.exception.ConfigurationException;
import loop.model.simulator.exception.PluginNotFoundException;

/**
 * This class takes a {@link UserConfiguration} and generates all associated
 * elementary configurations. These are returned as {@link Configuration}s. It
 * also provides functionality to create a deep copy of a {@link Configuration}
 * .
 * 
 * @author Peter Koepernik
 *
 */
public class ConfigurationCreator {
    
    private static Game game;
    private static int roundCount;
    private static int iterationCount;
    private static boolean mixedStrategies;
    private static List<EngineSegment> engineSegments;
    private static SuccessQuantifier successQuantifier;
    private static PairBuilder pairBuilder;
    private static StrategyAdjuster strategyAdjuster;
    private static EquilibriumCriterion equilibriumCriterion;
    private static int maxAdapts;
    
    
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
	    List<Configuration> configurations = new ArrayList<Configuration>();
	    
	    if (!config.isMulticonfiguration()) { //no multiconfiguration
	        configureAllExcept(null, config);
	        configurations.add(new Configuration(game, roundCount, mixedStrategies, engineSegments, pairBuilder,
	                successQuantifier, strategyAdjuster, equilibriumCriterion, maxAdapts, config.getIterationCount()));
	        return configurations;
	    }
	    
	    MulticonfigurationParameter multiParam = config.getMulticonfigurationParameter();
	    configureAllExcept(multiParam.getType(), config);
	    switch(multiParam.getType()) {
	        case ROUND_COUNT:
	            multiParam.getParameterValues().stream().mapToInt(d -> d.intValue()).forEach(
	                    (rounds) -> {
	                        roundCount = rounds;
	                        configurations.add(createConfiguration());
	                    });
	            return configurations;
	        case PB_PARAM:
	            Plugin<PairBuilder> pluginPB = CentralRepository.getInstance().getPairBuilderRepository().getEntityByName(config.getPairBuilderName());
	            multiconfiguredParameters(pluginPB, config.getPairBuilderParameters(), config.getMulticonfigurationParameter()).forEach(
	                            params -> {
	                                pairBuilder = pluginPB.getNewInstance(params);
	                                configurations.add(createConfiguration());
	                            });
	            return configurations;
	        case SQ_PARAM:
                Plugin<SuccessQuantifier> pluginSQ = CentralRepository.getInstance().getSuccessQuantifiernRepository().getEntityByName(config.getSuccessQuantifierName());
                multiconfiguredParameters(pluginSQ, config.getSuccessQuantifierParameters(), config.getMulticonfigurationParameter()).forEach(
                                params -> {
                                    successQuantifier = pluginSQ.getNewInstance(params);
                                    configurations.add(createConfiguration());
                                });
                return configurations;
	        case SA_PARAM:
                Plugin<StrategyAdjuster> pluginSA = CentralRepository.getInstance().getStrategyAdjusterRepository().getEntityByName(config.getStrategyAdjusterName());
                multiconfiguredParameters(pluginSA, config.getStrategyAdjusterParameters(), config.getMulticonfigurationParameter()).forEach(
                                params -> {
                                    strategyAdjuster = pluginSA.getNewInstance(params);
                                    configurations.add(createConfiguration());
                                });
                return configurations;
	        case EC_PARAM:
                Plugin<EquilibriumCriterion> pluginEC = CentralRepository.getInstance().getEquilibriumCriterionRepository().getEntityByName(config.getEquilibriumCriterionName());
                multiconfiguredParameters(pluginEC, config.getEquilibriumCriterionParameters(), config.getMulticonfigurationParameter()).forEach(
                                params -> {
                                    equilibriumCriterion = pluginEC.getNewInstance(params);
                                    configurations.add(createConfiguration());
                                });
                return configurations;
	        case MAX_ADAPTS:
	            multiParam.getParameterValues().stream().mapToInt(d -> d.intValue()).forEach(
                        (adapts) -> {
                            maxAdapts = adapts;
                            configurations.add(createConfiguration());
                        });
                return configurations;
            default: //CD_PARAM, SEGMENT_SIZE and GROUP_SIZE
                multiconfiguredPopulations(config.getMulticonfigurationParameter().getType(), config).forEach(
                        (pop) -> {
                            initialiseEngineSegments(pop);
                            configurations.add(createConfiguration());
                        });
                return configurations;
	    }
	}
	
	//TODO exceptions schmeiﬂen (pluginnotfound und pluginparameter)
	private static void configureAllExcept(MulticonfigurationParameterType type, UserConfiguration config) {
	    game = CentralRepository.getInstance().getGameRepository().getEntityByName(config.getGameName());
	    iterationCount = config.getIterationCount();
	    if (type == null || !type.equals(MulticonfigurationParameterType.ROUND_COUNT))
	        roundCount = config.getRoundCount();
	    mixedStrategies = config.getMixedAllowed();
	    if (type == null || !type.equals(MulticonfigurationParameterType.PB_PARAM))
    	    pairBuilder = CentralRepository.getInstance().getPairBuilderRepository()
                .getEntityByName(config.getPairBuilderName()).getNewInstance(config.getPairBuilderParameters());
	    if (type == null || !type.equals(MulticonfigurationParameterType.SQ_PARAM))
    	    successQuantifier = CentralRepository.getInstance().getSuccessQuantifiernRepository()
                .getEntityByName(config.getSuccessQuantifierName()).getNewInstance(config.getSuccessQuantifierParameters());
	    if (type == null || !type.equals(MulticonfigurationParameterType.SA_PARAM))
    	    strategyAdjuster = CentralRepository.getInstance().getStrategyAdjusterRepository()
                .getEntityByName(config.getStrategyAdjusterName()).getNewInstance(config.getStrategyAdjusterParameters());
	    if (type == null || !type.equals(MulticonfigurationParameterType.EC_PARAM))
    	    equilibriumCriterion = CentralRepository.getInstance().getEquilibriumCriterionRepository()
                .getEntityByName(config.getEquilibriumCriterionName()).getNewInstance(config.getEquilibriumCriterionParameters());
	    if (type == null || !type.equals(MulticonfigurationParameterType.MAX_ADAPTS))
    	    maxAdapts = config.getMaxAdapts();
	    
	    if (type == null || !(type.equals(MulticonfigurationParameterType.SEGMENT_SIZE) || type.equals(MulticonfigurationParameterType.GROUP_SIZE)
                || type.equals(MulticonfigurationParameterType.CD_PARAM))) {
	        //initialise segments
	        Population population = CentralRepository.getInstance().getPopulationRepository().getEntityByName(config.getPopulationName());
	        initialiseEngineSegments(population);
	    }
	    
	}
	
	private static void initialiseEngineSegments(Population population) {
        List<Group> groups = population.getGroups();
        engineSegments = new ArrayList<EngineSegment>();
        groups.forEach(group -> group.getSegments().forEach(
                        seg -> engineSegments.add(new EngineSegment(
                            (int) (population.getGroupSize(group) * group.getSegmentSize(seg)),
                            group.isCohesive() ? groups.indexOf(group) : -1,
                            CentralRepository.getInstance().getDiscreteDistributionRepository()
                                .getEntityByName(seg.getCapitalDistributionName()).getNewInstance(seg.getCapitalDistributionParameters()),
                            new UniformFiniteDistribution<Strategy>(
                                    CentralRepository.getInstance().getStrategyRepository().getEntitiesByNames(seg.getStrategyNames()))
                        ))));
	}
	
	private static List<List<Double>> multiconfiguredParameters(Plugin<?> plugin,
	        List<Double> pluginParameterValues, MulticonfigurationParameter multiParam) {
	    List<List<Double>> parameters = new ArrayList<List<Double>>();
	    while (parameters.size() < multiParam.getParameterValues().size()) {
	        parameters.add(new ArrayList<Double>(pluginParameterValues));
	    }
	    
	    int paramIndex = plugin.getParameters().indexOf(
	            plugin.getParameters().stream().filter(p -> p.getName().equals(multiParam.getParameterName())).findAny().get());
	    
	    for (int i = 0; i < multiParam.getParameterValues().size(); i++) {
	        parameters.get(i).set(paramIndex, multiParam.getParameterValues().get(i));
	    }

	    return parameters;
	}
	
	private static List<Population> multiconfiguredPopulations(MulticonfigurationParameterType type, UserConfiguration config)
	        throws ConfigurationException {
	    if (!(type.equals(MulticonfigurationParameterType.SEGMENT_SIZE) || type.equals(MulticonfigurationParameterType.GROUP_SIZE)
	            || type.equals(MulticonfigurationParameterType.CD_PARAM))) {
	        return null;
	    }
	    
        if (!CentralRepository.getInstance().getPopulationRepository().containsEntityName(config.getPopulationName())) {
            throw new PluginNotFoundException(config.getPopulationName());
        }
        Population population = CentralRepository.getInstance().getPopulationRepository().getEntityByName(config.getPopulationName());
	    
	    switch (type) {
	        case GROUP_SIZE: return multiconfiguredPopulationsGroupSize(population, config);
	        case SEGMENT_SIZE: return multiconfiguredPopulationsSegmentSize(population, config);
	        case CD_PARAM: return multiconfiguredPopulationsCD(population, config);
	        default: return null;
	    }
	}
	
	private static List<Population> multiconfiguredPopulationsGroupSize(Population population, UserConfiguration config)
	        throws PluginNotFoundException {
	    List<Population> populations = new ArrayList<Population>(config.getParameterValues().size());
	    while (populations.size() < config.getParameterValues().size()) {
            populations.add(copyPopulation(population));
        }
	    
	    MulticonfigurationParameter param = config.getMulticonfigurationParameter();
	    
	    //determine the index of the group whose size is variable
	    int groupIndex;
	    try {
	        groupIndex = population.getGroups().indexOf(
	                population.getGroups().stream().filter(group -> group.getName().equals(param.getGroupName())).findAny().get());
	    } catch(NoSuchElementException e) {
	        throw new PluginNotFoundException(param.getGroupName());
	    }
	    
	    //replace group sizes in the copies of the population accordingly
	    for (int i = 0; i < param.getParameterValues().size(); i++) {
	        populations.get(i).getGroupSizes().set(groupIndex, param.getParameterValues().get(i).intValue());
	    }
	    
	    return populations;
	}
	
	private static List<Population> multiconfiguredPopulationsSegmentSize(Population population, UserConfiguration config)
	        throws PluginNotFoundException { 
	    List<Population> populations = new ArrayList<Population>(config.getParameterValues().size());
        while (populations.size() < config.getParameterValues().size()) {
            populations.add(copyPopulation(population));
        }
        
        MulticonfigurationParameter param = config.getMulticonfigurationParameter();
        
        //determine the index of the group that contains the segments with variable size
        int groupIndex;
        try {
            groupIndex = population.getGroups().indexOf(
                    population.getGroups().stream().filter(group -> group.getName().equals(param.getGroupName())).findAny().get());
        } catch(NoSuchElementException e) {
            throw new PluginNotFoundException(param.getGroupName());
        }
        
        //replace segment sizes in the copies of the population accordingly
        for (int i = 0; i < param.getParameterValues().size(); i++) {
            Group group = populations.get(i).getGroups().get(groupIndex);
            group.getSegmentSizes().set(0, param.getParameterValues().get(i));
            group.getSegmentSizes().set(1, 1.0 - param.getParameterValues().get(i)); //group only has two segments
        }
        
        return populations;
	}
	
	private static List<Population> multiconfiguredPopulationsCD(Population population, UserConfiguration config)
            throws PluginNotFoundException {
	    List<Population> populations = new ArrayList<Population>(config.getParameterValues().size());
        while (populations.size() < config.getParameterValues().size()) {
            populations.add(copyPopulation(population));
        }
        
        MulticonfigurationParameter param = config.getMulticonfigurationParameter();
        
        //determine the index of the group whose size is variable
        int groupIndex;
        try {
            groupIndex = population.getGroups().indexOf(
                    population.getGroups().stream().filter(group -> group.getName().equals(param.getGroupName())).findAny().get());
        } catch(NoSuchElementException e) {
            throw new PluginNotFoundException(param.getGroupName());
        }
        int segmentIndex = param.getSegmentIndex();
        
        //load capital distribution from the central repository
        String distName = population.getGroups().get(groupIndex).getSegments().get(segmentIndex).getCapitalDistributionName();
        Plugin<DiscreteDistribution> distPlugin = CentralRepository.getInstance().getDiscreteDistributionRepository().getEntityByName(distName);
        
        //determine the index of the variable parameter
        int paramIndex = distPlugin.getParameters().indexOf(
                distPlugin.getParameters().stream().filter(p -> p.getName().equals(param.getParameterName())).findAny().get());
        
        //replace group sizes in the copies of the population accordingly
        for (int i = 0; i < param.getParameterValues().size(); i++) {
            populations.get(i).getGroups().get(groupIndex).getSegments().get(segmentIndex)
                .getCapitalDistributionParameters().set(paramIndex, param.getParameterValues().get(i));
        }
        
        return populations;
    }
	
	private static Population copyPopulation(Population population) {
	    List<Integer> groupSizes = new ArrayList<Integer>(population.getGroupSizes());
	    List<Group> groups = new ArrayList<Group>(population.getGroupCount());
	    population.getGroups().stream().forEach(group -> groups.add(copyGroup(group)));
	    return new Population(population.getName(), population.getDescription(), groups, groupSizes);
	}
	
	private static Group copyGroup(Group group) {
	    List<Double> segmentSizes = new ArrayList<Double>(group.getSegmentSizes());
	    List<Segment> segments = new ArrayList<Segment>(group.getSegmentCount());
	    group.getSegments().stream().forEach(seg -> segments.add(copySegment(seg)));
	    return new Group(group.getName(), group.getDescription(), segments, segmentSizes, group.isCohesive());
	}
	
	private static Segment copySegment(Segment segment) {
	    return new Segment(segment.getCapitalDistributionName(), new ArrayList<Double>(segment.getCapitalDistributionParameters()),
	            new ArrayList<String>(segment.getStrategyNames()));
	}
	
	private static Configuration createConfiguration() {
	    return new Configuration(game, roundCount, mixedStrategies, engineSegments, pairBuilder,
                successQuantifier, strategyAdjuster, equilibriumCriterion, maxAdapts, iterationCount);
	}
	
}
