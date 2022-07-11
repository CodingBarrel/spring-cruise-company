package ua.cruise.springcruise.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<LoginMatch, String> {
    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        return password != null && password.matches("^[a-zA-Z0-9!@#$%^&*()_=+]{6,36}$");
    }
}
