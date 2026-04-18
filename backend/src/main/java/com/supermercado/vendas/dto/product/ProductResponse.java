package com.supermercado.vendas.dto.product;

import com.supermercado.vendas.model.ProductType;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ProductResponse(
        String id,
        String name,
        BigDecimal currentPrice,
        BigDecimal promotionalPrice,
        ProductType type,
        String description,
        LocalDate expirationDate
) {
}
