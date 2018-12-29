package loop.model.simulationengine;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import loop.model.simulationengine.strategies.MixedStrategy;
import loop.model.simulationengine.strategies.RealVector;
import loop.model.simulationengine.strategies.Strategy;

/**
 * This class provides a method to execute a single iteration to a given (elementary) configuration.
 * The result is returned as an instance of the {@link IterationResult} class.
 * @author Peter Koepernik
 *
 */
public class SimulationEngine {
    
    private List<Agent> agents;
    private SimulationHistory history;
    private PairBuilder pairBuilder;
    private SuccessQuantifier successQuantifier;
    private StrategyAdjuster strategyAdjuster;
    private EquilibriumCriterion equilibriumCriterion;
    private int adaptionsteps;
    private boolean equilibriumReached;
    private Configuration configuration;
    
    /**
     * Executes an iteration to the given elementary configuration and returns the result
     * as an instance of the {@link IterationResult} class.
     * 
     * @param configuration the elementary configuration to which an iteration shall be executed
     * @return the result of the iteration
     */
    public IterationResult executeIteration(Configuration configuration) {
        this.configuration = configuration;
        
        initialiseAgents();
        
        history = new SimulationHistoryTable();
        pairBuilder = configuration.getPairBuilder();
        successQuantifier = configuration.getSuccessQuantifier();
        strategyAdjuster = configuration.getStrategyAdjuster();
        equilibriumCriterion = configuration.getEquilibriumCriterion();
        
        adaptionsteps = 0;
        equilibriumReached = false;
        
        while (equilibriumReached == false && adaptionsteps < configuration.getMaxAdapts()) {
            executeAdaptionStep();
            printStepInfo();
        }
        
        IterationResult result = createResult();
        return result;
    }
    
    private void initialiseAgents() {
        agents = new ArrayList<Agent>();
        for (EngineSegment segment: configuration.getSegments()) {
            agents.addAll(new AgentInitialiser().initialiseAgents(segment, configuration.allowsMixedStrategies()));
        }
    }
    
    private void executeAdaptionStep() {
        //reset history
        history.reset();
        
        //execute rounds
        for (int round = 0; round < configuration.getRoundCount(); round++) {
            List<AgentPair> agentPairs = pairBuilder.buildPairs(agents, history);
            for (AgentPair pair: agentPairs) {
                playGame(pair);
            }
        }
        
        //rank agents
        agents = successQuantifier.createRanking(agents, history);
        
        //adapt strategies
        strategyAdjuster.adaptStrategies(agents, history);
        
        //check for equilibrium
        equilibriumReached = equilibriumCriterion.isEquilibrium(agents, history);
        
        adaptionsteps++;
    }
    
    private IterationResult createResult() {
        double efficiency = 0;
        for (Agent a: agents) {
            for (Agent b: agents) {
                if (a == b) continue;
                efficiency += a.getStrategy().getCooperationProbability(a, b, history);
            }
        }
        efficiency /= agents.size() * (agents.size() - 1);
        return new IterationResult(agents, history, equilibriumReached, efficiency, adaptionsteps);
    }
    
    private void playGame(AgentPair pair) {
        Agent p1 = pair.getFirstAgent();
        Agent p2 = pair.getSecondAgent();
        boolean p1Cooperates = p1.getStrategy().isCooperative(p1, p2, history);
        boolean p2Cooperates = p2.getStrategy().isCooperative(p2, p1, history);
        history.addResult(configuration.getGame().play(p1, p2, p1Cooperates, p2Cooperates));
    }
    
    private void printStepInfo() {
        System.out.println("\n-----------step " + this.adaptionsteps + "-----------");
        
        //strategy composition
        boolean allMixed = true;
        List<Strategy> strategies = new ArrayList<Strategy>();
        for (Agent agent: agents) {
            if (!(agent.getStrategy() instanceof MixedStrategy))
                allMixed = false;
            strategies.add(agent.getStrategy());
        }
        
        if (allMixed) {
            Map<Strategy, Double> stratCounts = new HashMap<Strategy, Double>();
            for (Agent agent: agents) {
                MixedStrategy strategy = (MixedStrategy) agent.getStrategy();
                for (Strategy pureStrategy: strategy.getComponentStrategies()) {
                    if (stratCounts.containsKey(pureStrategy)) {
                        stratCounts.put(pureStrategy, stratCounts.get(pureStrategy)
                                + strategy.getComponent(strategy.getComponentStrategies().indexOf(pureStrategy)));
                    } else {
                        stratCounts.put(pureStrategy, strategy.getComponent(strategy.getComponentStrategies().indexOf(pureStrategy)));
                    }
                }
            }
            List<Strategy> sortedStrategies = new ArrayList<Strategy>(stratCounts.keySet());
            sortedStrategies.sort((s1, s2) -> s1.getName().compareTo(s2.getName()));
            NumberFormat formatter = new DecimalFormat("#0.00");
            for (Strategy strategy: sortedStrategies) {
                System.out.println(String.format("strategy %-20s: %s.", strategy.getName(), formatter.format(stratCounts.get(strategy) / (double) agents.size() * 100.0) + "%"));
            }
            
            double meanStratDistance = 0.0;
            Agent sampleAgent = agents.get(0);
            RealVector minusStratSample = ((MixedStrategy) sampleAgent.getStrategy()).clone().mutliplyBy(-1);
            for (Agent agent: agents) {
                if (agent == sampleAgent) continue;
                MixedStrategy strat = (MixedStrategy) agent.getStrategy();
                meanStratDistance += strat.clone().add(minusStratSample).getSumNorm();
            }
            meanStratDistance /= (double) 2 * (agents.size() - 1);
            System.out.println(String.format("mean strategy distance = %s", formatter.format(meanStratDistance)));
        } else {
            Map<Strategy, Integer> stratCounts = new HashMap<Strategy, Integer>();
            for (Agent agent: agents) {
                Strategy strategy = agent.getStrategy();
                if (stratCounts.containsKey(strategy)) {
                    stratCounts.put(strategy, stratCounts.get(strategy) + 1);
                } else {
                    stratCounts.put(strategy, 1);
                }
            }
            List<Strategy> sortedStrategies = new ArrayList<Strategy>(stratCounts.keySet());
            sortedStrategies.sort((s1, s2) -> s1.getName().compareTo(s2.getName()));
            for (Strategy strategy: sortedStrategies) {
                NumberFormat formatter = new DecimalFormat("#0.00");
                System.out.println(String.format("strategy %-20s: %s.", strategy.getName(), formatter.format((double) stratCounts.get(strategy) / (double) agents.size() * 100.0) + "%"));
            }
        }
    }
    
}
