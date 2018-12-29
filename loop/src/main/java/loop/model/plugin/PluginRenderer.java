package loop.model.plugin;

/**
 * This interface is an abstract factory that creates PluginControls for some plugin. It is
 * supplied by an instance of the Plugin class
 *
 * @author Pierre Toussing
 *
 */
public interface PluginRenderer {
	
	/**
	 * Returns a PluginControl instance.
	 * @return a PluginControl instance
	 */
	public PluginControl renderPlugin();
}
