package loop.model.simulationengine;

import java.util.ArrayList;
import java.util.List;

import loop.model.simulationengine.strategies.MixedStrategy;
import loop.model.simulationengine.strategies.Strategy;

/**
 * Realises the "Strategiegleichgewicht" described in the specification.
 * 
 * @author Peter Koepernik
 *
 */
public class StrategyEquilibrium extends CountingEquilibriumCriterion {
    
    private double alpha;
    private int G;
    
    /**
     * Creates a new strategy equilibrium instance with the given parameters.
     * 
     * @param alpha the strictness of the equilibrium (between zero and one)
     * @param minRounds the minimum amount of consecutive condition-meeting adaption steps
     */
    public StrategyEquilibrium(double alpha, int minRounds) {
        if (alpha < 0 || alpha > 1 || minRounds < 0) {
            throw new IllegalArgumentException("Invalid parameters for intialisation of strategy equilibrium.");
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
        
        List<Strategy> oldStrategies = new ArrayList<Strategy>();
        List<Strategy> newStrategies = new ArrayList<Strategy>();
        for (Agent a: agents) {
            oldStrategies.add(lastRoundAgentsDeepCopy.get(lastRoundAgentsFlatCopy.indexOf(a)).getStrategy());
            newStrategies.add(a.getStrategy());
        }
        
        //Check whether all strategies are mixed
        boolean allMixed = true;
        for (Strategy s: oldStrategies) {
            if (!(s instanceof MixedStrategy)) allMixed = false;
        }
        for (Strategy s: newStrategies) {
            if (!(s instanceof MixedStrategy)) allMixed = false;
        }
        
        double diff = 0;
        for (int i = 0; i < agents.size(); i++) {
            if (allMixed) {
                diff += ((MixedStrategy) newStrategies.get(i)).clone().add(((MixedStrategy) oldStrategies.get(i)).clone().mutliplyBy(-1)).getSumNorm(); 
            } else {
                diff += (newStrategies.get(i) != oldStrategies.get(i)) ? 2 : 0;
            }
        }
        diff *= 0.5;
        
        return (diff < this.alpha * agents.size());
    }

    @Override
    public boolean longEnough(int steps) {
        return (steps >= this.G);
    }

}
