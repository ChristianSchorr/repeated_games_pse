package loop.controller.validation;

import javafx.scene.control.Control;
import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.Validator;

import java.util.Scanner;
import java.util.function.Predicate;


/**
 * This class checks whether the text-property of a bound control is a valid double value
 *
 * @author Christian Schorr
 */
public class IntegerValidator implements Validator<String> {

    private String validationErrorMessage;
    private Predicate<Integer> condition;

    /**
     * Creates a new validator which checks whether the input is a double
     *
     * @param message the error message which shall be shown in case of a validation error
     */
    public IntegerValidator(String message) {
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
    public IntegerValidator(String message, Predicate<Integer> condition) {
        this.condition = condition;
        validationErrorMessage = message;
    }

    @Override
    public ValidationResult apply(Control control, String s) {
        boolean validationError = isInteger(s) && condition.test(Integer.valueOf(s));
        return ValidationResult.fromMessageIf(control, validationErrorMessage, Severity.ERROR, !validationError);
    }

    private boolean isInteger(String value) {
        Scanner sc = new Scanner(value.trim());
        if(!sc.hasNextInt(10)) return false;
        // we know it starts with a valid int, now make sure
        // there's nothing left!
        sc.nextInt(10);
        return !sc.hasNext();
    }
}