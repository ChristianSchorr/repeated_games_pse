package loop.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
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
 *
 */
public class HeadController {
	
	private static final String INITIAL_DIRECTORY = "./bin/main/personallib/Configurations";
			 
	private UserConfiguration activeConfiguration;
	
	private Simulator simulator;
	
	private HistoryController historyController;
	
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
	void initialize() {
	    //setup history
	    FXMLLoader loader = new FXMLLoader(getClass().getResource("history.fxml"));
	    historyController = loader.getController();
	    try {
            historyView = loader.load();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
	    
	    //load default configuration
	    updateConfiguration(UserConfiguration.getDefaultConfiguration());
	    
	    //create simulator
	    simulator = new ThreadPoolSimulator(Runtime.getRuntime().availableProcessors());
	}
	
	@FXML
	void saveConfiguration() {
		 FileChooser fileChooser = new FileChooser();
	     fileChooser.setTitle("Save Configuration");
	     fileChooser.setInitialDirectory(new File(INITIAL_DIRECTORY));
	     FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Loop Configuration File", ".config");
	     fileChooser.getExtensionFilters().add(extFilter);
	     File saveFile = fileChooser.showSaveDialog(new Stage());	       
	     try {
	         FileIO.saveEntity(saveFile, activeConfiguration);
	     } catch (IOException e) {
	         e.printStackTrace();
	         Alert alert = new Alert(AlertType.ERROR, "File could not be saved.", ButtonType.OK);
	         alert.showAndWait();
	         return;
	     }
	}
	
	@FXML
	void loadConfiguration() {
		FileChooser fileChooser = new FileChooser();
	     fileChooser.setTitle("Open Configuration");
	     fileChooser.setInitialDirectory(new File(INITIAL_DIRECTORY));
	     FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Loop Configuration File", ".config");
	     fileChooser.getExtensionFilters().add(extFilter);
	     File openFile = fileChooser.showOpenDialog(new Stage());	
	     try {
			updateConfiguration(FileIO.loadEntity(openFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			Alert alert = new Alert(AlertType.ERROR, "File could not be opened.", ButtonType.OK);
	         alert.showAndWait();
		}
	}
	
	@FXML 
	void editConfiguraiton() {
		Parent configParent = null;
		try {
			configParent = FXMLLoader.load(getClass().getResource("ConfigurationWindow.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Scene configScene = new Scene(configParent);
		Stage configWindow = new Stage();
        configWindow.setTitle("Edit configuration");
        configWindow.setScene(configScene);

        // Specifies the modality for new window.
        configWindow.initModality(Modality.WINDOW_MODAL);      
        configWindow.show();
	}
		
	@FXML
	void loadResults() {
		
	}
	
	@FXML
	void startSimuation() {
	    SimulationResult result;
        try {
            result = simulator.startSimulation(activeConfiguration);
        } catch (ConfigurationException e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR, "Active configuration is faulty.", ButtonType.OK);
            alert.showAndWait();
            return;
        }
	    historyController.addSimulation(result);
	}
	
	@FXML
	void openNewGameWindow() {
		Parent newGameParent = null;
		NewGameController controller = null;
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("NewGameWindow.fxml"));
			controller = loader.getController();
		    newGameParent = loader.load();
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
        newGameWindow.initModality(Modality.WINDOW_MODAL);      
        newGameWindow.show();
	}

	/*
	@FXML
	void openNewStrategyWindow() {
		Parent newStrategyParent = null;
		try {
			newStrategyParent = FXMLLoader.load(getClass().getResource("NewStrategyWindow.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Scene newStrategyScene = new Scene(newStrategyParent);
		Stage newStrategyWindow = new Stage();
        newStrategyWindow.setTitle("Create a new Strategy");
        newStrategyWindow.setScene(newStrategyScene);

        // Specifies the modality for new window.
        newStrategyWindow.initModality(Modality.WINDOW_MODAL);      
        newStrategyWindow.show();
	}
	*/
	
	@FXML
	void openNewGroupWindow(ActionEvent event) {
		Parent newGroupParent = null;
		GroupController controller = null;
		try {
		    FXMLLoader loader = new FXMLLoader(getClass().getResource("NewGroupWindow.fxml"));
		    controller = (GroupController) loader.getController();
			newGroupParent = loader.load();
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
        newGroupWindow.initModality(Modality.WINDOW_MODAL);      
        newGroupWindow.show();
	}
	
	@FXML
	void openNewPopulationWindow() {
		Parent newPopulationParent = null;
		PopulationController controller = null;
		try {
		    FXMLLoader loader = new FXMLLoader(getClass().getResource("NewPopulationWindow.fxml"));
		    controller = (PopulationController) loader.getController();
			newPopulationParent = loader.load();
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
        newPopulationWindow.initModality(Modality.WINDOW_MODAL);      
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
	
	private void updateConfiguration(UserConfiguration configuration) {
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
