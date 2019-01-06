package loop.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import loop.model.UserConfiguration;
import loop.model.simulationengine.IterationResult;
import loop.model.simulator.SimulationResult;

/**
 * This class represents the controller responsible for the output of a multi-simulations
 * abstracted multiconfiguration-results (Page 3 in the output described in the “Pflichtenheft”).
 * 
 * @author Peter Koepernik
 *
 */
public class MultiConfigOutputController {
    
    private SimulationResult displayedResult;
    private UserConfiguration config;
    
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;
    
    /*-----------------charts-----------------*/
    
    @FXML
    private ComboBox<String> consideredIterationsComboBox;
    private final static String ALL = "all iterations";
    private final static String ONLY_EQUI = "only iterations where an equilibrium was reached";
    private final static String ONLY_NO_EQUI = "only iterations where no equilibrium was reached";
    
    @FXML
    private LineChart<Number, Number> efficiencyAndFrequencyChart;
    
    @FXML
    private LineChart<Number, Number> executedAdaptsChart;
    
    /**
     * Called by the FXMLLoader when initialization is complete
     */
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
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
        
        update();
    }
    
    /*-----------------------------------------------update methods-----------------------------------------------*/
    
    private void update() {
        //efficiency and adapt chart
        updateCharts();
    }
    
    private void updateCharts() {
        updateEfficiencyAndFrequencyChart();
        updateExecutedAdaptsChart();
    }
    
    private void updateEfficiencyAndFrequencyChart() {
        //initialize chart
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        efficiencyAndFrequencyChart = new LineChart<Number, Number>(xAxis, yAxis);
        efficiencyAndFrequencyChart.setTitle("Mean Efficiency and Equilibrium Frequency");
        xAxis.setLabel(config.getVariableParameterName());
        
        //efficiency
        XYChart.Series<Number, Number> efficiencySeries = new XYChart.Series<Number, Number>();
        efficiencySeries.setName("Mean Efficiency");
        for (int i = 0; i < displayedResult.getConfigurationCount(); i++) {
            double meanEfficiency = displayedResult.getIterationResults(i).stream().filter(it -> filterIteration(it))
                    .mapToDouble(it -> it.getEfficiency()).sum() / (double) config.getIterationCount();
            efficiencySeries.getData().add(new XYChart.Data<Number, Number>(config.getParameterValues().get(i), meanEfficiency));
        }
        efficiencyAndFrequencyChart.getData().add(efficiencySeries);
        
        //equilibrium frequency
        XYChart.Series<Number, Number> frequencySeries = new XYChart.Series<Number, Number>();
        frequencySeries.setName("Equilibrium Frequency");
        for (int i = 0; i < displayedResult.getConfigurationCount(); i++) {
            double equilibriumFrequency = ((double) displayedResult.getIterationResults(i).stream().filter(
                    it -> it.equilibriumReached()).count()) / (double) config.getIterationCount();
            frequencySeries.getData().add(new XYChart.Data<Number, Number>(config.getParameterValues().get(i), equilibriumFrequency));
        }
        efficiencyAndFrequencyChart.getData().add(frequencySeries);
    }
    
    private void updateExecutedAdaptsChart() {
        //initialize chart
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        executedAdaptsChart = new LineChart<Number, Number>(xAxis, yAxis);
        executedAdaptsChart.setTitle("Mean Amount of Executed Adaption Steps");
        xAxis.setLabel(config.getVariableParameterName());
        
        //calculate chart data
        XYChart.Series<Number, Number> adaptsSeries = new XYChart.Series<Number, Number>();
        //adaptsSeries.setName("Executed Adapts");
        for (int i = 0; i < displayedResult.getConfigurationCount(); i++) {
            int meanAdapts = displayedResult.getIterationResults(i).stream().filter(it -> filterIteration(it))
                    .mapToInt(it -> it.getAdapts()).sum() / config.getIterationCount();
            adaptsSeries.getData().add(new XYChart.Data<Number, Number>(config.getParameterValues().get(i), meanAdapts));
        }
        
        executedAdaptsChart.getData().add(adaptsSeries);
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
    private void handleChartComboBox(ActionEvent event) {
        updateCharts();
    }
    
}
