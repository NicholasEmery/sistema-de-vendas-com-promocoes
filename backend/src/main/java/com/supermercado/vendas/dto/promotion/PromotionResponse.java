package com.supermercado.vendas.dto.promotion;

import com.supermercado.vendas.model.ProductType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

public record PromotionResponse(
        String id,
        String name,
        String description,
        Set<ProductType> targetProductTypes,
        BigDecimal discountPercent,
        LocalDate startDate,
        LocalDate endDate,
        Set<String> customerCpfTargets
) {
}
