package com.invoicesystem.companyservice.service;

import lombok.Getter;

import java.util.Collections;
import java.util.List;

public record CompanyValidationResult(boolean isValid, List<String> errorMessages) {

    public CompanyValidationResult(boolean isValid, List<String> errorMessages) {
        this.isValid = isValid;
        this.errorMessages = Collections.unmodifiableList(errorMessages);
    }

    public boolean isNotValid() {
        return !isValid;
    }
}
