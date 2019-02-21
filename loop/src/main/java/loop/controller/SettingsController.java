package loop.controller;

import java.io.File;
import java.io.IOException;

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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import loop.LoopSettings;
import loop.model.repository.FileIO;

public class SettingsController {
	private Stage stage;
	private LoopSettings settings;
	
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

		url_ListView.getItems().add("/home/loop_user/custom_folder_structure");

	}
	
	@FXML
	void addURL(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Add your own folder");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        File openFile = fileChooser.showOpenDialog(new Stage());
        if (!openFile.isDirectory()) {
        	Alert alert = new Alert(AlertType.ERROR, "Please choose a folder", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        else settings.addURL(openFile.getAbsolutePath());
        //TODO Add to List
	}
	
	@FXML
	void deleteURL(ActionEvent event) {
		//TODO
	}
	
	@FXML
	void confirmSettings(ActionEvent event) {
		settings.setEnable_notification(this.notification_CheckBox.isSelected());
		settings.setEnable_tooltip(this.tooltip_CheckBox.isSelected());
		settings.setReserveThread(this.reserveThread_CheckBox.isSelected());
		settings.setThreadcount((int) this.threadcount_Slider.getValue());
		try {
			FileIO.saveEntity(FileIO.SETTINGS_DIR, settings);
		} catch (IOException e) {
			e.printStackTrace();
		}
		//TODO Pass to HeadController
	}
}
