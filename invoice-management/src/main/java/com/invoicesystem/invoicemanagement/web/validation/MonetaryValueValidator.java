package com.invoicesystem.invoicemanagement.web.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.math.BigDecimal;

public class MonetaryValueValidator implements ConstraintValidator<MonetaryValue, BigDecimal> {

    private int precision;

    @Override
    public void initialize(MonetaryValue constraintAnnotation) {
        this.precision = constraintAnnotation.precision();
    }

    @Override
    public boolean isValid(BigDecimal value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        return value.scale() <= this.precision;
    }
}
