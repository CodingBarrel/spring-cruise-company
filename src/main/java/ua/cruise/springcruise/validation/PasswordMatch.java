package ua.cruise.springcruise.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = LoginValidator.class)
@Target( {ElementType.METHOD, ElementType.FIELD} )
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordMatch {
    String message() default "Wrong password";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}