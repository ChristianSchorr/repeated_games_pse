package loop.controller;

import java.io.File;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Region;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import loop.LoopSettings;

public class SettingsController {
	private Stage stage;
	private LoopSettings settings = LoopSettings.getInstance();
	
	@FXML
	private CheckBox notification_CheckBox;

	
	@FXML
	private CheckBox tooltip_CheckBox;

	@FXML
	private Slider threadcount_Slider;

	@FXML
	private ListView url_ListView;

	@FXML
	private Label warning;

	private int threadCount;


	public void setStage(Stage s) {
		this.stage = s;
	}
	
	public void setSettings(LoopSettings settings) {
		this.settings = settings;
		notification_CheckBox.setSelected(settings.isEnable_notification());
		tooltip_CheckBox.setSelected(settings.isEnable_tooltip());
		threadcount_Slider.setValue(settings.getThreadCount());
		url_ListView.getItems().addAll(settings.getPersonalURLs());
	}

	FadeTransition fadeIn = new FadeTransition(Duration.millis(750));
	FadeTransition fadeOut = new FadeTransition(Duration.millis(750));

	@FXML
	void initialize() {
		threadCount = Runtime.getRuntime().availableProcessors();

		fadeIn.setByValue(1);
		fadeIn.setNode(warning);

		fadeOut.setByValue(-1);
		fadeOut.setNode(warning);
		warning.setOpacity(0);

		threadcount_Slider.setMin(1);
		threadcount_Slider.setMax(threadCount);
		threadcount_Slider.setMajorTickUnit(1);
		threadcount_Slider.setMinorTickCount(1);
		threadcount_Slider.setBlockIncrement(1);
		threadcount_Slider.valueProperty().addListener((c, o , n) -> {
			if (n.intValue() == threadCount) fadeIn.playFromStart();
			else fadeOut.playFromStart();
		});
	}
	
	@FXML
	void addURL(ActionEvent event) {
		DirectoryChooser dirChooser = new DirectoryChooser();
        dirChooser.setTitle("Add your own folder");
        dirChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        File openFile = dirChooser.showDialog(new Stage());
        if (!openFile.isDirectory()) {
        	Alert alert = new Alert(AlertType.ERROR, "Please choose a folder", ButtonType.OK);
			alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        	alert.showAndWait();
            return;
        }
        else {
        	settings.addURL(openFile.getAbsolutePath());
        	url_ListView.getItems().add(openFile.getAbsolutePath());
        }
	}
	
	@FXML
	void deleteURL(ActionEvent event) {
		settings.deleteURL((String) url_ListView.getSelectionModel().getSelectedItem());
		url_ListView.getItems().remove(url_ListView.getSelectionModel().getSelectedIndex());
	}
	
	@FXML
	void confirmSettings(ActionEvent event) {
		settings.setEnable_notification(this.notification_CheckBox.isSelected());
		settings.setEnable_tooltip(this.tooltip_CheckBox.isSelected());
		settings.setThreadCount((int) this.threadcount_Slider.getValue());
		settings.save();
		this.stage.close();
	}
}
