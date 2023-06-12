package ru.practicum.shareit.validator;

import ru.practicum.shareit.customAnnotation.IsNotMatching;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class LoginValidator implements ConstraintValidator<IsNotMatching, String> {
    private String forbiddenSymbol;

    @Override
    public void initialize(IsNotMatching constraintAnnotation) {
        forbiddenSymbol = constraintAnnotation.matchValue();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return !s.matches(forbiddenSymbol);
    }
}
