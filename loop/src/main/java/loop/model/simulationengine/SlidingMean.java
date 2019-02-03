package loop.model.simulationengine;

import java.util.ArrayList;
import java.util.List;

import loop.model.plugin.Parameter;
import loop.model.plugin.ParameterValidator;
import loop.model.plugin.Plugin;

/**
 * Realises the "Gleitender Durchschnitt" success quantification.
 * 
 * @author Peter Koepernik
 *
 */
public class SlidingMean implements SuccessQuantifier {
    
    public static final String NAME = "Sliding Mean";
    private static final String DESCRIPTION = "Calculates a sliding mean with window size w over the payoffs received in the elapsed"
            + " adaption step for each agent. Details in the specification";
    
    private int w;
    
    /**
     * Creates a new {@code SlidingMean} instance with the given window size.
     * 
     * @param windowSize the window size for the calculation of the mean
     */
    public SlidingMean(int windowSize) {
        this.w = windowSize;
    }
    
    @Override
    public List<Agent> createRanking(List<Agent> agents, SimulationHistory history) {
        int R = history.getResultsByAgent(agents.get(0)).size();
        if (R == 0) return agents;
        
        int[][] a = new int[agents.size()][R];
        int[][] A = new int[agents.size()][R];
        
        List<GameResult> games;
        for (int i = 0; i < agents.size(); i++) {
            Agent agent = agents.get(i);
            games = history.getResultsByAgent(agent);
            for (int k = 0; k < R; k++) {
                a[i][k] = (games.size() > k) ? games.get(k).getPayoff(agent) : 0;
            }
        }
        
        for (int i = 0; i < agents.size(); i++) {
            A[i][0] = a[i][0];
            for (int k = 1; k < w; k++) {
                A[i][k] = A[i][k - 1] + a[i][k];
            }
            
            for (int k = w; k < R; k++) {
                A[i][k]  = A[i][k - 1] - a[i][k - w] + a[i][k];
            }
        }
        
        int[][] r = new int[agents.size()][R];
        List<Agent> sortedAgents = new ArrayList<Agent>(agents);
        for (int k = 0; k < R; k++) {
            final int kFinal = k;
            sortedAgents.sort((a1, a2) -> A[agents.indexOf(a2)][kFinal] - A[agents.indexOf(a1)][kFinal]);
            for (int i = 0; i < agents.size(); i++) {
                r[i][k] = sortedAgents.indexOf(agents.get(i));
            }
        }
        
        int[] rankSum = new int[agents.size()];
        for (int i = 0; i < agents.size(); i++) {
            rankSum[i] = 0;
            for (int k = 0; k < R; k++) {
                rankSum[i] += r[i][k];
            }
        }
        
        sortedAgents.sort((a1, a2) -> rankSum[agents.indexOf(a1)] - rankSum[agents.indexOf(a2)]);
        return sortedAgents;
    }
    
    /**
     * Returns a {@link Plugin} instance wrapping this implementation of the {@link SuccessQuantifier} interface.
     * 
     * @return a plugin instance.
     */
    public static Plugin<SuccessQuantifier> getPlugin() {
        if (plugin == null) {
            plugin = new SlidingMeanPlugin();
        }
        return plugin;
    }
    
    private static SlidingMeanPlugin plugin;
    
    private static class SlidingMeanPlugin extends Plugin<SuccessQuantifier> {
        
        private List<Parameter> parameters = new ArrayList<Parameter>();
        
        public SlidingMeanPlugin() {
            Parameter windowSize = new Parameter(1.0, 500.0, 1.0, "window size", "the window size used to calculate the sliding mean");
            parameters.add(windowSize);
        }
        
        @Override
        public String getName() {
            return NAME;
        }

        @Override
        public String getDescription() {
            return DESCRIPTION;
        }

        @Override
        public List<Parameter> getParameters() {
            return parameters;
        }

        @Override
        public SuccessQuantifier getNewInstance(List<Double> params) {
            if (!ParameterValidator.areValuesValid(params, parameters)) {
                throw new IllegalArgumentException("Invalid parameters given for the creation of a 'sliding mean' object");
            }
            return new SlidingMean(params.get(0).intValue());
        }
    }

}
