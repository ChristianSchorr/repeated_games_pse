package loop.controller;

import java.io.File;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
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
	private CheckBox reserveThread_CheckBox;
	
	@FXML
	private ListView url_ListView;

	private int threadCount;

	private BooleanProperty saveThread = new SimpleBooleanProperty();
	private IntegerProperty sliderVal = new SimpleIntegerProperty();

	public void setStage(Stage s) {
		this.stage = s;
	}
	
	public void setSettings(LoopSettings settings) {
		this.settings = settings;
	}
	
	@FXML
	void initialize() {
		threadCount = Runtime.getRuntime().availableProcessors();
		this.notification_CheckBox.setSelected(true);
		this.tooltip_CheckBox.setSelected(true);

		threadcount_Slider.setMin(1);
		threadcount_Slider.setMax(threadCount);
		threadcount_Slider.setMajorTickUnit(1);
		threadcount_Slider.setMinorTickCount(1);
		threadcount_Slider.setBlockIncrement(1);
		threadcount_Slider.valueProperty().bindBidirectional(sliderVal);
		sliderVal.addListener(c -> {
					if (saveThread.getValue()) {
						if (sliderVal.getValue() == threadCount)
							sliderVal.setValue(threadCount - 1);
					}
				});

		reserveThread_CheckBox.selectedProperty().bindBidirectional(saveThread);
		saveThread.addListener(c -> {
			if (saveThread.getValue()) {
				if (sliderVal.getValue() == threadCount)
					sliderVal.setValue(threadCount - 1);
			}
		});
		url_ListView.getItems().addAll(settings.getPersonalURLs());
	}
	
	@FXML
	void addURL(ActionEvent event) {
		DirectoryChooser dirChooser = new DirectoryChooser();
        dirChooser.setTitle("Add your own folder");
        dirChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        File openFile = dirChooser.showDialog(new Stage());
        if (!openFile.isDirectory()) {
        	Alert alert = new Alert(AlertType.ERROR, "Please choose a folder", ButtonType.OK);
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
		settings.setReserveThread(this.reserveThread_CheckBox.isSelected());
		settings.setThreadcount((int) this.threadcount_Slider.getValue());
		settings.save();
		this.stage.close();
	}
}
