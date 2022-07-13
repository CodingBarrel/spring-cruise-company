package ua.cruise.springcruise.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Class contains name validating annotation implementation
 * @author Vladyslav Kucher
 * @version 1.1
 * @see NameMatch
 */

public class NameValidator implements ConstraintValidator<NameMatch, String> {
    @Override
    public boolean isValid(String name, ConstraintValidatorContext context) {
        return name != null && name.matches("^(\\S).{1,120}(\\S)$");
    }
}
