package ua.cruise.springcruise.validation;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NameValidator.class)
@Target( {ElementType.METHOD, ElementType.FIELD} )
@Retention(RetentionPolicy.RUNTIME)
public @interface NameMatch {
    String message() default "Wrong name";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}