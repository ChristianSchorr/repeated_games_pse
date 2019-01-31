package loop.controller;

import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.converter.NumberStringConverter;
import loop.controller.validation.DoubleValidator;
import loop.controller.validation.IntegerValidator;
import loop.model.*;
import loop.model.plugin.Parameter;
import loop.model.plugin.ParameterValidator;
import loop.model.plugin.Plugin;
import loop.model.plugin.PluginControl;
import loop.model.repository.CentralRepository;
import loop.model.repository.FileIO;
import loop.model.repository.Repository;
import loop.model.simulationengine.*;
import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;


public class ConfigController implements CreationController<UserConfiguration> {

    private static final int VARIABLE_PARAM_CNT = 8;
    private static final String CONFIG_ERR_MSG = "";
    private static final String ALERT_TITLE = "";

    @FXML
    private ChoiceBox gameBox;

    @FXML
    private TextField roundField;

    @FXML
    private TextField iterationField;

    @FXML
    private CheckBox mixedStrategyCheckBox;

    @FXML
    private ChoiceBox populationBox;

    // Pair Builder
    @FXML
    private ChoiceBox pairBuilderBox;

    @FXML
    private HBox pairBuilderContainer;

    // SuccessQuantifier
    @FXML
    private ChoiceBox successQuantifierBox;

    @FXML
    private HBox successQuantifierContainer;

    // StrategyAdjuster
    @FXML
    private ChoiceBox strategyAdjusterBox;

    @FXML
    private HBox strategyAdjusterContainer;

    // EquilibriumCriterion
    @FXML
    private ChoiceBox equilibriumCriterionBox;

    @FXML
    private HBox equilibriumCriterionContainer;

    @FXML
    private TextField maxAdaptsField;

    // MultiConfig
    @FXML
    private ChoiceBox<MultiParamItem> multiParamBox;

    @FXML
    private TextField startValue;

    @FXML
    private TextField endValue;

    @FXML
    private TextField stepSize;

    @FXML
    private TableView<GameTableEntry> gameTable;

    @FXML
    private TableColumn<GameTableEntry, String> firstCol;

    @FXML
    private TableColumn<GameTableEntry, String> secondCol;

    @FXML
    private TableColumn<GameTableEntry, String> thirdCol;

    @FXML
    private Label descriptionLabel;

    private StringProperty gameNameProperty = new SimpleStringProperty();
    private IntegerProperty iterationCountProperty = new SimpleIntegerProperty();
    private IntegerProperty roundCountProperty = new SimpleIntegerProperty();
    private BooleanProperty mixedStrategyProperty = new SimpleBooleanProperty();
    private StringProperty populationProperty = new SimpleStringProperty();
    private IntegerProperty maxAdaptsProperty = new SimpleIntegerProperty();
    private StringProperty gameDescriptionProperty = new SimpleStringProperty();

    private StringProperty pairBuilderProperty = new SimpleStringProperty();
    private StringProperty successQuantifierProperty = new SimpleStringProperty();
    private StringProperty strategyAdjusterProperty = new SimpleStringProperty();
    private StringProperty equilibriumCriterionProperty = new SimpleStringProperty();

    private ObjectProperty<MultiParamItem> multiParamProperty = new SimpleObjectProperty<>();
    private DoubleProperty startValueProperty = new SimpleDoubleProperty();
    private DoubleProperty endValueProperty = new SimpleDoubleProperty(1);
    private DoubleProperty stepSizeProperty = new SimpleDoubleProperty(1);

    private PluginControl pairBuilderControl;
    private PluginControl successQuantifierControl;
    private PluginControl strategyAdjusterControl;
    private PluginControl equilibriumCriterionControl;

    private CentralRepository repository;
    private UserConfiguration config;

    private final ValidationSupport support = new ValidationSupport();
    private final FileChooser fileChooser = new FileChooser();


    private List<Consumer<UserConfiguration>> creationListener;


    public ConfigController() {
        this(UserConfiguration.getDefaultConfiguration());
    }

    public ConfigController(UserConfiguration config) {
        creationListener = new ArrayList<>();
        repository = CentralRepository.getInstance();
        this.config = config;
    }

    public void initialize() {
        String errorMsg = "not a positive integer";
        // initialize multi Param
        List<MultiParamItem> multiConfigParamNames = new ArrayList<>();
        multiConfigParamNames.add(new MultiParamItem(MulticonfigurationParameterType.ITERATION_COUNT,
                MulticonfigurationParameterType.ITERATION_COUNT.getDescriptionFormat(),
                new IntegerValidator(errorMsg, (i) -> i > 0), iterationField));
        multiConfigParamNames.add(new MultiParamItem(MulticonfigurationParameterType.ROUND_COUNT,
                MulticonfigurationParameterType.ROUND_COUNT.getDescriptionFormat(),
                new IntegerValidator(errorMsg, (i) -> i > 0), roundField));
        multiConfigParamNames.add(new MultiParamItem(MulticonfigurationParameterType.MAX_ADAPTS,
                MulticonfigurationParameterType.MAX_ADAPTS.getDescriptionFormat(),
                new IntegerValidator(errorMsg, (i) -> i > 0), maxAdaptsField));

        ObservableList<MultiParamItem> observableMultiConfigParamNames = FXCollections.observableArrayList();
        observableMultiConfigParamNames.add(new MultiParamItem(null, "No multiconfig. parameter", new DoubleValidator(""), null));
        observableMultiConfigParamNames.addAll(multiConfigParamNames);
        multiParamBox.setItems(observableMultiConfigParamNames);
        multiParamBox.valueProperty().bindBidirectional(multiParamProperty);
        multiParamBox.valueProperty().addListener((c, oldValue, newValue) -> {
            setDisableMultiParam(true, newValue);
            if (oldValue != null) setDisableMultiParam(false, oldValue);
        });

        // inititalize gameNames
        List<String> gameNames = repository.getGameRepository().getAllEntityNames();
        ObservableList<String> observableGameNames = FXCollections.observableArrayList(gameNames);
        gameNameProperty.setValue(config.getGameName());
        gameBox.setItems(observableGameNames);
        gameBox.valueProperty().addListener((ChangeListener<String>)
                (observable, oldValue, newValue) -> gameChanged(newValue));
        gameBox.valueProperty().bindBidirectional(gameNameProperty);
        descriptionLabel.textProperty().bindBidirectional(gameDescriptionProperty);

        // initialize rounds
        roundCountProperty.setValue(config.getRoundCount());
        roundField.textProperty().bindBidirectional(roundCountProperty, new NumberStringConverter(Locale.ENGLISH));

        // initialize iterationCount
        iterationCountProperty.setValue(config.getIterationCount());
        iterationField.textProperty().bindBidirectional(iterationCountProperty, new NumberStringConverter(Locale.ENGLISH));

        // initialize mixedStrategies
        mixedStrategyProperty.setValue(config.getMixedAllowed());
        mixedStrategyCheckBox.selectedProperty().bindBidirectional(mixedStrategyProperty);

        // initialize populations
        List<String> populationNames = repository.getPopulationRepository().getAllEntityNames();
        ObservableList<String> observablePopulationNames = FXCollections.observableArrayList(populationNames);
        populationProperty.setValue(config.getPopulationName());
        populationBox.setItems(observablePopulationNames);
        populationBox.valueProperty().addListener((ChangeListener<String>)
                (observable, oldValue, newValue) -> populationChanged(newValue));
        populationBox.valueProperty().bindBidirectional(populationProperty);

        // initialize pairBuilder
        List<String> pairBuilderNames = repository.getPairBuilderRepository().getAllEntityNames();
        ObservableList<String> observablePairBuilderNames = FXCollections.observableArrayList(pairBuilderNames);
        pairBuilderProperty.setValue(config.getPairBuilderName());
        pairBuilderBox.setItems(observablePairBuilderNames);
        pairBuilderBox.valueProperty().addListener((ChangeListener<String>)
                (observable, oldValue, newValue) -> pairBuilderChanged(oldValue, newValue));
        pairBuilderBox.valueProperty().bindBidirectional(pairBuilderProperty);
        pairBuilderControl.setParameters(config.getPairBuilderParameters());

        // initialize succesQuantifier
        List<String> successQuantifierNames = repository.getSuccessQuantifiernRepository().getAllEntityNames();
        ObservableList<String> observableSuccessQuantifierNames = FXCollections.observableArrayList(successQuantifierNames);
        successQuantifierProperty.setValue(config.getSuccessQuantifierName());
        successQuantifierBox.setItems(observableSuccessQuantifierNames);
        successQuantifierBox.valueProperty().addListener((ChangeListener<String>)
                (observable, oldValue, newValue) -> successQuantifierChanged(oldValue, newValue));
        successQuantifierBox.valueProperty().bindBidirectional(successQuantifierProperty);
        successQuantifierControl.setParameters(config.getSuccessQuantifierParameters());

        // initialize strategyAdjuster
        List<String> strategyAdjusterNames = repository.getStrategyAdjusterRepository().getAllEntityNames();
        ObservableList<String> observableStrategyAdjusterNames = FXCollections.observableArrayList(strategyAdjusterNames);
        strategyAdjusterProperty.setValue(config.getStrategyAdjusterName());
        strategyAdjusterBox.setItems(observableStrategyAdjusterNames);
        strategyAdjusterBox.valueProperty().addListener((ChangeListener<String>)
                (observable, oldValue, newValue) -> strategyAdjusterChanged(oldValue, newValue));
        strategyAdjusterBox.valueProperty().bindBidirectional(strategyAdjusterProperty);
        strategyAdjusterControl.setParameters(config.getStrategyAdjusterParameters());

        // initialize equilibriumCriterion
        List<String> equilibriumCriterionNames = repository.getEquilibriumCriterionRepository().getAllEntityNames();
        ObservableList<String> observableEquilibriumCriterionNames = FXCollections.observableArrayList(equilibriumCriterionNames);
        equilibriumCriterionProperty.setValue(config.getEquilibriumCriterionName());
        equilibriumCriterionBox.setItems(observableEquilibriumCriterionNames);
        equilibriumCriterionBox.valueProperty().addListener((ChangeListener<String>)
                (observable, oldValue, newValue) -> equilibriumCriterionChanged(oldValue, newValue));
        equilibriumCriterionBox.valueProperty().bindBidirectional(equilibriumCriterionProperty);
        equilibriumCriterionControl.setParameters(config.getEquilibriumCriterionParameters());

        // initialize maxAdaptsLabel
        maxAdaptsProperty.setValue(config.getMaxAdapts());
        maxAdaptsField.textProperty().bindBidirectional(maxAdaptsProperty, new NumberStringConverter(Locale.ENGLISH));

        startValue.textProperty().bindBidirectional(startValueProperty, new NumberStringConverter(Locale.ENGLISH));
        endValue.textProperty().bindBidirectional(endValueProperty, new NumberStringConverter(Locale.ENGLISH));
        stepSize.textProperty().bindBidirectional(stepSizeProperty, new NumberStringConverter(Locale.ENGLISH));



        // initialize game table
        firstCol.setCellValueFactory(cellData -> cellData.getValue().firstColumnContent);
        secondCol.setCellValueFactory(cellData -> cellData.getValue().secondColumnContent);
        thirdCol.setCellValueFactory(cellData -> cellData.getValue().thirdColumnContent);

        Game game = repository.getGameRepository().getEntityByName(gameNameProperty.getValue());
        ObservableList<GameTableEntry> items = FXCollections.observableArrayList(new GameTableEntry(game, true),
                new GameTableEntry(game, false));
        gameTable.setItems(items);
        gameTable.setSelectionModel(null);

        initializeValidation();
    }

    private void initializeValidation() {
        String errorMsg = "not a positive integer";
        support.registerValidator(roundField, false, new IntegerValidator(errorMsg, (i) -> i > 0));
        support.registerValidator(iterationField, false, new IntegerValidator(errorMsg, (i) -> i > 0));


        support.registerValidator(startValue, false, (Control c, String v) -> {
            if (multiParamProperty.getValue() == null)
                return ValidationResult.fromMessageIf(c, "", Severity.ERROR, false);
            else {
                return ValidationResult.fromResults(multiParamProperty.getValue().validator.apply(c, v),
                        new DoubleValidator("start value greater than end value!",
                                d -> d < endValueProperty.getValue()).apply(c, v));
            }
        });

        support.registerValidator(endValue, false, (Control c, String v) -> {
            if (multiParamProperty.getValue() == null)
                return ValidationResult.fromMessageIf(c, "", Severity.ERROR, false);
            else {
                return ValidationResult.fromResults(multiParamProperty.getValue().validator.apply(c, v),
                        new DoubleValidator("end value lower than start value!",
                                d -> d > startValueProperty.getValue()).apply(c, v));
            }
        });

        support.registerValidator(stepSize,false , new DoubleValidator("step size has to be a positve number", d -> d > 0));

        startValueProperty.addListener((c, oldV, newV) -> {
            String endVal = endValue.getText();
            endValue.setText(endVal + "#");
            endValue.setText(endVal);
        });
        endValueProperty.addListener((c, oldV, newV) -> {
            String startVal = startValue.getText();
            startValue.setText(startVal + "#");
            startValue.setText(startVal);
        });
    }

    private boolean isPluginConfigurationInvalid() {
        return pairBuilderControl.hasConfigurationErrors() || successQuantifierControl.hasConfigurationErrors() ||
                strategyAdjusterControl.hasConfigurationErrors() || equilibriumCriterionControl.hasConfigurationErrors();
    }

    @FXML
    private void applyConfig(ActionEvent actionEvent) {
        if (support.isInvalid() || isPluginConfigurationInvalid()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("The configuration is faulty");
            alert.setContentText("There are some errors in this configuration!\nPlease make sure all parameters are set properly");
            alert.showAndWait();
            return;
        }
        createConfig();
        Stage s = (Stage) this.maxAdaptsField.getScene().getWindow();
        s.close();
        
    }

    @FXML
    private void resetConfig(ActionEvent actionEvent) {
        setConfiguration(UserConfiguration.getDefaultConfiguration());
    }


    private void gameChanged(String newValue) {
        Repository<Game> repo = repository.getGameRepository();
        Game game = repo.getEntityByName(newValue);
        for (GameTableEntry entry : gameTable.getItems())
            entry.update(game);
        gameDescriptionProperty.setValue(game.getDescription());
    }

    private void populationChanged(String newValue) {
        Repository<Population> repo = repository.getPopulationRepository();
        Population population = repo.getEntityByName(newValue);
        updateMultiParamBox(population);
    }

    private void pairBuilderChanged(String oldValue, String newValue) {
        Repository<Plugin<PairBuilder>> repo = repository.getPairBuilderRepository();
        Plugin<PairBuilder> pairBuilderPlugin = repo.getEntityByName(newValue);

        pairBuilderControl = pairBuilderPlugin.getRenderer().renderPlugin();
        pairBuilderContainer.getChildren().clear();
        pairBuilderContainer.getChildren().add(pairBuilderControl);
        pairBuilderBox.setTooltip(createTooltip(pairBuilderPlugin.getDescription()));

        Plugin<PairBuilder> oldPlugin = repo.getEntityByName(oldValue);
        updateMultiParamBox(oldPlugin, pairBuilderPlugin, MulticonfigurationParameterType.PB_PARAM);
    }

    private void successQuantifierChanged(String oldValue, String newValue) {
        Repository<Plugin<SuccessQuantifier>> repo = repository.getSuccessQuantifiernRepository();
        Plugin<SuccessQuantifier> successQuantifierPlugin = repo.getEntityByName(newValue);

        successQuantifierControl = successQuantifierPlugin.getRenderer().renderPlugin();
        successQuantifierContainer.getChildren().clear();
        successQuantifierContainer.getChildren().add(successQuantifierControl);
        successQuantifierBox.setTooltip(createTooltip(successQuantifierPlugin.getDescription()));

        Plugin<SuccessQuantifier> oldPlugin = repo.getEntityByName(oldValue);
        updateMultiParamBox(oldPlugin, successQuantifierPlugin, MulticonfigurationParameterType.SQ_PARAM);
    }

    private void strategyAdjusterChanged(String oldValue, String newValue) {
        Repository<Plugin<StrategyAdjuster>> repo = repository.getStrategyAdjusterRepository();
        Plugin<StrategyAdjuster> strategyAdjusterPlugin = repo.getEntityByName(newValue);

        strategyAdjusterControl = strategyAdjusterPlugin.getRenderer().renderPlugin();
        strategyAdjusterContainer.getChildren().clear();
        strategyAdjusterContainer.getChildren().add(strategyAdjusterControl);
        strategyAdjusterBox.setTooltip(createTooltip(strategyAdjusterPlugin.getDescription()));

        Plugin<StrategyAdjuster> oldPlugin = repo.getEntityByName(oldValue);
        updateMultiParamBox(oldPlugin, strategyAdjusterPlugin, MulticonfigurationParameterType.SA_PARAM);
    }

    private void equilibriumCriterionChanged(String oldValue, String newValue) {
        Repository<Plugin<EquilibriumCriterion>> repo = repository.getEquilibriumCriterionRepository();
        Plugin<EquilibriumCriterion> equilibriumCriterionPlugin = repo.getEntityByName(newValue);

        equilibriumCriterionControl = equilibriumCriterionPlugin.getRenderer().renderPlugin();
        equilibriumCriterionContainer.getChildren().clear();
        equilibriumCriterionContainer.getChildren().add(equilibriumCriterionControl);
        equilibriumCriterionBox.setTooltip(createTooltip(equilibriumCriterionPlugin.getDescription()));

        Plugin<EquilibriumCriterion> oldPlugin = repo.getEntityByName(oldValue);
        updateMultiParamBox(oldPlugin, equilibriumCriterionPlugin, MulticonfigurationParameterType.EC_PARAM);
    }

    private Tooltip createTooltip(String desc) {
        Tooltip tooltip = new Tooltip(desc);
        tooltip.getStyleClass().add("ttip");
        tooltip.setWrapText(true);
        tooltip.setPrefWidth(600);
        return tooltip;
    }

    private <T> void updateMultiParamBox(Plugin<T> oldPlugin, Plugin<T> newPlugin, MulticonfigurationParameterType type) {
        ObservableList<MultiParamItem> items = multiParamBox.getItems();
        if (oldPlugin != null)
            items.removeIf((item) -> item.toString().substring(item.toString().indexOf(':') + 1).equals(oldPlugin.getName()));

        for (Parameter param : newPlugin.getParameters()) {
            String errorMsg = "Not a valid value for the " + param.getName() + " parameter";
            items.add(new MultiParamItem(type, param.getName() + ":" + newPlugin.getName(),
                    new DoubleValidator(errorMsg, d -> ParameterValidator.isValueValid(d, param)), null));
        }
        multiParamBox.requestLayout();
    }

    private void updateMultiParamBox(Population newPopulation) {
        ObservableList<MultiParamItem> items = multiParamBox.getItems();
        items.removeIf((item) -> item.type != null && item.type.equals(MulticonfigurationParameterType.SEGMENT_SIZE));

        for (Group grp : newPopulation.getGroups()) {
            String grpSize = "group size: " + grp.getName();
            items.add(new MultiParamItem(MulticonfigurationParameterType.GROUP_SIZE, grpSize,
                    new DoubleValidator("group size has to be greater than 0", d -> d >= 0), null));
            if (grp.getSegmentCount() == 2) {
                String str = "segment size: " + grp.getName();
                items.add(new MultiParamItem(MulticonfigurationParameterType.SEGMENT_SIZE, str,
                        new DoubleValidator("segment size has to be between 0 and 1", (d) -> d >= 0 && d <= 1), null));
            }
        }
    }

    @Override
    public void registerElementCreated(Consumer<UserConfiguration> action) {
        creationListener.add(action);
    }

    public void setConfiguration(UserConfiguration config) {
        iterationCountProperty.setValue(config.getIterationCount());
        roundCountProperty.setValue(config.getRoundCount());
        mixedStrategyProperty.setValue(config.getMixedAllowed());
        maxAdaptsProperty.setValue(config.getMaxAdapts());

        int cnt = 0;
        if (repository.getGameRepository().containsEntityName(config.getGameName())) {
            gameNameProperty.setValue(config.getGameName());
            cnt++;
        }
        if (repository.getPopulationRepository().containsEntityName(config.getPopulationName())) {
            populationProperty.setValue(config.getPopulationName());
            cnt++;
        }
        if (repository.getGameRepository().containsEntityName(config.getGameName())) {
            gameNameProperty.setValue(config.getGameName());
            cnt++;
        }
        if (repository.getPopulationRepository().containsEntityName(config.getPopulationName())) {
            populationProperty.setValue(config.getPopulationName());
            cnt++;
        }
        if (repository.getPairBuilderRepository().containsEntityName(config.getPairBuilderName())) {
            pairBuilderProperty.setValue(config.getPairBuilderName());
            pairBuilderControl.setParameters(config.getPairBuilderParameters());
            cnt++;
        }
        if (repository.getSuccessQuantifiernRepository().containsEntityName(config.getSuccessQuantifierName())) {
            successQuantifierProperty.setValue(config.getSuccessQuantifierName());
            successQuantifierControl.setParameters(config.getSuccessQuantifierParameters());
            cnt++;
        }
        if (repository.getStrategyAdjusterRepository().containsEntityName(config.getStrategyAdjusterName())) {
            strategyAdjusterProperty.setValue(config.getStrategyAdjusterName());
            strategyAdjusterControl.setParameters(config.getStrategyAdjusterParameters());
            cnt++;
        }
        if (repository.getEquilibriumCriterionRepository().containsEntityName(config.getEquilibriumCriterionName())) {
            equilibriumCriterionProperty.setValue(config.getEquilibriumCriterionName());
            equilibriumCriterionControl.setParameters(config.getEquilibriumCriterionParameters());
            cnt++;
        }

        if (config.isMulticonfiguration()) {
            setMultiParam(config.getMulticonfigurationParameter());
            setMultiParamValues(config.getParameterValues());
        } else
            multiParamProperty.setValue(multiParamBox.getItems().get(0));

        if (cnt != VARIABLE_PARAM_CNT) {
            showWarningAlert();
        }
    }

    private void setDisableMultiParam(boolean enable, MultiParamItem newItem) {
        if (newItem.type == null) {
            startValue.setDisable(enable);
            endValue.setDisable(enable);
            stepSize.setDisable(enable);
        } else {
            switch (newItem.type) {
                case ITERATION_COUNT:
                case ROUND_COUNT:
                case MAX_ADAPTS:
                    newItem.parameterField.setDisable(enable);
                    break;
                case SA_PARAM:
                    strategyAdjusterControl.disableParamert(newItem.displayString.split(":")[0], enable);
                    break;
                case PB_PARAM:
                    pairBuilderControl.disableParamert(newItem.displayString.split(":")[0], enable);
                    break;
                case EC_PARAM:
                    equilibriumCriterionControl.disableParamert(newItem.displayString.split(":")[0], enable);
                    break;
                case SQ_PARAM:
                    successQuantifierControl.disableParamert(newItem.displayString.split(":")[0], enable);
                    break;
                default:
                    break;
            }
        }
    }

    private void setMultiParam(MulticonfigurationParameter param) {
        String paramName = param.getParameterName();
        switch (param.getType()) {
            case SQ_PARAM:
                setMultiParamName(repository.getSuccessQuantifiernRepository(), successQuantifierProperty.getValue(), paramName, param.getType());
                break;
            case EC_PARAM:
                setMultiParamName(repository.getEquilibriumCriterionRepository(), equilibriumCriterionProperty.getValue(), paramName, param.getType());
                break;
            case PB_PARAM:
                setMultiParamName(repository.getPairBuilderRepository(), pairBuilderProperty.getValue(), paramName, param.getType());
                break;
            case SA_PARAM:
                setMultiParamName(repository.getStrategyAdjusterRepository(), strategyAdjusterProperty.getValue(), paramName, param.getType());
                break;
            case MAX_ADAPTS:
            case ROUND_COUNT:
            case ITERATION_COUNT:
                multiParamProperty.setValue(multiParamBox.getItems()
                        .filtered((item) -> item.type != null && item.type.equals(param.getType())).get(0));
                break;
            case SEGMENT_SIZE:
                multiParamProperty.setValue(multiParamBox.getItems()
                        .filtered((item) -> item.displayString.endsWith(param.getGroupName())).get(0));
                break;
            case GROUP_SIZE:
                multiParamProperty.setValue(multiParamBox.getItems()
                        .filtered((item) -> item.displayString.endsWith(param.getGroupName())).get(0));
            default:
                // TODO CapitalDistribution, Segment size, Group Size
                break;
        }
    }

    private <T> void setMultiParamName(Repository<Plugin<T>> repo, String entityName,
                                       String parameterName, MulticonfigurationParameterType type) {
        Plugin<T> plugin = repo.getEntityByName(entityName);
        boolean hasParameter = plugin.getParameters().stream().anyMatch(p -> p.getName().equals(parameterName));
        if (hasParameter)
            multiParamProperty.setValue(multiParamBox.getItems().filtered((item) -> item.displayString.equals(parameterName + ":" + plugin.getName())).get(0));
        else multiParamProperty.setValue(null);
    }

    private void setMultiParamValues(List<Double> values) {
        double startValue = values.get(0);
        double endValue = values.get(values.size() - 1);
        double stepSize = (endValue - startValue) / (values.size() - 1);
        startValueProperty.setValue(startValue);
        endValueProperty.setValue(endValue);
        stepSizeProperty.setValue(stepSize);
    }

    private void showWarningAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(ALERT_TITLE);
        alert.setContentText(CONFIG_ERR_MSG);
        alert.showAndWait();
    }

    private void createConfig() {
        boolean isMulti = multiParamProperty.getValue().type != null;

        MulticonfigurationParameter param = null;

        if (isMulti) {
            double startValue = startValueProperty.getValue();
            double endValue = endValueProperty.getValue();
            double stepSize = stepSizeProperty.getValue();
            String[] parts = multiParamProperty.getValue().toString().split(":");
            MulticonfigurationParameterType multiParamType = multiParamProperty.getValue().type;

            switch (multiParamType) {
                case SQ_PARAM:
                case EC_PARAM:
                case PB_PARAM:
                case SA_PARAM:
                    param = new MulticonfigurationParameter(multiParamType, startValue, endValue, stepSize, parts[0]);
                    break;
                case MAX_ADAPTS:
                case ROUND_COUNT:
                case ITERATION_COUNT:
                    param = new MulticonfigurationParameter(multiParamType, (int) startValue, (int) endValue, (int) stepSize);
                    break;
                case SEGMENT_SIZE:
                    param = new MulticonfigurationParameter(startValue, endValue, stepSize, parts[1]);
                    break;
                case GROUP_SIZE:
                    param = new MulticonfigurationParameter((int)startValue, (int)endValue, (int)stepSize, parts[1]);
                    break;
                default:
                    // TODO CapitalDistribution,Group Size
                    param = null;
                    break;
            }
        }

        config = new UserConfiguration(gameNameProperty.getValue(), roundCountProperty.getValue(),
                iterationCountProperty.getValue(), mixedStrategyProperty.getValue(), populationProperty.getValue(),
                pairBuilderProperty.getValue(), pairBuilderControl.getParameters(), successQuantifierProperty.getValue(),
                successQuantifierControl.getParameters(), strategyAdjusterProperty.getValue(),
                strategyAdjusterControl.getParameters(), equilibriumCriterionProperty.getValue(),
                equilibriumCriterionControl.getParameters(), maxAdaptsProperty.getValue(), isMulti, param);

        for (Consumer<UserConfiguration> creationHandler : creationListener) {
            creationHandler.accept(config);
        }
    }

    private class GameTableEntry {
        private StringProperty firstColumnContent = new SimpleStringProperty();
        private StringProperty secondColumnContent = new SimpleStringProperty();
        private StringProperty thirdColumnContent = new SimpleStringProperty();
        private boolean isFirst;

        private GameTableEntry(Game game, boolean isFirst) {
            firstColumnContent.setValue(isFirst ? "SP 1 koop." : "SP 1 def.");
            this.isFirst = isFirst;
            update(game);
        }

        private void update(Game newGame) {
            secondColumnContent.setValue(getColumnContent(newGame, isFirst, true));
            thirdColumnContent.setValue(getColumnContent(newGame, isFirst, false));
        }

        private String getColumnContent(Game game, boolean p1Cooperates, boolean p2Cooperates) {
            Agent p1 = new Agent(0, null, 0);
            Agent p2 = new Agent(0, null, 0);
            GameResult res = game.play(p1, p2, p1Cooperates, p2Cooperates);
            return res.getPayoff(p1) + "/" + res.getPayoff(p2);
        }

    }

    private class MultiParamItem {
        private MulticonfigurationParameterType type;
        private String displayString;
        private Validator<String> validator;
        private TextField parameterField;

        /*private MultiParamItem(MulticonfigurationParameterType type, String displayString) {
            this(type, displayString, new DoubleValidator(""));
        }*/

        private MultiParamItem(MulticonfigurationParameterType type, String displayString,
                               Validator<String> validator, TextField field) {
            this.type = type;
            this.displayString = displayString;
            this.validator = validator;
            this.parameterField = field;
        }

        @Override
        public String toString() {
            return displayString;
        }
    }
}
