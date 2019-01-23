package loop.controller.validation;

import javafx.scene.control.Control;
import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.Validator;

import java.util.function.Predicate;


/**
 * This class checks whether the text-property of a bound control is a valid double value
 *
 * @author Christian Schorr
 */
public class DoubleValidator implements Validator<String> {

    private String validationErrorMessage;
    private Predicate<Double> condition;

    /**
     * Creates a new validator which checks whether the input is a double
     *
     * @param message the error message which shall be shown in case of a validation error
     */
    public DoubleValidator(String message) {
        this.condition = (d) -> true;
        validationErrorMessage = message;
    }

    /**
     * Creates a new validator which checks whether the input is a double and
     * fulfills the given condition.
     *
     * @param message the error message which shall be shown in case of a validation error
     * @param condition the condition the value has to fulfill
     */
    public DoubleValidator(String message, Predicate<Double> condition) {
        this.condition = condition;
        validationErrorMessage = message;
    }

    @Override
    public ValidationResult apply(Control control, String s) {
        boolean validationError = isDouble(s) && condition.test(Double.valueOf(s));
        return ValidationResult.fromMessageIf(control, validationErrorMessage, Severity.ERROR, !validationError);
    }

    private final String regex = "[\\x00-\\x20]*[+-]?(((((\\p{Digit}+)(\\.)?((\\p{Digit}+)?)([eE][+-]?(\\p{Digit}+))?)|(\\.((\\p{Digit}+))([eE][+-]?(\\p{Digit}+))?)|(((0[xX](\\p{XDigit}+)(\\.)?)|(0[xX](\\p{XDigit}+)?(\\.)(\\p{XDigit}+)))[pP][+-]?(\\p{Digit}+)))[fFdD]?))[\\x00-\\x20]*";

    private boolean isDouble(String value) {
        return value != null ? (value.matches(regex)) : false;
    }
}
