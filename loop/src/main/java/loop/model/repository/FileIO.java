package loop.model.repository;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import loop.model.simulationengine.SimulationHistory;
import loop.model.simulationengine.strategies.Strategy;
import loop.model.simulator.SimulationResult;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * This class provides static functionality for loading and storing entities in files.
 * 
 * @author Pierre Toussing
 *
 */
public class FileIO {

	private final static String currentUsersDir = System.getProperty("user.home") + "/loop/personallib";
	
	public final static File USER_CONFIG_DIR = new File(currentUsersDir + "/configurations");
	public final static File GAME_DIR = new File(currentUsersDir + "/games");
	public final static File STRATEGY_DIR = new File(currentUsersDir + "/strategies");
	public final static File GROUP_DIR = new File(currentUsersDir + "/groups");
	public final static File POPULATION_DIR = new File(currentUsersDir + "/populations");
	public final static File SIMULATIONRESULTS_DIR = new File(currentUsersDir + "/simulationresults");
	public final static File SETTINGS_DIR = new File(currentUsersDir + "/settings");

	/**
	 * Creates the directories necessary to save and load objects in loop.
	 */
	public static void initializeDirectories() {
		String[] directories = {"/configurations" , "/games" , "/strategies" ,
				"/populations" , "/simulationresults" , "/groups" , "/settings"
		};
		File file;
		for(String s: directories) {
			file = new File(currentUsersDir + s);
			file.mkdirs();
		}
	}
	

	/**
	 * Loads an entity of type T out of the given file
	 * @param file the file from which the entity shall be loaded
	 * @return the loaded entity or null if no entity could be loaded
	 * @throws FileNotFoundException when the given file doesn�t exist
	 * @throws IOException when the entity can not be deserialized
	 */
	@SuppressWarnings("unchecked")
	public static <T> T loadEntity(File file) throws FileNotFoundException, IOException {
		T entity = null;
		FileInputStream fis = new FileInputStream (file);
		@SuppressWarnings("resource")
		ObjectInputStream ois = new ObjectInputStream (fis);			  
		try {
			entity = (T) ois.readObject ();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}			
		return entity;
	}
	
	/**
	 * Loads all entities of type T from files in the given directory
	 * @param dir: the directory from which the entities shall be loaded
	 * @return a list of all loaded entities
	 * @throws FileNotFoundException when the given directory doesn�t exist
	 */
	public static <T> List<T> loadAllEntities(File dir) throws FileNotFoundException {
		ArrayList<T> list = new ArrayList<T>();
		Stack<File> files = new Stack<File>();
		pushFilesOnStack(files, dir);
	    while (!files.empty()) {
	    	File f = files.pop();
	    	if (f.isDirectory()) {
	    		pushFilesOnStack(files, f);
	    	}
	    	else {
	    		try {
					list.add(FileIO.loadEntity(f));
				} catch (IOException e) {
					System.err.println("Invalid file : " + f.getAbsolutePath());
				}
	    	}
	    }
	    if (list.isEmpty()) {
	    	return null;
	    }
	    else {
	    	return list;
	    }
	}
	
	/**
	 * Saves the given entity in the given file.
	 * @param file the file to which the entity shall be saved
	 * @param entity: the entity that shall be saved
	 * @return true if the saving process finished successfully, false otherwise
	 * @throws IOException when the given entity can not be serialized or the given file
	 * doesn�t exist and can not be created
	 */
	public static <T> void saveEntity(File file, T entity) throws IOException {
		FileOutputStream fos = new FileOutputStream (file);
		@SuppressWarnings("resource")
		ObjectOutputStream oos = new ObjectOutputStream (fos);
		oos.writeObject(entity);
	}
	
	/**
	 * Saves a {@link SimulationResult} object in Json format
	 * @param result the SimulationResult which shall be saved
	 * @param file the file to which the SimulationResult shall be saved
	 */
	public static void saveResult(SimulationResult result, File file) {
		//Create our gson instance
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(Strategy.class, new StrategySerializer());
		builder.registerTypeAdapter(SimulationHistory.class, new HistorySerializer());
		Gson gson = builder.create();
		String save = gson.toJson(result);
		try (FileWriter filewriter = new FileWriter(file)) {
			filewriter.write(save);
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	/**
	 * Loads a {@link SimulationResult} object (in Json format) out of a text file
	 * @param file the file from which the SimulationResult shall be loaded
	 * @return the loaded SimulationResult
	 */
	public static SimulationResult loadResult(File file) {
		String load = "";
		String zeile;
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(Strategy.class, new StrategySerializer());
        builder.registerTypeAdapter(SimulationHistory.class, new HistorySerializer());
		Gson gson = builder.create();
		try (BufferedReader bf = new BufferedReader(new FileReader(file))) {
			while( (zeile = bf.readLine()) != null )
		    {
		      load += zeile;
		    }
		} catch (IOException e) {
			e.printStackTrace();
		}	
		return gson.fromJson(load, SimulationResult.class);
	}
	
	/**
	 * Pushes all Files in a directory on the stack
	 * @param s stack on which the files are pushed
	 * @param dir directory to search for files
	 */
	private static void pushFilesOnStack(Stack<File> s, File dir) {
		for(int i = 0; i < dir.listFiles().length; i++) {
			s.push(dir.listFiles()[i]);
		}
	}
}
