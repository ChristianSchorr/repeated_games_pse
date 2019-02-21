package loop;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import loop.model.repository.CentralRepository;
import loop.model.repository.FileIO;
/**
 * This class represents some global settings for the loop programm
 * @author Pierre Toussing
 *
 */
public class LoopSettings implements Serializable {
	private boolean enable_tooltip = true;
	private boolean enable_notification = true;
	private boolean reserveThread = true;
	private int threadcount = Runtime.getRuntime().availableProcessors();
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
		this.personalURLs = new LinkedList<String>();
	}
	
	public boolean isReserveThread() {
		return reserveThread;
	}

	public void setReserveThread(boolean reserveThread) {
		this.reserveThread = reserveThread;
	}

	public boolean isEnable_tooltip() {
		return enable_tooltip;
	}
	public void setEnable_tooltip(boolean enable_tooltip) {
		this.enable_tooltip = enable_tooltip;
	}
	public boolean isEnable_notification() {
		return enable_notification;
	}
	public void setEnable_notification(boolean enable_notification) {
		this.enable_notification = enable_notification;
	}
	public int getThreadcount() {
		return threadcount;
	}
	public void setThreadcount(int threadcount) {
		this.threadcount = threadcount;
	}
	
	public void addURL(String url) {
		personalURLs.add(url);
	}
	
	public void deleteURL(String url) {
		personalURLs.remove(url);
	}
	
	public List<String> getPersonalURLs() {
		return personalURLs;
	}
	
	public void save() {
		try {
			FileIO.saveEntity(new File(FileIO.SETTINGS_DIR + "/currentSettings") , instance);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
