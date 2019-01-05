package loop.controller;

import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import loop.model.Group;
import loop.model.simulationengine.Agent;
import loop.model.simulationengine.IterationResult;
import loop.model.simulationengine.strategies.MixedStrategy;
import loop.model.simulationengine.strategies.Strategy;

import org.controlsfx.control.RangeSlider;

/**
 * This class represents the controller responsible for the detailed output of a simulation’s
 * result (Page 1 in the output described in the “Pichtenheft”).
 * 
 * @author Peter Koepernik
 *
 */
public class DetailedOutputController {
    
    private SimulationResult displayedResult;
    private UserConfiguration config;
    
    
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;
    
    /*-----------------title-----------------*/
    
    @FXML
    private Label gameNameLabel;
    
    @FXML
    private Label gameIdLabel;
    
    /*-----------------header line-----------------*/
    
    @FXML
    private Label exitConditionLabel;
    
    @FXML
    private Label multiconfigurationLabel;
    
    @FXML
    private Label multiconfigurationParameterNameLabel;
    
    /*-----------------slider line-----------------*/
    
    @FXML                                      // fx:id="iterationSliderLabel"
    private Label iterationSliderLabel;        // Value injected by FXMLLoader
    
    @FXML                                  // fx:id="iterationSlider"
    private Slider iterationSlider;        // Value injected by FXMLLoader
    private int selectedIterationNumber;
    private IterationResult selectedIteration;
    
    @FXML
    private Label configSliderLabel;
    
    @FXML
    private Slider configSlider;
    private int selectedConfigurationNumber;
    
    @FXML
    private Label configSliderParameterLabel;
    
    /*-----------------description of final state-----------------*/
    
    @FXML
    private Label exitDescriptionLabel;
    
    @FXML
    private Label efficiencyLabel;
    
    @FXML
    private CheckBox meanOverAllIterationsCheckbox;
    private boolean meanOverAllIterations;
    
    /*-----------------charts and diagrams-----------------*/
    
    @FXML
    private PieChart strategyChart;
    
    @FXML
    private BarChart<Number, Number> capitalDiagram;
    private static final int NUMBER_OF_BINS = 15;
    private static final double CUTOFF = 0.05; //cuts off x% to the right and left of the distribution
    
    @FXML
    private RangeSlider consideredAgentsRangeSlider;
    private int min;
    private int max;
    
    /*-----------------navigation to other result pages-----------------*/
    // TODO
    /*------------------------------------------------------------------*/
    
    /**
     * Called by the FXMLLoader when initialization is complete
     */
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        this.iterationSlider.setMin(1);
        //TODO
    }
    
    /**
     * Sets the result that shall be displayed.
     * 
     * @param result the result that shall be displayed
     */
    public void setDisplayedResult(SimulationResult result) {
        this.displayedResult = result;
        this.config = result.getUserConfiguration();
        update();
    }
    
    /**
     * Updates the whole output window.
     */
    public void update() {
        //update local variables from user input
        updateLocalVariables();
        
        //title
        updateTitle();
        
        //header line
        updateHeaderLine();
        
        //slider line
        updateSliders();
        
        //description of final state
        updateDescription();
        
        //pie and bar chart
        updateCharts();
    }
    
    private void updateLocalVariables() {
        this.selectedIterationNumber = this.iterationSlider.valueProperty().intValue();
        this.selectedConfigurationNumber = this.config.isMulticonfiguration() ? this.configSlider.valueProperty().intValue() : 0;
        this.meanOverAllIterations = this.meanOverAllIterationsCheckbox.isSelected();
        this.min = this.consideredAgentsRangeSlider.lowValueProperty().intValue();
        this.max = this.consideredAgentsRangeSlider.highValueProperty().intValue();
        
        this.selectedIteration = this.meanOverAllIterations ? null
                : this.displayedResult.getIterationResults(this.selectedConfigurationNumber).get(this.selectedIterationNumber);
    }
    
    private void updateTitle() {
        this.gameNameLabel.setText(config.getGameName());
        this.gameIdLabel.setText(String.format("#%03s", this.displayedResult.getId()));
    }
    
    private void updateHeaderLine() {
        this.exitConditionLabel.setText(config.getEquilibriumCriterionName());
        
        this.multiconfigurationLabel.setText(config.isMulticonfiguration() ? "Yes" : "No");
        
        this.multiconfigurationParameterNameLabel.setVisible(config.isMulticonfiguration());
        this.multiconfigurationParameterNameLabel.setText("Multiconfiguration Parameter: " + config.getVariableParameterName());
    }
    
    private void updateSliders() {
        this.iterationSlider.setVisible(!meanOverAllIterations);
        this.iterationSliderLabel.setVisible(!meanOverAllIterations);
        this.iterationSliderLabel.setText(String.format("%s/%s", this.selectedIterationNumber, (int) this.iterationSlider.getMax()));
        
        this.configSlider.setVisible(config.isMulticonfiguration());
        this.configSliderLabel.setVisible(config.isMulticonfiguration());
        this.configSliderParameterLabel.setVisible(config.isMulticonfiguration());
        this.configSliderLabel.setText(String.format("%s/%s", this.selectedConfigurationNumber, (int) this.configSlider.getMax()));
        this.configSliderParameterLabel.setText(String.format("%s: %s", this.config.getVariableParameterName(),
                this.config.getParameterValues().get(this.selectedConfigurationNumber)));
    }
    
    private void updateDescription() {
        if (this.meanOverAllIterations) {
            double meanAdapts = 0;
            double equilibriumPortion = 0.0;
            double meanEfficiency = 0.0;
            for (IterationResult it: this.displayedResult.getIterationResults(selectedConfigurationNumber)) {
                meanAdapts += it.getAdapts();
                equilibriumPortion += it.equilibriumReached() ? 1.0 : 0.0;
                meanEfficiency = it.getEfficiency();
            }
            meanAdapts /= config.getIterationCount();
            equilibriumPortion /= config.getIterationCount();
            meanEfficiency /= config.getIterationCount();
            this.exitDescriptionLabel.setText(String.format("Equilibrium reached in %d% of all simulations, on average %d executed adaption steps.",
                    (int) 100 * equilibriumPortion, (int) meanAdapts));
            NumberFormat formatter = new DecimalFormat("#0.00");
            this.efficiencyLabel.setText(String.format("Mean efficiency of final state: ", formatter.format(meanEfficiency)));
            
            
        } else {
            this.exitDescriptionLabel.setText(
                    this.selectedIteration.equilibriumReached()
                    ? "Equilibrium reached after " + this.selectedIteration.getAdapts() + " adaption steps."
                    : "No equilibrium reached, cancelled simulation after " + this.selectedIteration.getAdapts() + " adaption steps.");
            NumberFormat formatter = new DecimalFormat("#0.00");
            this.efficiencyLabel.setText(String.format("Efficiency of final state: ", formatter.format(this.selectedIteration.getEfficiency())));
        }
    }
    
    private void updateCharts() {
        updateStrategyChart();
        updateCapitalChart();
    }
    
    private void updateStrategyChart() {
        
        ObservableList<PieChart.Data> pieChartData = null;
        
        List<Strategy> strategies = new ArrayList<Strategy>();
        
        //minimal/maximal index of an agent in the list of all agents (increases as max/min decreases)
        int minIndex = (int) Math.floor((1 - max) * config.getAgentCount());
        int maxIndex = config.getAgentCount() - 1 - (int) Math.floor(min * config.getAgentCount());
        
        if (this.meanOverAllIterations) {
            List<IterationResult> iterations = this.displayedResult.getIterationResults(selectedConfigurationNumber);
            
            for (IterationResult it: iterations) {
                for (Agent a: it.getAgents()) {
                    if (minIndex <= it.getAgents().indexOf(a) && it.getAgents().indexOf(a) <= maxIndex)
                        strategies.add(a.getStrategy());
                }
            }
        } else {
            for (Agent a: this.selectedIteration.getAgents()) {
                if (minIndex <= selectedIteration.getAgents().indexOf(a) && selectedIteration.getAgents().indexOf(a) <= maxIndex)
                    strategies.add(a.getStrategy());
            }
        }
        
        if (config.getMixedAllowed()) { //mixed strategies
            //cast to mixed strategies
            List<MixedStrategy> mixedStrategies = new ArrayList<MixedStrategy>();
            for (Strategy s: strategies) {
                mixedStrategies.add((MixedStrategy) s);
            }
            
            //initialise map
            Map<String, Double> portions = new HashMap<String, Double>();
            List<Strategy> pureStrategies = mixedStrategies.get(0).getComponentStrategies();
            for (Strategy s: pureStrategies) {
                portions.put(s.getName(), 0.0);
            }
            
            //calculate portions
            for (MixedStrategy s: mixedStrategies) {
                for (int i = 0; i < s.getSize(); i++) {
                    portions.put(s.getComponentStrategies().get(i).getName(),
                            portions.get(s.getComponentStrategies().get(i).getName()) + s.getComponent(i));
                }
            }
            
            //create pie chart data
            List<PieChart.Data> dataList = new ArrayList<PieChart.Data>();
            for (String strategyName: portions.keySet()) {
                dataList.add(new PieChart.Data(strategyName, portions.get(strategyName)));
            }
            
            pieChartData = FXCollections.observableArrayList(dataList);
        } else { //only pure strategies
            //calculate strategy counts
            Map<String, Integer> portions = new HashMap<String, Integer>();
            for (Strategy s: strategies) {
                if (!portions.containsKey(s.getName()))
                    portions.put(s.getName(), 0);
                portions.put(s.getName(), portions.get(s.getName()) + 1);
            }
            
          //create pie chart data
            List<PieChart.Data> dataList = new ArrayList<PieChart.Data>();
            for (String strategyName: portions.keySet()) {
                dataList.add(new PieChart.Data(strategyName, portions.get(strategyName)));
            }
            
            pieChartData = FXCollections.observableArrayList(dataList);
        }
        
        this.strategyChart.setData(pieChartData);
    }
    
    private void updateCapitalChart() {
        //setup the diagram
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        capitalDiagram = new BarChart<Number, Number>(xAxis, yAxis);
        capitalDiagram.setTitle("Capital Distribution");
        xAxis.setLabel("Capital");
        yAxis.setLabel("Agent Count");
        
        //calculate capital distribution
        Map<String, Map<Integer, Integer>> groupCapitals = new HashMap<String, Map<Integer, Integer>>(); //maps group names to capital distributions
        List<Group> groups = CentralRepository.getInstance().getPopulationRepository().getEntityByName(config.getPopulationName()).getGroups();
        for (Group group: groups) {
            if (group.isCohesive())
                groupCapitals.put(group.getName(), new HashMap<Integer, Integer>());
        }
        if (groups.stream().anyMatch(group -> !group.isCohesive()))
            groupCapitals.put("Groupless Agents", new HashMap<Integer, Integer>());
        
        Set<Integer> allCapitals = new HashSet<Integer>();
        if (this.meanOverAllIterations) {
            for (IterationResult it: displayedResult.getIterationResults(selectedConfigurationNumber)) {
                for (Agent a: it.getAgents()) {
                    Map<Integer, Integer> dist = (a.getGroupId() != -1) ? groupCapitals.get(groups.get(a.getGroupId()).getName())
                                                                        : groupCapitals.get("Groupless Agents");
                    if (!dist.containsKey(a.getCapital()))
                        dist.put(a.getCapital(), 0);
                    dist.put(a.getCapital(), dist.get(a.getCapital()) + 1);
                    
                    allCapitals.add(a.getCapital());
                }
            }
            //normalize
            groupCapitals.forEach((s, map) -> map.forEach((cap, agents) -> map.replace(cap, agents / config.getIterationCount())));
        } else {
            for (Agent a: this.selectedIteration.getAgents()) {
                Map<Integer, Integer> dist = (a.getGroupId() != -1) ? groupCapitals.get(groups.get(a.getGroupId()).getName())
                                                                    : groupCapitals.get("Groupless Agents");
                if (!dist.containsKey(a.getCapital()))
                    dist.put(a.getCapital(), 0);
                dist.put(a.getCapital(), dist.get(a.getCapital()) + 1);
                
                allCapitals.add(a.getCapital());
            }
        }
        
        //create and fill bins
        Integer[] sortedCapitals = allCapitals.stream().sorted().toArray(Integer[]::new);
        int minCapital = sortedCapitals[(int) Math.floor(CUTOFF * sortedCapitals.length)];
        int maxCapital = sortedCapitals[sortedCapitals.length - 1 - (int) Math.floor(CUTOFF * sortedCapitals.length)];
        
        int binWidth = (int) Math.ceil(((double) (maxCapital - minCapital)) / (double) NUMBER_OF_BINS);
        int binCount = (int) Math.ceil(((double) (maxCapital - minCapital + 1)) / (double) binWidth);
        
        Map<String, int[]> groupHistograms = new HashMap<String, int[]>();
        for (Group group: groups) {
            if (group.isCohesive())
                groupHistograms.put(group.getName(), new int[binCount]);
        }
        if (groups.stream().anyMatch(group -> !group.isCohesive()))
            groupHistograms.put("Groupless Agents", new int[binCount]);
        
        groupCapitals.forEach((s, dist) -> dist.forEach((cap, agents) -> groupHistograms.get(s)[binIndex(binWidth, cap, minCapital)] += agents));
        
        //calculate mean of each bin for labeling
        int[] histogramLabels = new int[binCount];
        int offset = (int) Math.round(((double) (binWidth - 1)) / 2.0);
        for (int i = 0; i < binCount; i++) {
            histogramLabels[i] = minCapital + i * binWidth + offset;
        }
        
        //create bar chart data
        List<XYChart.Series<Number, Number>> groupData = new ArrayList<XYChart.Series<Number, Number>>();
        for (Group group: groups) { //cohesive groups
            if (!group.isCohesive()) continue;
            
            int[] histogram = groupHistograms.get(group.getName());
            XYChart.Series<Number, Number> series = new XYChart.Series<Number, Number>();
            series.setName(group.getName());
            for (int i = 0; i < binCount; i++) {
                series.getData().add(new XYChart.Data<Number, Number>(histogramLabels[i], histogram[i]));
            }
            groupData.add(series);
        }
        
        XYChart.Series<Number, Number> grouplessSeries = new XYChart.Series<Number, Number>(); //groupless agents
        grouplessSeries.setName("Groupless Agents");
        for (int i = 0; i < binCount; i++) {
            grouplessSeries.getData().add(new XYChart.Data<Number, Number>(histogramLabels[i], groupHistograms.get("Groupless Agents")[i]));
        }
        groupData.add(grouplessSeries);
        
        //add data to bar chart
        this.capitalDiagram.getData().addAll(groupData);
    }
    
    private int binIndex(int binWidth, int number, int min) {
        int index = 0;
        while (number > min - 1 + (index + 1) * binWidth) index++;
        return index;
    }
    
    //TODO: handler methoden für buttons etc.
    //in handler für range slider verschiebung: wenn kein agent mehr den range constraint erfüllt änderung rückgängig machen
}
