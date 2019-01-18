package loop.model.plugin;

import java.util.LinkedList;
import java.util.List;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * This class is an implementation of the PluginControl class that displays a text field for
 * each of the parameters. The input will be checked for correctness using the ParameterValidator.
 * 
 * @author Pierre Toussing
 *
 */
public class TextFieldPluginControl extends PluginControl {
	private List<Parameter> params;
	
	/**
	 * Creates a new TextFieldPluginControl.
	 * @param params a list of the configurable Parameters
	 */
	TextFieldPluginControl(List<Parameter> params) {
		this.params = params;
		for(Parameter p : params) {
			Label label = new Label();
			TextField field = new TextField();
			label.setText(p.getName() + " :");
			field.setId(p.getName());
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
		List<Double> list = new LinkedList<Double>();
		Double d;
		int i = 0;
		
		for (Node n: this.getChildren()) {
			if (n instanceof TextField) {
				try {
					d = Double.parseDouble(((TextField)n).getText());
				}
				catch(NumberFormatException e) {
					//Exception
					return null;
				}
				if (ParameterValidator.isValueValid(d, params.get(i))) {
					list.add(d);
					i++;
				}
				else {
					//IllegalArgumentException
					return null;
				}
			}
		}
		return list;
	}

    @Override
    public void setParameters(List<Double> parameters) {
        int i = 0;
        for (Node n: this.getChildren()) {
            if (!(n instanceof TextField)) continue;
            ((TextField) n).setText(parameters.get(i++).toString());
        }
    }

}
