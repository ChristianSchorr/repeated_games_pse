package loop.controller;

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
import loop.Main;
import loop.model.Group;
import loop.model.Population;
import loop.model.UserConfiguration;
import loop.model.repository.CentralRepository;
import loop.model.repository.FileIO;
import loop.model.simulationengine.Game;
import loop.model.simulator.SimulationResult;
import loop.model.simulator.Simulator;
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

    private Simulator simulator;

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
    
    private CentralRepository repository = CentralRepository.getInstance();
    
    @FXML
    void initialize() {
        //load default configuration
        updateConfiguration(UserConfiguration.getDefaultConfiguration(), false);

        //create simulator
        simulator = new ThreadPoolSimulator(Runtime.getRuntime().availableProcessors());
        
        //register callback for the import of configurations from simulation results
        historyViewController.registerImportUserConfiguration(config -> importConfiguration(config));
        historyViewController.registerCancleRequestHandler(sim -> simulator.stopSimulation(sim));
    }
    
    public void importConfiguration(UserConfiguration config) {
        updateConfiguration(config, true);
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
            controller.registerElementCreated((config) -> updateConfiguration(config, false));
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/windows/ConfigurationWindow.fxml"));
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
        new Thread(new ResultLoader(openFile)).start();
    }
    
    private class ResultLoader implements Runnable {
        
        File openFile;
        
        private ResultLoader(File openFile) {
            this.openFile = openFile;
        }

        @Override
        public void run() {
            setBufferingCursor();
            SimulationResult loadedResult = FileIO.loadResult(openFile);
            loadedResult.setId(simulator.getSimulationId());
            simulator.incrementSimulationId();
            addSimulationToHistoryController(loadedResult);
            setDefaultCursor();
        }
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
    void closeSimulator(ActionEvent event) {
        Platform.exit();
        simulator.stopAllSimulations();
    }

    @FXML
    void showInfo(ActionEvent event) {
    	//TODO
    }

    @FXML
    void showHelp(ActionEvent event) {
    	//TODO
    }

    @FXML
    void openNewGameWindow(ActionEvent event) {
        Parent newGameParent;
        NewGameController controller;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/windows/GameWindow.fxml"));
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
			newStrategyParent = FXMLLoader.load(getClass().getResource("/view/windows/StrategyWindow.fxml"));
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/windows/GroupWindow.fxml"));
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/windows/PopulationWindow.fxml"));
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

    private void updateConfiguration(UserConfiguration configuration, boolean checkForUnknownEntities) {
        if (checkForUnknownEntities) {
            boolean error = false;
            String errorMsg = "The opened configuration contains unknown entities:";
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
                return;
            }
        }
        
        activeConfiguration = configuration;
        updateConfigurationPreview();
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
