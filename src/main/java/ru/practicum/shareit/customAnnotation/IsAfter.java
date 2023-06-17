package ru.practicum.shareit.customAnnotation;

import ru.practicum.shareit.validator.DateValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateValidator.class)
@Documented
public @interface IsAfter {
    String message() default "Wrong date";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String dateLimit();
}