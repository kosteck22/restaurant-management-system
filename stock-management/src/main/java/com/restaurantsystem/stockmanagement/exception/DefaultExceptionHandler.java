package com.restaurantsystem.stockmanagement.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class DefaultExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultExceptionHandler.class);
    private final ObjectMapper objectMapper;


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleException(ResourceNotFoundException e, HttpServletRequest request) {
        LOGGER.error(e.getMessage(), e);

        ApiError apiError = new ApiError(
                request.getRequestURI(),
                List.of(e.getMessage()),
                HttpStatus.NOT_FOUND.value(),
                new Date()
        );
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationErrors(MethodArgumentNotValidException e, HttpServletRequest request) {
        LOGGER.error(e.getMessage(), e);

        List<String> fieldErrors = e.getBindingResult().getFieldErrors()
                .stream()
                .map(f -> f.getField() + ": " + f.getDefaultMessage())
                .toList();

        ApiError apiError = new ApiError(
                request.getRequestURI(),
                fieldErrors,
                HttpStatus.BAD_REQUEST.value(),
                new Date()
        );
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<Object> handleFeignException(FeignException e) {
        ApiError apiError;
        try {
            apiError = objectMapper.readValue(e.contentUTF8(), ApiError.class);
        } catch (Exception ex) {
            apiError = new ApiError("Unknown", List.of("An error occurred with the external service"), e.status(), new Date());
        }

        HttpStatus status = HttpStatus.resolve(e.status());
        if (status != null) {
            return new ResponseEntity<>("Feign client error: " + e.getMessage(), status);
        }

        return new ResponseEntity<>(apiError, HttpStatus.valueOf(apiError.statusCode()));
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

