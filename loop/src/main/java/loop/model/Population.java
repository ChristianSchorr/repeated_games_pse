package loop.model;

import java.io.Serializable;
import java.util.List;

import loop.model.Nameable;

/** 
 * This class represents a population. It offers getter methods for size and group composition.
 * 
 * @author Luc Mercatoris
 *
 */

public class Population implements Nameable, Serializable {
	
	private String name;
	private String description;
	private List<Group> groups;
	private List<Integer> groupSizes;
	
	public Population(String name, String description, List<Group> groups, List<Integer> groupSizes) {
		this.name = name;
		this.description = description;
		this.groups = groups;
		this.groupSizes = groupSizes;
	}
	
	/**
	 * Returns the size of this population
	 * 
	 * @return the size of this population
	 */
	public int getSize() {
		int groupSize = 0;
		for (int i : groupSizes) {
			groupSize += i;
		}
		return groupSize;
	}
	
	/**
	 * Returns the groups this population is composed of
	 * 
	 * @return the groups this population is composed of
	 */
	public List<Group> getGroups() {
		return this.groups;
	}
	
	/**
	 * Returns the size of the given group if it is part of this population, 0 otherwise
	 * 
	 * @param group the group whose size shall be returned
	 * @return the size of the given group if it is part of this population, 0 otherwise
	 */
	public int getGroupSize(Group group) {
		if (this.groups.contains(group)) {
			int groupIndex = this.groups.indexOf(group);
			return this.groupSizes.get(groupIndex);
		} else return 0;
	}
	
	/**
	 * Returns the amount of groups in this population
	 * 
	 * @return the amount of groups in this population
	 */
	public int getGroupCount() {
		return this.groups.size();
	}
	
	/**
	 * Returns the name of this population
	 * 
	 * @return the name of this population
	 */
	@Override
	public String getName() {
		return this.name;
	}
	
	/**
	 * Returns the description of this population
	 * 
	 * @return the description of this population
	 */
	@Override
	public String getDescription() {
		return this.description;
	}
	
}
