package loop.model.repository;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * This class provides static functionality for loading and storing entities in files.
 * 
 * @author Pierre Toussing
 *
 */
public class FileIO {

	/**
	 * Loads an entity of type T out of the given file
	 * @param file the file from which the entity shall be loaded
	 * @return the loaded entity or null if no entity could be loaded
	 * @throws FileNotFoundException when the given file doesn’t exist
	 * @throws IOException when the entity can not be deserialized
	 */
	@SuppressWarnings("unchecked")
	static <T> T loadEntity(File file) throws FileNotFoundException, IOException {
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
	 * @throws FileNotFoundException when the given directory doesn’t exist
	 */
	static <T> List<T> loadAllEntities(File dir) throws FileNotFoundException {
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
					e.printStackTrace();
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
	 * doesn’t exist and can not be created
	 */
	static <T> void saveEntity(File file, T entity) throws IOException {
		FileOutputStream fos = new FileOutputStream (file);
		@SuppressWarnings("resource")
		ObjectOutputStream oos = new ObjectOutputStream (fos);
		oos.writeObject(entity);		
	}
	
	/**
	 * Pushes all Files in a directory on the stack
	 * @param s stack on which the files are pushed
	 * @param dir directory to search for files
	 */
	static private void pushFilesOnStack(Stack<File> s, File dir) {
		for(int i = 0; i < dir.listFiles().length; i++) {
			s.push(dir.listFiles()[i]);
		}
	}
}
