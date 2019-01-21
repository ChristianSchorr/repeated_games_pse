package loop.model.plugin;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.converter.NumberStringConverter;

/**
 * This class is an implementation of the PluginControl class that displays a text field for
 * each of the parameters. The input will be checked for correctness using the ParameterValidator.
 * 
 * @author Pierre Toussing
 *
 */
public class TextFieldPluginControl extends PluginControl{
	private List<Parameter> params;

	List<DoubleProperty> properties;
	
	/**
	 * Creates a new TextFieldPluginControl.
	 * @param params a list of the configurable Parameters
	 */
	TextFieldPluginControl(List<Parameter> params) {
		this.params = params;
		properties = new ArrayList<>();
		for(Parameter p : params) {
			Label label = new Label();
			TextField field = new TextField();
			label.setText(p.getName() + " :");
			DoubleProperty prop = new SimpleDoubleProperty();
			field.textProperty().bindBidirectional(prop, new NumberStringConverter());
			properties.add(prop);
			this.getChildren().addAll(label, field);
		}
	}
	
	/**
	 * Adds a Parameter, which is then configurable with this control element.
	 * @param param the Parameter that shall be added
	 */
	public void addParameter(Parameter param) {
		params.add(param);
		Label label = new Label();
		TextField field = new TextField();
		label.setText(param.getName() + " :");
		field.setId(param.getName());
		this.getChildren().addAll(label, field);
	}
	
	/**
	 * Adds a list of Parameters, which are then configurable with this control element
	 * @param params the Parameters that shall be added
	 */
	public void addParameters(List<Parameter> params) {
		for(Parameter p: params) {
			this.addParameter(p);
		}
	}
	
	@Override
	public List<Double> getParameters() {
		return properties.stream().map(DoubleProperty::getValue).collect(Collectors.toList());
	}

}
