package loop.model.plugin;

import java.util.List;

/**
 * This class serves as a control element for the configuration of a plugin 
 * that can be dynamically embedded into the UI. Upon request, it returns the entered values of the parameters.
 * 
 * If the user selects an algorithm or mechanism that was integrated per plugin in the configuration window,
 * the corresponding PluginControl object will be inserted below the
 * corresponding dropdown menu to configurate the parameters of the plugin.

 * @author Pierre Toussing
 *
 */
public abstract class PluginControl extends javafx.scene.layout.Pane{

	/**
	 * Returns a list of the entered parameter values.
	 * @return a list of the entered parameter values
	 */
	public abstract List<Double> getParameters();
}
