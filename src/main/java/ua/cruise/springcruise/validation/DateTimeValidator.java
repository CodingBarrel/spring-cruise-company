package ua.cruise.springcruise.validation;

import org.springframework.beans.BeanWrapperImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;


/**
 * Class contains date-time validating annotation implementation
 * @author Vladyslav Kucher
 * @version 1.1
 * @see DateTimeMatch
 */

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
