package com.restaurantsystem.salesmanagement.web.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Range;


public record SaleItemRequest(
        @NotBlank String menuItemId,
        @NotNull @Min(value = 1) Integer quantity,
        @Range(min = 0, max = 100) Integer discount
) {
}
