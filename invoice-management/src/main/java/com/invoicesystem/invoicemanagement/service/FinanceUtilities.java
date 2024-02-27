package com.invoicesystem.invoicemanagement.service;

import com.invoicesystem.invoicemanagement.web.dto.ProductSearchDto;
import jakarta.validation.ValidationException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

public class FinanceUtilities {
    public static void validateDate(LocalDate startDate, LocalDate endDate) {
        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            throw new ValidationException("The start date must be earlier than the end date");
        }
    }

    public static <T> BigDecimal sum(List<T> items, Function<T, BigDecimal> valueExtractor) {
        return items.stream().map(valueExtractor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public static <T> Map<Integer, BigDecimal> groupAndSum(List<T> items,
                                                           Function<T, Integer> groupFunction,
                                                           Function<T, BigDecimal> valueExtractor) {
        return items.stream()
                .collect(Collectors.groupingBy(
                        groupFunction,
                        Collectors.reducing(BigDecimal.ZERO, valueExtractor, BigDecimal::add)
                ));
    }
}
