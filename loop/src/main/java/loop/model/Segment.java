package loop.model;

import java.io.Serializable;
import java.util.List;

/** 
 * This class represents a segment. It provides getter methods for (the names of) 
 * its capital- and strategy distributions.
 * 
 * @author Luc Mercatoris
 *
 */

public class Segment implements Serializable {
	
	private String capitalDistributionName;
	private List<Double> capitalDistributionParameters;
	private List<String> strategyNames;
	
	
	/**
	 * Creates a new segment with the given capital- and strategy distribution
	 * 
	 * @param capitalDistributionName the name of the capital distribution of this segment
	 * @param strategyNames the names of the strategies in the strategy distribution of this segment
	 */
	public Segment(String capitalDistributionName, List<Double> capitalDistributionParameters, List<String> strategyNames) {
		this.capitalDistributionName = capitalDistributionName;
		this.capitalDistributionParameters = capitalDistributionParameters;
		this.strategyNames = strategyNames;
	}
	
	/**
	 * Returns the name of the capital distribution of this segment
	 * 
	 * @return the name of the capital distribution of this segment
	 */
	public String getCapitalDistributionName() {
		return this.capitalDistributionName;
	}
	
	/**
	 * Returns the parameters of the capital distribution of this segment.
	 * 
	 * @return the parameters of the capital distribution of this segment
	 */
	public List<Double> getCapitalDistributionParameters() {
	    return this.capitalDistributionParameters;
	}
	
	/**
	 * Returns the names of the strategies in the strategy distribution of this segment
	 * 
	 * @return the names of the strategies in the strategy distribution of this segment
	 */
	public List<String> getStrategyNames() {
		return this.strategyNames;
	}

}
