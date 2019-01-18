package loop.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import loop.model.Group;
import loop.model.Population;
import loop.model.repository.CentralRepository;
import loop.model.repository.FileIO;

/**
 * This class represents the controller associated with the population creation window. It
 * creates new populations based on the user input and notifies the {@link HeadController} whenever
 * a new population has been created.
 * 
 * @author Peter Koepernik
 *
 */
public class PopulationController implements CreationController<Population> {
    
    private static final String INITIAL_DIRECTORY = "./bin/main/personallib/Populations";
    
    private List<String> selectedGroups = new ArrayList<String>();
    private List<Integer> groupSizes = new ArrayList<Integer>();
    private List<GroupCellController> cellControllers = new ArrayList<GroupCellController>();
    
    private List<Consumer<Population>> elementCreatedHandlers = new ArrayList<Consumer<Population>>();
    
    /*-----------------FXML variables-----------------*/
    
    @FXML
    private TextField populationNameTextField;
    
    @FXML
    private TextField populationDescriptionTextField;
    
    @FXML
    private ComboBox<String> groupSelectionBox;
    
    @FXML
    private TextField agentCountTextField;
    
    @FXML
    private Button addGroupButton;
    
    @FXML
    private FlowPane groupPane;
    
    @FXML
    private Label totalAgentCountLabel;
    private int totalAgentCount = 0;
    
    //the stage of the population creator window
    private Stage stage;
    
    @FXML
    private void initialize() {
        
    }
    
    /**
     * Set the stage of this population creation window. Must be called by the creating controller upon creation.
     * 
     * @param stage the stage
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
    @Override
    public void registerElementCreated(Consumer<Population> action) {
        elementCreatedHandlers.add(action);
    }
    
    /*------------------------------button handlers------------------------------*/
    
    @FXML
    private void handleAddGroupButton(ActionEvent action) {
        //get name and agent count
        String groupName = groupSelectionBox.getValue();
        int agentCount = 0;
        try {
            agentCount = Integer.parseInt(agentCountTextField.getText());
        } catch(NumberFormatException e) {
            Alert alert = new Alert(AlertType.ERROR, "Illegal input for agent count.", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        
        //update flow pane
        GroupCellController cellController = new GroupCellController(groupName, agentCount, this);
        groupPane.getChildren().add(cellController.getContainer());
        
        //update agent count
        totalAgentCount += agentCount;
        totalAgentCountLabel.setText(String.format("%d", totalAgentCount));
        
        //add to lists
        selectedGroups.add(groupName);
        groupSizes.add(agentCount);
        cellControllers.add(cellController);
    }
    
    @FXML
    private void resetPopulation(ActionEvent event) {
        //confirm
        Alert alert = new Alert(AlertType.CONFIRMATION, "Are you sure you want to reset all settings?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.NO) {
            return;
        }
        
        //reset
        this.agentCountTextField.setText("0");
        this.populationDescriptionTextField.setText("");
        this.populationNameTextField.setText("");
        groupPane.getChildren().clear();
        this.selectedGroups.clear();
        this.groupSizes.clear();
        this.cellControllers.clear();
    }
    
    @FXML
    private void handleSavePopulationButton(ActionEvent event) {
        Population population = createPopulation();
        
        if (population.getName().trim() == "" || population.getDescription().trim() == "") {
            Alert alert = new Alert(AlertType.ERROR, "Name and description must not be empty.", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        
        //save dialog
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Population");
        fileChooser.setInitialDirectory(new File(INITIAL_DIRECTORY));
        fileChooser.setInitialFileName(population.getName().toLowerCase());
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Loop Population File", ".pop");
        fileChooser.getExtensionFilters().add(extFilter);
        File saveFile = fileChooser.showSaveDialog(stage);
        
        if (saveFile == null) {
            return;
        }
        
        try {
            FileIO.saveEntity(saveFile, population);
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR, "File could not be saved.", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        
        this.elementCreatedHandlers.forEach(handler -> handler.accept(population));
        stage.close();
    }
    
    /*---------------------------------private helper methods---------------------------------*/
    
    private void removeGroupCell(GroupCellController cell) {
        int index = cellControllers.indexOf(cell);
        selectedGroups.remove(index);
        groupSizes.remove(index);
        cellControllers.remove(index);
        groupPane.getChildren().remove(cell.getContainer());
        totalAgentCount -= cell.getAgentCount();
        totalAgentCountLabel.setText(String.format("%d", cell.getAgentCount()));
    }
    
    private Population createPopulation() {
        List<Group> groups = selectedGroups.stream().map(
                g -> CentralRepository.getInstance().getGroupRepository().getEntityByName(g)).collect(Collectors.toList());
        Population population = new Population(this.populationNameTextField.getText(), this.populationDescriptionTextField.getText(),
                groups, groupSizes);
        return population;
    }
    
    private class GroupCellController {
        
        private static final String FXML_NAME = "groupCell.fxml";
        
        private String groupName;
        private int agentCount;
        private PopulationController parent;
        
        @FXML
        private Label groupNameLabel;
        
        @FXML
        private Label groupCountLabel;
        
        @FXML
        private HBox container;
        
        public GroupCellController(String groupName, int agentCount, PopulationController parent) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(FXML_NAME));
            fxmlLoader.setController(this);
            try {
                fxmlLoader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            this.groupName = groupName;
            this.agentCount = agentCount;
            this.parent = parent;
        }
        
        @FXML
        void intialize() {
            groupNameLabel.setText(groupName);
            groupCountLabel.setText(agentCount + " agents.");
        }
        
        @FXML
        private void handleClosed(ActionEvent event) {
            parent.removeGroupCell(this);
        }
        
        /**
         * Return the container of this node.
         * 
         * @return the container of this node
         */
        public HBox getContainer() {
            return container;
        }
        
        public int getAgentCount() {
            return agentCount;
        }
    }
    
}
