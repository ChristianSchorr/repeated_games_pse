package loop.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.controlsfx.control.ListSelectionView;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import loop.model.Group;
import loop.model.Segment;
import loop.model.plugin.Plugin;
import loop.model.plugin.PluginControl;
import loop.model.repository.CentralRepository;
import loop.model.repository.FileIO;
import loop.model.simulationengine.distributions.DiscreteDistribution;
import loop.model.simulationengine.distributions.DiscreteUniformDistribution;
import loop.model.plugin.PluginControl;

/**
 * This class represents the controller associated with the group creation window. It creates
 * new groups based on the user input and notifies the HeadController whenever a new
 * group has been created.
 * 
 * @author Pierre Toussing
 *
 */
public class GroupController implements CreationController<Group> {
	
    private static final String defaultDistributionName = DiscreteUniformDistribution.NAME;
    
    private Map<TabController,Tab> tabControllers = new HashMap<TabController,Tab>();
    
    private List<Consumer<Group>> elementCreatedHandlers = new ArrayList<Consumer<Group>>();
    
	/*------global properties-----*/
	@FXML 
	private TextField groupNameTextField;
	
	@FXML 
	private TextField descriptionTextField;
	
	@FXML
	private Button saveGroupButton;
	
	@FXML
	private Button resetGroupButton;
	
	/*------segment organisation-----*/
	
	@FXML
	private Button addSegmentButton;
	
	@FXML
	private CheckBox isCohesiveCheckBox;
	
	/*-----Tabs-----*/
	@FXML
	private TabPane segmentTabs;
	
	private Stage stage;
	
	@FXML 
	void initialize() {
	    segmentTabs.getTabs().addListener((ListChangeListener.Change<? extends Tab> c) -> {
            int i = 1;
            for (Tab tab: segmentTabs.getTabs()) {
                tab.setText("Segment " + (i++));
            }
        });
	    isCohesiveCheckBox.setSelected(true);
	    addSegmentTab();
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
    public void registerElementCreated(Consumer<Group> action) {
        elementCreatedHandlers.add(action);
    }
	
	/*------------------------------button handlers------------------------------*/
	
	@FXML
	void handleAddSegment(ActionEvent event) {
	    addSegmentTab();
	}
	
	@FXML
	void handleSave(ActionEvent event) {
	    Group group = createGroup();
	    
	    if (group.getName().trim().equals("") || group.getDescription().trim().equals("")) {
            Alert alert = new Alert(AlertType.ERROR, "Name and description must not be empty.", ButtonType.OK);
            alert.showAndWait();
            return;
	    }
	    
	    //save dialog
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Group");
        fileChooser.setInitialDirectory(FileIO.GROUP_DIR);
        fileChooser.setInitialFileName(group.getName().toLowerCase());
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Loop Group File", ".grp");
        fileChooser.getExtensionFilters().add(extFilter);
        File saveFile = fileChooser.showSaveDialog(stage);
        
        if (saveFile == null) {
            return;
        }
        
        try {
            FileIO.saveEntity(saveFile, group);
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR, "File could not be saved:\n" + e.getMessage(), ButtonType.OK);
            alert.showAndWait();
            return;
        }
        
        this.elementCreatedHandlers.forEach(handler -> handler.accept(group));
        stage.close();
	}
	
	@FXML
	void handleLoadGroup(ActionEvent event) {
	    FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load Group");
        fileChooser.setInitialDirectory(FileIO.GROUP_DIR);
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Loop Group File", ".grp");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(stage);
        
        if (file == null) {
            return;
        }
        
        Group group;
        try {
            group = (Group) FileIO.loadEntity(file);
        } catch (IOException e) {
            Alert alert = new Alert(AlertType.ERROR, "File could not be loaded.", ButtonType.OK);
            alert.showAndWait();
            e.printStackTrace();
            return;
        }
        
        groupNameTextField.setText(group.getName());
        descriptionTextField.setText(group.getDescription());
        isCohesiveCheckBox.setSelected(group.isCohesive());
        
        tabControllers.clear();
        segmentTabs.getTabs().clear();
        for (Segment segment: group.getSegments()) {
            TabController tabController = new TabController(this);
            Tab tab = new Tab();
            tab.setContent(tabController.getContent());
            tabControllers.put(tabController, tab);
            segmentTabs.getTabs().add(tab);
            
            tabController.distributionChoice.getSelectionModel().select(segment.getCapitalDistributionName());
            ((PluginControl) tabController.distributionPluginPane.getChildren().get(0)).setParameters(segment.getCapitalDistributionParameters());
            segment.getStrategyNames().forEach(s -> {
                tabController.strategyChoice.getSourceItems().remove(s);
                tabController.strategyChoice.getTargetItems().add(s);
            });
        }
        segmentTabs.getSelectionModel().select(0);
	}
	
	@FXML
	void handleReset(ActionEvent event) {
	    //confirm
        Alert alert = new Alert(AlertType.CONFIRMATION, "Are you sure you want to reset all settings?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.NO) {
            return;
        }
        
        //reset
	    groupNameTextField.setText("");
	    descriptionTextField.setText("");
	    tabControllers.clear();
	    segmentTabs.getTabs().clear();
	    isCohesiveCheckBox.setSelected(true);
	    addSegmentTab();
	}
	
	/*---------------------------------private helper methods---------------------------------*/
	
	private void addSegmentTab() {
        TabController tabController = new TabController(this);
        Tab newTab = new Tab();
        newTab.setContent(tabController.getContent());
        tabControllers.put(tabController, newTab);
        segmentTabs.getTabs().add(newTab);
        segmentTabs.getSelectionModel().select(newTab);
    }
	
	private void removeTab(TabController controller) {
	    tabControllers.remove(controller);
	    segmentTabs.getTabs().remove(tabControllers.get(controller));
	}
	
	private Group createGroup() {
	    List<Segment> segments = tabControllers.keySet().stream().map(c -> c.returnSegment()).collect(Collectors.toList());
	    double segmentSize = 1.0 / segments.size();
	    List<Double> segmentSizes = new ArrayList<Double>();
	    while (segmentSizes.size() < segments.size()) {
	        segmentSizes.add(segmentSize);
	    }
	    Group group = new Group(groupNameTextField.getText(), descriptionTextField.getText(), segments, segmentSizes, isCohesiveCheckBox.isSelected());
	    return group;
	}
	
	private class TabController {
	    
	    private static final String FXML_NAME = "/view/controls/segmentTab.fxml";
	    
		@FXML
		private ChoiceBox<String> distributionChoice;
		
		@FXML
		private Pane distributionPluginPane;
		
		@FXML
		private ListSelectionView<String> strategyChoice;
		
		private GroupController parent;
		
		@FXML
		private VBox content;
		
		public TabController(GroupController parent) {
		    FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_NAME));
		    loader.setController(this);
		    try {
                loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
		    this.parent = parent;
		}
		
		@FXML
		void initialize() {
		    //setup strategy choice
			for (String s : CentralRepository.getInstance().getStrategyRepository().getAllEntityNames()) {
				strategyChoice.getSourceItems().add(s);
			}
			
			//setup distribution choice
			distributionChoice.getItems().addAll(CentralRepository.getInstance()
                    .getDiscreteDistributionRepository().getAllEntityNames());
            distributionChoice.getSelectionModel().select(defaultDistributionName);
            distributionChoice.valueProperty().addListener((obs, oldDistName, newDistName) -> {
                distributionPluginPane.getChildren().clear();
                distributionPluginPane.getChildren().add(CentralRepository.getInstance().getDiscreteDistributionRepository()
                    .getEntityByName(newDistName).getRenderer().renderPlugin());
            });
            
			PluginControl p = CentralRepository.getInstance().getDiscreteDistributionRepository()
			        .getEntityByName(defaultDistributionName).getRenderer().renderPlugin();
			distributionPluginPane.getChildren().add(p);
		}
		
		@FXML
		void capitalDistributionSelected() {
			String selected = distributionChoice.getValue();
			Plugin<DiscreteDistribution> plugin = CentralRepository.getInstance()
					.getDiscreteDistributionRepository().getEntityByName(selected);
			PluginControl newpane = plugin.getRenderer().renderPlugin();
			distributionPluginPane.getChildren().clear();
			distributionPluginPane.getChildren().add(newpane);
		}
		
		@FXML
		void handleClosed(ActionEvent event) {
		    parent.removeTab(this);
		}
		
		/**
		 * Creates a Segment out of the user data.
		 * @return Segment created out of the user data
		 */
		public Segment returnSegment() {
			String capitalDistributionName = distributionChoice.getValue();
			List<Double> capitalDistributionParameters = ((PluginControl) distributionPluginPane.getChildren().get(0)).getParameters();
			List<String> strategyNames = new ArrayList<String>(strategyChoice.getTargetItems());		
			return new Segment(capitalDistributionName, capitalDistributionParameters, strategyNames);
		}
		
		public VBox getContent() {
		    return content;
		}
	}
}
