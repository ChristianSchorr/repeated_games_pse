package loop.model.plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

/**
 * This class provides the functionality to dynamically load plugins at run time using the
 * Java ServiceLoader-API. It is invoked by the HeadController at the start of the program
 * to load all plugins.
 * 
 * @author Pierre Toussing
 *
 */
public class PluginLoader {

	/**
	 * Loads all available plugins of the given type T.
	 * @return a list of the loaded plugins
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	static <T> List<T> loadPlugins() {
		ServiceLoader<Plugin> loader = ServiceLoader.load(Plugin.class);
		ArrayList<T> plugins = new ArrayList<T>();
		while (loader.iterator().hasNext()) {
			plugins.add((T) loader.iterator().next());
		}
		return plugins;		
	}
}
