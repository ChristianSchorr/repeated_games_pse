package loop.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import loop.model.UserConfiguration;
import loop.model.simulator.SimulationResult;

/**
 * This controller is responsible for displaying the results of finished simulations in the main
 * window.
 * 
 * @author Peter Koepernik
 *
 */
public class OutputController {
    
    private SimulationResult displayedResult;
    private UserConfiguration config;
    private DetailedOutputController detailedOutputController;
    //TODO other controllers
    
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
    
    /*-----------------navigation between result pages-----------------*/
    // TODO
    
    /*------------------------------------------------------------------*/
    
    /**
     * Called by the FXMLLoader when initialization is complete
     */
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        //TODO:
        //initialize other controllers
        //set label like "no simulation to display"
    }
    
    /**
     * Sets the result that shall be displayed.
     * 
     * @param result the result that shall be displayed
     */
    public void setDisplayedResult(SimulationResult result) {
        this.displayedResult = result;
        this.config = result.getUserConfiguration();
        this.detailedOutputController.setDisplayedResult(result);
        
        update();
    }
    
    private void update() {
        updateTitle();
        updateHeaderLine();
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
    
    //TODO:
    //navigation, initialization, save config (buttons top right)
}
