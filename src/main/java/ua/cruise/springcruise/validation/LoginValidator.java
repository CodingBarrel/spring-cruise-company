package ua.cruise.springcruise.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class LoginValidator implements ConstraintValidator<LoginMatch, String> {
    @Override
    public boolean isValid(String name, ConstraintValidatorContext context) {
        return name != null && name.matches("^[a-zA-Z0-9_]{3,30}$");
    }
}
