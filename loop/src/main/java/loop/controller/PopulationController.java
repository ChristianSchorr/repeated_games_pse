package loop.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.converter.NumberStringConverter;
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
    private ChoiceBox<String> groupSelectionBox;
    
    @FXML
    private TextField agentCountTextField;

    @FXML
    private FlowPane groupPane;
    
    @FXML
    private Label totalAgentCountLabel;

    private IntegerProperty totalAgentCountProperty = new SimpleIntegerProperty();
    private IntegerProperty agentCountProperty = new SimpleIntegerProperty();
    
    private Stage stage;
    
    /**
    * Set the stage of this population creation window. Must be called by the creating controller upon creation.
    * 
    * @param stage the stage
    */
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
    @FXML
    private void initialize() {
        totalAgentCountLabel.textProperty().bindBidirectional(totalAgentCountProperty, new NumberStringConverter());
        totalAgentCountProperty.setValue(0);
        agentCountTextField.textProperty().bindBidirectional(agentCountProperty, new NumberStringConverter());
        agentCountProperty.setValue(20);
        
        groupSelectionBox.getItems().addAll(CentralRepository.getInstance().getGroupRepository().getAllEntityNames());
        groupSelectionBox.getSelectionModel().select(0);
    }

    @Override
    public void registerElementCreated(Consumer<Population> action) {
        elementCreatedHandlers.add(action);
    }
    
    /*------------------------------button handlers------------------------------*/
    
    @FXML
    private void handleAddGroupButton(ActionEvent action) {
        if (agentCountProperty.getValue() <= 0) {
            Alert alert = new Alert(AlertType.ERROR, "A group must consits of at least one agent.", ButtonType.OK);
            alert.showAndWait();
            return;
        } 
        
        //get name and agent count
        String groupName = groupSelectionBox.getValue();
        int agentCount = agentCountProperty.getValue();
        
        //check if group is already there
        Optional<GroupCellController> opt = cellControllers.stream().filter(c -> c.getGroupName().equals(groupName)).findAny();
        if (opt.isPresent()) {
            GroupCellController groupCell = opt.get();
            groupCell.increaseAgentCount(agentCount);
            totalAgentCountProperty.setValue(totalAgentCountProperty.getValue() + agentCount);
            
            int index = cellControllers.indexOf(groupCell);
            groupSizes.remove(index);
            groupSizes.add(index, groupCell.getAgentCount());
            
            return;
        }
        
        //update flow pane
        GroupCellController cellController = new GroupCellController(groupName, agentCount, this);
        groupPane.getChildren().add(cellController.getContainer());
        
        //update agent count
        totalAgentCountProperty.setValue(totalAgentCountProperty.getValue() + agentCount);
        
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
        this.groupSelectionBox.getSelectionModel().select(0);
        agentCountProperty.setValue(20);
        totalAgentCountProperty.setValue(0);
        this.populationDescriptionTextField.setText("");
        this.populationNameTextField.setText("");
        groupPane.getChildren().clear();
        this.selectedGroups.clear();
        this.groupSizes.clear();
        this.cellControllers.clear();
    }
    
    @FXML
    void exportPopulation(ActionEvent event) {        
        if (!validateSettings(true)) return;
        
        Population population = createPopulation();
        
        //save dialog
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Population");
        fileChooser.setInitialDirectory(FileIO.POPULATION_DIR);
        fileChooser.setInitialFileName(population.getName().toLowerCase());
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Loop Population File", "*.pop");
        fileChooser.getExtensionFilters().add(extFilter);
        Window stage = ((Node) event.getTarget()).getScene().getWindow();
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
    }
    
    @FXML
    void savePopulation() {
        if (!validateSettings(true)) return;
        
        //TODO unschön, aber wie sonst?
        if (CentralRepository.getInstance().getPopulationRepository().containsEntityName(populationNameTextField.getText())) {
            Alert alert = new Alert(AlertType.CONFIRMATION, "A population with this name already exists. Do you want to overwrite it? Note that"
                    + " in that case all configurations that currently use the overwritten population would from now on use this one instead.",
                    ButtonType.YES, ButtonType.NO);
            alert.showAndWait();
            boolean override = (alert.getResult() == ButtonType.YES);
            if (!override) return;
            CentralRepository.getInstance().getPopulationRepository().removeEntity(populationNameTextField.getText());
        }
        
        Population population = createPopulation();
        
        this.elementCreatedHandlers.forEach(handler -> handler.accept(population));
        this.stage.close();
    }
    
    private boolean validateSettings(boolean alertIfFaulty) {
        if (populationNameTextField.getText().trim().equals("") || populationDescriptionTextField.getText().trim().equals("")) {
            if (alertIfFaulty) {
                Alert alert = new Alert(AlertType.ERROR, "Name and description must not be empty.", ButtonType.OK);
                alert.showAndWait();
            }
            return false;
        } else if (this.selectedGroups.isEmpty()) {
            if (alertIfFaulty) {
                Alert alert = new Alert(AlertType.ERROR, "A population must consist of at least one group.", ButtonType.OK);
                alert.showAndWait();
            }
            return false;
        }
        return true;
    }
    
    @FXML
    private void handleLoadPopulation(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load Population");
        fileChooser.setInitialDirectory(FileIO.POPULATION_DIR);
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Loop Population File", "*.pop");
        fileChooser.getExtensionFilters().add(extFilter);
        Window stage = ((Node) event.getTarget()).getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);
        
        if (file == null) {
            return;
        }
        
        Population population;
        try {
            population = (Population) FileIO.loadEntity(file);
        } catch (IOException e) {
            Alert alert = new Alert(AlertType.ERROR, "File could not be loaded.", ButtonType.OK);
            alert.showAndWait();
            e.printStackTrace();
            return;
        }
        
        populationNameTextField.setText(population.getName());
        populationDescriptionTextField.setText(population.getDescription());
        
        selectedGroups.clear();
        groupSizes.clear();
        cellControllers.clear();
        groupPane.getChildren().clear();
        this.totalAgentCountProperty.setValue(population.getSize());
        totalAgentCountLabel.setText(String.format("%d", totalAgentCountProperty.getValue()));
        
        for (Group group: population.getGroups()) {
            String groupName = group.getName();
            int agentCount = population.getGroupSize(group);
            
            //update flow pane
            GroupCellController cellController = new GroupCellController(groupName, agentCount, this);
            groupPane.getChildren().add(cellController.getContainer());
            
            //add to lists
            selectedGroups.add(groupName);
            groupSizes.add(agentCount);
            cellControllers.add(cellController);
        }
    }
    
    /*---------------------------------private helper methods---------------------------------*/
    
    private void removeGroupCell(GroupCellController cell) {
        int index = cellControllers.indexOf(cell);
        selectedGroups.remove(index);
        groupSizes.remove(index);
        cellControllers.remove(index);
        groupPane.getChildren().remove(cell.getContainer());
        totalAgentCountProperty.setValue(totalAgentCountProperty.getValue() - cell.getAgentCount());
        totalAgentCountLabel.setText(String.format("%d", totalAgentCountProperty.getValue()));
    }
    
    private Population createPopulation() {
        List<Group> groups = selectedGroups.stream().map(
                g -> CentralRepository.getInstance().getGroupRepository().getEntityByName(g)).collect(Collectors.toList());
        Population population = new Population(this.populationNameTextField.getText(), this.populationDescriptionTextField.getText(),
                groups, groupSizes);
        return population;
    }
    
    private class GroupCellController {
        
        private static final String FXML_NAME = "/view/controls/groupCell.fxml";
        
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
            this.groupName = groupName;
            this.agentCount = agentCount;
            this.parent = parent;
            try {
                fxmlLoader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        
        @FXML
        void initialize() {
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
        
        /**
         * Return the amount of agents.
         * 
         * @return the amount of agents
         */
        public int getAgentCount() {
            return agentCount;
        }
        
        /**
         * Return the name of the group.
         * 
         * @return the name of the group
         */
        public String getGroupName() {
            return groupName;
        }
        
        /**
         * Increase the agent count by the given amount.
         * 
         * @param count the number tha agent count shall be increased by
         */
        public void increaseAgentCount(int count) {
            agentCount += count;
            groupCountLabel.setText(agentCount + " agents.");
        }
    }
    
}
