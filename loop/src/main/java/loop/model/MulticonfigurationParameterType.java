package loop.model;

/**
 * Represents the type of a multiconfiguration parameter, f.ex. 'round count' or 'pairbuilder parameter'.
 * 
 * @author Peter Koepernik
 *
 */
public enum MulticonfigurationParameterType {

    /**
     * The amount of iterations in this simulation
     */
    ITERATION_COUNT("iteration count"),

    /**
     * The amount of rounds per adaption step.
     */
    ROUND_COUNT("round count"),
    
    /*ITERATION_COUNT,*/
    
    /**
     * A parameter of the selected pair builder.
     */
    PB_PARAM("%s"),
    
    /**
     * A parameter of the selected success quantifier.
     */
    SQ_PARAM("%s"),
    
    /**
     * A parameter of the selected strategy adjuster
     */
    SA_PARAM("%s"),
    
    /**
     * A parameter of the selected equilibrium criterion.
     */
    EC_PARAM("%s"),
    
    /**
     * A parameter of the selected capital distribution.
     */
    CD_PARAM("%s"),
    
    /**
     * The maximum of executed adaption steps per iteration.
     */
    MAX_ADAPTS("max. adaption steps"),
    
    /**
     * The amount of agents in a group.
     */
    GROUP_SIZE("amount of agents in the group %s"),
    
    /**
     * The relative size of a segment in a group.
     */
    SEGMENT_SIZE("segment size (group: %s)");
    
    private String descriptionFormat;
    
    private MulticonfigurationParameterType(String descriptionFormat) {
        this.descriptionFormat = descriptionFormat;
    }
    
    /**
     * Returns the format of the description of this parameter.
     * 
     * @return the format of the description of this parameter
     */
    public String getDescriptionFormat() {
        return this.descriptionFormat;
    }
}
