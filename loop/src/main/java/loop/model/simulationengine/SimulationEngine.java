package loop.model.simulationengine;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.scene.chart.PieChart;
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
    
    //for the strategy distributions over time
    private List<double[]> strategyPortions;
    private List<String> strategyNames;
    
    private boolean printInfo = false;
    private int printPeriod = 5;
    private int printCounter;
    
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
        strategyPortions = new ArrayList<double[]>();
        strategyNames = new ArrayList<String>();
        if (configuration.allowsMixedStrategies()) {
            strategyNames = ((MixedStrategy) agents.get(0).getStrategy()).getComponentStrategies().stream().map(
                    s -> s.getName()).collect(Collectors.toList());
        } else {
            configuration.getSegments().stream().forEach(seg -> 
            seg.getStrategyDistribution().getSupport().stream().filter(
                    s -> !strategyNames.contains(s.getName())).forEach(s -> strategyNames.add(s.getName())));
        }
        
        adaptionsteps = 0;
        equilibriumReached = false;
        
        printCounter = 1;
        
        while (equilibriumReached == false && adaptionsteps < configuration.getMaxAdapts()) {
            executeAdaptionStep();
            calculateStrategyPortions();
            printStepInfo();
        }
        
        IterationResult result = createResult();
        return result;
    }
    
    private void initialiseAgents() {
        agents = new AgentInitialiser().initialiseAgents(configuration.getSegments(), configuration.allowsMixedStrategies());
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
    
    private void calculateStrategyPortions() {
        double[] portions = new double[strategyNames.size()];
        
        if (configuration.allowsMixedStrategies()) {
            //cast to mixed strategies
            List<MixedStrategy> mixedStrategies = agents.stream().map(a -> (MixedStrategy) a.getStrategy()).collect(Collectors.toList());
            
            for (int i = 0; i < strategyNames.size(); i++) {
                portions[i] = 0.0;
            }
            
            //calculate portions
            for (MixedStrategy s : mixedStrategies) {
                for (int i = 0; i < s.getSize(); i++) {
                    portions[i] += s.getComponent(i);
                }
            }
        } else { //only pure strategies
            List<Strategy> strategies = agents.stream().map(a -> a.getStrategy()).collect(Collectors.toList());
            //calculate strategy counts
            for (Strategy s : strategies) {
                portions[strategyNames.indexOf(s.getName())] += 1.0;
            }
        }
        double n = (double) agents.size();
        for (int i = 0; i < portions.length; i++) {
            portions[i] /= n;
        }
        strategyPortions.add(portions);
    }
    
    private IterationResult createResult() {
        double efficiency = calculateEfficiency();
        return new IterationResult(agents, history, equilibriumReached, efficiency, adaptionsteps, strategyPortions, strategyNames);
    }
    
    private void playGame(AgentPair pair) {
        Agent p1 = pair.getFirstAgent();
        Agent p2 = pair.getSecondAgent();
        boolean p1Cooperates = p1.getStrategy().isCooperative(p1, p2, history);
        boolean p2Cooperates = p2.getStrategy().isCooperative(p2, p1, history);
        history.addResult(configuration.getGame().play(p1, p2, p1Cooperates, p2Cooperates));
    }
    
    private double calculateEfficiency() {
        double efficiency = 0.0;
        for (Agent a: agents) {
            for (Agent b: agents) {
                if (a == b) continue;
                efficiency += a.getStrategy().getCooperationProbability(a, b, history);
            }
        }
        efficiency /= agents.size() * (agents.size() - 1);
        return efficiency;
    }
    
    private void printStepInfo() {
        if (!printInfo) return;
        if (printCounter++ < printPeriod && this.equilibriumReached == false && this.adaptionsteps != 1) {
            System.out.print(".");
            return;
        }
        printCounter = (this.adaptionsteps == 1) ? 2 : 1;
        
        System.out.println("\n-----------step " + this.adaptionsteps + "-----------");
        
        //all strategies mixed?
        boolean allMixed = true;
        List<Strategy> strategies = new ArrayList<Strategy>();
        for (Agent agent: agents) {
            if (!(agent.getStrategy() instanceof MixedStrategy))
                allMixed = false;
            strategies.add(agent.getStrategy());
        }
        
        //formatters for output
        NumberFormat percentFormatter = new DecimalFormat("#0.00");
        NumberFormat rankFormatter = new DecimalFormat("#0.0");
        
        if (allMixed) {
            List<MixedStrategy> mixedStrategies = new ArrayList<MixedStrategy>();
            //cast to mixed strategies
            for (Strategy strategy: strategies) {
                mixedStrategies.add((MixedStrategy) strategy);
            }
            
            //strategy composition
            Map<Strategy, Double> stratCounts = new HashMap<Strategy, Double>();
            for (MixedStrategy strategy: mixedStrategies) {
                for (Strategy pureStrategy: strategy.getComponentStrategies()) {
                    if (stratCounts.containsKey(pureStrategy)) {
                        stratCounts.put(pureStrategy, stratCounts.get(pureStrategy)
                                + strategy.getComponent(strategy.getComponentStrategies().indexOf(pureStrategy)));
                    } else {
                        stratCounts.put(pureStrategy, strategy.getComponent(strategy.getComponentStrategies().indexOf(pureStrategy)));
                    }
                }
            }
            
            //mean rank of each strategy
            List<Strategy> pureStrategies = mixedStrategies.get(0).getComponentStrategies();
            double[] strategySum = new double[pureStrategies.size()];
            double[] strategyRankingSum = new double[pureStrategies.size()];
            for (MixedStrategy strategy: mixedStrategies) {
                for (int i = 0; i < pureStrategies.size(); i++) {
                    strategySum[i] += strategy.getComponent(i);
                    strategyRankingSum[i] += (1 + mixedStrategies.indexOf(strategy)) * strategy.getComponent(i);
                }
            }
            double[] meanRank = new double[pureStrategies.size()];
            for (int i = 0; i < pureStrategies.size(); i++) {
                meanRank[i] = strategyRankingSum[i] / strategySum[i];
            }
            
            
            //print strategy composition and mean ranks
            List<Strategy> sortedStrategies = new ArrayList<Strategy>(pureStrategies);
            sortedStrategies.sort((s1, s2) -> s1.getName().compareTo(s2.getName()));
            for (Strategy strategy: sortedStrategies) {
                System.out.println(String.format("strategy %-20s: %7s | mean rank: %6s",
                        strategy.getName(), percentFormatter.format(stratCounts.get(strategy) / (double) agents.size() * 100.0) + "%",
                        rankFormatter.format(meanRank[pureStrategies.indexOf(strategy)])));
            }
            
            //calculate and print mean strategy distance (from number 1 strategy to others in ||.||_1-norm)
            double meanStratDistance = 0.0;
            Agent sampleAgent = agents.get(0);
            RealVector minusStratSample = ((MixedStrategy) sampleAgent.getStrategy()).clone().mutliplyBy(-1);
            for (Agent agent: agents) {
                if (agent == sampleAgent) continue;
                MixedStrategy strat = (MixedStrategy) agent.getStrategy();
                meanStratDistance += strat.clone().add(minusStratSample).getSumNorm();
            }
            meanStratDistance /= (double) 2 * (agents.size() - 1);
            System.out.println(String.format("mean strategy distance: %s", percentFormatter.format(meanStratDistance)));
        } else {
            //calculate strategy composition
            Map<Strategy, Integer> stratCounts = new HashMap<Strategy, Integer>();
            for (Agent agent: agents) {
                Strategy strategy = agent.getStrategy();
                if (stratCounts.containsKey(strategy)) {
                    stratCounts.put(strategy, stratCounts.get(strategy) + 1);
                } else {
                    stratCounts.put(strategy, 1);
                }
            }
            //calculate mean strategy ranks
            double[] strategySum = new double[strategies.size()];
            double[] strategyRankingSum = new double[strategies.size()];
            for (Agent agent: agents) {
                Strategy strategy = agent.getStrategy();
                strategySum[strategies.indexOf(strategy)] += 1;
                strategyRankingSum[strategies.indexOf(strategy)] += (1 + agents.indexOf(agent));
            }
            double[] meanRank = new double[strategies.size()];
            for (int i = 0; i < strategies.size(); i++) {
                meanRank[i] = strategyRankingSum[i] / strategySum[i];
            }
            
            //print strategy composition and mean ranks
            List<Strategy> sortedStrategies = new ArrayList<Strategy>(stratCounts.keySet());
            sortedStrategies.sort((s1, s2) -> s1.getName().compareTo(s2.getName()));
            for (Strategy strategy: sortedStrategies) {
                System.out.println(String.format("strategy %-20s: %7s | mean rank: %6s",
                        strategy.getName(), percentFormatter.format((double) stratCounts.get(strategy) / (double) agents.size() * 100.0) + "%",
                        rankFormatter.format(meanRank[strategies.indexOf(strategy)])
                        ));
            }
        }
        System.out.println(String.format("efficiency: %s", percentFormatter.format(calculateEfficiency())));
    }
    
}
