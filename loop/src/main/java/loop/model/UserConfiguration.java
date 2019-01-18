package loop.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import loop.model.repository.CentralRepository;
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
import loop.model.simulationengine.strategies.RealVector;
import loop.model.simulationengine.strategies.Strategy;

/**
 * This class represents a user-created configuration. It provides getter methods for all
 * parameters as well as static access to a default configuration.
 * 
 * @author Christian Schorr
 *
 */
public class UserConfiguration implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String gameName;
	private int roundCount;
	private int iterationCount;
	private boolean mixedAllowed;
	private String populationName;
	private String pairBuilderName;
	private List<Double> pairBuilderParameters;
	private String successQuantifierName;
	private List<Double> successQuantifierParameters;
	private String strategyAdjusterName;
	private List<Double> strategyAdjusterParameters;
	private String equilibriumCriterionName;
	private List<Double> equilibriumCriterionParameters;
	private int maxAdapts;
	private boolean isMulticonfiguration;
	//private String variableParameterName;
	private MulticonfigurationParameter multiconfigurationParameter;
	//private List<Double> parameterValues;
	
	/**
	 * Creates a new UserConfiguration with the given parameters
	 * @param gameName the name of the game
	 * @param roundCount the amount of rounds per adaptionstep
	 * @param iterationCount the amount of iterations
	 * @param mixedAllowed {@code true}, if mixed strategies are allowed, {@code false} otherwise.
	 * @param populationName the name of the population
	 * @param pairBuilderName the name of the pair builder
	 * @param pairBuilderParameters a list with the values of the parameters of the pair builder
	 * @param successQuantifierName the name of the success quantifier
	 * @param successQuantifierParameters a list with the values of the parameters of the success quantifier
	 * @param strategyAdjusterName the name of the strategy adjuster
	 * @param strategyAdjusterParameters a list with the values of the parameters of the strategy adjuster
	 * @param equilibriumCriterionName the name of the equilibrium criterion
	 * @param equilibriumCriterionParameters a list with the values of the parameters of the equilibrium criterion
	 * @param maxAdapts the maximum amount of simulated adaption steps per iteration
	 * @param isMulticonfiguration {@code true}, if this is a multiconfiguration, {@code false} otherwise
	 * @param multiconfigurationParameter the multiconfiguration parameter, if this is a multiconfiguration,
	 *                                    {@code null} otherwise
	 */
	public UserConfiguration(String gameName, int roundCount, int iterationCount,
			boolean mixedAllowed, String populationName, String pairBuilderName,
			List<Double> pairBuilderParameters, String successQuantifierName, List<Double> successQuantifierParameters,
			String strategyAdjusterName, List<Double> strategyAdjusterParameters, String equilibriumCriterionName,
			List<Double> equilibriumCriterionParameters, int maxAdapts, boolean isMulticonfiguration,
			MulticonfigurationParameter multiconfigurationParameter) {
		
		this.gameName = gameName;
		this.roundCount = roundCount;
		this.iterationCount = iterationCount;
		this.mixedAllowed = mixedAllowed;
		this.populationName = populationName;
		this.pairBuilderName = pairBuilderName;
		this.pairBuilderParameters = pairBuilderParameters;
		this.successQuantifierName = successQuantifierName;
		this.successQuantifierParameters = successQuantifierParameters;
		this.strategyAdjusterName = strategyAdjusterName;
		this.strategyAdjusterParameters = strategyAdjusterParameters;
		this.equilibriumCriterionName = equilibriumCriterionName;
		this.equilibriumCriterionParameters = equilibriumCriterionParameters;
		this.maxAdapts = maxAdapts;
		this.isMulticonfiguration = isMulticonfiguration;
		this.multiconfigurationParameter = multiconfigurationParameter;
	}
	
	/**
	 * Returns a default version of this configuration. The default configuration will be the
	 * active configuration at the start of the program.
	 * @return a default configuration
	 */
	public static UserConfiguration getDefaultConfiguration() {
	    int agentCount = 50;
        int roundCount  = 200;
        int iterationCount = 10;
        int maxAdapts = 10000;
        String pairBuilderName = RandomPairBuilder.NAME;
        List<Double> pairBuilderParameters = new ArrayList<Double>();
        String successQuantifierName = PayoffInLastAdapt.NAME;
        List<Double> successQuantifierParameters = new ArrayList<Double>();
        String strategyAdjusterName = ReplicatorDynamic.NAME;
        List<Double> strategyAdjusterParameters = toList(0.5, 0.5);
        String equilibriumCriterionName = StrategyEquilibrium.NAME;
        double alpha = 0.005;
        int G = 50;
        List<Double> equilibriumCriterionParameters = toList(alpha, (double) G);
        
        String gameName = ConcreteGame.prisonersDilemma().getName();
        boolean mixedStrategies = true;
        
        String capitalDistributionName = DiscreteUniformDistribution.NAME;
        List<Double> distParameters = toList(0.0, 0.0);
        List<String> strategyNames = toList(PureStrategy.alwaysCooperate().getName(), PureStrategy.neverCooperate().getName(),
                PureStrategy.titForTat().getName(), PureStrategy.grim().getName());
        Segment segment = new Segment(capitalDistributionName, distParameters, strategyNames);
        Group group = new Group("DEFAULT_GROUP", "", toList(segment), toList(1.0), false);
        Population population = new Population("DEFAULT_POPULATION", "", toList(group), toList(agentCount));
        CentralRepository.getInstance().getPopulationRepository().addEntity(population.getName(), population);
        
        UserConfiguration defaultConfiguration = new UserConfiguration(gameName, roundCount, iterationCount, mixedStrategies, population.getName(),
                pairBuilderName, pairBuilderParameters, successQuantifierName, successQuantifierParameters, strategyAdjusterName,
                strategyAdjusterParameters, equilibriumCriterionName, equilibriumCriterionParameters, maxAdapts, false, null);
	    
		return defaultConfiguration;
	}
	
	@SuppressWarnings("unchecked")
    private static <T> List<T> toList(T... items) {
        List<T> list = new ArrayList<T>();
        for (int i = 0; i < items.length; i++) {
            list.add(items[i]);
        }
        return list;
    }
	
	/**
	 * Returns the name of the game of this configuration
	 * @return the name of the game of this configuration
	 */
	public String getGameName() {
		return gameName;
	}
	
	/**
	 * Returns the amount of rounds per adaption step in this configuration
	 * @return the amount of rounds per adaption step in this configuration
	 */
	public int getRoundCount() {
		return roundCount;
	}
	
	/**
	 * Returns the amount of iterations in this configuration
	 * @return the amount of iterations in this configuration
	 */
	public int getIterationCount() {
		return iterationCount;
	}
	
	/**
	 * Returns, whether mixed strategies are allowed in this configuration
	 * @return {@code true}, if mixed strategies are allowed in this configuration; {@code false} otherwise
	 */
	public boolean getMixedAllowed() {
		return mixedAllowed;
	}
	
	/**
	 * Returns the name of the {@link Population} of this configuration
	 * @return the name of the {@link Population} of this configuration
	 */
	public String getPopulationName() {
		return populationName;
	}
	
	/**
	 * Returns the name of the {@link PairBuilder} of this configuration
	 * @return the name of the {@link PairBuilder} of this configuration
	 */
	public String getPairBuilderName() {
		return pairBuilderName;
	}
	
	/**
	 * Returns a list with the values of the parameters of the {@link PairBuilder} of this configuration
	 * @return a list with the values of the parameters of the {@link PairBuilder} of this configuration
	 */
	public List<Double> getPairBuilderParameters() {
		return pairBuilderParameters;
	}
	
	/**
	 * Returns the name of the {@link SuccessQuantifier} of this configuration
	 * @return the name of the {@link SuccessQuantifier} of this configuration
	 */
	public String getSuccessQuantifierName() {
		return successQuantifierName;
	}
	
	/**
	 * Returns a list with the values of the parameters of the {@link SuccessQuantifier} of this configuration
	 * @return a list with the values of the parameters of the {@link SuccessQuantifier} of this configuration
	 */
	public List<Double> getSuccessQuantifierParameters() {
		return successQuantifierParameters;
	}
	
	/**
	 * Returns the name of the {@link StrategyAdjuster} of this configuration.
	 * @return the name of the {@link StrategyAdjuster} of this configuration.
	 */
	public String getStrategyAdjusterName() {
		return strategyAdjusterName;
	}
	
	/**
	 * Returns a list with the values of the parameters of the {@link StrategyAdjuster} of this configuration
	 * @return a list with the values of the parameters of the {@link StrategyAdjuster} of this configuration
	 */
	public List<Double> getStrategyAdjusterParameters() {
		return strategyAdjusterParameters;
	}
	
	/**
	 * Returns the name of the {@link EquilibriumCriterion} of this configuration.
	 * @return the name of the {@link EquilibriumCriterion} of this configuration.
	 */
	public String getEquilibriumCriterionName() {
		return equilibriumCriterionName;
	}
	
	/**
	 * Returns a list with the values of the parameters of the {@link EquilibriumCriterion} of this configuration
	 * @return a list with the values of the parameters of the {@link EquilibriumCriterion} of this configuration
	 */
	public List<Double> getEquilibriumCriterionParameters() {
		return equilibriumCriterionParameters;
	}
	
	/**
	 * Returns the maximum amount of simulated adaption steps per iteration in this configuration
	 * @return the maximum amount of simulated adaption steps per iteration in this configuration
	 */
	public int getMaxAdapts() {
		return maxAdapts;
	}
	
	/**
	 * Returns whether this is a multiconfiguration.
	 * @return {@code true}, if this is a multiconfiguration, {@code false} otherwise
	 */
	public boolean isMulticonfiguration() {
		return isMulticonfiguration;
	}
	
	/**
	 * Returns the multiconfiguration parameter, if this is a multiconfiguration, {@code null} otherwise.
	 * 
	 * @return the multiconfiguration parameter, if this is a multiconfiguration, {@code null} otherwise
	 */
	public MulticonfigurationParameter getMulticonfigurationParameter() {
	    return this.multiconfigurationParameter;
	}
	
	/**
	 * Returns the name of the multiconfiguration parameter of this configuration, if this
	 * is a multiconfiguration, "" otherwise.
	 * @return the name of the multiconfiguration parameter of this configuration, if this
	 * is a multiconfiguration, "" otherwise.
	 */
	public String getVariableParameterName() {
		return this.multiconfigurationParameter.getParameterName();
	}
	
	/**
	 * Returns the values of the multiconfiguration parameter if this is a multiconfiguration,
	 * {@code null} otherwise
	 * @return the values of the multiconfiguration parameter if this is a multiconfiguration,
     *         {@code null} otherwise
	 */
	public List<Double> getParameterValues() {
	    return this.multiconfigurationParameter.getParameterValues();
	}
}