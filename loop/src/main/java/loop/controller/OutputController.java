package loop.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import loop.model.UserConfiguration;
import loop.model.repository.FileIO;
import loop.model.simulator.SimulationResult;
import loop.model.simulator.SimulationStatus;

/**
 * This controller is responsible for displaying the results of finished simulations in the main
 * window.
 *
 * @author Peter Koepernik
 */
public class OutputController {

    private SimulationResult displayedResult;
    private UserConfiguration config;
    private DetailedOutputController detailedOutputController;
    private AbstractedOutputController abstractedOutputController;
    private MultiConfigOutputController multiConfigOutputController;

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

    /*-----------------content-----------------*/
    @FXML
    private TabPane tabPane;

    @FXML
    private Tab noOutputTab; //im fxml dokument bereits fertig, also mit dem label
    @FXML
    private Tab notFinishedTab;
    @FXML
    private Tab detailedOutputTab; //im fxml dokument leer
    @FXML
    private Tab abstractedOutputTab; //im fxml dokument leer
    @FXML
    private Tab multiConfigOutputTab; //im fxml dokument leer

    /*-----------------navigation between result pages-----------------*/
    @FXML
    private Button toLeft;

    @FXML
    private Button saveButton;

    @FXML
    private Button toDetailedOutput;

    @FXML
    private Button toAbstractedOutput;

    @FXML
    private Button toMultiOutput;


    @FXML
    private Pane container;

    /*------------------------------------------------------------------*/

    /**
     * Called by the FXMLLoader when initialization is complete
     */
    @FXML
    // This method is called by the FXMLLoader when initialization is complete
    void initialize() {

    }

    public Pane getContainer() {
        return container;
    }

    /**
     * Sets the result that shall be displayed. If {@code null} is given as an argument, no result will
     * be shown.
     *
     * @param result the result that shall be displayed
     */
    public void setDisplayedResult(SimulationResult result) {
        this.displayedResult = result;

        if (result == null) {
            tabPane.getSelectionModel().select(noOutputTab);
            deactivateAll();
            return;
        }

        if (result.getStatus() != SimulationStatus.FINISHED) {
            tabPane.getSelectionModel().select(notFinishedTab);
            deactivateAll();
            return;
        }

        activateAll();

        this.config = result.getUserConfiguration();

        if (this.detailedOutputController == null) {
            this.detailedOutputController = new DetailedOutputController(result);
            detailedOutputTab.setContent(detailedOutputController.getContainer());
        } else {
            this.detailedOutputController.setDisplayedResult(result);
        }

        if (this.abstractedOutputController == null) {
            this.abstractedOutputController = new AbstractedOutputController(result);
            abstractedOutputTab.setContent(abstractedOutputController.getContainer());
        } else {
            this.abstractedOutputController.setDisplayedResult(result);
        }

        if (config.isMulticonfiguration()) {
            if (this.multiConfigOutputController == null) {
                this.multiConfigOutputController = new MultiConfigOutputController(result);
                multiConfigOutputTab.setContent(multiConfigOutputController.getContainer());
            } else {
                this.multiConfigOutputController.setDisplayedResult(result);
            }

            toMultiOutput.setDisable(false);
        } else {
            toMultiOutput.setDisable(true);
        }

        tabPane.getSelectionModel().select(detailedOutputTab);

        update();
    }

    /*-----------------------------------update methods-----------------------------------*/

    private void update() {
        updateTitle();
        updateHeaderLine();
    }

    private void updateTitle() {
        this.gameNameLabel.setText(config.getGameName());
        this.gameIdLabel.setText(String.format("#%03d", this.displayedResult.getId()));
    }

    private void updateHeaderLine() {
        this.exitConditionLabel.setText(config.getEquilibriumCriterionName());

        this.multiconfigurationLabel.setText(config.isMulticonfiguration() ? "Yes" : "No");

        this.multiconfigurationParameterNameLabel.setVisible(config.isMulticonfiguration());
        if (config.isMulticonfiguration())
            this.multiconfigurationParameterNameLabel.setText("Multiconfiguration Parameter: " + config.getVariableParameterName());
    }

    /*-----------------------------------fxml handlers-----------------------------------*/

    @FXML
    private void handleToRight(ActionEvent event) {
        Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
        if (selectedTab == detailedOutputTab) {
            tabPane.getSelectionModel().select(abstractedOutputTab);
        }
        if (selectedTab == multiConfigOutputTab) {
            tabPane.getSelectionModel().select(detailedOutputTab);
        }
        if (selectedTab == abstractedOutputTab) {
            tabPane.getSelectionModel().select(config.isMulticonfiguration() ? multiConfigOutputTab : detailedOutputTab);
        }
    }

    @FXML
    private void handleToLeft(ActionEvent event) {
        Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
        if (selectedTab == multiConfigOutputTab) {
            tabPane.getSelectionModel().select(abstractedOutputTab);
        }
        if (selectedTab == abstractedOutputTab) {
            tabPane.getSelectionModel().select(detailedOutputTab);
        }
        if (selectedTab == detailedOutputTab) {
            tabPane.getSelectionModel().select(config.isMulticonfiguration() ? multiConfigOutputTab : abstractedOutputTab);
        }
    }

    @FXML
    private void handleToDetailedOutput(ActionEvent event) {
        tabPane.getSelectionModel().select(detailedOutputTab);
        ;
    }

    @FXML
    private void handleToAbstractedOutput(ActionEvent event) {
        tabPane.getSelectionModel().select(abstractedOutputTab);
    }

    @FXML
    private void handleToMultiOutput(ActionEvent event) {
        if (config.isMulticonfiguration()) {
            tabPane.getSelectionModel().select(multiConfigOutputTab);
        }
    }
    
    @FXML
    void saveResult(ActionEvent event) {
    	FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Results");
        fileChooser.setInitialDirectory(FileIO.SIMULATIONRESULTS_DIR);
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Loop Simulation Result File", ".sim");
        fileChooser.getExtensionFilters().add(extFilter);
        File saveFile = fileChooser.showSaveDialog(new Stage());
        FileIO.saveResult(displayedResult, saveFile);
    }

    /*-----------------------------------private helpers-----------------------------------*/

    private void deactivateAll() {
        this.gameNameLabel.setVisible(false);
        this.gameIdLabel.setVisible(false);
        this.exitConditionLabel.setVisible(false);
        this.multiconfigurationLabel.setVisible(false);
        this.multiconfigurationParameterNameLabel.setVisible(false);
        this.toLeft.setDisable(true);
        this.saveButton.setDisable(true);
        this.toDetailedOutput.setDisable(true);
        this.toAbstractedOutput.setDisable(true);
        this.toMultiOutput.setDisable(true);
    }

    private void activateAll() {
        this.gameNameLabel.setVisible(true);
        this.gameIdLabel.setVisible(true);
        this.exitConditionLabel.setVisible(true);
        this.multiconfigurationLabel.setVisible(true);
        this.multiconfigurationParameterNameLabel.setVisible(true);
        this.toLeft.setDisable(false);
        this.saveButton.setDisable(false);
        this.toDetailedOutput.setDisable(false);
        this.toAbstractedOutput.setDisable(false);
        this.toMultiOutput.setDisable(false);
    }

    //highlight buttons?
    /*private void selectTab(Tab tab) {
        if (tab == detailedOutputTab) {
            tabPane.getSelectionModel().select(tab);
            
        }
    }*/

    //TODO:
    //set config, save result buttons
}
