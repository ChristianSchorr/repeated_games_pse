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
import loop.model.simulationengine.distributions.BinomialDistribution;
import loop.model.simulationengine.distributions.DiscreteDistribution;

import java.util.ArrayList;
import java.util.List;

public class BinomialDistributionPlugin extends Plugin<DiscreteDistribution> {

    private static final String NAME = "Binomial Distribution";
    private static final String DESCRIPTION = "This is a binomial distribution that is shifted such that instead of taking values between"
            + " 0 and n, it takes values between specifiable bounds a and b, where a and b must be non-negative integers.";

    private List<Parameter> parameters = new ArrayList<Parameter>();

    public BinomialDistributionPlugin() {
        Parameter minParameter = new Parameter(0.0, 500.0, 1.0, "lower bound", "The lower bound of the distribution.");
        Parameter maxParameter = new Parameter(0.0, 500.0, 1.0, "upper bound", "The upper bound of the distribution.");
        Parameter probParameter = new Parameter(0.0, 1.0, "probability", "The probability of success, characterizing the bernoulli chain"
                + " modelled by this distribution.");
        parameters.add(minParameter);
        parameters.add(maxParameter);
        parameters.add(probParameter);
    }

    @Override
    public PluginRenderer getRenderer() {
        return () -> new CustomControl(parameters);
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
    public DiscreteDistribution getNewInstance(List<Double> params) {
        if (!ParameterValidator.areValuesValid(params, parameters)) {
            throw new IllegalArgumentException("Invalid parameters given for the creation of a 'binomial distribution' object");
        }
        return new BinomialDistribution(params.get(0).intValue(), params.get(1).intValue(), params.get(2));
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
            props.get(2).set(0.5);
            props.get(0).addListener((c, o, n) -> {
                int min = props.get(0).getValue().intValue();
                int max = props.get(1).getValue().intValue();
                double prop = props.get(2).getValue();
                updateChart(min, max, prop);
            });
            props.get(1).addListener((c, o, n) -> {
                int min = props.get(0).getValue().intValue();
                int max = props.get(1).getValue().intValue();
                double prop = props.get(2).getValue();
                updateChart(min, max, prop);
            });
            props.get(2).addListener((c, o, n) -> {
                int min = props.get(0).getValue().intValue();
                int max = props.get(1).getValue().intValue();
                double prop = props.get(2).getValue();
                updateChart(min, max, prop);
            });


            int min = (int)props.get(0).getValue().intValue();
            int max = props.get(1).getValue().intValue();
            double prop = props.get(2).getValue();

            updateChart(min, max, prop);
        }

        private void updateChart(int min, int max, double prop) {

            org.apache.commons.math3.distribution.BinomialDistribution dist =
                    new org.apache.commons.math3.distribution.BinomialDistribution(max - min, prop);

            XYChart.Series series = new XYChart.Series();
            for (int i = min; i <= max; i++) {
                series.getData().add(new XYChart.Data(String.format("%d", i), dist.probability(i - min)));
            }

            capitalDiagram.getData().clear();
            capitalDiagram.getData().add(series);
        }
    }
}
