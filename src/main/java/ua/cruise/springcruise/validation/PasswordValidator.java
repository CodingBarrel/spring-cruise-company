package ua.cruise.springcruise.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Class contains password validating annotation implementation
 * @author Vladyslav Kucher
 * @version 1.1
 * @see PasswordMatch
 */

public class PasswordValidator implements ConstraintValidator<LoginMatch, String> {
    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        return password != null && password.matches("^[a-zA-Z0-9!@#$%^&*()_=+]{6,36}$");
    }
}
