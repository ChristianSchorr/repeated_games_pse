package loop.model.simulator.exception;

import java.util.List;

/**
 * This PluginException is thrown when one of the plugin configuration parameters 
 * provided by a given {@link UserConfiguration} is faulty.
 * 
 * @author Christian Schorr
 *
 */
public class PluginConfigurationException extends PluginException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<Double> params;
	
	/**
	 * Creates a new PluginConfigurationException
	 * @param name the name of the plugin
	 * @param parameters a list of all faulty configuration parameters
	 */
	public PluginConfigurationException(String name, List<Double> parameters) {
		pluginName = name;
		params = parameters;
	}
	
	/**
	 * This method returns a list of all faulty parameters
	 * @return a list of all faulty parameters
	 */
	public List<Double> getParameter() {
		return params;
	}
}
