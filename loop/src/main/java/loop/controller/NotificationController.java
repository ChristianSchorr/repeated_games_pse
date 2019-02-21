package loop.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import loop.model.simulator.SimulationResult;

import java.io.IOException;

public class NotificationController {

    private final static String FXML_NAME = "/view/controls/NotificationControl.fxml";


    SimulationResult item;

    @FXML
    private HBox container;

    @FXML
    private Label simulationIdLabel;

    @FXML
    private Label gameNameLabel;

    public NotificationController(SimulationResult item) {
        this.item = item;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(FXML_NAME));
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @FXML
    void initialize() {
        simulationIdLabel.setText(String.format("#%03d", item.getId()));
        gameNameLabel.setText(item.getUserConfiguration().getGameName());
    }


    public HBox getContainer() {
        return container;
    }
}
