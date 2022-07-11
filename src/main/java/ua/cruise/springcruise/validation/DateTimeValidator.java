package ua.cruise.springcruise.validation;

import org.springframework.beans.BeanWrapperImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class DateTimeValidator implements ConstraintValidator<DateTimeMatch, Object> {

    private String field;
    private String fieldMatch;

    @Override
    public void initialize(DateTimeMatch constraintAnnotation) {
        this.field = constraintAnnotation.firstDateTime();
        this.fieldMatch = constraintAnnotation.secondDateTime();
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext constraintValidatorContext) {
        Object firstValue = new BeanWrapperImpl(object).getPropertyValue(field);
        Object secondValue = new BeanWrapperImpl(object).getPropertyValue(fieldMatch);
        try {
            LocalDateTime startDateTime = LocalDateTime.parse((CharSequence) firstValue);
            LocalDateTime endDateTime = LocalDateTime.parse((CharSequence) secondValue);
            return endDateTime.isAfter(startDateTime);
        } catch (NullPointerException | DateTimeParseException ignored) {
            return false;
        }
    }
}
