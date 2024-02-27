package com.invoiceSystem.invoiceExtractor.exception;


import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;
import java.util.List;

@ControllerAdvice
public class DefaultExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultExceptionHandler.class);

    @ExceptionHandler(RequestValidationException.class)
    public ResponseEntity<ApiError> handleException(RequestValidationException e, HttpServletRequest request) {
        LOGGER.error(e.getMessage(), e);

        ApiError apiError = new ApiError(
                request.getRequestURI(),
                List.of(e.getMessage()),
                HttpStatus.BAD_REQUEST.value(),
                new Date()
        );
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvoiceValidationException.class)
    public ResponseEntity<InvoiceApiError> handleException(InvoiceValidationException e, HttpServletRequest request) {
        LOGGER.error(e.getMessage(), e);

        ApiError apiError = new ApiError(
                request.getRequestURI(),
                List.of(e.getMessage()),
                HttpStatus.BAD_REQUEST.value(),
                new Date()
        );

        InvoiceApiError invoiceApiError = new InvoiceApiError(apiError, e.getInvoice());
        return new ResponseEntity<>(invoiceApiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleException(Exception e, HttpServletRequest request) {
        LOGGER.error(e.getMessage(), e);

        ApiError apiError = new ApiError(
                request.getRequestURI(),
                List.of(e.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                new Date()
        );
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
