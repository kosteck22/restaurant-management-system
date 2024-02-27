package com.invoicesystem.invoicemanagement.web.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NipValidator implements ConstraintValidator<ValidNip, String> {
    @Override
    public boolean isValid(String nip, ConstraintValidatorContext context) {
        if (nip == null) {
            return false;
        }

        return nip.replace("-", "").matches("^\\d{10}$");
    }
}
