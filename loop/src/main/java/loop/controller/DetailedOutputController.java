package loop.controller;

import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import loop.model.simulationengine.IterationResult;

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
    private BarChart<Integer, Integer> capitalDiagram;
    
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
        //TODO
    }
    
    //TODO: handler methoden für buttons etc.
}
