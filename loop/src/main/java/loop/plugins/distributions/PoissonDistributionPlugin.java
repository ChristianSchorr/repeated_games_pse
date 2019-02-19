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
import loop.model.simulationengine.distributions.PoissonDistribution;

import java.util.ArrayList;
import java.util.List;

public class PoissonDistributionPlugin extends Plugin<DiscreteDistribution> {

    private static final String NAME = "Poisson Distribution";
    private static final String DESCRIPTION = "This is a poisson distribution.";

    private List<Parameter> parameters = new ArrayList<Parameter>();

    public PoissonDistributionPlugin() {
        Parameter lambdaParameter = new Parameter(0.0, 500.0, "mean", "The mean of this distribution.");
        parameters.add(lambdaParameter);
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
            throw new IllegalArgumentException("Invalid parameters given for the creation of a 'poisson distribution' object");
        }
        return new PoissonDistribution(params.get(0));
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
            props.get(0).setValue(9d);
            props.get(0).addListener((c, o, n) -> {
                double lambda = props.get(0).getValue();
                updateChart(lambda);
            });

            double lambda = props.get(0).getValue();
            updateChart(lambda);
        }

        private void updateChart(double lambda) {
            if (lambda == 0) lambda = 0.00001;
            org.apache.commons.math3.distribution.PoissonDistribution dist =
                    new org.apache.commons.math3.distribution.PoissonDistribution(lambda);

            XYChart.Series series = new XYChart.Series();
            int i = 0;
            while (dist.cumulativeProbability(i) < 0.01) i++;
            while (dist.cumulativeProbability(i) < 0.99) {
                series.getData().add(new XYChart.Data(String.format("%d", i), dist.probability(i)));
                i++;
            }

            capitalDiagram.getData().clear();
            capitalDiagram.getData().add(series);
        }
    }
}
