package loop.model.plugin;

import loop.model.Nameable;

/**
 * This class defines a configuration parameter for plugins. It provides functionality to set
 * the value range and granularity of the configuration parameter.
 * 
 * @author Pierre Toussing
 *
 */

public abstract class Parameter implements Nameable{
	
	private double minVal, maxVal, stepSize;

	/**
	 * Creates a new unbounded Parameter.
	 */
	public Parameter() {
		this.minVal = Double.NEGATIVE_INFINITY;
		this.maxVal = Double.POSITIVE_INFINITY;
		this.stepSize = 0;
	}
	
	/**
	 * Creates a new bounded Parameter
	 * @param minVal: the lower bound of the Parameter
	 * @param maxVal: the upper bound of the Parameter
	 */
	public Parameter(double minVal, double maxVal) {
		this.minVal = minVal;
		this.maxVal = maxVal;
		this.stepSize = 0;
	}
	
	/**
	 * Creates a new unbounded Parameter with constricted granularity
	 * @param minVal: the lower bound of the Parameter
	 * @param maxVal: the upper bound of the Parameter
	 * @param stepSize: the granularity of the parameter
	 */
	public Parameter(double minVal, double maxVal, double stepSize) {
		this.minVal = minVal;
		this.maxVal = maxVal;
		this.stepSize = stepSize;
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
}
