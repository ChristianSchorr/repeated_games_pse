package loop.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import loop.model.simulationengine.strategy.strategybuilder.Operator;

import java.io.IOException;
import java.util.function.Consumer;

public class StrategyCellController {

    private static final String FXML_NAME = "/view/controls/StrategyCell.fxml";

    @FXML
    private Label strategyLabel;

    @FXML
    private HBox container;

    Consumer<StrategyCellController> closeNotifier;
    private String strategyText;

    public StrategyCellController(String strategyText, Consumer<StrategyCellController> closeNotifier) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(FXML_NAME));
        fxmlLoader.setController(this);
        this.closeNotifier = closeNotifier;
        this.strategyText = strategyText;
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void initialize() {
        strategyLabel.textProperty().setValue(strategyText);
    }


    @FXML
    private void handleDelete(ActionEvent e) {
        closeNotifier.accept(this);
    }

    public HBox getContainer() {
        return container;
    }
}
