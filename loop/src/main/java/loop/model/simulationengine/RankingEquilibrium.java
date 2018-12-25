package loop.model.simulationengine;

import java.util.List;

/**
 * Realises the “Ranggleichgewicht” described in the specication.
 * 
 * @author Peter Koepernik
 *
 */
public class RankingEquilibrium extends CountingEquilibriumCriterion {
    
    private double alpha;
    private int G;
    
    /**
     * Creates a new ranking equilibrium instance with the given parameters.
     * 
     * @param alpha the strictness of the equilibrium (between zero and one)
     * @param minRounds the minimum amount of consecutive condition-meeting adaption steps
     */
    public RankingEquilibrium(double alpha, int minRounds) {
        if (alpha < 0 || alpha > 1 || minRounds < 0) {
            throw new IllegalArgumentException("Invalid parameters for intialisation of ranking equilibrium.");
        }
        
        this.alpha = alpha;
        this.G = minRounds;
    }
    
    @Override
    public boolean hasEquilibriumCondition(final List<Agent> lastRoundAgentsFlatCopy,
            final List<Agent> lastRoundAgentsDeepCopy, final List<Agent> agents) {
        if (lastRoundAgentsFlatCopy.size() != agents.size() || lastRoundAgentsDeepCopy.size() != agents.size()) {
            throw new IllegalArgumentException("Invalid parameters in call of 'hasEquilibriumCondition'");
        }
        
        int[] oldRank = new int[agents.size()];
        
        for (int i = 0; i < agents.size(); i++) {
            oldRank[i] = lastRoundAgentsFlatCopy.indexOf(agents.get(i));
        }
        
        int diff = 0;
        for (int i = 0; i < agents.size(); i++) {
            diff += Math.abs(i - oldRank[i]);
        }
        
        return (diff < 0.5 * alpha * agents.size() * agents.size());
    }

    @Override
    public boolean longEnough(int steps) {
        return (steps >= this.G);
    }

}
