package loop.controller;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import loop.LoopSettings;
import loop.Main;
import loop.model.Group;
import loop.model.Population;
import loop.model.UserConfiguration;
import loop.model.repository.CentralRepository;
import loop.model.repository.FileIO;
import loop.model.simulationengine.Game;
import loop.model.simulator.SimulationResult;
import loop.model.simulator.ThreadPoolSimulator;
import loop.model.simulator.exception.ConfigurationException;

/**
 * This controller is associated with the main window and thus instantiated when the program starts.
 * It is also responsible for the communication between controller and model.
 *
 * @author Pierre Toussing
 */
public class HeadController {

    private UserConfiguration activeConfiguration;

    private ThreadPoolSimulator simulator;

    @FXML
    private HistoryController historyViewController;

    @FXML
    private Parent historyView;

    /*---------------------------configuration preview---------------------------*/

    @FXML
    private Label populationNameLabel;

    @FXML
    private Label gameNameLabel;

    @FXML
    private Label iterationCountLabel;

    @FXML
    private Label agentCountLabel;

    @FXML
    private Label roundCountLabel;

    @FXML
    private Label mixedStrategiesLabel;

    /*---------------------------menu---------------------------*/

    @FXML
    private MenuBar menuBar;

    /*---------------------------buttons top right---------------------------*/

    @FXML
    private Button editConfigurationButton;

    @FXML
    private Button startSimulationButton;

	@FXML
    private Pane mainPane;

    private static final String LOOP_BUFFER_PATH = "/loop_buffer.gif";
    private static final String MANUAL_PDF = "src/main/resources/manual.pdf";
    private static final String CONFIGURATIONWINDOW_FXML = "/view/windows/ConfigurationWindow.fxml";
    private static final String ABOUTWINDOW_FXML = "/view/windows/AboutWindow.fxml";
    private static final String GAMEWINDOW_FXML = "/view/windows/GameWindow.fxml";
    private static final String STRATEGYWINDOW_FXML = "/view/windows/StrategyWindow.fxml";
    private static final String GROUPWINDOW_FXML = "/view/windows/GroupWindow.fxml";
    private static final String POPULATIONWINDOW_FXML = "/view/windows/PopulationWindow.fxml";
    private static final String SETTINGS_FXML = "/view/windows/Settings.fxml";

    private CentralRepository repository = CentralRepository.getInstance();
    
    private LoopSettings settings = LoopSettings.getInstance();

    @FXML
    void initialize() {
        //load default configuration if valid
        updateConfiguration(UserConfiguration.getDefaultConfiguration(), false, "");
        updateConfiguration(settings.getConfiguration(), true, "configuration that was set as default");

        //create simulator
        simulator = new ThreadPoolSimulator(settings.getThreadCount());
        //register callback for the import of configurations from simulation results
        historyViewController.registerImportUserConfiguration(config -> importConfiguration(config));
        historyViewController.registerCancleRequestHandler(sim -> simulator.stopSimulation(sim));
    }

    private void importConfiguration(UserConfiguration config) {
        updateConfiguration(config, true, "opened configuration");
    }

    @FXML
    void saveConfiguration() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Configuration");
        fileChooser.setInitialDirectory(FileIO.USER_CONFIG_DIR);
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Loop Configuration File", "*.cnfg");
        fileChooser.getExtensionFilters().add(extFilter);
        File saveFile = fileChooser.showSaveDialog(new Stage());
        if (saveFile == null) return;
        try {
            FileIO.saveEntity(saveFile, activeConfiguration);
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR, "File could not be saved.", ButtonType.OK);
            alert.showAndWait();
        }
    }
    
    @FXML
    void setAsDefaultConfiguration() {
    	settings.setConfiguration(this.activeConfiguration);
    	settings.save();
    	Alert alert = new Alert(AlertType.CONFIRMATION, "Default Configuration updated. Remember to persistently store all self created populations, groups, strategies and games that are used in this configuration as well!", ButtonType.OK);
        alert.showAndWait();
    }

    @FXML
    void loadConfiguration() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Configuration");
        fileChooser.setInitialDirectory(FileIO.USER_CONFIG_DIR);
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Loop Configuration File", "*.cnfg");
        fileChooser.getExtensionFilters().add(extFilter);
        File openFile = fileChooser.showOpenDialog(new Stage());
        if (openFile == null) return;
        UserConfiguration config = null;
        try {
            config = FileIO.loadEntity(openFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR, "File could not be opened.", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        importConfiguration(config);
    }

    @FXML
    void editConfiguration() {
        Parent configParent = null;
        try {
            ConfigController controller = new ConfigController();
            controller.registerElementCreated((config) -> updateConfiguration(config, false, ""));
            FXMLLoader loader = new FXMLLoader(getClass().getResource(CONFIGURATIONWINDOW_FXML));
            loader.setController(controller);
            configParent = loader.load();
            controller.setConfiguration(activeConfiguration);

        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene configScene = new Scene(configParent);
        Stage configWindow = new Stage();

        configWindow.setTitle("Edit configuration");
        configWindow.setScene(configScene);
        configWindow.getIcons().add(new Image(Main.RING_LOGO_PATH));

        // Specifies the modality for new window.
        configWindow.initModality(Modality.APPLICATION_MODAL);
        configWindow.show();
    }

    @FXML
    void loadResults(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Simulation Results");
        fileChooser.setInitialDirectory(FileIO.SIMULATIONRESULTS_DIR);
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Loop Simulation Result File", "*.sim");
        fileChooser.getExtensionFilters().add(extFilter);
        File openFile = fileChooser.showOpenDialog(new Stage());
        if (openFile == null) return;
        SimulationResult loadedResult;
        try {
            loadedResult = (SimulationResult) FileIO.loadEntity(openFile);
        } catch (IOException e) {
            Alert alert = new Alert(AlertType.ERROR, "File could not be opened", ButtonType.OK);
            alert.showAndWait();
            e.printStackTrace();
            return;
        }
        loadedResult.setId(simulator.getSimulationId());
        simulator.incrementSimulationId();
        addSimulationToHistoryController(loadedResult);
    }

    private synchronized void addSimulationToHistoryController(SimulationResult result) {
        historyViewController.addSimulation(result);
    }

    private synchronized void setDefaultCursor() {
        mainPane.setCursor(Cursor.DEFAULT);
    }

    private synchronized void setBufferingCursor() {
        Image image = new Image(LOOP_BUFFER_PATH);
        mainPane.setCursor(new ImageCursor(image));
    }

    @FXML
    void startSimulation(ActionEvent event) {
        SimulationResult result;
        try {
            result = simulator.startSimulation(activeConfiguration);
        } catch (ConfigurationException e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR, "Active configuration is faulty.", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        historyViewController.addSimulation(result);
    }

    @FXML
    public void closeSimulator(ActionEvent event) throws InterruptedException {
        simulator.stopAllSimulations();
        simulator.stopSimulator();
        simulator = null;
        Platform.exit();
    }

    @FXML
    void showInfo(ActionEvent event) {
    	Parent newAboutParent = null;
        try {
            newAboutParent = FXMLLoader.load(getClass().getResource(ABOUTWINDOW_FXML));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene newAboutScene = new Scene(newAboutParent);
        Stage newAboutWindow = new Stage();
        newAboutWindow.setTitle("About");
        newAboutWindow.setScene(newAboutScene);

        // Specifies the modality for new window.
        newAboutWindow.initModality(Modality.APPLICATION_MODAL);
        newAboutWindow.getIcons().add(new Image(Main.RING_LOGO_PATH));
        newAboutWindow.show();
    }

    @FXML
    void showHelp(ActionEvent event) {
    	try {
            Desktop desktop = Desktop.getDesktop();
            if (desktop != null && desktop.isSupported(Desktop.Action.OPEN)) {
                desktop.open(new File(MANUAL_PDF));
            } else {
                System.err.println("PDF file can not be displayed!");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    @FXML
    void openSettings(ActionEvent event) {
    	Parent settingParent;
    	SettingsController controller;
        try {
        	FXMLLoader loader = new FXMLLoader(getClass().getResource(SETTINGS_FXML));
            settingParent = loader.load();
            controller = (SettingsController) loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        Scene settingScene = new Scene(settingParent);
        Stage settingWindow = new Stage();
        settingWindow.setTitle("Settings");
        settingWindow.setScene(settingScene);
        controller.setStage(settingWindow);
        controller.setSettings(LoopSettings.getInstance());

        // Specifies the modality for new window.
        settingWindow.initModality(Modality.APPLICATION_MODAL);
        settingWindow.getIcons().add(new Image(Main.RING_LOGO_PATH));
        settingWindow.show();
    }

    @FXML
    void openNewGameWindow(ActionEvent event) {
        Parent newGameParent;
        NewGameController controller;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(GAMEWINDOW_FXML));
            newGameParent = loader.load();
            controller = (NewGameController) loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        Scene newGameScene = new Scene(newGameParent);
        Stage newGameWindow = new Stage();
        newGameWindow.setTitle("Create a new Game");
        newGameWindow.setScene(newGameScene);

        controller.setStage(newGameWindow);
        controller.registerElementCreated(game -> newGameCreated(game));

        // Specifies the modality for new window.
        newGameWindow.initModality(Modality.APPLICATION_MODAL);
        newGameWindow.getIcons().add(new Image(Main.RING_LOGO_PATH));
        newGameWindow.show();
    }

    @FXML
    void openNewStrategyWindow(ActionEvent event) {
        Parent newStrategyParent = null;
        try {
            newStrategyParent = FXMLLoader.load(getClass().getResource(STRATEGYWINDOW_FXML));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene newStrategyScene = new Scene(newStrategyParent);
        Stage newStrategyWindow = new Stage();
        newStrategyWindow.setTitle("Create a new Strategy");
        newStrategyWindow.setScene(newStrategyScene);

        // Specifies the modality for new window.
        newStrategyWindow.initModality(Modality.APPLICATION_MODAL);
        newStrategyWindow.getIcons().add(new Image(Main.RING_LOGO_PATH));
        newStrategyWindow.show();
    }

    @FXML
    void openNewGroupWindow(ActionEvent event) {
        Parent newGroupParent;
        GroupController controller;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(GROUPWINDOW_FXML));
            newGroupParent = loader.load();
            controller = (GroupController) loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        Scene newGroupScene = new Scene(newGroupParent);
        Stage newGroupWindow = new Stage();
        newGroupWindow.setTitle("Create a new Group");
        newGroupWindow.setScene(newGroupScene);

        controller.setStage(newGroupWindow);
        controller.registerElementCreated(g -> newGroupCreated(g));

        // Specifies the modality for new window.
        newGroupWindow.initModality(Modality.APPLICATION_MODAL);
        newGroupWindow.getIcons().add(new Image(Main.RING_LOGO_PATH));
        newGroupWindow.show();
    }

    @FXML
    void openNewPopulationWindow(ActionEvent event) {
        Parent newPopulationParent;
        PopulationController controller;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(POPULATIONWINDOW_FXML));
            newPopulationParent = loader.load();
            controller = (PopulationController) loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        Scene newPopulationScene = new Scene(newPopulationParent);
        Stage newPopulationWindow = new Stage();
        newPopulationWindow.setTitle("Create a new Population");
        newPopulationWindow.setScene(newPopulationScene);

        controller.setStage(newPopulationWindow);
        controller.registerElementCreated(pop -> newPopulationCreated(pop));

        // Specifies the modality for new window.
        newPopulationWindow.initModality(Modality.APPLICATION_MODAL);
        newPopulationWindow.getIcons().add(new Image(Main.RING_LOGO_PATH));
        newPopulationWindow.show();
    }

    private void newGameCreated(Game game) {
        boolean stored = CentralRepository.getInstance().getGameRepository().addEntity(game.getName(), game);
        if (!stored) {
            //what now?
        }
    }

    private void newGroupCreated(Group group) {
        boolean stored = CentralRepository.getInstance().getGroupRepository().addEntity(group.getName(), group);
        if (!stored) {
            //what now?
        }
    }

    private void newPopulationCreated(Population population) {
        boolean stored = CentralRepository.getInstance().getPopulationRepository().addEntity(population.getName(), population);
        if (!stored) {
            //what now?
        }
    }

    private boolean updateConfiguration(UserConfiguration configuration, boolean checkForUnknownEntities, String configName) {
        if (checkForUnknownEntities) {
            boolean error = false;
            String errorMsg = "The " + configName + " contains unknown entities:";
            if (!repository.getGameRepository().containsEntityName(configuration.getGameName())) {
                errorMsg += "\n - the game '" + configuration.getGameName() + "'";
            }
            if (!repository.getPopulationRepository().containsEntityName(configuration.getPopulationName())) {
                errorMsg += "\n - the population '" + configuration.getPopulationName() + "'";
                error = true;
            }
            if (!repository.getPairBuilderRepository().containsEntityName(configuration.getPairBuilderName())) {
                errorMsg += "\n - the pair builder '" + configuration.getPairBuilderName() + "'";
                error = true;
            }
            if (!repository.getSuccessQuantifiernRepository().containsEntityName(configuration.getSuccessQuantifierName())) {
                errorMsg += "\n - the success quantification '" + configuration.getSuccessQuantifierName() + "'";
                error = true;
            }
            if (!repository.getStrategyAdjusterRepository().containsEntityName(configuration.getStrategyAdjusterName())) {
                errorMsg += "\n - the strategy adjuster '" + configuration.getStrategyAdjusterName() + "'";
                error = true;
            }
            if (!repository.getEquilibriumCriterionRepository().containsEntityName(configuration.getEquilibriumCriterionName())) {
                errorMsg += "\n - the equilibrium criterion '" + configuration.getEquilibriumCriterionName() + "'";
            }
            if (error) {
                Alert alert = new Alert(AlertType.ERROR, errorMsg, ButtonType.OK);
                alert.showAndWait();
                return true;
            }
        }

        activeConfiguration = configuration;
        updateConfigurationPreview();
        
        return false;
    }

    private void updateConfigurationPreview() {
        Population population = CentralRepository.getInstance().getPopulationRepository().getEntityByName(activeConfiguration.getPopulationName());
        this.agentCountLabel.setText(String.valueOf(population.getSize()));
        this.gameNameLabel.setText(activeConfiguration.getGameName());
        this.iterationCountLabel.setText(String.valueOf(activeConfiguration.getIterationCount()));
        this.mixedStrategiesLabel.setText(activeConfiguration.getMixedAllowed() ? "Yes" : "No");
        this.populationNameLabel.setText(activeConfiguration.getPopulationName());
        this.roundCountLabel.setText(String.valueOf(activeConfiguration.getRoundCount()));
    }    
}
