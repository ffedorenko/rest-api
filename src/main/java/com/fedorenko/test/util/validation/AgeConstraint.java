package com.fedorenko.test.util.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.springframework.beans.factory.annotation.Value;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AgeConstraintValidator.class)
public @interface AgeConstraint {
    @Value("${user_minimal_age}")
    int minimalAge = 0;

    String message() default "Age might be at least " + minimalAge;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
