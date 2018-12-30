package loop.model;

import java.io.Serializable;
import java.util.List;

import loop.model.Nameable;

/** 
 * This class represents a group. It provides getter methods for its segmental 
 * composition and cohesion.
 * 
 * @author Luc Mercatoris
 *
 */

public class Group implements Serializable, Nameable {
	
	private String name;
	private String description;
	private List<Segment> segments;
	private List<Double> segmentSizes;
	private boolean isCohesive;
	
	/**
	 * Creates a new group with name, description and segment composition as given
	 * 
	 * @param name the name of this group
	 * @param description the description of this group
	 * @param segments the segments this group is composed of
	 * @param segmentSizes the relative sizes of the segments in the same order as the segments themselves
	 * @param isCohesive indicates whether this group is cohesive
	 */
	public Group(String name, String description, List<Segment> segments, List<Double> segmentSizes, boolean isCohesive) {
		this.name = name;
		this.description = description;
		this.segments = segments;
		this.segmentSizes = segmentSizes;
		this.isCohesive = isCohesive;
	}
	
	
	/**
	 * Returns a list of the segments this group is composed of
	 * 
	 * @return a list of the segments this group is composed of
	 */	
	public List<Segment> getSegments() {
		return this.segments;
	}
	
	/**
	 * Returns the relative size of the given segment if it is part of this group, 0 otherwise
	 * 
	 * @param segment the segment whose relative size shall be returned
	 * @return the relative size of the given Segment if it is part of this group, 0 otherwise
	 */
	public double getSegmentSizes(Segment segment) {
		if (this.segments.contains(segment)) {
			int segmentIndex = this.segments.indexOf(segment);
			return this.segmentSizes.get(segmentIndex);
		} else return 0;	
	}
	
	/**
	 * Returns whether this group is cohesive
	 * 
	 * @return true if this group is cohesive, false otherwise
	 */
	public boolean isCohesive() {
		return this.isCohesive;
	}
	
	/**
	 * Returns the amount of segments in this group
	 * 
	 * @return the amount of segments in this group
	 */
	public int getSegementCount() {
		return this.segments.size();
	}

	/**
	 * Returns the name of this group
	 * 
	 * @return the name of this group
	 */
	@Override
	public String getName() {		
		return this.name;
	}

	/**
	 * Returns the description of this group
	 * 
	 * @return the description of this group
	 */
	@Override
	public String getDescription() {
		return this.description;
	}

}
