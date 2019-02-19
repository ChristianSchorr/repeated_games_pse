package loop.plugins.distributions;

import javafx.beans.property.DoubleProperty;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.HBox;
import loop.model.plugin.*;
import loop.model.simulationengine.distributions.DiscreteDistribution;
import loop.model.simulationengine.distributions.DiscreteUniformDistribution;

import java.util.ArrayList;
import java.util.List;

public class DiscreteUniformDistributionPlugin extends Plugin<DiscreteDistribution> {

    private static final String NAME = "Discrete Unfiorm Distribution";
    private static final String DESCRIPTION = "This is a discrete uniform distribution with specifiable lower and upper bound.";

    private List<Parameter> parameters = new ArrayList<Parameter>();

    public DiscreteUniformDistributionPlugin() {
        Parameter minParameter = new Parameter(0.0, 20000.0, 1.0, "lower bound", "The lower bound of this distribution.");
        Parameter maxParameter = new Parameter(0.0, 20000.0, 1.0, "upper bound", "The upper bound of this distribution.");
        parameters.add(minParameter);
        parameters.add(maxParameter);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getDescription() {
        return DESCRIPTION;
    }

    @Override
    public List<Parameter> getParameters() {
        return parameters;
    }

    @Override
    public PluginRenderer getRenderer() {
        return () -> new CustomControl(parameters);
    }

    @Override
    public DiscreteDistribution getNewInstance(List<Double> params) {
        if (!ParameterValidator.areValuesValid(params, parameters)) {
            throw new IllegalArgumentException("Invalid parameters given for the creation of a 'discrete uniform distribution' object");
        }
        return new DiscreteUniformDistribution(params.get(0).intValue(), params.get(1).intValue());
    }

    private class CustomControl extends TextFieldPluginControl {

        /**
         * Creates a new TextFieldPluginControl.
         *
         * @param params a list of the configurable Parameters
         */
        public CustomControl(List<Parameter> params) {
            super(params);
            setupChart();
            List<Node> children = new ArrayList<>(getChildren());
            getChildren().clear();

            HBox box = new HBox();
            box.getChildren().addAll(children);
            HBox outer = new HBox();
            outer.setSpacing(50);
            outer.setAlignment(Pos.CENTER_LEFT);
            outer.getChildren().addAll(box, capitalDiagram);
            this.getChildren().add(outer);
        }

        private BarChart<String, Number> capitalDiagram;

        private void setupChart() {
            NumberAxis yAxis = new NumberAxis();
            yAxis.setLabel("probability");
            yAxis.setAnimated(false);
            CategoryAxis xAxis = new CategoryAxis();
            xAxis.setAnimated(false);

            capitalDiagram = new BarChart<>(xAxis, yAxis);
            capitalDiagram.setAnimated(false);
            capitalDiagram.setPrefHeight(200);
            capitalDiagram.setLegendVisible(false);

            List<DoubleProperty> props = getBoundProperties();
            props.get(0).set(0);
            props.get(1).set(10);
            props.get(0).addListener((c, o, n) -> {
                int min = props.get(0).getValue().intValue();
                int max = props.get(1).getValue().intValue();
                updateChart(min, max);
            });
            props.get(1).addListener((c, o, n) -> {
                int min = props.get(0).getValue().intValue();
                int max = props.get(1).getValue().intValue();
                updateChart(min, max);
            });

            int min = (int)props.get(0).getValue().intValue();
            int max = props.get(1).getValue().intValue();

            updateChart(min, max);
        }

        private void updateChart(int min, int max) {
            Double val = 1d / (max - min + 1);

            XYChart.Series series = new XYChart.Series();
            for (int i = min; i <= max; i++) {
                series.getData().add(new XYChart.Data(String.format("%d", i), val));
            }

            capitalDiagram.getData().clear();
            capitalDiagram.getData().add(series);
        }
    }
}
