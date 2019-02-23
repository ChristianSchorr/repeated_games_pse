package loop;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Tooltip;
import javafx.util.Duration;
import loop.model.UserConfiguration;
import loop.model.repository.CentralRepository;
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
	private int threadCount;
	private List<String> personalURLs;
	private UserConfiguration configuration;
	
	private static LoopSettings instance;
	
	private static CentralRepository repo = CentralRepository.getInstance();
	
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
		hackTooltip(true);
		this.enable_notification = true;
		this.enable_tooltip = true;
		this.threadCount = Runtime.getRuntime().availableProcessors() - 1;
		this.personalURLs = new LinkedList<String>();
		this.configuration = UserConfiguration.getDefaultConfiguration();
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
		hackTooltip(enable_tooltip);
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
	public int getThreadCount() {
		return threadCount;
	}
	
	/**
	 * Returns the selected default configuration
	 * @return the selected default configuration
	 */
	public UserConfiguration getConfiguration() {
		return configuration;
	}

	/**
	 * Sets the default configuration
	 * @param configuration the default configuration
	 */
	public void setConfiguration(UserConfiguration configuration) {
		this.configuration = configuration;
	}

	/**
	 * Sets the number of threads used in the programm
	 * @param threadCount the number of threads
	 */
	public void setThreadCount(int threadCount) {
		this.threadCount = threadCount;
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



	private static void hackTooltip(boolean enable) {
		Tooltip tooltip = new Tooltip();
		try {
			Field fieldBehavior = tooltip.getClass().getDeclaredField("BEHAVIOR");
			fieldBehavior.setAccessible(true);
			Object objBehavior = fieldBehavior.get(tooltip);

			Field fieldTimer = objBehavior.getClass().getDeclaredField("activationTimer");
			fieldTimer.setAccessible(true);
			Timeline objTimer = (Timeline) fieldTimer.get(objBehavior);

			objTimer.getKeyFrames().clear();
			objTimer.getKeyFrames().add(new KeyFrame(new Duration(enable ? 500 : 50000000)));

			Field hideTimer = objBehavior.getClass().getDeclaredField("hideTimer");
			hideTimer.setAccessible(true);
			objTimer = (Timeline) hideTimer.get(objBehavior);

			objTimer.getKeyFrames().clear();
			objTimer.getKeyFrames().add(new KeyFrame(new Duration(10000)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
