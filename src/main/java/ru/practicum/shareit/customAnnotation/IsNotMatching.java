package ru.practicum.shareit.customAnnotation;

import ru.practicum.shareit.validator.LoginValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LoginValidator.class)
@Documented
public @interface IsNotMatching {
    String message() default "Found forbidden symbol";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String matchValue();
}