package com.restaurantsystem.articlemanagement.web.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Target({ElementType.FIELD, ElementType.PARAMETER,})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MonetaryValueValidator.class)
public @interface MonetaryValue {
    String message() default "Invalid monetary value";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int precision() default 2;
}
