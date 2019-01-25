package loop.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import org.controlsfx.glyphfont.Glyph;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Separator;
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
    
    private static final String ABSTRACTED_OUTPUT_TITLE = "Abstracted output";
    private static final String DETAILED_OUTPUT_TITLE = "Detailed output";
    private static final String MULTICONFIGURATION_OUTPUT_TITLE = "Multiconfiguration output";
    
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
    private Glyph checkGlyph;
    
    @FXML
    private Label gameNameLabel;

    @FXML
    private Label gameIdLabel;
    
    @FXML
    private Button importButton;
    
    @FXML
    private Button saveButton;
    
    @FXML
    private Separator sep1;
    @FXML
    private Separator sep2;

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
    private ChoiceBox<String> pageChoiceBox;
    
    @FXML
    private Button toRight;
    
    
    @FXML
    private Pane container;
    
    
    private List<Consumer<UserConfiguration>> configImportHandlers = new ArrayList<Consumer<UserConfiguration>>();
    
    /*------------------------------------------------------------------*/

    /**
     * Called by the FXMLLoader when initialization is complete
     */
    @FXML
    // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        //TODO tooltips
        
        pageChoiceBox.getItems().addAll(DETAILED_OUTPUT_TITLE, ABSTRACTED_OUTPUT_TITLE);
        pageChoiceBox.getSelectionModel().select(DETAILED_OUTPUT_TITLE);
        pageChoiceBox.getSelectionModel().selectedItemProperty().addListener((obs, oldPage, newPage) -> {
            switch (newPage) {
                case DETAILED_OUTPUT_TITLE:
                    toDetailedOutput();
                    break;
                case ABSTRACTED_OUTPUT_TITLE:
                    toAbstractedOutput();
                    break;
                case MULTICONFIGURATION_OUTPUT_TITLE:
                    toMultiOutput();
                    break;
                default: assert false;
            }
        });
        
        deactivateAll();
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
        }

        toDetailedOutput();
        
        //update navigation menubutton
        if (!config.isMulticonfiguration()) {
            pageChoiceBox.getItems().remove(MULTICONFIGURATION_OUTPUT_TITLE);
        } else {
            if (!pageChoiceBox.getItems().contains(MULTICONFIGURATION_OUTPUT_TITLE)) {
                pageChoiceBox.getItems().add(MULTICONFIGURATION_OUTPUT_TITLE);
            }
        }

        update();
    }
    
    /**
     * Register an action that will be executed whenever a user configuration shall be imported
     * from a simulation result.
     * 
     * @param action the action
     */
    public void registerImportUserConfiguration(Consumer<UserConfiguration> action) {
        configImportHandlers.add(action);
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
        this.exitConditionLabel.setText("Equilibrium criterion: " + config.getEquilibriumCriterionName());

        this.multiconfigurationLabel.setText("Multiconfiguration: " + (config.isMulticonfiguration() ? "Yes" : "No"));

        this.multiconfigurationParameterNameLabel.setVisible(config.isMulticonfiguration());
        if (config.isMulticonfiguration())
            this.multiconfigurationParameterNameLabel.setText("Multiconfiguration Parameter: " + config.getVariableParameterName());
    }

    /*-----------------------------------fxml handlers-----------------------------------*/

    @FXML
    private void handleToRight(ActionEvent event) {
        Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
        if (selectedTab == detailedOutputTab) {
            toAbstractedOutput();
        }
        if (selectedTab == multiConfigOutputTab) {
            toDetailedOutput();
        }
        if (selectedTab == abstractedOutputTab) {
            if (config.isMulticonfiguration()) {
                toMultiOutput();
            } else {
                toDetailedOutput();
            }
        }
    }

    @FXML
    private void handleToLeft(ActionEvent event) {
        Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
        if (selectedTab == multiConfigOutputTab) {
            toAbstractedOutput();
        }
        if (selectedTab == abstractedOutputTab) {
            toDetailedOutput();
        }
        if (selectedTab == detailedOutputTab) {
            if (config.isMulticonfiguration()) {
                toMultiOutput();
            } else {
                toAbstractedOutput();
            }
        }
    }

    private void toDetailedOutput() {
        tabPane.getSelectionModel().select(detailedOutputTab);
        pageChoiceBox.getSelectionModel().select(DETAILED_OUTPUT_TITLE);
    }

    private void toAbstractedOutput() {
        tabPane.getSelectionModel().select(abstractedOutputTab);
        pageChoiceBox.getSelectionModel().select(ABSTRACTED_OUTPUT_TITLE);
    }

    private void toMultiOutput() {
        if (config.isMulticonfiguration()) {
            tabPane.getSelectionModel().select(multiConfigOutputTab);
            pageChoiceBox.getSelectionModel().select(MULTICONFIGURATION_OUTPUT_TITLE);
        }
    }
    
    @FXML
    void saveResult(ActionEvent event) {
    	FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Results");
        fileChooser.setInitialDirectory(FileIO.SIMULATIONRESULTS_DIR);
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Loop Simulation Result File", "*.sim");
        fileChooser.getExtensionFilters().add(extFilter);
        File saveFile = fileChooser.showSaveDialog(new Stage());
        if (saveFile == null) return;
        new Thread(new ResultSaver(saveFile, displayedResult)).run();
    }
    
    private class ResultSaver implements Runnable {
        
        File saveFile;
        SimulationResult result;
        
        private ResultSaver(File saveFile, SimulationResult result) {
            this.saveFile = saveFile;
            this.result = result;
        }

        @Override
        public void run() {
            result.clearHandlers();
            FileIO.saveResult(result, saveFile);
        }
    }
    
    @FXML
    void importConfig() {
        if (config != null)
            this.configImportHandlers.forEach(c -> c.accept(config));
    }

    /*-----------------------------------private helpers-----------------------------------*/

    private void deactivateAll() {
        hide(this.checkGlyph);
        hide(this.gameNameLabel);
        hide(this.gameIdLabel);
        hide(this.sep1);
        hide(this.sep2);
        hide(this.exitConditionLabel);
        hide(this.multiconfigurationLabel);
        hide(this.multiconfigurationParameterNameLabel);
        hide(this.saveButton);
        hide(this.importButton);
        hide(this.toLeft);
        hide(this.toRight);
        hide(this.pageChoiceBox);
    }

    private void activateAll() {
        unHide(this.checkGlyph);
        unHide(this.gameNameLabel);
        unHide(this.gameIdLabel);
        unHide(this.sep1);
        unHide(this.sep2);
        unHide(this.exitConditionLabel);
        unHide(this.multiconfigurationLabel);
        unHide(this.multiconfigurationParameterNameLabel);
        unHide(this.saveButton);
        unHide(this.importButton);
        unHide(this.toLeft);
        unHide(this.toRight);
        unHide(this.pageChoiceBox);
    }
    
    private void hide(Node node) {
        node.setDisable(true);
        node.setVisible(false);
    }
    
    private void unHide(Node node) {
        node.setDisable(false);
        node.setVisible(true);
    }

    //highlight buttons?
    /*private void selectTab(Tab tab) {
        if (tab == detailedOutputTab) {
            tabPane.getSelectionModel().select(tab);
            
        }
    }*/

    //TODO:
    //set config
}
