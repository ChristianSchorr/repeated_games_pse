package loop.model.simulationengine;

import java.util.List;
import java.util.Random;

/**
 * Realises the "Paarbildung nach Wunsch mit Zufall" pairing mechanism as described in the specification.
 * 
 * @author Peter Koepernik
 *
 */
public class RandomCooperationConsideringPairBuilder implements PairBuilder {
    
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
