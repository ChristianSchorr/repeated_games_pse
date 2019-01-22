package loop.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import loop.model.repository.FileIO;
import loop.model.simulationengine.ConcreteGame;
import loop.model.simulationengine.Game;

/**
 * This class represents the controller associated with the game creation window. It creates
 * new games based on the user input and notifies the {@link HeadController} whenever a new
 * game has been created.
 * 
 * @author Peter Koepernik
 *
 */
public class NewGameController implements CreationController<Game> {
    
    private List<Consumer<Game>> elementCreatedHandlers = new ArrayList<Consumer<Game>>();
    
    /*-----------------FXML variables-----------------*/
    
    @FXML
    private TextField gameNameTextField;
    
    @FXML
    private TextField gameDescriptionTextField;
    
    @FXML
    private TextField cc1TextField;
    @FXML
    private TextField cn1TextField;
    @FXML
    private TextField nc1TextField;
    @FXML
    private TextField nn1TextField;
    @FXML
    private TextField cc2TextField;
    @FXML
    private TextField cn2TextField;
    @FXML
    private TextField nc2TextField;
    @FXML
    private TextField nn2TextField;
    
    private Stage stage;
    
    /**
     * Set the stage of this population creation window. Must be called by the creating controller upon creation.
     * 
     * @param stage the stage
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
    @Override
    public void registerElementCreated(Consumer<Game> action) {
        elementCreatedHandlers.add(action);
    }
    
    /*------------------------------button handlers------------------------------*/
    
    @FXML
    private void handleReset() {
    	 //confirm
        Alert alert = new Alert(AlertType.CONFIRMATION, "Are you sure you want to reset all settings?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.NO) {
            return;
        }
      
        gameNameTextField.setText("");
        gameDescriptionTextField.setText("");
        
        cc1TextField.setText("");
        cn1TextField.setText("");
        nc1TextField.setText("");
        nn1TextField.setText("");
        cc2TextField.setText("");
        cn2TextField.setText("");
        nc2TextField.setText("");
        nn2TextField.setText("");
    }
    
    @FXML
    private void handleSaveGame() {
        Game game;
        try {
            game = createGame();
        } catch (IllegalArgumentException e) {
            Alert alert = new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.showAndWait();
            return;
        }
        
        //save dialog
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Game");
        fileChooser.setInitialDirectory(FileIO.GAME_DIR);
        fileChooser.setInitialFileName(game.getName().toLowerCase());
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Loop Game File", ".gam");
        fileChooser.getExtensionFilters().add(extFilter);
        File saveFile = fileChooser.showSaveDialog(stage);
        
        if (saveFile == null) {
            return;
        }
        
        try {
            FileIO.saveEntity(saveFile, game);
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR, "File could not be saved.", ButtonType.OK);
            alert.showAndWait();
            return;
        };
        
        this.elementCreatedHandlers.forEach(handler -> handler.accept(game));
        stage.close();
    }
    
    /*---------------------------------private helper methods---------------------------------*/
    
    private Game createGame() throws IllegalArgumentException {
        //check payoffs
        int cc1, cn1, nc1, nn1, cc2, cn2, nc2, nn2;
        try {
            cc1 = Integer.parseInt(cc1TextField.getText());
            cn1 = Integer.parseInt(cn1TextField.getText());
            nc1 = Integer.parseInt(nc1TextField.getText());
            nn1 = Integer.parseInt(nn1TextField.getText());
            cc2 = Integer.parseInt(cc2TextField.getText());
            cn2 = Integer.parseInt(cn2TextField.getText());
            nc2 = Integer.parseInt(nc2TextField.getText());
            nn2 = Integer.parseInt(nn2TextField.getText());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Payoffs must be integers.");
        }
        
        //check name and description
        String name = gameNameTextField.getText();
        String description = gameDescriptionTextField.getText();

        if (name.trim().equals("") || description.trim().equals("")) {
            throw new IllegalArgumentException("Name and description must not be empty.");
        }
        
        //create and return game
        return new ConcreteGame(name, description, cc1, cn1, nc1, nn1, cc2, cn2, nc2, nn2);
    }

}
