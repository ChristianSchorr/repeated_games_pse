package loop.model.plugin;

import java.util.List;
import com.google.common.collect.Streams;

/**
 * This class provides functionality for validating a parameter assignment via static auxiliary
 * methods.
 *  
 * @author Pierre Toussing
 *
 */
public class ParameterValidator {
	final static double tolerance = 0.1e-8;	//floating point precision

	/**
	 * Checks if the given value is a valid assignment for the given parameter
	 * @param val: the value that shall be checked
	 * @param param: the parameter whose value range shall be considered
	 * @return true if the given value lies in the value range of the given parameter, false
	 * otherwise
	 */
	static boolean isValueValid(double val, Parameter param) {
		double min = param.getMinValue();
		double max = param.getMaxValue();
		double step = param.getStepSize();
		
		if ((val >= min) && (val <= max)) {		//checks if valid for given bounds
			if (step == 0) {					//no constraint for granularity
				return true;
			}
			else {								//checks if valid for given granularity
				for(double i = min; i <= max; i = Double.sum(i, step)) {
					if (Math.abs(i - val) < tolerance) {
						return true;
					}
				}
			}
		}
		return false;							//default
	}
	
	/**
	 * Checks if the given values are valid assignments to the given parameters.
	 * 
	 * @param values the given values
	 * @param params the given parameters
	 * @return  if the given values are valid assignments to the given parameters
	 */
	public static boolean areValuesValid(List<Double> values, List<Parameter> params) {
	    if (values.size() != params.size()) return false;
	    return Streams.zip(values.stream(), params.stream(), (val, param) -> isValueValid(val, param)).allMatch(b -> b);
	}
	
	/**
	 * Returns the value in the value range of the given parameter that is closest to the
	 * given value.
	 * @param val: the value that shall be considered
	 * @param param: the parameter whose value range shall be considered
	 * @return the value in the value range of the given parameter that is closest to the
	 * given value
	 */
	static double getClosestValid(double val, Parameter param) {
		double min = param.getMinValue();
		double max = param.getMaxValue();
		double step = param.getStepSize();
		
		if (isValueValid(val, param)) {			//already valid
			return val;
		}
		else if (val < min) {					//too low
			return min;
		}
		else if (val > max) {					//too high
			return max;
		}
		else {									//calculate valid value
			double valid = min;
			for(double i = min; i <= max; i += step) {
				if (Math.abs(i - val) <= (step / 2.0)) {
					valid = i;					//best choice
				}
			}
			return valid;
		}
	}
}
