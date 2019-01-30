package loop.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import loop.model.Population;
import loop.model.repository.CentralRepository;
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
    
    @FXML
    private Menu loadMenu;
    
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
    
    @FXML
    void initialize() {
        CentralRepository.getInstance().getGameRepository().getAllEntityNames().forEach(gameName -> {
            MenuItem gameItem = new MenuItem(gameName);
            loadMenu.getItems().add(gameItem);
            gameItem.setOnAction(event -> setGame(CentralRepository.getInstance().getGameRepository().getEntityByName(gameName), true));
        });
    }
    
    /*------------------------------button handlers------------------------------*/
    
    @FXML
    void resetGame() {
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
    void exportGame() {
        if (!validateSettings(true)) return;
        
        Game game = createGame();
        
        //save dialog
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Game");
        fileChooser.setInitialDirectory(FileIO.GAME_DIR);
        fileChooser.setInitialFileName(game.getName().toLowerCase().replace(' ', '_'));
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Loop Game File", "*.gam");
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
    }
    
    @FXML
    void saveGame() {
        if (!validateSettings(true)) return;
        
        //TODO unschön, aber wie sonst?
        if (CentralRepository.getInstance().getGameRepository().containsEntityName(gameNameTextField.getText())) {
            Alert alert = new Alert(AlertType.CONFIRMATION, "A game with this name already exists. Do you want to overwrite it? Note that"
                    + " in that case all configurations that currently use the overwritten game would from now on use this one instead.",
                    ButtonType.YES, ButtonType.NO);
            alert.showAndWait();
            boolean override = (alert.getResult() == ButtonType.YES);
            if (!override) return;
            CentralRepository.getInstance().getPopulationRepository().removeEntity(gameNameTextField.getText());
        }
        
        Game game = createGame();
        
        this.elementCreatedHandlers.forEach(handler -> handler.accept(game));
        stage.close();
    }
    
    private boolean validateSettings(boolean alertIfFaulty) {
        if (gameNameTextField.getText().trim().equals("") || gameDescriptionTextField.getText().trim().equals("")) {
            if (alertIfFaulty) {
                Alert alert = new Alert(AlertType.ERROR, "Name and description must not be empty.", ButtonType.OK);
                alert.showAndWait();
            }
            return false;
        }
        try {
            Integer.parseInt(cc1TextField.getText());
            Integer.parseInt(cn1TextField.getText());
            Integer.parseInt(nc1TextField.getText());
            Integer.parseInt(nn1TextField.getText());
            Integer.parseInt(cc2TextField.getText());
            Integer.parseInt(cn2TextField.getText());
            Integer.parseInt(nc2TextField.getText());
            Integer.parseInt(nn2TextField.getText());
        } catch (NumberFormatException e) {
            if (alertIfFaulty) {
                Alert alert = new Alert(AlertType.ERROR, "Payoffs must be integers.", ButtonType.OK);
                alert.showAndWait();
            }
            return false;
        }
        return true;
    }
    
    @FXML
    void importGame() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load Game");
        fileChooser.setInitialDirectory(FileIO.GAME_DIR);
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Loop Game File", "*.gam");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(stage);
        
        if (file == null) {
            return;
        }
        
        Game game;
        try {
            game = (Game) FileIO.loadEntity(file);
        } catch (IOException e) {
            Alert alert = new Alert(AlertType.ERROR, "File could not be opened.", ButtonType.OK);
            alert.showAndWait();
            e.printStackTrace();
            return;
        }
        
        setGame(game, true);
    }
    
    private void setGame(Game game, boolean showErrorDialog) {
        ConcreteGame cGame = null;
        try {
            cGame = (ConcreteGame) game;
        } catch(ClassCastException e) {
            if (showErrorDialog) {
                Alert alert = new Alert(AlertType.ERROR, "Failed to load game.", ButtonType.OK);
                alert.showAndWait();
            }
            return;
        }
        
        gameNameTextField.setText(game.getName());
        gameDescriptionTextField.setText(game.getDescription());
        
        cc1TextField.setText(String.valueOf(cGame.getCC1()));
        cn1TextField.setText(String.valueOf(cGame.getCN1()));
        nc1TextField.setText(String.valueOf(cGame.getNC1()));
        nn1TextField.setText(String.valueOf(cGame.getNN1()));
        cc2TextField.setText(String.valueOf(cGame.getCC2()));
        cn2TextField.setText(String.valueOf(cGame.getCN2()));
        nc2TextField.setText(String.valueOf(cGame.getNC2()));
        nn2TextField.setText(String.valueOf(cGame.getNN2()));
    }
    
    /*---------------------------------private helper methods---------------------------------*/
    
    private Game createGame() {
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
        
        //create and return game
        return new ConcreteGame(name, description, cc1, cn1, nc1, nn1, cc2, cn2, nc2, nn2);
    }

}
