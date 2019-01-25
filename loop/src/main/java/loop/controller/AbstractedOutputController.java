package loop.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.Future;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import loop.model.UserConfiguration;
import loop.model.simulationengine.IterationResult;
import loop.model.simulator.SimulationResult;

/**
 * This class represents the controller responsible for the abstracted output of a simulation�s
 * result (Page 2 in the output described in the �Pflichtenheft�).
 *
 * @author Peter Koepernik
 */
public class AbstractedOutputController {

    private static final String FXML_NAME = "/view/controls/AbstractedOutput.fxml";

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
    private final static double CUTOFF = 0.00;

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
    private ImageView efficiencyBufferGifView;

    @FXML
    private Rectangle efficiencyBufferRectangle;

    @FXML
    private BarChart<String, Number> executedAdaptsChart;

    @FXML
    private Label meanExecutedAdaptsLabel;

    @FXML
    private ImageView adaptsBufferGifView;

    @FXML
    private Rectangle adaptsBufferRectangle;

    @FXML
    private Pane container; //the pane holding the whole output (probably an HBox or VBox)

    private Future<?> chartUpdater;

    public AbstractedOutputController(SimulationResult result) {
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
        this.configSlider.setSnapToTicks(true);
        this.configSlider.setMajorTickUnit(1);
        this.configSlider.setMinorTickCount(0);
        this.configSlider.setShowTickLabels(false);
        this.configSlider.setShowTickMarks(true);
        this.configSlider.setMin(1);
        this.configSlider.valueProperty().addListener((obs, o, n) -> handleConfigurationSlider());
        
        this.consideredIterationsComboBox.getItems().add(ALL);
        if (displayedResult.getIterationResults(selectedConfigurationNumber).stream().anyMatch(it -> it.equilibriumReached())) {
            this.consideredIterationsComboBox.getItems().add(ONLY_EQUI);
        }
        if (displayedResult.getIterationResults(selectedConfigurationNumber).stream().anyMatch(it -> !it.equilibriumReached())) {
            this.consideredIterationsComboBox.getItems().add(ONLY_NO_EQUI);
        }
        this.consideredIterationsComboBox.setValue(ALL);
        this.consideredIterationsComboBox.valueProperty().addListener((obs, o, n) -> updateCharts());
        
        this.selectedConfigurationNumber = 0;
        
        this.efficiencyChart.setTitle("Efficiency Distribution");
        this.executedAdaptsChart.setTitle("Distribution of Executed Adaption Steps");
        
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

        this.configSlider.setMax(displayedResult.getConfigurationCount());
        update();
    }

    /*-----------------------------------------------update methods-----------------------------------------------*/

    private void update() {
        this.selectedConfigurationNumber = this.config.isMulticonfiguration() ? this.configSlider.valueProperty().intValue() - 1 : 0;
        
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
        this.equilibriumFrequencyLabel.setText(String.format("%d/%d (%d%%)", equilibriumCount, config.getIterationCount(), equilibriumPercentage));

        //config slider
        this.configSlider.setVisible(config.isMulticonfiguration());
        this.configSliderLabel.setVisible(config.isMulticonfiguration());
        this.configSliderParameterLabel.setVisible(config.isMulticonfiguration());
        this.configSliderLabel.setText(String.format("%s/%s", this.selectedConfigurationNumber + 1, (int) this.configSlider.getMax()));

        if (config.isMulticonfiguration())
            this.configSliderParameterLabel.setText(String.format("%s: %s", this.config.getVariableParameterName(),
                    this.config.getParameterValues().get(this.selectedConfigurationNumber)));
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
            updateEfficiencyChart();
            setBufferingAnimationEfficiency(false);
            updateExecutedAdaptsChart();
            setBufferingAnimationAdapts(false);
        }

        private void updateEfficiencyChart() {
            //calculate histogram
            List<Double> efficiencies = new ArrayList<Double>();
            displayedResult.getIterationResults(selectedConfigurationNumber).stream().filter(
                    it -> filterIteration(it)).forEach(it -> efficiencies.add(it.getEfficiency()));
            Map<String, Integer> hist = ChartUtils.createHistogram(efficiencies, NUMBER_OF_BINS, CUTOFF, true, 2);
            
            //update chart
            XYChart.Series<String, Number> series = new XYChart.Series<String, Number>();
            hist.keySet().stream().sorted((s1, s2) -> Double.compare(Double.valueOf(s1.replace(',', '.')), Double.valueOf(s2.replace(',', '.')))).forEach(
                    label -> series.getData().add(new XYChart.Data<String, Number>(label, hist.get(label))));
            setEfficiencyChartData(series);
            
            //update mean efficiency
            double mean = efficiencies.stream().mapToDouble(val -> val.doubleValue()).sum() / efficiencies.size();
            setMeanEfficiencyLabelData(mean);
        }

        private void updateExecutedAdaptsChart() {
            //calculate histogram
            List<Integer> steps = new ArrayList<Integer>();
            displayedResult.getIterationResults(selectedConfigurationNumber).stream().filter(
                    it -> filterIteration(it)).forEach(it -> steps.add(it.getAdapts()));
            Map<String, Integer> hist = ChartUtils.createHistogram(steps, NUMBER_OF_BINS, CUTOFF, false);

            //update chart
            XYChart.Series<String, Number> series = new XYChart.Series<String, Number>();
            hist.keySet().stream().sorted((s1, s2) -> 
                Integer.compare(Integer.valueOf(s1.substring(0, s1.indexOf('-'))), Integer.valueOf(s2.substring(0, s2.indexOf('-'))))).forEach(
                        label -> series.getData().add(new XYChart.Data<String, Number>(label, hist.get(label))));
            //hist.forEach((label, value) -> series.getData().add(new XYChart.Data<String, Number>(label, value)));
            setAdaptsChartData(series);

            //update mean efficiency
            double mean = steps.stream().mapToDouble(val -> val.doubleValue()).sum() / steps.size();
            setMeanAdaptsLabelData(mean);
        }

        private boolean filterIteration(IterationResult it) {
            switch (getConsideredIterationsValue()) {
                case ALL:
                    return true;
                case ONLY_EQUI:
                    return it.equilibriumReached();
                default:
                    return !it.equilibriumReached();
            }
        }
    }

    private synchronized void setBufferingAnimationEfficiency(boolean enabled) {
        efficiencyBufferGifView.setVisible(enabled);
        efficiencyBufferRectangle.setVisible(enabled);
    }

    private synchronized void setBufferingAnimationAdapts(boolean enabled) {
        adaptsBufferGifView.setVisible(enabled);
        adaptsBufferRectangle.setVisible(enabled);
    }

    private synchronized String getConsideredIterationsValue() {
        return consideredIterationsComboBox.getValue();
    }

    private synchronized void setEfficiencyChartData(XYChart.Series<String, Number> data) {
        efficiencyChart.getData().clear();
        efficiencyChart.getData().add(data);
    }

    private synchronized void setMeanEfficiencyLabelData(double meanEfficiency) {
        meanEfficiencyLabel.setText(ChartUtils.decimalFormatter(EFFICIENCY_MEAN_PRECISION).format(meanEfficiency));
    }

    private synchronized void setAdaptsChartData(XYChart.Series<String, Number> data) {
        executedAdaptsChart.getData().clear();
        executedAdaptsChart.getData().add(data);
    }

    private synchronized void setMeanAdaptsLabelData(double meanAdapts) {
        meanExecutedAdaptsLabel.setText(ChartUtils.decimalFormatter(0).format(meanAdapts));
    }

    /*-----------------------------------------------event handlers-----------------------------------------------*/

    private void handleConfigurationSlider() {
        this.selectedConfigurationNumber = this.config.isMulticonfiguration() ? this.configSlider.valueProperty().intValue() - 1 : 0;
        
        String prevSelected = this.consideredIterationsComboBox.getValue();
        this.consideredIterationsComboBox.getItems().clear();
        this.consideredIterationsComboBox.getItems().add(ALL);
        if (displayedResult.getIterationResults(selectedConfigurationNumber).stream().anyMatch(it -> it.equilibriumReached())) {
            this.consideredIterationsComboBox.getItems().add(ONLY_EQUI);
        }
        if (displayedResult.getIterationResults(selectedConfigurationNumber).stream().anyMatch(it -> !it.equilibriumReached())) {
            this.consideredIterationsComboBox.getItems().add(ONLY_NO_EQUI);
        }
        this.consideredIterationsComboBox.setValue(this.consideredIterationsComboBox.getItems().contains(prevSelected) ? prevSelected : ALL);
        
        updateSliders();
        updateCharts();
    }
}
