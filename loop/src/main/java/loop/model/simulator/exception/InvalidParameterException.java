package loop.model.simulator.exception;

/**
 * A {@link ConfigurationException} that is thrown when one of the numerical parameters in a
 * {@link UserConfiguration} given to a {@link Simulator} has an invalid value assigned to it.
 * 
 * @author Christian Schorr
 *
 */
public class InvalidParameterException extends ConfigurationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private double parameterValue;
	private String parameterName;
	
	/**
	 * Creates a new InvalidParameterException.
	 * @param value the value of the faulty parameter
	 * @param name the name of the faulty parameter configuration
	 */
	public InvalidParameterException(double value, String name) {
		super();
		parameterValue = value;
		parameterName = name;
	}
	
	/**
	 * This method returns the value of the faulty parameter.
	 * @return the value of the faulty parameter
	 */
	public double getParameterValue() {
		return parameterValue;
	}
	
	/**
	 * This method returns the name of the faulty parameter.
	 * @return the name of the faulty parameter
	 */
	public String getParameterName() {
		return parameterName;
	}

}
