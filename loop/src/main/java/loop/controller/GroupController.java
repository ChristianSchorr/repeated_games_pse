package loop.controller;

import java.util.List;
import java.util.function.Consumer;

import org.controlsfx.control.ListSelectionView;

import javafx.collections.ObservableList;
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
import loop.model.plugin.Plugin;
import loop.model.plugin.PluginControl;
import loop.model.repository.CentralRepository;
import loop.model.simulationengine.distributions.DiscreteDistribution;

/**
 * This class represents the controller associated with the group creation window. It creates
 * new groups based on the user input and notifies the HeadController whenever a new
 * group has been created.
 * 
 * @author Pierre Toussing
 *
 */
public class GroupController implements CreationController<Group> {
	
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
	
	/*----------------------------------------*/
	
	@Override
	public void registerElementCreated(Consumer<Group> action) {
		// TODO Auto-generated method stub
		
	}
	
	private class TabController {
		
		@FXML
		private ComboBox<String> distributionChoice;
		
		@FXML
		private FlowPane distributionPluginPane;
		
		@FXML
		private ListSelectionView<String> strategyChoice;
		
		@FXML
		void initialize() {
			for (String s : CentralRepository.getInstance().getStrategyRepository().getAllEntityNames()) {
				strategyChoice.getSourceItems().add(s);
			}
			PluginControl p = null;
			distributionPluginPane.getChildren().add(p);
			distributionChoice.setItems((ObservableList<String>) CentralRepository.getInstance()
					.getDiscreteDistributionRepository().getAllEntityNames());
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
		
		/**
		 * Creates a Segment out of the user data.
		 * @return Segment created out of the user data
		 */
		private Segment returnSegment() {
			String capitalDistributionName = (String) distributionChoice.getValue();
			List<Double> capitalDistributionParameters = ((PluginControl) distributionPluginPane.getChildren().get(0)).getParameters();
			List<String> strategyNames = strategyChoice.getTargetItems();		
			return new Segment(capitalDistributionName, capitalDistributionParameters, strategyNames);
		}
	}
}
