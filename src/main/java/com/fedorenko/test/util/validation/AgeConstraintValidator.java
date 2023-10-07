package com.fedorenko.test.util.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;
import java.time.Period;

public class AgeConstraintValidator implements ConstraintValidator<AgeConstraint, LocalDate> {
    @Value("${user_minimal_age}")
    int minimalAge;

    @Override
    public void initialize(AgeConstraint ageConstraint) {
    }

    @Override
    public boolean isValid(LocalDate birthdate, ConstraintValidatorContext context) {
        if (birthdate == null) {
            return false;
        }

        LocalDate currentDate = LocalDate.now();
        Period age = Period.between(birthdate, currentDate);

        return age.getYears() >= minimalAge;
    }
}
