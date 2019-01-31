package loop.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import loop.model.Nameable;

/** 
 * This class represents a population. It offers getter methods for size and group composition.
 * 
 * @author Luc Mercatoris
 *
 */

public class Population implements Nameable, Serializable {
	   
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String description;
	private List<String> groupNames;
	private List<Integer> groupSizes;
	
	public Population(String name, String description, List<String> groupNames, List<Integer> groupSizes) {
	    if (groupNames.size() != groupSizes.size()) {
            throw new IllegalArgumentException("Invalid parameters in creation of new population: more or less groups given then group sizes.");
        }
	    
	    //check for duplicates
	    Map<String, Boolean> hashMap = new HashMap<String, Boolean>();
	    boolean duplicate = false;
	    for (String s: groupNames) {
	        if (hashMap.putIfAbsent(s, true) != null) {
	            duplicate = true;
	        }
	    }
	    if (duplicate) {
	        throw new IllegalArgumentException("A group cannot be contained more than once in a population.");
	    }
	    
	    int sizeSum = 0;
        for (Integer a: groupSizes) {
            if (a < 1)
                throw new IllegalArgumentException("Invalid parameters in creation of new population: groups must have sizes larger than zero.");
            
            sizeSum += a;
        }
        if (sizeSum % 2 == 1) {
            throw new IllegalArgumentException("Invalid parameters in creation of new population: agent count must be even.");
        }
	    
	    this.name = name;
		this.description = description;
		this.groupNames = groupNames;
		this.groupSizes = groupSizes;
	}
	
	/**
	 * Returns the size of this population
	 * 
	 * @return the size of this population
	 */
	public int getSize() {
	    return groupSizes.stream().mapToInt(i -> i.intValue()).sum();
	}
	
	/**
	 * Returns the groups this population is composed of
	 * 
	 * @return the groups this population is composed of
	 */
	public List<String> getGroupNames() {
		return this.groupNames;
	}
	
	/**
	 * Returns the size of the given group if it is part of this population, 0 otherwise
	 * 
	 * @param group the group whose size shall be returned
	 * @return the size of the given group if it is part of this population, 0 otherwise
	 */
	public int getGroupSize(String groupName) {
		if (this.getGroupNames().contains(groupName)) {
			int groupIndex = this.getGroupNames().indexOf(groupName);
			return this.groupSizes.get(groupIndex);
		} else return 0;
	}
	
	/**
	 * Returns the sizes of the groups this population consists of.
	 * 
	 * @return the sizes of the groups this population consists of
	 */
	public List<Integer> getGroupSizes() {
	    return this.groupSizes;
	}
	
	/**
	 * Returns the amount of groups in this population
	 * 
	 * @return the amount of groups in this population
	 */
	public int getGroupCount() {
		return this.groupNames.size();
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
