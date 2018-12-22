/**
 * The model contains the Simulator interface, which provides means of starting und stopping
 * simulations to given configurations. When a simulation is started, an instance of
 * the class SimulationResult is returned instantly, which collects the results of the single
 * iterations as they finish. The execution of the iterations is carried out by instances of the
 * SimulationEngine class.
 * The model contains a central repository which gives static access to all available populations,
 * groups, strategies, games, pair builders, success quantifiers, strategy adjusters,
 * equilibrium criteria and discrete distributions. The access key for the stored instances is
 * their name, wherefore all corresponding classes and interfaces implement the Nameable
 * interface. Populations, groups, strategies, games and configurations can also be exported
 * and imported as files, which is why all of those must be {@link java.io.Serializable}.
 * 
 * The configuration of a simulation, as specified by the user in the configuration window,
 * is represented by the UserConfiguration class. It directly stores the users input, where
 * numerical inputs are represented by integers or doubles and algorithms, populations,
 * strategies etc. are represented as Strings containing the corresponding names. When a
 * simulation to a given UserConfiguration is started, an instance of the Configuration
 * class is generated for each associated elementary configuration. An instance of the
 * Configuration class contains only the information needed to execute a single iteration
 * and holds references to concrete instances of all involved classes (such as the pair builder,
 * the success quantifier and the strategies).
 * 
 * Lastly, the model contains the plugin system which can be used to integrate own implementations
 * of the pair builder, the strategy adjuster, the success quantifier, the equilibrium
 * criterion or the discrete distribution.
*/
/**
 * @author Peter Koepernik
 *
 */
package loop.model;