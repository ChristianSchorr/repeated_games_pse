package loop.model.simulationengine;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents an equilibrium criterion that checks a certain condition each round
 * and recognizes an equilibrium, if this condition is met for a certain number of consecutive
 * adaptation steps. This condition and the necessary amount of consecutive condition-meeting
 * adaptation steps are implemented as template methods.
 * 
 * @author Peter Koepernik
 *
 */
public abstract class CountingEquilibriumCriterion implements EquilibriumCriterion {
    
    private int consecutiveRounds = 0;
    private List<Agent> lastRoundAgentsFlatCopy;
    private List<Agent> lastRoundAgentsDeepCopy;
    
    @Override
    public boolean isEquilibrium(final List<Agent> agents, final SimulationHistory history) {

        if (lastRoundAgentsFlatCopy != null && !lastRoundAgentsFlatCopy.contains(agents.get(0))) {
            lastRoundAgentsFlatCopy = null;
            lastRoundAgentsDeepCopy = null;
        }

        //check for equilibrium
        boolean isEquilibrium = false;
        if (this.lastRoundAgentsFlatCopy != null && this.lastRoundAgentsFlatCopy != null
                && this.hasEquilibriumCondition(lastRoundAgentsFlatCopy, lastRoundAgentsDeepCopy, agents)) {
            isEquilibrium = this.longEnough(++this.consecutiveRounds);
        } else {
            this.consecutiveRounds = 0;
        }

        this.lastRoundAgentsFlatCopy = new ArrayList<Agent>(agents);
        this.lastRoundAgentsDeepCopy = this.deepCopy(agents);

        return isEquilibrium;
    }
    
    /**
     * Returns whether the equilibrium condition is met for the current adaption step.
     * 
     * @param lastRoundAgentsFlatCopy a list of the current agents ordered as they were last round
     * @param lastRoundAgentsDeepCopy a deep copy of the list of all agents from last round
     * @return {@code true} if the condition is met, {@code false} otherwise
     */
    public abstract boolean hasEquilibriumCondition(final List<Agent> lastRoundAgentsFlatCopy,
            final List<Agent> lastRoundAgentsDeepCopy, final List<Agent> agents);
    
    /**
     * Returns whether {@code steps} is larger or equal to the minimum amount of consecutive
     * condition-meeting adaption steps.
     * 
     * @param steps the amount of adaption steps that shall be checked
     * @return {@code true} if {@code steps} is sufficiently large, {@code false} otherwise
     */
    public abstract boolean longEnough(int steps);
    
    private List<Agent> deepCopy(final List<Agent> agents) {
        List<Agent> copy = new ArrayList<Agent>();
        for (Agent a: agents) {
            copy.add(a.getCopy());
        }
        return copy;
    }
}
