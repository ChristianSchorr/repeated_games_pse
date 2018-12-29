package loop.model.plugin;

import java.util.List;

/**
 * This class implements the PluginRenderer interface and creates a TextFieldPluginControl
 * instance for any given plugin.
 * 
 * @author Pierre Toussing
 *
 */
public class GenericRenderer implements PluginRenderer{
	private PluginControl control;
	
	/**
	 * Creates a new GenericRenderer for the given plugin
	 * @param plugin: the Plugin that shall be rendered by this GenericRenderer
	 */
	public <T> GenericRenderer(Plugin<T> plugin) {
		control = new TextFieldPluginControl(plugin.getParameters());
	}
	
	/**
	 * Creates a new GenericRenderer for a plugin with the given parameters
	 * @param params: a list with the Parameters of the Plugin that shall be rendered by this
	 * GenericRenderer
	 */
	public GenericRenderer(List<Parameter> params) {
		control = new TextFieldPluginControl(params);
	}
	
	@Override
	public PluginControl renderPlugin() {
		return control;
	}

}
