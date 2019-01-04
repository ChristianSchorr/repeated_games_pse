package loop.model.simulator;

import java.util.List;

import loop.model.UserConfiguration;
import loop.model.simulationengine.Configuration;
import loop.model.simulator.exception.ConfigurationException;

/**
 * This class takes a {@link UserConfiguration} and generates all associated
 * elementary configurations. These are returned as {@link Configuration}s. It
 * also provides functionality to create a deep copy of a {@link Configuration}
 * .
 * 
 * @author Christian Schorr
 *
 */
public class ConfigurationCreator {

	/**
	 * Generates all associated elementary configurations to the given
	 * {@link UserConfiguration} and returns them as {@link Configuration}s.
	 * 
	 * @param config the {@link UserConfiguration} whose associated elementary
	 *               configurations shall be generated
	 * @return all associated elementary configurations of the given
	 *         {@link UserConfiguration} as {@link Configuration}s
	 *
	 * @throws ConfigurationException {@link ConfigurationException} when the
	 *                                provided configuration is faulty
	 */
	public static List<Configuration> generateConfigurations(UserConfiguration config) throws ConfigurationException {
		return null;

	}


	/**
	 * Provides a deep copy of the given configuration.
	 * 
	 * @param config the configuration to copy
	 * @return the newly created deep copy of the given configuration
	 */
	public static Configuration getCopy(Configuration config) {
		return null;
	}
}
