package loop.controller;

import java.util.ArrayList;
import java.util.function.Consumer;

import org.controlsfx.control.ListSelectionView;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import loop.model.Group;
import loop.model.Segment;
import loop.model.repository.CentralRepository;
import loop.model.simulationengine.distributions.Distribution;
import loop.model.simulationengine.strategies.Strategy;

/**
 * This class represents the controller associated with the group creation window. It creates
 * new groups based on the user input and notifies the HeadController whenever a new
 * group has been created.
 * 
 * @author Pierre Toussing
 *
 */
public class GroupController implements CreationController<Group> {
	private Group group;
	private String name, description;
	private ArrayList<Segment> segments;
	
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
	private Slider segmentProportionsSlider;
	
	@FXML
	private Button addSegmentButton;
	
	@FXML
	private CheckBox allAgentsGrouplessCheckBox;
	
	/*-----Tabs-----*/
	@FXML
	private TabPane tabs;

	@FXML 
	void initialize() {
		
	}
	
	/*---------------------Handlers-----------*/
	@FXML
	private void setName() {
		name = groupNameTextField.getText();
	}
	
	@FXML
	private void setDescription() {
		description = descriptionTextField.getText();
	}
	
	/*----------------------------------------*/
	
	@Override
	public void registerElementCreated(Consumer<Group> action) {
		// TODO Auto-generated method stub
		
	}
	
	private class TabController {
		
		@FXML
		private ComboBox distributionChoice;
		
		@FXML
		private FlowPane distributionPluginPane;
		
		@FXML
		private ListSelectionView strategyChoice;
		
		@FXML
		void initialize() {
			for (String s : CentralRepository.getInstance().getStrategyRepository().getAllEntityNames()) {
				strategyChoice.getSourceItems().add(s);
			}
			
		}
	}
}
