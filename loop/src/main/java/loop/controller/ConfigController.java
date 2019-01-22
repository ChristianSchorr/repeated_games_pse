package loop.controller;

import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.converter.NumberStringConverter;
import loop.model.MulticonfigurationParameter;
import loop.model.MulticonfigurationParameterType;
import loop.model.UserConfiguration;
import loop.model.plugin.Parameter;
import loop.model.plugin.Plugin;
import loop.model.plugin.PluginControl;
import loop.model.repository.CentralRepository;
import loop.model.repository.FileIO;
import loop.model.repository.Repository;
import loop.model.simulationengine.EquilibriumCriterion;
import loop.model.simulationengine.PairBuilder;
import loop.model.simulationengine.StrategyAdjuster;
import loop.model.simulationengine.SuccessQuantifier;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;


public class ConfigController implements CreationController<UserConfiguration> {

    private static final int VARIABLE_PARAM_CNT = 6;
    private static final String CONFIG_ERR_MSG = "";
    private static final String ALERT_TITLE = "";

    @FXML
    private ChoiceBox gameBox;

    @FXML
    private Slider roundSlider;

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
    private TextField maxAdapts;

    // MultiConfig
    @FXML
    private ChoiceBox multiParamBox;

    @FXML
    private TextField startValue;

    @FXML
    private TextField endValue;

    @FXML
    private TextField stepSize;

    private StringProperty gameNameProperty = new SimpleStringProperty();
    private IntegerProperty iterationCountProperty = new SimpleIntegerProperty();
    private IntegerProperty roundCountProperty = new SimpleIntegerProperty();
    private BooleanProperty mixedStrategyProperty = new SimpleBooleanProperty();
    private StringProperty populationProperty = new SimpleStringProperty();

    private StringProperty pairBuilderProperty = new SimpleStringProperty();
    private StringProperty successQuantifierProperty = new SimpleStringProperty();
    private StringProperty strategyAdjusterProperty = new SimpleStringProperty();
    private StringProperty equilibriumCriterionProperty = new SimpleStringProperty();

    private IntegerProperty maxAdaptsProperty = new SimpleIntegerProperty();

    private StringProperty multiParamProperty = new SimpleStringProperty();
    private DoubleProperty startValueProperty = new SimpleDoubleProperty();
    private DoubleProperty endValueProperty = new SimpleDoubleProperty();
    private DoubleProperty stepSizeProperty = new SimpleDoubleProperty();

    private PluginControl pairBuilderControl;
    private PluginControl successQuantifierControl;
    private PluginControl strategyAdjusterControl;
    private PluginControl equilibriumCriterionControl;

    private MulticonfigurationParameterType multiParamType;

    private CentralRepository repository;
    private UserConfiguration config;

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
        // inititalize gameNames
        List<String> gameNames = repository.getGameRepository().getAllEntityNames();
        ObservableList<String> observableGameNames = FXCollections.observableArrayList(gameNames);
        gameNameProperty.setValue(config.getGameName());
        gameBox.setItems(observableGameNames);
        gameBox.valueProperty().bindBidirectional(gameNameProperty);

        // initialize rounds
        roundCountProperty.setValue(config.getRoundCount());
        roundSlider.valueProperty().bindBidirectional(roundCountProperty);
        roundField.textProperty().bindBidirectional(roundCountProperty, new NumberStringConverter());

        // initialize iterationCount
        iterationCountProperty.setValue(config.getIterationCount());
        iterationField.textProperty().bindBidirectional(iterationCountProperty, new NumberStringConverter());

        // initialize mixedStrategies
        mixedStrategyProperty.setValue(config.getMixedAllowed());
        mixedStrategyCheckBox.selectedProperty().bindBidirectional(mixedStrategyProperty);

        // initialize populations
        List<String> populationNames = repository.getPopulationRepository().getAllEntityNames();
        ObservableList<String> observablePopulationNames = FXCollections.observableArrayList(populationNames);
        populationProperty.setValue(config.getPopulationName());
        populationBox.setItems(observablePopulationNames);
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
        maxAdapts.textProperty().bindBidirectional(maxAdaptsProperty, new NumberStringConverter());

        // initialize multi Param
        List<String> multiConfigParamNames = Arrays.stream(MulticonfigurationParameterType.values())
                .map(MulticonfigurationParameterType::getDescriptionFormat)
                .collect(Collectors.toList());
        ObservableList<String> observableMultiConfigParamNames = FXCollections.observableArrayList(multiConfigParamNames);
        multiParamBox.setItems(observableMultiConfigParamNames);
        multiParamBox.valueProperty().bindBidirectional(multiParamProperty);

        startValue.textProperty().bindBidirectional(startValueProperty, new NumberStringConverter());
        endValue.textProperty().bindBidirectional(endValueProperty, new NumberStringConverter());
        stepSize.textProperty().bindBidirectional(stepSizeProperty, new NumberStringConverter());
    }

    @FXML
    private void applyConfig(ActionEvent actionEvent) {
        createConfig();
    }

    @FXML
    private void resetConfig(ActionEvent actionEvent) {
        setConfiguration(UserConfiguration.getDefaultConfiguration());
    }

    @FXML
    private void loadConfig(ActionEvent actionEvent) {
        Window stage = ((Node)actionEvent.getTarget()).getScene().getWindow();
        fileChooser.setInitialDirectory(FileIO.USER_CONFIG_DIR);
        File file = fileChooser.showOpenDialog(stage);
        UserConfiguration config = null;
        if (file != null){
            try {
                config = FileIO.loadEntity(file);
            } catch (IOException e) {
                // TODO Show Error Dialog
                config = this.config;
            }
        }
        setConfiguration(config);
    }

    @FXML
    private void saveConfig(ActionEvent actionEvent) {
        Window stage = ((Node)actionEvent.getTarget()).getScene().getWindow();
        fileChooser.setInitialDirectory(FileIO.USER_CONFIG_DIR);
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            createConfig();
            try {
                FileIO.saveEntity(file, config);
            } catch (IOException e) {
                // TODO Show Error Dialog
            }
        }
    }

    private void pairBuilderChanged(String oldValue, String newValue) {
        String pairBuilderName = pairBuilderProperty.getValue();
        Repository<Plugin<PairBuilder>> repo = repository.getPairBuilderRepository();
        Plugin<PairBuilder> pairBuilderPlugin = repo.getEntityByName(pairBuilderName);

        pairBuilderControl = pairBuilderPlugin.getRenderer().renderPlugin();
        pairBuilderContainer.getChildren().clear();
        pairBuilderContainer.getChildren().add(pairBuilderControl);

        Plugin<PairBuilder> oldPlugin = repo.getEntityByName(oldValue);
        updateMultiParamBox(oldPlugin, pairBuilderPlugin);
    }

    private void successQuantifierChanged(String oldValue, String newValue) {
        String successQuantifierName = successQuantifierProperty.getValue();
        Repository<Plugin<SuccessQuantifier>> repo = repository.getSuccessQuantifiernRepository();
        Plugin<SuccessQuantifier> successQuantifierPlugin = repo.getEntityByName(successQuantifierName);

        successQuantifierControl = successQuantifierPlugin.getRenderer().renderPlugin();
        successQuantifierContainer.getChildren().clear();
        successQuantifierContainer.getChildren().add(successQuantifierControl);

        Plugin<SuccessQuantifier> oldPlugin = repo.getEntityByName(oldValue);
        updateMultiParamBox(oldPlugin, successQuantifierPlugin);
    }

    private void strategyAdjusterChanged(String oldValue, String newValue) {
        String strategyAdjusterName = strategyAdjusterProperty.getValue();
        Repository<Plugin<StrategyAdjuster>> repo = repository.getStrategyAdjusterRepository();
        Plugin<StrategyAdjuster> strategyAdjusterPlugin = repo.getEntityByName(strategyAdjusterName);

        strategyAdjusterControl = strategyAdjusterPlugin.getRenderer().renderPlugin();
        strategyAdjusterContainer.getChildren().clear();
        strategyAdjusterContainer.getChildren().add(strategyAdjusterControl);


        Plugin<StrategyAdjuster> oldPlugin = repo.getEntityByName(oldValue);
        updateMultiParamBox(oldPlugin, strategyAdjusterPlugin);
    }

    private void equilibriumCriterionChanged(String oldValue, String newValue) {
        String equilibriumCriterionName = equilibriumCriterionProperty.getValue();
        Repository<Plugin<EquilibriumCriterion>> repo = repository.getEquilibriumCriterionRepository();
        Plugin<EquilibriumCriterion> equilibriumCriterionPlugin = repo.getEntityByName(equilibriumCriterionName);

        equilibriumCriterionControl = equilibriumCriterionPlugin.getRenderer().renderPlugin();
        equilibriumCriterionContainer.getChildren().clear();
        equilibriumCriterionContainer.getChildren().add(equilibriumCriterionControl);

        Plugin<EquilibriumCriterion> oldPlugin = repo.getEntityByName(oldValue);
        updateMultiParamBox(oldPlugin, equilibriumCriterionPlugin);
    }

    private <T> void updateMultiParamBox(Plugin<T> oldPlugin, Plugin<T> newPlugin) {
        ObservableList<String> items = multiParamBox.getItems();
        if (oldPlugin != null)
            items.removeIf((str) -> str.substring(str.indexOf(':') + 1).equals(oldPlugin.getName()));

        for (Parameter param: newPlugin.getParameters()) {
            items.add(param.getName() + ":" + newPlugin.getName());
        }
    }

    private void updateMultiParamType() {
        String paramName = multiParamProperty.getValue();
        if (paramName.contains(":")) {
            String pluginName = paramName.split(":")[1];
            if (repository.getPairBuilderRepository().containsEntityName(pluginName)) {
                multiParamType = MulticonfigurationParameterType.PB_PARAM;
            } else if (repository.getSuccessQuantifiernRepository().containsEntityName(pluginName)) {
                multiParamType = MulticonfigurationParameterType.SQ_PARAM;
            } else if (repository.getStrategyAdjusterRepository().containsEntityName(pluginName)) {
                multiParamType = MulticonfigurationParameterType.SA_PARAM;
            } else if (repository.getEquilibriumCriterionRepository().containsEntityName(pluginName)) {
                multiParamType = MulticonfigurationParameterType.EC_PARAM;
            }

        } else {
            multiParamType = MulticonfigurationParameterType.valueOf(paramName);
        }
    }

    @Override
    public void registerElementCreated(Consumer<UserConfiguration> action) {
        creationListener.add(action);
    }

    public void setConfiguration(UserConfiguration config) {
        int cnt = 0;

        if (repository.getGameRepository().containsEntityName(config.getGameName())) {
            gameNameProperty.setValue(config.getGameName());
            cnt++;
        }
        roundCountProperty.setValue(config.getRoundCount());
        mixedStrategyProperty.setValue(config.getMixedAllowed());
        if (repository.getPopulationRepository().containsEntityName(config.getPopulationName())) {
            populationProperty.setValue(config.getPopulationName());
            cnt++;
        }
        if (repository.getPairBuilderRepository().containsEntityName(config.getPairBuilderName())) {
            pairBuilderProperty.setValue(config.getPairBuilderName());
            cnt++;
        }
        if (repository.getSuccessQuantifiernRepository().containsEntityName(config.getSuccessQuantifierName())) {
            successQuantifierProperty.setValue(config.getSuccessQuantifierName());
            cnt++;
        }
        if (repository.getStrategyAdjusterRepository().containsEntityName(config.getStrategyAdjusterName())) {
            strategyAdjusterProperty.setValue(config.getStrategyAdjusterName());
            cnt++;
        }
        if (repository.getEquilibriumCriterionRepository().containsEntityName(config.getEquilibriumCriterionName())) {
            equilibriumCriterionProperty.setValue(config.getEquilibriumCriterionName());
            cnt++;
        }

        if(config.isMulticonfiguration()) {
            setMultiParam(config.getMulticonfigurationParameter());
            setMultiParamValues(config.getParameterValues());
        } else {
            multiParamProperty.setValue(null);
        }

        if (cnt != VARIABLE_PARAM_CNT) {
            showWarningAlert();
        }
    }

    private void setMultiParam (MulticonfigurationParameter param) {
        String paramName = param.getParameterName();
        switch (param.getType()) {
            case SQ_PARAM:
                setMultiParamName(repository.getSuccessQuantifiernRepository(), successQuantifierProperty.getValue(), paramName);
                break;
            case EC_PARAM:
                setMultiParamName(repository.getEquilibriumCriterionRepository(), equilibriumCriterionProperty.getValue(), paramName);
                break;
            case PB_PARAM:
                setMultiParamName(repository.getPairBuilderRepository(), pairBuilderProperty.getValue(), paramName);
                break;
            case SA_PARAM:
                setMultiParamName(repository.getStrategyAdjusterRepository(), strategyAdjusterProperty.getValue(), paramName);
                break;
            case MAX_ADAPTS:
            case ROUND_COUNT:
            case ITERATION_COUNT:
                multiParamProperty.setValue(param.getType().getDescriptionFormat());
                break;
            default:
                // TODO CapitalDistribution, Segment size, Group Size
                multiParamProperty.setValue("");
                break;
        }
    }

    private <T> void setMultiParamName(Repository<Plugin<T>> repo, String entityName, String parameterName) {
        Plugin<T> plugin = repo.getEntityByName(entityName);
        boolean hasParameter = plugin.getParameters().stream().anyMatch(p -> p.getName().equals(parameterName));
        if (hasParameter) multiParamProperty.setValue(parameterName + ":" + plugin.getName());
        else multiParamProperty.setValue(null);
    }

    private void setMultiParamValues(List<Double> values) {
        double startValue = values.get(0);
        double endValue = values.get(values.size() - 1);
        double stepSize = (endValue - startValue) / values.size();
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
        boolean isMulti = multiParamProperty.getValue() != null;

        MulticonfigurationParameter param = null;

        if (isMulti) {
            double startValue = startValueProperty.getValue();
            double endValue = endValueProperty.getValue();
            double stepSize = stepSizeProperty.getValue();
            String paramName = multiParamProperty.getValue().split(":")[0];

               switch (multiParamType) {
                case SQ_PARAM:
                case EC_PARAM:
                case PB_PARAM:
                case SA_PARAM:
                    param = new MulticonfigurationParameter(multiParamType, startValue, endValue, stepSize, paramName);
                    break;
                case MAX_ADAPTS:
                case ROUND_COUNT:
                case ITERATION_COUNT:
                    param = new MulticonfigurationParameter(multiParamType, (int) startValue, (int) endValue, (int) stepSize);
                    break;
                default:
                    // TODO CapitalDistribution, Segment size, Group Size
                    param = null;
                    break;
            }
        }

        System.out.println(strategyAdjusterControl.getParameters());

        config = new UserConfiguration(gameNameProperty.getValue(), roundCountProperty.getValue(),
                iterationCountProperty.getValue(), mixedStrategyProperty.getValue(), populationProperty.getValue(),
                pairBuilderProperty.getValue(), pairBuilderControl.getParameters(), successQuantifierProperty.getValue(),
                successQuantifierControl.getParameters(), strategyAdjusterProperty.getValue(),
                strategyAdjusterControl.getParameters(), equilibriumCriterionProperty.getValue(),
                equilibriumCriterionControl.getParameters(), maxAdaptsProperty.getValue(), isMulti, param);

        for (Consumer<UserConfiguration> creationHandler: creationListener) {
            creationHandler.accept(config);
        }
    }
}
