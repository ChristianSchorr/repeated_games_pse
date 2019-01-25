package loop.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import loop.model.repository.CentralRepository;
import loop.model.repository.FileIO;
import loop.model.simulationengine.Game;
import loop.model.simulationengine.strategies.PureStrategy;
import loop.model.simulationengine.strategies.Strategy;
import loop.model.simulationengine.strategy.strategybuilder.*;
import org.checkerframework.dataflow.qual.Pure;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class StrategyController implements CreationController<Strategy> {

    private static final double PERCENTAGE = 0.1;

    @FXML
    private TextField nameField;

    @FXML
    private TextField descriptionField;

    @FXML
    private FlowPane expressionContainer;

    @FXML
    private ChoiceBox thenBox;

    @FXML
    private ChoiceBox elseBox;

    @FXML
    private ChoiceBox cooperationParticipants;

    @FXML
    private ChoiceBox cooperationQuantors;

    @FXML
    private ChoiceBox capitalBox;

    @FXML
    private ChoiceBox rankBox;

    private StringProperty nameProperty = new SimpleStringProperty();
    private StringProperty descriptionProperty = new SimpleStringProperty();
    private StringProperty thenProperty = new SimpleStringProperty();
    private StringProperty elseProperty = new SimpleStringProperty();

    private StringProperty coopParticipantProperty = new SimpleStringProperty();
    private StringProperty coopQuantorProperty = new SimpleStringProperty();
    private StringProperty capitalProperty = new SimpleStringProperty();
    private StringProperty rankProperty = new SimpleStringProperty();

    private Map<String, Map<String, Strategy>> coopMap;
    private Map<String, Strategy> capitalMap;
    private List<String> participants;
    private List<String> quantors;
    private List<String> assesment;

    private List<Consumer<Strategy>> creationListener;
    CentralRepository repository;

    private ContainerNode expressionRoot;
    private ContainerNode selectedOperand;

    public StrategyController() {
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
        thenProperty.setValue(stratNames.get(0));
        elseBox.setItems(obsStratNames);
        elseBox.valueProperty().bindBidirectional(elseProperty);
        elseProperty.setValue(stratNames.get(0));

        participants = new ArrayList<>();
        participants.add("The opponent");
        participants.add("The agent himself");
        participants.add("An agent of the same group");

        quantors = new ArrayList<>();
        quantors.add("every time");
        quantors.add("once");
        quantors.add("last time");
        quantors.add("never");

        assesment = new ArrayList<>();
        assesment.add("higher");
        assesment.add("lower");
        assesment.add("similar");

        cooperationParticipants.setItems(FXCollections.observableArrayList(participants));
        cooperationParticipants.valueProperty().bindBidirectional(coopParticipantProperty);
        cooperationParticipants.setValue(participants.get(0));

        cooperationQuantors.setItems(FXCollections.observableArrayList(quantors));
        cooperationQuantors.valueProperty().bindBidirectional(coopQuantorProperty);
        cooperationQuantors.setValue(quantors.get(0));

        capitalBox.setItems(FXCollections.observableArrayList(assesment));
        capitalBox.valueProperty().bindBidirectional(capitalProperty);
        capitalBox.setValue(assesment.get(0));

        rankBox.setItems(FXCollections.observableArrayList(assesment));
        rankBox.valueProperty().bindBidirectional(rankProperty);
        rankBox.setValue(assesment.get(0));

        setupStrategyMaps();
    }

    private void setupStrategyMaps() {
        coopMap = new HashMap<>();

        Map<String, Strategy> opponentMap = new HashMap<>();
        opponentMap.put(quantors.get(0), PureStrategy.opponentAlwaysCooperated());
        opponentMap.put(quantors.get(1), PureStrategy.opponentCooperatedAtLeastOnce());
        opponentMap.put(quantors.get(2), PureStrategy.opponentCooperatedLastTime());
        opponentMap.put(quantors.get(3), PureStrategy.opponentCooperatedNever());
        coopMap.put(participants.get(0), opponentMap);

        Map<String, Strategy> currentMap = new HashMap<>();
        currentMap.put(quantors.get(0), PureStrategy.currAgentAlwaysCooperated());
        currentMap.put(quantors.get(1), PureStrategy.currAgentCooperatedAtLeastOnce());
        currentMap.put(quantors.get(2), PureStrategy.currAgentCooperatedLastTime());
        currentMap.put(quantors.get(3), PureStrategy.currAgentCooperatedNever());
        coopMap.put(participants.get(1), currentMap);

        capitalMap = new HashMap<>();
        capitalMap.put(assesment.get(0), PureStrategy.opponentHasHigherCapital());
        capitalMap.put(assesment.get(1), PureStrategy.opponentHasLowerCapital());
        capitalMap.put(assesment.get(2), PureStrategy.opponentHasSimilarCapital(PERCENTAGE));
    }

    @FXML
    private void reset() {
        expressionRoot = null;
        selectedOperand = null;
        nameProperty.setValue("");
        descriptionProperty.setValue("");
    }

    @FXML
    private void handleAddCooperation(ActionEvent e) {
        Map<String, Strategy> map = coopMap.get(cooperationParticipants.getValue());
        if (map != null) {
            Strategy strat = coopMap.get(coopParticipantProperty.getValue()).get(coopQuantorProperty.getValue());
            if (strat != null)
                addStrategy(strat);
        }
    }

    @FXML
    private void handleAddCapital(ActionEvent e) {
        Strategy strat = capitalMap.get(capitalProperty.getValue());
        if (strat != null)
            addStrategy(strat);
    }

    @FXML
    private void handleAddGroup(ActionEvent e) {
        addStrategy(PureStrategy.opponentIsInTheSameGroup());
    }

    @FXML
    private void addNot(ActionEvent e) {
        addOperator(ConcreteOperator.NOT());
    }

    @FXML
    private void addAnd(ActionEvent e) {
        addOperator(ConcreteOperator.AND());
    }

    @FXML
    private void addOr(ActionEvent e) {
        addOperator(ConcreteOperator.OR());
    }

    @FXML
    private void saveStrategy() {
    	 Strategy strat;
         try {
             strat = expressionRoot.getStrategy();
         } catch (IllegalArgumentException e) {
             Alert alert = new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK);
             alert.showAndWait();
             return;
         }
         
         //save dialog
         FileChooser fileChooser = new FileChooser();
         fileChooser.setTitle("Save Strategy");
         fileChooser.setInitialDirectory(FileIO.GAME_DIR);
         fileChooser.setInitialFileName(strat.getName().toLowerCase());
         FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Loop Strategy File", ".strat");
         fileChooser.getExtensionFilters().add(extFilter);
         File saveFile = fileChooser.showSaveDialog(new Stage());
         
         if (saveFile == null) {
             return;
         }
         
         try {
             FileIO.saveEntity(saveFile, strat);
         } catch (IOException e) {
             e.printStackTrace();
             Alert alert = new Alert(AlertType.ERROR, "File could not be saved.", ButtonType.OK);
             alert.showAndWait();
             return;
         };
         
         this.creationListener.forEach(handler -> handler.accept(strat));
    }
    
    @FXML
    private void applyStrategy() {
    	Strategy strat;
    	try {
            strat = expressionRoot.getStrategy();
        } catch (IllegalArgumentException e) {
            Alert alert = new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.showAndWait();
            return;
        }
    	this.repository.getStrategyRepository().addEntity(strat.getName(), strat);
    	Alert alert = new Alert(AlertType.CONFIRMATION, "Strategy added to repository", ButtonType.OK);
    	alert.showAndWait();
    }
    
    private void insertContainerNode(ContainerNode child, ContainerNode parent) {
        int index = parent.children.indexOf(selectedOperand);
        parent.removeNode(selectedOperand);
        parent.getChildren().add(index, child);
        parent.syntaxNode.insertNode(child.syntaxNode);
    }

    private void addStrategy(Strategy strategy) {
        if (selectedOperand == null && expressionRoot == null) {
            expressionRoot = new ContainerNode(strategy, null);
            expressionRoot.syntaxNode = new SyntaxNode(strategy, null);
        } else if (selectedOperand != null && selectedOperand.syntaxNode == null) {
            ContainerNode node = new ContainerNode(strategy, selectedOperand.parent);
            node.syntaxNode = new SyntaxNode(strategy, null);
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
        } else if (selectedOperand.syntaxNode == null) {
            ContainerNode newNode = new ContainerNode(op, selectedOperand.parent);
            newNode.syntaxNode = new SyntaxNode(null, op);
            insertContainerNode(newNode, selectedOperand.parent);
            newNode.selectNode((ContainerNode) newNode.getChildren().get(0));
        } else {
            ContainerNode newNode = new ContainerNode(op, selectedOperand.parent);
            newNode.syntaxNode = new SyntaxNode(null, op);
            newNode.syntaxNode.insertNode(selectedOperand.syntaxNode);
            newNode.getChildren().remove(0);
            newNode.getChildren().add(0, selectedOperand);
            if (selectedOperand.parent != null)
                insertContainerNode(newNode, selectedOperand.parent);
            else {
                expressionRoot = newNode;
            }
            selectedOperand.parent = newNode;
            if (newNode.getChildren().size() == 2)
                newNode.selectNode((ContainerNode) newNode.getChildren().get(1));
            else newNode.selectNode(null);
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
        private boolean isHovered = false;

        public ContainerNode(Operator operator, ContainerNode parent) {
            this.parent = parent;
            isInner = true;
            OperatorCellController operatorCellController = new OperatorCellController(operator, (c) -> removeNode());
            content = operatorCellController.getContainer();
            for (int i = 0; i < operator.getOperandCount(); i++) {
                children.add(new ContainerNode(this));
            }
            content.setOnMouseClicked((e) -> selectNode(this));
            content.hoverProperty().addListener((obs, oldValue, newValue) -> {
                if (isHovered != newValue) {
                    isHovered = newValue;
                    updateFlowPane();
                }
            });
        }

        public ContainerNode(Strategy strategy, ContainerNode parent) {
            this.parent = parent;
            isInner = false;
            StrategyCellController strategyCellController = new StrategyCellController(strategy, (c) -> removeNode());
            content = strategyCellController.getContainer();
        }

        public ContainerNode(ContainerNode parent) {
            this.parent = parent;
            content = new HBox();
            content.getStyleClass().add(EMPTY_STYLE);
            content.setOnMouseClicked((e) -> selectNode(this));
        }

        private void removeNode() {
            if (parent == null) {
                expressionRoot = null;
                selectedOperand = null;
                return;
            }
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
            if (node != null)
                node.content.getStyleClass().add(SELECTED_STYLE);
            updateFlowPane();
        }

        private void addToPane(FlowPane container) {
            if (!isInner) container.getChildren().add(content);
            else {
                if (this != expressionRoot)
                    container.getChildren().add(getBrace(true, isHovered, selectedOperand == this));
                if (children.size() == 2) {
                    ((ContainerNode) children.get(0)).addToPane(container);
                    if (isHovered) content.getStyleClass().add("empty-card-hovered");
                    else content.getStyleClass().removeIf((str) -> str.equals("empty-card-hovered"));
                    container.getChildren().add(content);
                    ((ContainerNode) children.get(1)).addToPane(container);
                } else {
                    container.getChildren().add(content);
                    ((ContainerNode) children.get(0)).addToPane(container);
                }
                if (this != expressionRoot)
                    container.getChildren().add(getBrace(false, isHovered, selectedOperand == this));
            }
        }
        
        private Strategy getStrategy() {
        	Strategy strat = null;
            if (expressionRoot == null) {
                // TODO error
            } else {
                SyntaxTree tree = new SyntaxTree(expressionRoot.syntaxNode);
                boolean error = !tree.checkSyntax();
                if (error) {
                    // TODO error
                }
                if (nameProperty.getValue() == null || descriptionProperty.getValue() == null) {
                    // TODO error
                }
                strat = StrategyBuilder.creatNewStrategy(tree.getRoot(),
                        nameProperty.getValue(), descriptionProperty.getValue());
            }
            return strat;
        }

        private HBox getBrace(boolean open, boolean isHovered, boolean selected) {
            String content = open ? "(" : ")";
            HBox box = new HBox();
            box.getStyleClass().add("brace-card");
            if (isHovered) box.getStyleClass().add("hovered-brace-card");
            if (selected) box.getStyleClass().add("selected-brace-card");
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
