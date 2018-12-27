package loop.model.simulator.exception;

/**
 * A {@link ConfigurationException} that is thrown when an error associated with one of the 
 * plugins referenced by a given UserConfiguration occured.
 * 
 * @author Christian Schorr
 *
 */
public abstract class PluginException extends ConfigurationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected String pluginName;

	/**
	 * This method returns the name of the plugin that caused the error
	 * @return the name of the plugin that caused the error
	 */
	public String getPluginName() {
		return pluginName;
	}
}
