package loop.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import loop.model.repository.CentralRepository;
import loop.model.simulationengine.strategies.Strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class StrategyControler implements CreationController<Strategy> {

    @FXML
    private TextField nameField;

    @FXML
    private TextField descriptionField;

    @FXML
    private FlowPane strategyContainer;

    @FXML
    private FlowPane operatorContainer;

    @FXML
    private FlowPane expressionContainer;

    @FXML
    private ChoiceBox thenBox;

    @FXML
    private ChoiceBox elseBox;


    private StringProperty nameProperty = new SimpleStringProperty();
    private StringProperty descriptionProperty = new SimpleStringProperty();
    private StringProperty thenProperty = new SimpleStringProperty();
    private StringProperty elseProperty = new SimpleStringProperty();

    private List<Consumer<Strategy>> creationListener;
    CentralRepository repository;

    public StrategyControler() {
        creationListener = new ArrayList<>();
        repository = CentralRepository.getInstance();
    }

    public void initialize() {
        nameField.textProperty().bindBidirectional(nameProperty);
        descriptionField.textProperty().bindBidirectional(descriptionProperty);

        List<String> stratNames = repository.getStrategyRepository().getAllEntityNames();
        ObservableList<String> obsStratNames = FXCollections.observableArrayList(stratNames);
        thenBox.setItems(obsStratNames);
        thenBox.valueProperty().bindBidirectional(thenProperty);
        elseBox.setItems(obsStratNames);
        elseBox.valueProperty().bindBidirectional(elseProperty);
    }

    @Override
    public void registerElementCreated(Consumer<Strategy> action) {
        creationListener.add(action);
    }
}
