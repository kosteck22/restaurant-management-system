package com.invoiceSystem.invoiceExtractor.exception;

import java.util.Date;
import java.util.List;

public record ApiError(
        String path,
        List<String> errors,
        int statusCode,
        Date timestamp
) {
}