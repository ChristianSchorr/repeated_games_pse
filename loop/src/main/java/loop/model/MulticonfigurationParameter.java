package loop.model;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to represent the multiconfiguration parameter of a multiconfiguration. It stores the type of the
 * parameter (f.ex. 'round count' or 'pairbuilder parameter'), its name and its value range.
 * 
 * @author Peter Koepernik
 *
 */
public class MulticonfigurationParameter {
    
    private MulticonfigurationParameterType type;
    private String parameterName;
    private List<Double> parameterValues;
    private double startValue;
    private double endValue;
    private double stepSize;
    
    /**
     * Only used if type is GROUP_SIZE, SEGMENT_SIZE or CD_PARAM.
     */
    private String groupName = null;
    
    /**
     * Only used if type is CD_PARAM.
     */
    private int segmentIndex;
    
    /**
     * Constructor for ROUND_COUNT, ITERATION_COUNT and MAX_ADAPTS
     * 
     * @param type the type of the multiconfiguration parameter
     * @param startValue the start value of the parameter
     * @param endValue the end value of the parameter
     * @param stepSize the step size of the parameter
     */
    public MulticonfigurationParameter(MulticonfigurationParameterType type, int startValue, int endValue, int stepSize) {
        if (!(type.equals(MulticonfigurationParameterType.ROUND_COUNT) || type.equals(MulticonfigurationParameterType.MAX_ADAPTS))) {
            throw new IllegalArgumentException("Wrong constructor used.");
        }
        this.type = type;
        this.parameterName = type.getDescriptionFormat();
        this.startValue = startValue;
        this.endValue = endValue;
        this.stepSize = stepSize;
        
        createParameterValueList();
    }
    
    /**
     * Constructor for PB_PARAM, SA_PARAM, SQ_PARAM and EC_PARAM.
     * 
     * @param type the type of the multiconfiguration parameter
     * @param startValue the start value of the parameter
     * @param endValue the end value of the parameter
     * @param stepSize the step size of the parameter
     * @param parameterName the name of the configuration parameter of the multiconfigured algorithm
     * @param pluginName the name of the algorithm or mechanism (the pair builder, success quantifier,...)
     *                   that is multiconfigured by this parameter
     */
    public MulticonfigurationParameter(MulticonfigurationParameterType type, double startValue, double endValue, double stepSize, String parameterName) {
        if (!(type.equals(MulticonfigurationParameterType.PB_PARAM) || type.equals(MulticonfigurationParameterType.SA_PARAM)
           || type.equals(MulticonfigurationParameterType.SQ_PARAM) || type.equals(MulticonfigurationParameterType.EC_PARAM))) {
            throw new IllegalArgumentException("Wrong constructor used.");
        }
        this.type = type;
        this.parameterName = parameterName;
        this.startValue = startValue;
        this.endValue = endValue;
        this.stepSize = stepSize;
        
        createParameterValueList();
    }
    
    /**
     * Constructor for GROUP_SIZE.
     * 
     * @param startValue the start value of the parameter
     * @param endValue the end value of the parameter
     * @param stepSize the step size of the parameter
     * @param groupName the name of the group whose size is represented by this parameter
     */
    public MulticonfigurationParameter(int startValue, int endValue, int stepSize, String groupName) {
        this.type = MulticonfigurationParameterType.GROUP_SIZE;
        this.parameterName = String.format(type.getDescriptionFormat(), groupName);
        this.startValue = startValue;
        this.endValue = endValue;
        this.stepSize = stepSize;
        this.groupName = groupName;
        
        createParameterValueList();
    }
    
    /**
     * Constructor for SEGMENT_SIZE.
     * 
     * @param startValue the start value of the parameter
     * @param endValue the end value of the parameter
     * @param stepSize the step size of the parameter
     * @param groupName the name of the group in which segment sizes are multiconfigured
     */
    public MulticonfigurationParameter(double startValue, double endValue, double stepSize, String groupName) {
        this.type = MulticonfigurationParameterType.SEGMENT_SIZE;
        this.parameterName = String.format(type.getDescriptionFormat(), groupName);
        this.startValue = startValue;
        this.endValue = endValue;
        this.stepSize = stepSize;
        this.groupName = groupName;
        
        createParameterValueList();
    }
    
    /**
     * Constructor for CD_PARAM
     * 
     * @param startValue the start value of the parameter
     * @param endValue the end value of the parameter
     * @param stepSize the step size of the parameter
     * @param parameterName the name of the parameter
     * @param groupName the name of the group that contains the segment whose capital distribution is multiconfigured
     * @param segmentIndex the index of the segment whose capital distribution is multiconfigured within its group
     */
    public MulticonfigurationParameter(double startValue, double endValue, double stepSize, String parameterName, String groupName, int segmentIndex) {
        this.type = MulticonfigurationParameterType.CD_PARAM;
        this.parameterName = parameterName;
        this.startValue = startValue;
        this.endValue = endValue;
        this.stepSize = stepSize;
        
        this.groupName = groupName;
        this.segmentIndex = segmentIndex;
        
        createParameterValueList();
    }
    
    private void createParameterValueList() {
        this.parameterValues = new ArrayList<Double>();
        double param = startValue;
        while (param <= endValue + Math.pow(10, -7)) {
            parameterValues.add(param);
            param += stepSize;
        }
        //if (!parameterValues.contains((double) endValue))
        //    parameterValues.add((double) endValue);
    }
    
    public MulticonfigurationParameterType getType() {
        return this.type;
    }
    
    /**
     * Returns the name of this multiconfiguration parameter, as desplayed in the output for the user.
     * 
     * @return the name of this multiconfiguration parameter
     */
    public String getParameterName() {
        return this.parameterName;
    }
    
    /**
     * Returns the values taken by this multiconfiguration parameter.
     * 
     * @return the values taken by this multiconfiguration parameter
     */
    public List<Double> getParameterValues() {
        return this.parameterValues;
    }
    
    /**
     * Returns the group name of the multiconfigured group if this parameter is of type GROUP_SIZE, SEGMENT_SIZE or CD_PARAM,
     * otherwise throws an exception.
     * 
     * @return the group name of the multiconfigured group if this parameter is of type GROUP_SIZE, SEGMENT_SIZE or CD_PARAM
     */
    public String getGroupName() {
        if (!(type.equals(MulticonfigurationParameterType.SEGMENT_SIZE) || type.equals(MulticonfigurationParameterType.GROUP_SIZE)
                || type.equals(MulticonfigurationParameterType.CD_PARAM))) {
            throw new NullPointerException("group name not defined for multiconfiguration parameter '" + this.parameterName + "'.");
        }
        return this.groupName;
    }
    
    /**
     * Returns the index of the multiconfigured segment in the multiconfigured group if this is of type CD_PARAM, otherwise throws
     * an exception.
     * 
     * @return the index of the multiconfigured segment in the multiconfigured group if this is of type CD_PARAM
     */
    public int getSegmentIndex() {
        if (!type.equals(MulticonfigurationParameterType.CD_PARAM)) {
            throw new NullPointerException("segment index not defined for multiconfiguration parameter '" + this.parameterName + "'.");
        }
        return this.segmentIndex;
    }
}
