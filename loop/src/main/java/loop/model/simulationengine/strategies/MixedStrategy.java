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
    
    public MixedStrategy(String name, String description, List<Strategy> strategies, List<Double> probabilities) {
        this.name = name;
        this.description = description;
        
        if (strategies.size() != probabilities.size()) {
            throw new IllegalArgumentException("Invalid initialisation of a new mixed strategy");
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
    public Vector<Double> clone() {
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
    public void mutliplyBy(double scalar) {
        for (int i = 0; i < this.probabilities.size(); i++) 
            this.probabilities.set(i, scalar * this.probabilities.get(i));
    }

    @Override
    public void add(RealVector vector) {
        if (this.getSize() != vector.getSize()) {
            throw new IllegalArgumentException("Attempted to add two real vectors of different sizes");
        }
        for (int i = 0; i < this.probabilities.size(); i++)
            this.probabilities.set(i, this.probabilities.get(i) + vector.getComponent(i));
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
