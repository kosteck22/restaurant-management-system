package com.restaurantsystem.salesmanagement.web.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Range;


public record SoldItemRequest(
        @NotBlank String articleId,
        @NotNull @Min(value = 1) int quantity,
        @Range(min = 0, max = 100) Integer itemDiscount
) {
}
