/**
 * This package contains the central repository, through which all currently
 * available games, strategies, populations as well as pairing algorithms,
 * success quantifications, adaptation mechanisms, equilibrium criteria and
 * discrete distributions can be obtained. The reposi- tory does not distinguish
 * between algorithms and mechanisms that were integrated per plugin and such
 * that are present by default. This is because the default implementa- tions of
 * the interfaces that may be extended per plugin are also stored in
 * corresponding Plugin<T> instances that are instanciated at program start. The
 * CentralRepository singleton class is the programs central access point to all
 * avail- able games, strategies, etc. Upon initialisation, it creates an
 * instance of the Repository<T> interface for each type T of stored objects (
 * Game , Population , Plugin<PairBuilder> ,...) and loads all plugins, games,
 * strategies, populations and groups using the PluginLoader and the FileIO
 * respectively. The loaded instances are then stored in the corresponding
 * repositories. Whenever the user loads a game, strategy, population or game
 * from a file, the FileIO is used to extract the object from the file and the
 * CentralRepository then stores that object in the corresponding repository.
 * The FileIO is also used whenever the user exports a game, strategy,
 * population or group as a file during runtime.
 * 
 * @author Christian Schorr
 *
 */
package loop.model.repository;