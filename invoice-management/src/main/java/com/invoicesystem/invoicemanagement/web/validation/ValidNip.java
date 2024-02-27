package com.invoicesystem.invoicemanagement.web.validation;

import com.invoicesystem.invoicemanagement.web.validation.NipValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NipValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidNip {
    String message() default "Invalid NIP format";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
