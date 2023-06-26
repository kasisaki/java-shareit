package ru.practicum.shareit.validator;

import ru.practicum.shareit.customAnnotation.IsNotAfter;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class DateValidator implements ConstraintValidator<IsNotAfter, LocalDate> {
    private String validDate;

    @Override
    public void initialize(IsNotAfter constraintAnnotation) {
        validDate = constraintAnnotation.dateLimit();
    }

    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext constraintValidatorContext) {
        String[] splitDate = validDate.split("-");
        return date.isAfter(LocalDate.of(Integer.parseInt(splitDate[0]), Integer.parseInt(splitDate[1]),
                Integer.parseInt(splitDate[2])));
    }
}
