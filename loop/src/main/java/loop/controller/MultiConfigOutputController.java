package loop.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import loop.model.UserConfiguration;
import loop.model.simulationengine.IterationResult;
import loop.model.simulator.SimulationResult;

/**
 * This class represents the controller responsible for the output of a multi-simulations
 * abstracted multiconfiguration-results (Page 3 in the output described in the �Pflichtenheft�).
 * 
 * @author Peter Koepernik
 *
 */
public class MultiConfigOutputController {
    
    private static final String FXML_NAME = "/view/controls/MultiConfigOutput.fxml";
    
    private SimulationResult displayedResult;
    private UserConfiguration config;
    
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;
    
    /*-----------------charts-----------------*/
    
    @FXML
    private ChoiceBox<String> consideredIterationsComboBox;
    private final static String ALL = "all iterations";
    private final static String ONLY_EQUI = "only iterations where an equilibrium was reached";
    private final static String ONLY_NO_EQUI = "only iterations where no equilibrium was reached";
    
    @FXML
    private LineChart<Number, Number> efficiencyAndFrequencyChart;
    
    @FXML
    private ImageView effBufferGifView;
    
    @FXML
    private Rectangle effBufferRectangle;
    
    @FXML
    private LineChart<Number, Number> executedAdaptsChart;
    
    @FXML
    private ImageView adaptsBufferGifView;
    
    @FXML
    private Rectangle adaptsBufferRectangle;
    
    @FXML
    private Pane container; //the pane holding the whole output (probably an HBox or VBox)
    
    private Future<?> chartUpdater;
    
    public MultiConfigOutputController(SimulationResult result) {
        this.displayedResult = result;
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
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        consideredIterationsComboBox.getItems().add(ALL);
        if (displayedResult.getAllIterationResults().stream().allMatch(itList -> itList.stream().anyMatch(it -> it.equilibriumReached()))) {
            consideredIterationsComboBox.getItems().add(ONLY_EQUI);
        }
        if (displayedResult.getAllIterationResults().stream().allMatch(itList -> itList.stream().anyMatch(it -> !it.equilibriumReached()))) {
            consideredIterationsComboBox.getItems().add(ONLY_NO_EQUI);
        }
        consideredIterationsComboBox.setValue(ALL);
        consideredIterationsComboBox.valueProperty().addListener((obs, o, n) -> {
            if (n == null) return;
            updateCharts();
        });
        setDisplayedResult(displayedResult);
        
        efficiencyAndFrequencyChart.setTitle("Mean Efficiency and Equilibrium Frequency");
        efficiencyAndFrequencyChart.getXAxis().setLabel(config.getVariableParameterName());
        
        executedAdaptsChart.setTitle("Mean Amount of Executed Adaption Steps");
        executedAdaptsChart.getXAxis().setLabel(config.getVariableParameterName());
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
        
        update();
    }
    
    /*-----------------------------------------------update methods-----------------------------------------------*/
    
    private void update() {
        updateConsideredIterationsComboBox();
        
        //efficiency and adapt chart
        updateCharts();
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
            setBufferingAnimationEfficiency(true);
            setBufferingAnimationAdapts(true);
            updateEfficiencyAndFrequencyChart();
            setBufferingAnimationEfficiency(false);
            updateExecutedAdaptsChart();
            setBufferingAnimationAdapts(false);
        }
        
        private void updateEfficiencyAndFrequencyChart() {
            //efficiency
            XYChart.Series<Number, Number> efficiencySeries = new XYChart.Series<Number, Number>();
            efficiencySeries.setName("Mean Efficiency");
            for (int i = 0; i < displayedResult.getConfigurationCount(); i++) {
                double meanEfficiency = displayedResult.getIterationResults(i).stream().filter(it -> filterIteration(it))
                        .mapToDouble(it -> it.getEfficiency()).sum() / (double) config.getIterationCount();
                efficiencySeries.getData().add(new XYChart.Data<Number, Number>(config.getParameterValues().get(i), meanEfficiency));
            }
            
            //equilibrium frequency
            XYChart.Series<Number, Number> frequencySeries = new XYChart.Series<Number, Number>();
            frequencySeries.setName("Equilibrium Frequency");
            for (int i = 0; i < displayedResult.getConfigurationCount(); i++) {
                double equilibriumFrequency;
                if (getConsideredIterationsValue() == ONLY_EQUI) equilibriumFrequency = 1.0;
                else if (getConsideredIterationsValue() == ONLY_NO_EQUI) equilibriumFrequency = 0.0;
                else {
                    equilibriumFrequency = ((double) displayedResult.getIterationResults(i).stream().filter(
                            it -> it.equilibriumReached()).count()) / (double) config.getIterationCount();
                }
                frequencySeries.getData().add(new XYChart.Data<Number, Number>(config.getParameterValues().get(i), equilibriumFrequency));
            }
            
            setEfficiencyAndFrequencyChartData(efficiencySeries, frequencySeries);
        }
        
        private void updateExecutedAdaptsChart() {
            //calculate chart data
            XYChart.Series<Number, Number> adaptsSeries = new XYChart.Series<Number, Number>();
            //adaptsSeries.setName("Executed Adapts");
            for (int i = 0; i < displayedResult.getConfigurationCount(); i++) {
                int meanAdapts = displayedResult.getIterationResults(i).stream().filter(it -> filterIteration(it))
                        .mapToInt(it -> it.getAdapts()).sum() / config.getIterationCount();
                adaptsSeries.getData().add(new XYChart.Data<Number, Number>(config.getParameterValues().get(i), meanAdapts));
            }
            
            setAdaptsChartData(adaptsSeries);
        }
        
        private boolean filterIteration(IterationResult it) {
            switch (getConsideredIterationsValue()) {
                case ALL: return true;
                case ONLY_EQUI: return it.equilibriumReached();
                default: return !it.equilibriumReached();
            }
        }
    }
    
    private void setBufferingAnimationEfficiency(boolean enabled) {
        effBufferGifView.setVisible(enabled);
        effBufferRectangle.setVisible(enabled);
    }
    
    private void setBufferingAnimationAdapts(boolean enabled) {
        adaptsBufferGifView.setVisible(enabled);
        adaptsBufferRectangle.setVisible(enabled);
    }
    
    private synchronized String getConsideredIterationsValue() {
        return consideredIterationsComboBox.getValue();
    }
    
    private synchronized void setEfficiencyAndFrequencyChartData(XYChart.Series<Number, Number> effData,
            XYChart.Series<Number, Number> freqData) {
        efficiencyAndFrequencyChart.getData().clear();
        efficiencyAndFrequencyChart.getData().add(effData);
        efficiencyAndFrequencyChart.getData().add(freqData);
        ((NumberAxis) efficiencyAndFrequencyChart.getXAxis()).setLowerBound(config.getParameterValues().get(0));
        ((NumberAxis) efficiencyAndFrequencyChart.getXAxis()).setUpperBound(config.getParameterValues().get(config.getParameterValues().size() - 1));
    }
    
    private synchronized void setAdaptsChartData(XYChart.Series<Number, Number> data) {
        executedAdaptsChart.getData().clear();
        executedAdaptsChart.getData().add(data);
        ((NumberAxis) executedAdaptsChart.getXAxis()).setLowerBound(config.getParameterValues().get(0));
        ((NumberAxis) executedAdaptsChart.getXAxis()).setUpperBound(config.getParameterValues().get(config.getParameterValues().size() - 1));
    }
    
    /*-----------------------------------------------event handlers-----------------------------------------------*/
    
    @FXML
    private void handleChartComboBox(ActionEvent event) {
        updateCharts();
    }
    
    
    private void updateConsideredIterationsComboBox() {
        String prevSelected = this.consideredIterationsComboBox.getValue();
        this.consideredIterationsComboBox.getItems().clear();
        this.consideredIterationsComboBox.getItems().add(ALL);
        if (displayedResult.getAllIterationResults().stream().allMatch(itList -> itList.stream().anyMatch(it -> it.equilibriumReached()))) {
            this.consideredIterationsComboBox.getItems().add(ONLY_EQUI);
        }
        if (displayedResult.getAllIterationResults().stream().allMatch(itList -> itList.stream().anyMatch(it -> !it.equilibriumReached()))) {
            this.consideredIterationsComboBox.getItems().add(ONLY_NO_EQUI);
        }
        this.consideredIterationsComboBox.setValue(this.consideredIterationsComboBox.getItems().contains(prevSelected) ? prevSelected : ALL);
    }
}
