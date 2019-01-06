package loop.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import loop.model.simulationengine.IterationResult;

/**
 * This class represents the controller responsible for the abstracted output of a simulation’s
 * result (Page 2 in the output described in the “Pflichtenheft”).
 * 
 * @author Peter Koepernik
 *
 */
public class AbstractedOutputController {
    
    private SimulationResult displayedResult;
    private UserConfiguration config;
    
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;
    
    /*-----------------slider line-----------------*/
    
    @FXML
    private Label equilibriumFrequencyLabel;
    
    @FXML
    private Label configSliderLabel;
    
    @FXML
    private Slider configSlider;
    private int selectedConfigurationNumber;
    
    @FXML
    private Label configSliderParameterLabel;
    
    /*-----------------charts-----------------*/
    
    //used for both bar charts
    private final static int NUMBER_OF_BINS = 15;
    private final static double CUTOFF = 0.05;
    
    @FXML
    private ComboBox<String> consideredIterationsComboBox;
    private final static String ALL = "all iterations";
    private final static String ONLY_EQUI = "only iterations where an equilibrium was reached";
    private final static String ONLY_NO_EQUI = "only iterations where no equilibrium was reached";
    
    @FXML
    private BarChart<String, Number> efficiencyChart;
    
    @FXML
    private Label meanEfficiencyLabel;
    private final static int EFFICIENCY_MEAN_PRECISION = 3; //amount of positions after decimal point in the efficiency mean
    
    @FXML
    private BarChart<String, Number> executedAdaptsChart;
    
    @FXML
    private Label meanExecutedAdaptsLabel;
    
    /**
     * Called by the FXMLLoader when initialization is complete
     */
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        this.configSlider.setSnapToTicks(true);
        this.configSlider.setMajorTickUnit(1);
        this.configSlider.setMinorTickCount(0);
        this.configSlider.setShowTickLabels(false);
        this.configSlider.setShowTickMarks(true);
        this.configSlider.setMin(1);
        this.consideredIterationsComboBox.getItems().addAll(ALL, ONLY_EQUI, ONLY_NO_EQUI);
    }
    
    /**
     * Sets the result that shall be displayed.
     * 
     * @param result the result that shall be displayed
     */
    public void setDisplayedResult(SimulationResult result) {
        this.displayedResult = result;
        this.config = result.getUserConfiguration();
        
        this.configSlider.setMax(config.getParameterValues().size());
        
        update();
    }
    
    /*-----------------------------------------------update methods-----------------------------------------------*/
    
    private void update() {
        //slider line
        updateSliders();
        
        //efficiency and adapt chart
        updateCharts();
    }
    
    private void updateSliders() {
        //equilibrium frequency
        int equilibriumCount = (int) this.displayedResult.getIterationResults(selectedConfigurationNumber).stream()
                .filter(it -> it.equilibriumReached()).count();
        int equilibriumPercentage = (int) Math.round(100 * (((double) equilibriumCount) / ((double) config.getIterationCount())));
        this.equilibriumFrequencyLabel.setText(String.format("%d/%d (%d%)", equilibriumCount, config.getIterationCount(), equilibriumPercentage));
        
        //config slider
        this.configSlider.setVisible(config.isMulticonfiguration());
        this.configSliderLabel.setVisible(config.isMulticonfiguration());
        this.configSliderParameterLabel.setVisible(config.isMulticonfiguration());
        this.configSliderLabel.setText(String.format("%s/%s", this.selectedConfigurationNumber, (int) this.configSlider.getMax()));
        this.configSliderParameterLabel.setText(String.format("%s: %s", this.config.getVariableParameterName(),
                this.config.getParameterValues().get(this.selectedConfigurationNumber)));
    }
    
    private void updateCharts() {
        updateEfficiencyChart();
        updateExecutedAdaptsChart();
    }
    
    private void updateEfficiencyChart() {
        //setup the diagram
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        efficiencyChart = new BarChart<String, Number>(xAxis, yAxis);
        efficiencyChart.setTitle("Efficiency Distribution");
        xAxis.setLabel("Efficiency");
        yAxis.setLabel("Agent Count");
        
        //calculate histogram
        List<Double> efficiencies = new ArrayList<Double>();
        displayedResult.getIterationResults(selectedConfigurationNumber).stream().filter(
                it -> filterIteration(it)).forEach(it -> efficiencies.add(it.getEfficiency()));
        Map<String, Integer> hist = ChartUtils.createHistogram(efficiencies, NUMBER_OF_BINS, CUTOFF, true, 1);
        
        //update chart
        XYChart.Series<String, Number> series = new XYChart.Series<String, Number>();
        hist.forEach((label, value) -> series.getData().add(new XYChart.Data<String, Number>(label, value)));
        efficiencyChart.getData().add(series);
        
        //update mean efficiency
        double mean = efficiencies.stream().mapToDouble(val -> val.doubleValue()).sum() / efficiencies.size();
        this.meanEfficiencyLabel.setText(String.format("Mean: %s", ChartUtils.decimalFormatter(EFFICIENCY_MEAN_PRECISION).format(mean)));
    }
    
    private void updateExecutedAdaptsChart() {
        //setup the diagram
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        efficiencyChart = new BarChart<String, Number>(xAxis, yAxis);
        efficiencyChart.setTitle("Distribution of Executed Adaption Steps");
        xAxis.setLabel("Executed Adaption Steps");
        yAxis.setLabel("Agent Count");
        
        //calculate histogram
        List<Integer> steps = new ArrayList<Integer>();
        displayedResult.getIterationResults(selectedConfigurationNumber).stream().filter(
                it -> filterIteration(it)).forEach(it -> steps.add(it.getAdapts()));
        Map<String, Integer> hist = ChartUtils.createHistogram(steps, NUMBER_OF_BINS, CUTOFF, false);
        
        //update chart
        XYChart.Series<String, Number> series = new XYChart.Series<String, Number>();
        hist.forEach((label, value) -> series.getData().add(new XYChart.Data<String, Number>(label, value)));
        this.executedAdaptsChart.getData().add(series);
        
        //update mean efficiency
        double mean = steps.stream().mapToDouble(val -> val.doubleValue()).sum() / steps.size();
        this.meanExecutedAdaptsLabel.setText(String.format("Mean: %s", ChartUtils.decimalFormatter(0).format(mean)));
    }
    
    private boolean filterIteration(IterationResult it) {
        switch (this.consideredIterationsComboBox.getValue()) {
            case ALL: return true;
            case ONLY_EQUI: return it.equilibriumReached();
            default: return !it.equilibriumReached();
        }
    }
    
    /*-----------------------------------------------event handlers-----------------------------------------------*/
    
    @FXML
    private void handleConfigurationSlider(ActionEvent event) {
        this.selectedConfigurationNumber = this.config.isMulticonfiguration() ? this.configSlider.valueProperty().intValue() : 0;
        
        updateSliders();
        updateCharts();
    }
    
    @FXML
    private void handleChartComboBox(ActionEvent event) {
        updateCharts();
    }
}
