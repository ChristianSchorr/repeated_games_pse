package loop.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
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
 * This class represents the controller responsible for the detailed output of a simulation�s
 * result (Page 1 in the output described in the �Pichtenheft�).
 *
 * @author Peter Koepernik
 */
public class DetailedOutputController {

    private static final String FXML_NAME = "/view/controls/DetailedOutput.fxml";

    private SimulationResult displayedResult;
    private UserConfiguration config;

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
    private ImageView strategyBufferGifView;

    @FXML
    private Rectangle strategyBufferRectangle;

    @FXML
    private BarChart<String, Number> capitalDiagram;
    private static final int NUMBER_OF_BINS = 15;
    private static final double CUTOFF = 0.00; //cuts off x% to the right and left of the distribution

    @FXML
    private ImageView capitalBufferGifView;

    @FXML
    private Rectangle capitalBufferRectangle;

    @FXML
    private RangeSlider consideredAgentsRangeSlider;
    private int minRankIndex; //minimal index of an agent in the list of all agents (increases as max decreases)
    private int maxRankIndex; //maximal index of an agent in the list of all agents (increases as min decreases)

    @FXML
    private Pane container; //the pane holding the whole output (probably an HBox or VBox)

    private Future<?> chartUpdater;

    public DetailedOutputController(SimulationResult result) {
        displayedResult = result;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(FXML_NAME));
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Pane getContainer() {
        return container;
    }

    /**
     * Called by the FXMLLoader when initialization is complete
     */
    @FXML
    // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        this.iterationSlider.setSnapToTicks(true);
        this.iterationSlider.setMajorTickUnit(1);
        this.iterationSlider.setMinorTickCount(0);
        this.iterationSlider.setShowTickLabels(false);
        this.iterationSlider.setShowTickMarks(true);
        this.iterationSlider.setMin(1);
        this.iterationSlider.valueProperty().addListener((obs, o, n) -> handleIterationSlider());
        
        this.configSlider.setSnapToTicks(true);
        this.configSlider.setMajorTickUnit(1);
        this.configSlider.setMinorTickCount(0);
        this.configSlider.setShowTickLabels(false);
        this.configSlider.setShowTickMarks(true);
        this.configSlider.setMin(1);
        this.configSlider.valueProperty().addListener((obs, o, n) -> handleConfigurationSlider());;
        
        this.selectedIterationNumber = 0;
        this.selectedConfigurationNumber = 0;
        this.selectedIteration = this.meanOverAllIterations ? null
                : this.displayedResult.getIterationResults(this.selectedConfigurationNumber).get(this.selectedIterationNumber);
        
        minRankIndex = 0;
        maxRankIndex = displayedResult.getIterationResults(0).get(0).getAgents().size();
        
        meanOverAllIterations = false;
        meanOverAllIterationsCheckbox.selectedProperty().addListener((obs, o, n) -> handleMeanOverAllIterationsCheckbox());
        
        strategyChart.setTitle("Strategy Distribution");
        capitalDiagram.setTitle("Capital Distribution");
        
        //TODO change listener slider
        
        setDisplayedResult(displayedResult);
    }

    /**
     * Sets the result that shall be displayed.
     *
     * @param result the result that shall be displayed
     */
    public void setDisplayedResult(SimulationResult result) {
        if (chartUpdater != null) {
            chartUpdater.cancel(true);
        }

        this.displayedResult = result;
        this.config = result.getUserConfiguration();

        this.iterationSlider.setMax(config.getIterationCount());

        if (config.isMulticonfiguration())
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
        this.iterationSlider.setDisable(meanOverAllIterations);
        //this.iterationSlider.setVisible(!meanOverAllIterations);
        this.iterationSliderLabel.setVisible(!meanOverAllIterations);
        this.iterationSliderLabel.setText(String.format("%s/%s", this.selectedIterationNumber + 1, (int) this.iterationSlider.getMax()));

        this.configSlider.setVisible(config.isMulticonfiguration());
        this.configSliderLabel.setVisible(config.isMulticonfiguration());
        this.configSliderParameterLabel.setVisible(config.isMulticonfiguration());
        this.configSliderLabel.setText(String.format("%s/%s", this.selectedConfigurationNumber + 1, (int) this.configSlider.getMax()));

        if (config.isMulticonfiguration())
            this.configSliderParameterLabel.setText(String.format("%s: %s", this.config.getVariableParameterName(),
                    this.config.getParameterValues().get(this.selectedConfigurationNumber)));
    }

    private void updateDescription() {
        if (this.meanOverAllIterations) {
            double meanAdapts = 0;
            double equilibriumPortion = 0.0;
            double meanEfficiency = 0.0;
            for (IterationResult it : this.displayedResult.getIterationResults(selectedConfigurationNumber)) {
                meanAdapts += it.getAdapts();
                equilibriumPortion += it.equilibriumReached() ? 1.0 : 0.0;
                meanEfficiency += it.getEfficiency();
            }
            meanAdapts /= config.getIterationCount();
            equilibriumPortion /= config.getIterationCount();
            meanEfficiency /= config.getIterationCount();
            this.exitDescriptionLabel.setText(String.format("Equilibrium reached in %d%% of all simulations, on average %d executed adaption steps.",
                    Math.round(100.0 * equilibriumPortion), Math.round(meanAdapts)));
            this.efficiencyLabel.setText(String.format("Mean efficiency of final state: %s", ChartUtils.decimalFormatter(2).format(meanEfficiency)));
        } else {
            this.exitDescriptionLabel.setText(
                    this.selectedIteration.equilibriumReached()
                            ? "Equilibrium reached after " + this.selectedIteration.getAdapts() + " adaption steps."
                            : "No equilibrium reached, cancelled simulation after " + this.selectedIteration.getAdapts() + " adaption steps.");
            this.efficiencyLabel.setText(String.format("Efficiency of final state: %s", ChartUtils.decimalFormatter(2).format(this.selectedIteration.getEfficiency())));
        }
    }

    private void updateCharts() {
        if (chartUpdater != null) {
            chartUpdater.cancel(true);
        }/*
        chartUpdater = CompletableFuture.supplyAsync(() -> {
            ChartUpdater updater = new ChartUpdater();
            updater.run();
            return null;
        });*/
        ChartUpdater updater = new ChartUpdater();
        updater.run();
    }

    private class ChartUpdater implements Runnable {

        @Override
        public void run() {
            setBufferingAnimationStrategy(true);
            setBufferingAnimationCapital(true);
            updateStrategyChart();
            setBufferingAnimationStrategy(false);
            updateCapitalChart();
            setBufferingAnimationCapital(false);
        }

        private void updateStrategyChart() {
            ObservableList<PieChart.Data> pieChartData = null;

            List<Strategy> strategies = new ArrayList<Strategy>();

            if (meanOverAllIterations) {
                List<IterationResult> iterations = displayedResult.getIterationResults(selectedConfigurationNumber);

                for (IterationResult it : iterations) {
                    for (Agent a : it.getAgents()) {
                        if (minRankIndex <= it.getAgents().indexOf(a) && it.getAgents().indexOf(a) <= maxRankIndex)
                            strategies.add(a.getStrategy());
                    }
                }
            } else {
                for (Agent a : selectedIteration.getAgents()) {
                    if (minRankIndex <= selectedIteration.getAgents().indexOf(a) && selectedIteration.getAgents().indexOf(a) <= maxRankIndex)
                        strategies.add(a.getStrategy());
                }
            }

            if (config.getMixedAllowed()) { //mixed strategies
                //cast to mixed strategies
                List<MixedStrategy> mixedStrategies = new ArrayList<MixedStrategy>();
                for (Strategy s : strategies) {
                    mixedStrategies.add((MixedStrategy) s);
                }

                //initialise map
                Map<String, Double> portions = new HashMap<String, Double>();
                List<Strategy> pureStrategies = mixedStrategies.get(0).getComponentStrategies();
                for (Strategy s : pureStrategies) {
                    portions.put(s.getName(), 0.0);
                }

                //calculate portions
                for (MixedStrategy s : mixedStrategies) {
                    for (int i = 0; i < s.getSize(); i++) {
                        portions.put(s.getComponentStrategies().get(i).getName(),
                                portions.get(s.getComponentStrategies().get(i).getName()) + s.getComponent(i));
                    }
                }

                //create pie chart data
                List<PieChart.Data> dataList = new ArrayList<PieChart.Data>();
                for (String strategyName : portions.keySet()) {
                    dataList.add(new PieChart.Data(strategyName, portions.get(strategyName)));
                }

                pieChartData = FXCollections.observableArrayList(dataList);
            } else { //only pure strategies
                //calculate strategy counts
                Map<String, Integer> portions = new HashMap<String, Integer>();
                for (Strategy s : strategies) {
                    if (!portions.containsKey(s.getName()))
                        portions.put(s.getName(), 0);
                    portions.put(s.getName(), portions.get(s.getName()) + 1);
                }

                //create pie chart data
                List<PieChart.Data> dataList = new ArrayList<PieChart.Data>();
                for (String strategyName : portions.keySet()) {
                    dataList.add(new PieChart.Data(strategyName, portions.get(strategyName)));
                }

                pieChartData = FXCollections.observableArrayList(dataList);
            }

            setStrategyChartData(pieChartData);
        }

        private void updateCapitalChart() {
            //collect capital values of all relevant agents, sorted after their groups
            Map<String, List<Integer>> groupCapitals = new HashMap<String, List<Integer>>();
            List<Group> groups = CentralRepository.getInstance().getPopulationRepository().getEntityByName(config.getPopulationName()).getGroups();
            groups.stream().filter(group -> group.isCohesive()).forEach((group) -> groupCapitals.put(group.getName(), new ArrayList<Integer>()));
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
            Map<String, Map<String, Integer>> groupHistograms = ChartUtils.createHistograms(groupCapitals, NUMBER_OF_BINS, CUTOFF, true);
            if (meanOverAllIterations) { //normalize
                groupHistograms.forEach((groupName, hist) -> hist.forEach((binLabel, value) -> value /= config.getIterationCount()));
            }

            //create histogram data
            List<XYChart.Series<String, Number>> groupSeries = new ArrayList<XYChart.Series<String, Number>>();
            groupHistograms.forEach(
                    (groupName, hist) -> { //includes the groupless agents
                        XYChart.Series<String, Number> series = new XYChart.Series<String, Number>();
                        series.setName(groupName);
                        hist.keySet().stream().map(s -> Integer.valueOf(s)).sorted().map(i -> String.valueOf(i)).forEach(
                                (binLabel) -> series.getData().add(new XYChart.Data<String, Number>(binLabel, hist.get(binLabel).intValue())));
                        groupSeries.add(series);
                    });

            //add data to bar chart
            setCapitalChartData(groupSeries);
        }

    }

    private synchronized void setBufferingAnimationStrategy(boolean enabled) {
        strategyBufferGifView.setVisible(enabled);
        strategyBufferRectangle.setVisible(enabled);
    }

    private synchronized void setBufferingAnimationCapital(boolean enabled) {
        capitalBufferGifView.setVisible(enabled);
        capitalBufferRectangle.setVisible(enabled);
    }

    private synchronized void setStrategyChartData(ObservableList<PieChart.Data> data) {
        strategyChart.setData(data);
    }

    private synchronized void setCapitalChartData(Collection<? extends XYChart.Series<String, Number>> data) {
        capitalDiagram.getData().clear();
        capitalDiagram.getData().addAll(data);
    }

    /*-----------------------------------------------event handlers-----------------------------------------------*/

    private void handleIterationSlider() {
        this.selectedIterationNumber = this.iterationSlider.valueProperty().intValue() - 1;
        this.selectedIteration = this.meanOverAllIterations ? null
                : this.displayedResult.getIterationResults(this.selectedConfigurationNumber).get(this.selectedIterationNumber);
        
        updateSliders();
        updateDescription();
        updateCharts();
    }

    private void handleConfigurationSlider() {
        this.selectedConfigurationNumber = this.config.isMulticonfiguration() ? this.configSlider.valueProperty().intValue() - 1: 0;
        this.selectedIteration = this.meanOverAllIterations ? null
                : this.displayedResult.getIterationResults(this.selectedConfigurationNumber).get(this.selectedIterationNumber);

        updateSliders();
        updateDescription();
        updateCharts();
    }

    private void handleMeanOverAllIterationsCheckbox() {
        this.meanOverAllIterations = this.meanOverAllIterationsCheckbox.isSelected();
        this.selectedIteration = this.meanOverAllIterations ? null
                : this.displayedResult.getIterationResults(this.selectedConfigurationNumber).get(this.selectedIterationNumber);
        
        updateSliders();
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
        int agentCount = displayedResult.getIterationResults(selectedConfigurationNumber).get(0).getAgents().size();
        minRankIndex = (int) Math.floor((1 - maxPercent) * agentCount);
        maxRankIndex = agentCount - 1 - (int) Math.floor(minPercent * agentCount);
    }

}
