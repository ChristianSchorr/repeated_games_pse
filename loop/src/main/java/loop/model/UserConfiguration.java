package loop.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import loop.model.simulationengine.strategies.RealVector;

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
	private int agentCount;
	private int roundCount;
	private int iterationCount;
	private List<String> availableStrategyNames;
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
	private double startValue;
	private double endValue;
	private double stepSize;
	
	/**
	 * Creates a new UserConfiguration with the given parameters
	 * @param gameName the name of the game
	 * @param agentCount the amount of agents
	 * @param roundCount the amount of rounds per adaptionstep
	 * @param iterationCount the amount of iterations
	 * @param availableStrategyNames a list with the names of all allowed strategies
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
	public UserConfiguration(String gameName, int agentCount, int roundCount, int iterationCount,
			List<String> availableStrategyNames, boolean mixedAllowed, String populationName, String pairBuilderName,
			List<Double> pairBuilderParameters, String successQuantifierName, List<Double> successQuantifierParameters,
			String strategyAdjusterName, List<Double> strategyAdjusterParameters, String equilibriumCriterionName,
			List<Double> equilibriumCriterionParameters, int maxAdapts, boolean isMulticonfiguration,
			MulticonfigurationParameter multiconfigurationParameter) {
		
		this.gameName = gameName;
		this.agentCount = agentCount;
		this.roundCount = roundCount;
		this.iterationCount = iterationCount;
		this.availableStrategyNames = availableStrategyNames;
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
		
		UserConfiguration config = null;// new UserConfiguration("", 100, 100, 10, null, false, "", "", null, "", null, "", null, "", null,
														// 100, false, "", 0, 0, 0);
		return config;
	}
	
	/**
	 * Returns the name of the game of this configuration
	 * @return the name of the game of this configuration
	 */
	public String getGameName() {
		return gameName;
	}
	
	/**
	 * Returns the amount of agents in this configuration
	 * @return the amount of agents in this configuration
	 */
	public int getAgentCount() {
		return agentCount;
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
	 * Returns a list with names of all available strategies in this configuration
	 * @return a list with names of all available strategies in this configuration
	 */
	public List<String> getAvailableStrategyNames() {
		return availableStrategyNames;
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
	
	/**
	 * Returns the starting value of the multiconfiguration parameter of this configuration,
	 * if this is a multiconfiguration, {@code 0} otherwise.
	 * @return the starting value of the multiconfiguration parameter of this configuration,
	 * if this is a multiconfiguration, {@code 0} otherwise.
	 */
	public double getStartValue() {
		return startValue;
	}
	
	/**
	 * Returns the end value of the multiconfiguration parameter of this configuration, if
	 * this is a multiconfiguration, {@code 0} otherwise.
	 * @return the end value of the multiconfiguration parameter of this configuration, if
	 * this is a multiconfiguration, {@code 0} otherwise.
	 */
	public double getEndValue() {
		return endValue;
	}
	
	/**
	 * Returns the step size of the multiconfiguration parameter of this configuration, if
	 * this is a multiconfiguration, {@code 0} otherwise.
	 * @return the step size of the multiconfiguration parameter of this configuration, if
	 * this is a multiconfiguration, {@code 0} otherwise.
	 */
	public double getStepSize() {
		return stepSize;
	}
}