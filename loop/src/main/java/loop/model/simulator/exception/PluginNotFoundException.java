package loop.model.simulator.exception;

/**
 * This PluginException is thrown when a plugin referenced by a given {@link UserConfiguration}
 * could not be found (i.e. when the name given by the configuration is not known to the repository).
 * 
 * @author Christian Schorr
 *
 */
public class PluginNotFoundException extends PluginException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Creates a new PluginNotFoundException
	 * @param name the name of the plugin that was not found
	 */
	public PluginNotFoundException(String name) {
		pluginName = name;
	}

}
