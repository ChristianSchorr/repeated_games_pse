package loop.model.simulationengine;

import java.util.List;

/**
 * This class represents an elementary conguration and contains only the information needed
 * to execute a single iteration:
 * - the game
 * - rounds per adaption step
 * - whether mixed strategies are allowed
 * - the segments
 * - the pair builder
 * - the success quantifier
 * - the strategy adjuster
 * - the equilibrium criterion
 * - the maximum number of adaption steps
 * 
 * @author Peter Koepernik
 *
 */
public class Configuration {
    
    private Game game;
    private int roundCount;
    private boolean mixedAllowed;
    private List<EngineSegment> segments;
    private PairBuilder pairBuilder;
    private SuccessQuantifier successQuantifier;
    private StrategyAdjuster strategyAdjuster;
    private EquilibriumCriterion equilibriumCriterion;
    private int maxAdapts;
    
    /**
     * Creates a new configuration with the given parameters.
     * 
     * @param game the game
     * @param roundCount the rounds per adaption step
     * @param mixedAllowed whether mixed strategies are allowed
     * @param segments the segments
     * @param pairBuilder the pair builder
     * @param successQuantifier the success qauntifier
     * @param strategyAdjuster the strategy adjuster
     * @param equilibriumCriterion the equilibrium criterion
     * @param maxAdapts the maximum amount of adaption steps
     */
    public Configuration(Game game, int roundCount, boolean mixedAllowed, List<EngineSegment> segments, PairBuilder pairBuilder,
            SuccessQuantifier successQuantifier, StrategyAdjuster strategyAdjuster, EquilibriumCriterion equilibriumCriterion,
            int maxAdapts) {
        this.game = game;
        this.roundCount = roundCount;
        this.mixedAllowed = mixedAllowed;
        this.segments = segments;
        this.pairBuilder = pairBuilder;
        this.successQuantifier = successQuantifier;
        this.strategyAdjuster = strategyAdjuster;
        this.equilibriumCriterion = equilibriumCriterion;
        this.maxAdapts = maxAdapts;
    }
    
    /**
     * Returns the game of this configuration.
     * 
     * @return the game of this configuration
     */
    public Game getGame() {
        return this.game;
    }
    
    /**
     * Returns the amount of rounds per adaption step.
     * 
     * @return the amount of rounds per adaption step
     */
    public int getRoundCount() {
        return this.roundCount;
    }
    
    /**
     * Returns whether mixed strategies are allowed in this configuration.
     * 
     * @return {@code true} if mixed strategies are allowed in this configuration, {@code false} otherwise
     */
    public boolean allowsMixedStrategies() {
        return this.mixedAllowed;
    }
    
    /**
     * Returns the segments of this configuration.
     * 
     * @return the segments of this configuration
     */
    public List<EngineSegment> getSegments() {
        return this.segments;
    }
    
    /**
     * Returns the pair builder of this configuration.
     * 
     * @return the pair builder of this configuration
     */
    public PairBuilder getPairBuilder() {
        return this.pairBuilder;
    }
    
    /**
     * Returns the success quantifier of this configuration.
     * 
     * @return the success quantifier of this configuration
     */
    public SuccessQuantifier getSuccessQuantifier() {
        return this.successQuantifier;
    }
    
    /**
     * Returns the strategy adjuster of this configuration.
     * 
     * @return the strategy adjuster of this configuration
     */
    public StrategyAdjuster getStrategyAdjuster() {
        return this.strategyAdjuster;
    }
    
    /**
     * Returns the equilibrium criterion of this configuration.
     * 
     * @return the equilibrium criterion of this configuration
     */
    public EquilibriumCriterion getEquilibriumCriterion() {
        return this.equilibriumCriterion;
    }
    
    /**
     * Returns the maximum amount of simulated adaption steps per iteration in this configuration.
     * 
     * @return the maximum amount of simulated adaption steps per iteration in this configuration
     */
    public int getMaxAdapts() {
        return this.maxAdapts;
    }
}
