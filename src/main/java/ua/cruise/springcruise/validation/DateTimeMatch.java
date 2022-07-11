package ua.cruise.springcruise.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DateTimeValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DateTimeMatch {
    String message() default "Wrong datetime";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String firstDateTime();

    String secondDateTime();

    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @interface List {
        DateTimeMatch[] value();
    }
}
