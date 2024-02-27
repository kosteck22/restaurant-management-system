package com.invoicesystem.invoicemanagement.web.dto;

import java.util.List;

public record ProductSearchSummaryDto (
        List<ProductSearchDto> products,
        TotalSumsDto summary
) {
}
