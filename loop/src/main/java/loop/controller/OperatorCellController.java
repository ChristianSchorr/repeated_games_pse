package loop.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import loop.model.simulationengine.strategy.strategybuilder.Operator;

import java.io.IOException;
import java.util.function.Consumer;

public class OperatorCellController {

    private static final String FXML_NAME = "/view/controls/OperatorCell.fxml";

    @FXML
    private Label operatorName;

    @FXML
    private HBox container;

    Consumer<OperatorCellController> closeNotifier;
    private Operator operator;

    public OperatorCellController(Operator op, Consumer<OperatorCellController> closeNotifier) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(FXML_NAME));
        fxmlLoader.setController(this);
        this.closeNotifier = closeNotifier;
        this.operator  = op;
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void initialize() {
        this.operatorName.setText(operator.getName());
    }


    @FXML
    private void handleDelete(ActionEvent e) {
        closeNotifier.accept(this);
    }

    public HBox getContainer() {
        return container;
    }
}
