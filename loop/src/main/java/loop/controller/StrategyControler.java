package loop.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import loop.model.repository.CentralRepository;
import loop.model.simulationengine.strategies.Strategy;
import loop.model.simulationengine.strategy.strategybuilder.*;

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

    private ContainerNode expressionRoot;
    private ContainerNode selectedOperand;

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

    @FXML
    private void addNot(ActionEvent e) {
        //addOperator(ConcreteOperator.NOT());
        addStrategy();
    }

    @FXML
    private void addAnd(ActionEvent e) {
        addOperator(ConcreteOperator.AND());
    }

    @FXML
    private void addOr(ActionEvent e) {
        addOperator(ConcreteOperator.OR());
    }

    private void insertContainerNode(ContainerNode child, ContainerNode parent) {
        int index = parent.children.indexOf(selectedOperand);
        parent.removeNode(selectedOperand);
        parent.getChildren().add(index, child);
        parent.syntaxNode.insertNode(child.syntaxNode);
    }

    private void addStrategy() {
        if (selectedOperand == null && expressionRoot == null) {
            expressionRoot = new ContainerNode("sadfsdf", null);
        } else if (selectedOperand != null) {
            ContainerNode node = new ContainerNode("TEST!", selectedOperand.parent);
            insertContainerNode(node, selectedOperand.parent);
            selectedOperand = null;
        }
        updateFlowPane();
    }

    private void addOperator(Operator op) {
        if (selectedOperand == null) {
            ContainerNode node = new ContainerNode(op, null);
            node.syntaxNode = new SyntaxNode(null, op);
            if (expressionRoot == null) {
                expressionRoot = node;
                node.selectNode((ContainerNode) node.getChildren().get(0));
            } else {
                node.syntaxNode.insertNode(expressionRoot.syntaxNode);
                node.getChildren().remove(0);
                node.getChildren().add(0, expressionRoot);
                node.selectNode((ContainerNode) node.getChildren().get(1));
                expressionRoot = node;
            }

        } else {
            //insertContainerNode(newNode);
            ContainerNode newNode = new ContainerNode(op, selectedOperand.parent);
            newNode.syntaxNode = new SyntaxNode(null, op);
            insertContainerNode(newNode, selectedOperand.parent);
            newNode.selectNode((ContainerNode) newNode.getChildren().get(0));
        }
        updateFlowPane();
    }

    private void updateFlowPane() {
        expressionContainer.getChildren().clear();
        if (expressionRoot != null) {
            expressionRoot.addToPane(expressionContainer);
        }
    }


    @Override
    public void registerElementCreated(Consumer<Strategy> action) {
        creationListener.add(action);
    }

    private class ContainerNode implements TreeNode<HBox> {

        private static final String EMPTY_STYLE = "empty-card";
        private static final String SELECTED_STYLE = "empty-card-selected";

        private HBox content;
        private List<TreeNode<HBox>> children = new ArrayList<>();
        private ContainerNode parent;
        private SyntaxNode syntaxNode;

        private boolean isInner = false;
        private boolean isEmpty = false;

        public ContainerNode(Operator operator, ContainerNode parent) {
            this.parent = parent;
            isInner = true;
            OperatorCellController operatorCellController = new OperatorCellController(operator, (c) -> removeNode());
            content = operatorCellController.getContainer();
            for (int i = 0; i < operator.getOperandCount(); i++) {
                children.add(new ContainerNode(this));
            }
        }

        public ContainerNode(String strategyName, ContainerNode parent) {
            this.parent = parent;
            isInner = false;
            StrategyCellController strategyCellController = new StrategyCellController(strategyName, (c) -> removeNode());
            content = strategyCellController.getContainer();
        }

        public ContainerNode(ContainerNode parent) {
            this.parent = parent;
            isEmpty = true;
            content = new HBox();
            content.getStyleClass().add(EMPTY_STYLE);
            content.setOnMouseClicked((e) -> {
                selectNode(this);
            });
        }

        private void removeNode() {
            int index = parent.children.indexOf(this);
            parent.removeNode(this);
            ContainerNode emptyNode = new ContainerNode(parent);
            selectNode(emptyNode);
            parent.getChildren().add(index, emptyNode);
            updateFlowPane();
        }

        private void selectNode(ContainerNode node) {
            if (selectedOperand != null)
                selectedOperand.getContent().getStyleClass().removeIf((str) -> str.equals(SELECTED_STYLE));
            selectedOperand = node;
            node.content.getStyleClass().add(SELECTED_STYLE);
        }

        private void addToPane(FlowPane container) {
            if (!isInner) container.getChildren().add(content);
            else {
                if (this != expressionRoot)
                    container.getChildren().add(getBrace(true));
                if (children.size() == 2) {
                    ((ContainerNode) children.get(0)).addToPane(container);
                    container.getChildren().add(content);
                    ((ContainerNode) children.get(1)).addToPane(container);
                } else {
                    container.getChildren().add(content);
                    ((ContainerNode) children.get(0)).addToPane(container);
                }
                if (this != expressionRoot)
                    container.getChildren().add(getBrace(false));
            }
        }

        private HBox getBrace(boolean open) {
            String content = open ? "(" : ")";
            HBox box = new HBox();
            box.getStyleClass().add("brace-card");
            box.setAlignment(Pos.CENTER);
            box.setSpacing(5);
            Label label = new Label(content);
            label.getStyleClass().add("key-word");
            box.getChildren().add(label);
            return box;
        }

        @Override
        public HBox getContent() {
            return content;
        }

        @Override
        public List<TreeNode<HBox>> getChildren() {
            return children;
        }

        @Override
        public void insertNode(TreeNode<HBox> node) {
            children.add(node);
        }

        @Override
        public boolean removeNode(TreeNode<HBox> node) {
            return children.remove(node);
        }
    }
}
