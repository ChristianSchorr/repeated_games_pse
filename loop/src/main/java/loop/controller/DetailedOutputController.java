package loop.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import loop.model.UserConfiguration;
import loop.model.repository.CentralRepository;
import loop.model.simulationengine.Agent;
import loop.model.simulationengine.IterationResult;
import loop.model.simulationengine.strategies.MixedStrategy;
import loop.model.simulationengine.strategies.Strategy;
import loop.model.simulator.SimulationResult;

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
    private BarChart<String, Number> capitalDiagram;
    private static final int NUMBER_OF_BINS = 15;
    private static final double CUTOFF = 0.05; //cuts off x% to the right and left of the distribution
    
    @FXML
    private RangeSlider consideredAgentsRangeSlider;
    private int minRankIndex; //minimal index of an agent in the list of all agents (increases as max decreases)
    private int maxRankIndex; //maximal index of an agent in the list of all agents (increases as min decreases)
    
    /**
     * Called by the FXMLLoader when initialization is complete
     */
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        this.iterationSlider.setSnapToTicks(true);
        this.iterationSlider.setMajorTickUnit(1);
        this.iterationSlider.setMinorTickCount(0);
        this.iterationSlider.setShowTickLabels(false);
        this.iterationSlider.setShowTickMarks(true);
        this.iterationSlider.setMin(1);
        
        this.configSlider.setSnapToTicks(true);
        this.configSlider.setMajorTickUnit(1);
        this.configSlider.setMinorTickCount(0);
        this.configSlider.setShowTickLabels(false);
        this.configSlider.setShowTickMarks(true);
        this.configSlider.setMin(1);
    }
    
    /**
     * Sets the result that shall be displayed.
     * 
     * @param result the result that shall be displayed
     */
    public void setDisplayedResult(SimulationResult result) {
        this.displayedResult = result;
        this.config = result.getUserConfiguration();
        
        this.iterationSlider.setMax(config.getIterationCount());
        this.configSlider.setMax(config.getParameterValues().size());
        
        /*this.consideredAgentsRangeSlider.highValueProperty().addListener((source, oldHigh, newHigh) -> {
            updateRankIndices(minPercent, newHigh.intValue());
            if (minRankIndex >= maxRankIndex) {
                this.consideredAgentsRangeSlider.setHighValue(oldHigh.doubleValue());
                updateRankIndices(minPercent, maxPercent);
            } else {
                maxPercent = newHigh.intValue();
            }
            updateCharts();
        });*/
        
        update();
    }
    
    /*-----------------------------------------------update methods-----------------------------------------------*/
    
    /**
     * Updates the whole output window.
     */
    public void update() {
        //slider line
        updateSliders();
        
        //description of final state
        updateDescription();
        
        //pie and bar chart
        updateCharts();
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
            this.efficiencyLabel.setText(String.format("Mean efficiency of final state: ", ChartUtils.decimalFormatter(2).format(meanEfficiency)));
            
            
        } else {
            this.exitDescriptionLabel.setText(
                    this.selectedIteration.equilibriumReached()
                    ? "Equilibrium reached after " + this.selectedIteration.getAdapts() + " adaption steps."
                    : "No equilibrium reached, cancelled simulation after " + this.selectedIteration.getAdapts() + " adaption steps.");
            this.efficiencyLabel.setText(String.format("Efficiency of final state: ", ChartUtils.decimalFormatter(2).format(this.selectedIteration.getEfficiency())));
        }
    }
    
    private void updateCharts() {
        updateStrategyChart();
        updateCapitalChart();
    }
    
    private void updateStrategyChart() {
        
        ObservableList<PieChart.Data> pieChartData = null;
        
        List<Strategy> strategies = new ArrayList<Strategy>();
        
        if (this.meanOverAllIterations) {
            List<IterationResult> iterations = this.displayedResult.getIterationResults(selectedConfigurationNumber);
            
            for (IterationResult it: iterations) {
                for (Agent a: it.getAgents()) {
                    if (minRankIndex <= it.getAgents().indexOf(a) && it.getAgents().indexOf(a) <= maxRankIndex)
                        strategies.add(a.getStrategy());
                }
            }
        } else {
            for (Agent a: this.selectedIteration.getAgents()) {
                if (minRankIndex <= selectedIteration.getAgents().indexOf(a) && selectedIteration.getAgents().indexOf(a) <= maxRankIndex)
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
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        capitalDiagram = new BarChart<String, Number>(xAxis, yAxis);
        capitalDiagram.setTitle("Capital Distribution");
        xAxis.setLabel("Capital");
        yAxis.setLabel("Agent Count");
        
        //collect capital values of all relevant agents, sorted after their groups
        Map<String, List<Integer>> groupCapitals = new HashMap<String, List<Integer>>();
        List<Group> groups = CentralRepository.getInstance().getPopulationRepository().getEntityByName(config.getPopulationName()).getGroups();
        groups.stream().filter(group -> group.isCohesive()).forEach((group) ->  groupCapitals.put(group.getName(), new ArrayList<Integer>()));
        if (groups.stream().anyMatch(group -> !group.isCohesive()))
            groupCapitals.put("Groupless Agents", new ArrayList<Integer>());
        
        if (meanOverAllIterations) {
            //cohesive groups
            displayedResult.getIterationResults(selectedConfigurationNumber).stream().forEach(
                    it -> it.getAgents().stream().filter(a -> a.getGroupId() != -1).forEach(
                            a -> groupCapitals.get(groups.get(a.getGroupId()).getName()).add(a.getCapital())));
            //groupless agents
            displayedResult.getIterationResults(selectedConfigurationNumber).stream().forEach(
                    it -> it.getAgents().stream().filter(a -> a.getGroupId() == -1).forEach(
                            a -> groupCapitals.get("Groupless Agents").add(a.getCapital())));
        } else {
            //cohesive groups
            selectedIteration.getAgents().stream().filter(a -> a.getGroupId() != -1).forEach(
                    a -> groupCapitals.get(groups.get(a.getGroupId()).getName()).add(a.getCapital()));
            //groupless agents
            selectedIteration.getAgents().stream().filter(a -> a.getGroupId() == -1).forEach(
                    a -> groupCapitals.get("Groupless Agents").add(a.getCapital()));
        }
        
        //create histograms
        Map<String, Map<String, Integer>> groupHistograms = new HashMap<String, Map<String, Integer>>();
        groupCapitals.forEach(
                (groupName, capitals) -> groupHistograms.put(groupName, ChartUtils.createHistogram(capitals, NUMBER_OF_BINS, CUTOFF, true)));
        if (meanOverAllIterations) { //normalize
            groupHistograms.forEach((groupName, hist) -> hist.forEach((binLabel, value) -> value /= config.getIterationCount()));
        }
        
        //create histogram data
        List<XYChart.Series<String, Number>> groupSeries = new ArrayList<XYChart.Series<String, Number>>();
        groupHistograms.forEach(
                (groupName, hist) -> { //includes the groupless agents
                    XYChart.Series<String, Number> series = new XYChart.Series<String, Number>();
                    series.setName(groupName);
                    hist.forEach((binLabel, value) -> series.getData().add(new XYChart.Data<String, Number>(binLabel, value)));
                    groupSeries.add(series);
                });
        
        //add data to bar chart
        capitalDiagram.getData().addAll(groupSeries);
    }
    
    /*-----------------------------------------------event handlers-----------------------------------------------*/
    
    @FXML
    private void handleIterationSlider(ActionEvent event) {
        this.selectedIterationNumber = this.iterationSlider.valueProperty().intValue();
        this.selectedIteration = this.meanOverAllIterations ? null
                : this.displayedResult.getIterationResults(this.selectedConfigurationNumber).get(this.selectedIterationNumber);
        
        updateDescription();
        updateCharts();
    }
    
    @FXML
    private void handleConfigurationSlider(ActionEvent event) {
        this.selectedConfigurationNumber = this.config.isMulticonfiguration() ? this.configSlider.valueProperty().intValue() : 0;
        this.selectedIteration = this.meanOverAllIterations ? null
                : this.displayedResult.getIterationResults(this.selectedConfigurationNumber).get(this.selectedIterationNumber);
        
        updateSliders();
        updateDescription();
        updateCharts();
    }
    
    @FXML
    private void handleMeanOverAllIterationsCheckbox(ActionEvent event) {
        this.meanOverAllIterations = this.meanOverAllIterationsCheckbox.isSelected();
        this.selectedIteration = this.meanOverAllIterations ? null
                : this.displayedResult.getIterationResults(this.selectedConfigurationNumber).get(this.selectedIterationNumber);
        
        updateDescription();
        updateCharts();
    }
    
    @FXML
    private void handleRangeSlider(ActionEvent event) {
        int minPercent = (int) this.consideredAgentsRangeSlider.getLowValue();
        int maxPercent = (int) this.consideredAgentsRangeSlider.getHighValue();
        updateRankIndices(minPercent, maxPercent);
        
        updateCharts();
    }
    
    private void updateRankIndices(int minPercent, int maxPercent) {
        minRankIndex = (int) Math.floor((1 - maxPercent) * config.getAgentCount());
        maxRankIndex = config.getAgentCount() - 1 - (int) Math.floor(minPercent * config.getAgentCount());
    }
    
}
