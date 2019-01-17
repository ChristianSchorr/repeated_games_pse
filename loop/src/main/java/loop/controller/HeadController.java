package loop.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import loop.model.UserConfiguration;
import loop.model.repository.FileIO;
import loop.model.simulator.Simulator;

/**
 * This controller is associated with the main window and thus instantiated when the program starts. 
 * It is also responsible for the communication between controller and model.
 * 
 * @author Pierre Toussing
 *
 */
public class HeadController {
	
	private static final String INITIAL_DIRECTORY = "./bin/main/personallib/Configurations";
			 
	private UserConfiguration configuration;
	
	private Simulator simulator;
	
	@FXML
	void saveConfiguration() {
		 FileChooser fileChooser = new FileChooser();
	     fileChooser.setTitle("Save Configuration");
	     fileChooser.setInitialDirectory(new File(INITIAL_DIRECTORY));
	     FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Loop Configuration File", ".config");
	     fileChooser.getExtensionFilters().add(extFilter);
	     File saveFile = fileChooser.showSaveDialog(new Stage());	       
	     try {
	         FileIO.saveEntity(saveFile, configuration);
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

	}
	
	@FXML
	void openNewGameWindow() {
		Parent newGameParent = null;
		try {
			newGameParent = FXMLLoader.load(getClass().getResource("NewGameWindow.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Scene newGameScene = new Scene(newGameParent);
		Stage newGameWindow = new Stage();
        newGameWindow.setTitle("Create a new Game");
        newGameWindow.setScene(newGameScene);

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
	void openNewGroupWindow() {
		Parent newGroupParent = null;
		try {
			newGroupParent = FXMLLoader.load(getClass().getResource("NewGroupWindow.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Scene newGroupScene = new Scene(newGroupParent);
		Stage newGroupWindow = new Stage();
        newGroupWindow.setTitle("Create a new Group");
        newGroupWindow.setScene(newGroupScene);

        // Specifies the modality for new window.
        newGroupWindow.initModality(Modality.WINDOW_MODAL);      
        newGroupWindow.show();
	}
	
	@FXML
	void openNewPopulationWindow() {
		Parent newPopulationParent = null;
		try {
			newPopulationParent = FXMLLoader.load(getClass().getResource("NewPopulationWindow.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Scene newPopulationScene = new Scene(newPopulationParent);
		Stage newPopulationWindow = new Stage();
        newPopulationWindow.setTitle("Create a new Population");
        newPopulationWindow.setScene(newPopulationScene);

        // Specifies the modality for new window.
        newPopulationWindow.initModality(Modality.WINDOW_MODAL);      
        newPopulationWindow.show();
	}
	
	public void initialize() {
		
	}
	@FXML
	private void updateConfiguration(UserConfiguration con) {
		this.configuration = con;
		updateConfigurationPreview();
	}
	
	private void updateConfigurationPreview() {
		
	}
 }
