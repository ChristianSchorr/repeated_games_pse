package loop.model.simulationengine.strategies;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import loop.model.simulationengine.Agent;
import loop.model.simulationengine.SimulationHistory;

/**
 * This class represents a mixed strategy that is composed of multiple {@link Strategy}s, each one
 * chosen with a certain probability on every evaluation. It also implements the functionality
 * of a {@link RealVector} with the probabilities of the different strategies as components.
 * 
 * @author Peter Koepernik
 *
 */
public class MixedStrategy implements Strategy, RealVector {
    
    private String name;
    private String description;
    private List<Strategy> strategies;
    private List<Double> probabilities;
    
    /**
     * The accuracy with which the sum of all probabilities must be equal to one.
     */
    private static final double ACCURACY = Math.pow(10, -7);
    
    /**
     * Creates a new mixed strategy consisting of the gicen strategies with the given probabilities.
     * 
     * @param name the name of the strategy
     * @param description the description of the strategy
     * @param strategies the strategies this mixed strategy consists of
     * @param probabilities the probabilities of the given strategies (in the same order)
     */
    public MixedStrategy(String name, String description, List<Strategy> strategies, List<Double> probabilities) {
        this.name = name;
        this.description = description;
        
        if (strategies.size() != probabilities.size()) {
            throw new IllegalArgumentException("Invalid initialisation of new mixed strategy: "
                    + ((strategies.size() > probabilities.size()) ? "more" : "less") + " given"
                    + " strategies then probabilities");
        }
        double sum = 0;
        for (Double d: probabilities) {
            if (d < 0 || d > 1) {
                throw new IllegalArgumentException("Invalid initialisation of new mixed strategy: "
                        + "one of the given probabilities not between zero and one");
            }
            sum += d;
        }
        if (Math.abs(sum - 1) > MixedStrategy.ACCURACY) {
            throw new IllegalArgumentException("Invalid initialisation of new mixed strategy: "
                    + "given probabilitis do not sum to one.");
        }
        
        this.strategies = new ArrayList<Strategy>(strategies.size());
        this.probabilities = new ArrayList<Double>(strategies.size());
        
        for (int i = 0; i < strategies.size(); i++) {
            this.strategies.set(i, strategies.get(i));
            this.probabilities.set(i, probabilities.get(i));
        }
    }
    
    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public int getSize() {
        return strategies.size();
    }

    @Override
    public Double getComponent(final int index) {
        return this.probabilities.get(index);
    }

    @Override
    public void setComponent(int index, Double value) {
        this.probabilities.set(index, value);
    }

    @Override
    public RealVector clone() {
        return new MixedStrategy(this.name, this.description, this.strategies, this.probabilities);
    }

    @Override
    public double getEuclideanNorm() {
        double sum = 0;
        for (Double d: this.probabilities)
            sum += d * d;
        return Math.sqrt(sum);
    }

    @Override
    public double getSumNorm() {
        double sum = 0;
        for (Double d: this.probabilities)
            sum += Math.abs(d);
        return sum;
    }

    @Override
    public RealVector mutliplyBy(double scalar) {
        for (int i = 0; i < this.probabilities.size(); i++) 
            this.probabilities.set(i, scalar * this.probabilities.get(i));
        
        return this;
    }

    @Override
    public RealVector add(RealVector vector) {
        if (this.getSize() != vector.getSize()) {
            throw new IllegalArgumentException("Attempted to add two real vectors of different sizes");
        }
        for (int i = 0; i < this.probabilities.size(); i++)
            this.probabilities.set(i, this.probabilities.get(i) + vector.getComponent(i));
        
        return this;
    }

    @Override
    public boolean isCooperative(Agent player, Agent opponent, SimulationHistory history) {
        double prob = this.getCooperationProbability(player, opponent, history);
        return (new Random().nextDouble() <= prob) ? true : false;
    }

    @Override
    public double getCooperationProbability(Agent player, Agent opponent, SimulationHistory history) {
        double prob = 0;
        for (int i = 0; i < this.getSize(); i++) {
            prob += this.probabilities.get(i) * this.strategies.get(i).getCooperationProbability(player, opponent, history);
        }
        return prob;
    }

}
