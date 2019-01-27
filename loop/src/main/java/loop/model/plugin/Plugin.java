package loop.model.plugin;

import java.util.List;

import loop.model.Nameable;
import loop.model.simulationengine.EquilibriumCriterion;
import loop.model.simulationengine.PairBuilder;
import loop.model.simulationengine.StrategyAdjuster;
import loop.model.simulationengine.SuccessQuantifier;
import loop.model.simulationengine.distributions.DiscreteDistribution;

/**
 * This class represents a dynamically loadable plugin. It is a generic container for another
 * class that implements the actual plugin functionality. It provides methods for creating
 * new parametrised instances of the wrapped class. A list of required configuration parameters can be queried.
 * 
 * @author Pierre Toussing
 *
 * @param <T> The type of the class that implements the plugin functionality.
 */
public abstract class Plugin<T> implements Nameable {
	
	/**
	 * Returns a PluginRenderer for this plugin
	 * @return a PluginRenderer for this plugin
	 */
	public PluginRenderer getRenderer() {
	    return new GenericRenderer(getParameters());
	}
	
	/**
	 * Returns a list of the configuration parameters of this plugin
	 * @return a list of the configuration parameters of this plugin
	 */
	public abstract List<Parameter> getParameters();
	
	/**
	 * Creates and returns a new instance of the functionality holding class of this plugin
	 * with the given values as parameters
	 * @param params: a list with the values of the configuration parameters
	 * @return the newly created and parametrised instance of the funcitonaliy holding
	 * class
	 */
	public abstract T getNewInstance(List<Double> params);

	public abstract class PairBuilderPlugin extends Plugin<PairBuilder> {}
	public abstract class EquilibriumCriterionPlugin extends Plugin<EquilibriumCriterion> {}
	public abstract class StrategyAdjusterPlugin extends Plugin<StrategyAdjuster> {}
	public abstract class SuccessQuantifierPlugin extends Plugin<SuccessQuantifier> {}
	public abstract class DiscreteDistributionPlugin extends Plugin<DiscreteDistribution> {}
}
