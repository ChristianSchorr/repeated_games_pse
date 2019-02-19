package loop.model.simulationengine;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import loop.model.plugin.Parameter;
import loop.model.plugin.ParameterValidator;
import loop.model.plugin.Plugin;

/**
 * Realises the "Paarbildung nach Wunsch mit Zufall" pairing mechanism as described in the specification.
 * 
 * @author Peter Koepernik
 *
 */
public class RandomCooperationConsideringPairBuilder implements PairBuilder {
    
    public static final String NAME = "Random Cooperation Considering Pair Builder";
    private static final String DESCRIPTION = "Tries to maximize mutual cooperation probability for each builded pair, i.e. builds pairs"
            + " such that the probability of both agents cooperating is as high as possible for each pair. Though with a certain probability,"
            + " given by the randomness factor, pairs the agents completely at random instead.";
    
    private double randomnessFactor;
    
    private PairBuilder cooperationConsideringPairBuilder = new CooperationConsideringPairBuilder();
    private PairBuilder randomPairBuilder = new RandomPairBuilder();
    
    /**
     * Creates a new random cooperation considering pair builder with the given randomness factor.
     * 
     * @param randomnessFactor the probability with which pairs are built random each round
     */
    public RandomCooperationConsideringPairBuilder(double randomnessFactor) {
        this.randomnessFactor = randomnessFactor;
    }
    
    @Override
    public List<AgentPair> buildPairs(List<Agent> agents, SimulationHistory history) {
        if (new Random().nextDouble() <= this.randomnessFactor) {
            return this.randomPairBuilder.buildPairs(agents, history);
        }
        return this.cooperationConsideringPairBuilder.buildPairs(agents, history);
    }
}
