package loop.model.plugin;

import loop.model.Nameable;

/**
 * This class defines a configuration parameter for plugins. It provides functionality to set
 * the value range and granularity of the configuration parameter.
 * 
 * @author Pierre Toussing
 *
 */

public class Parameter implements Nameable {
	
	private double minVal, maxVal, stepSize;
	private String name, description;

	/**
	 * Creates a new unbounded Parameter.
	 * @param name: The name of the parameter
	 * @param name: The description of the parameter
	 */
	public Parameter(String name, String description) {
		this.minVal = Double.NEGATIVE_INFINITY;
		this.maxVal = Double.POSITIVE_INFINITY;
		this.stepSize = 0;
		this.name = name;
		this.description = description;
	}
	
	/**
	 * Creates a new bounded Parameter
	 * @param minVal: the lower bound of the Parameter
	 * @param maxVal: the upper bound of the Parameter
	 * @param name: The name of the parameter
	 * @param name: The description of the parameter
	 */
	public Parameter(double minVal, double maxVal, String name, String description) {
		this.minVal = minVal;
		this.maxVal = maxVal;
		this.stepSize = 0;
		this.name = name;
		this.description = description;
	}
	
	/**
	 * Creates a new unbounded Parameter with constricted granularity
	 * @param minVal: the lower bound of the Parameter
	 * @param maxVal: the upper bound of the Parameter
	 * @param stepSize: the granularity of the parameter
	 * @param name: The name of the parameter
	 * @param name: The description of the parameter
	 */
	public Parameter(double minVal, double maxVal, double stepSize, String name, String description) {
		this.minVal = minVal;
		this.maxVal = maxVal;
		this.stepSize = stepSize;
		this.name = name;
		this.description = description;
	}

	/**
	 * Returns the lower bound of this Parameter.
	 * @return the lower bound of this Parameter (Double.NEGATIVE_INFINITY if unbounded)
	 */
	public double getMinValue() {
		return minVal;
	}

	/**
	 * Returns the upper bound of this Parameter.
	 * @return the upper bound of this Parameter (Double.POSITIVE_INFINITY if unbounded)
	 */
	public double getMaxValue() {
		return maxVal;
	}

	/**
	 * Returns the granularity of this parameter
	 * @return the granularity of this parameter (0 if no constraint)
	 */
	public double getStepSize() {
		return stepSize;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getDescription() {
		return description;
	}
}
