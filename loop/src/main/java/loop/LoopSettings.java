package loop;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import loop.model.repository.FileIO;
/**
 * This class represents some global and persistent settings for the loop programm
 * @author Pierre Toussing
 *
 */
public class LoopSettings implements Serializable {
	
	private static final long serialVersionUID = 4601976642416292878L;
	private boolean enable_tooltip;
	private boolean enable_notification;
	private boolean reserveThread;
	private int threadcount;
	private List<String> personalURLs;
	
	private static LoopSettings instance;
	
	/**
	 * Returns the singleton instance
	 * 
	 * @return the singleton instance
	 */
	public static LoopSettings getInstance() {
		if (instance == null) {
				instance = new LoopSettings();
				try {
					instance = FileIO.loadEntity(new File(FileIO.SETTINGS_DIR + "/currentSettings"));
				} catch (Exception e) {
					//Not available yet
				}			
		}
		return instance;
	}
	
	private LoopSettings() {
		this.enable_notification = true;
		this.enable_tooltip = true;
		this.reserveThread = true;
		this.threadcount = Runtime.getRuntime().availableProcessors();
		this.personalURLs = new LinkedList<String>();
	}
	
	/**
	 * Returns if one thread should be reserved for the GUI
	 * @return true if one thread should be reserved for the GUI
	 */
	public boolean isReserveThread() {
		return reserveThread;
	}

	/**
	 * Sets one thread should be reserved for the GUI
	 * @param reserveThread true if one thread should be reserved for the GUI
	 */
	public void setReserveThread(boolean reserveThread) {
		this.reserveThread = reserveThread;
	}

	/**
	 * Returns if tooltips are enabled
	 * @return true if tooltips are enabled
	 */
	public boolean isEnable_tooltip() {
		return enable_tooltip;
	}
	
	/**
	 * Sets tooltips enabled 
	 * @param enable_tooltip enable tooltips
	 */
	public void setEnable_tooltip(boolean enable_tooltip) {
		this.enable_tooltip = enable_tooltip;
	}
	
	/**
	 * Returns if notifications are enabled
	 * @return true if notifications are enabled
	 */
	public boolean isEnable_notification() {
		return enable_notification;
	}
	
	/**
	 * Sets notifications enabled
	 * @param enable_notification enable notifications
	 */
	public void setEnable_notification(boolean enable_notification) {
		this.enable_notification = enable_notification;
	}
	
	/**
	 * Returns the number of threads available 
	 * @return the number of threads available
	 */
	public int getThreadcount() {
		return threadcount;
	}
	
	/**
	 * Sets the number of threads used in the programm
	 * @param threadcount the number of threads 
	 */
	public void setThreadcount(int threadcount) {
		this.threadcount = threadcount;
	}
	
	/**
	 * Adds a personal url to the list
	 * @param url personal url
	 */
	public void addURL(String url) {
		personalURLs.add(url);
	}
	
	/**
	 * Deletes an url from the list
	 * @param url url to delete
	 */
	public void deleteURL(String url) {
		personalURLs.remove(url);
	}
	
	/**
	 * Returns a list of all urls
	 * @return a list of all urls
	 */
	public List<String> getPersonalURLs() {
		return personalURLs;
	}
	
	/**
	 * Saves the settings  
	 */
	public void save() {
		try {
			FileIO.saveEntity(new File(FileIO.SETTINGS_DIR + "/currentSettings") , instance);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
