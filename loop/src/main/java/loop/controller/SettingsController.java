package loop.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.stage.Stage;
import loop.LoopSettings;

public class SettingsController {
	private Stage stage;
	private LoopSettings settings;
	
	@FXML
	private CheckBox notification_CheckBox;
	
	@FXML
	private Label notification_Label;
	
	@FXML
	private CheckBox tooltip_CheckBox;
	
	@FXML
	private Label tooltip_Label;
	
	@FXML
	private Slider threadcount_Slider;
	
	@FXML
	private CheckBox reserveThread_CheckBox;
	
	@FXML
	private ListView url_ListView;
	
	@FXML
	private Button deleteURL_Button;
	
	@FXML
	private Button addURL_Button;

	public void setStage(Stage s) {
		this.stage = s;
	}
	
	@FXML
	void initialize() {
		this.notification_Label.setText("Get a notification whan a simulation finished");
		this.notification_CheckBox.setSelected(true);
		this.tooltip_Label.setText("Show tooltips");
		this.tooltip_CheckBox.setSelected(true);
	}
}
