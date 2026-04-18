package com.supermercado.vendas.dto.purchase;

import com.supermercado.vendas.model.ProductType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PurchaseResponse(
        String id,
        String customerId,
        String productName,
        BigDecimal price,
        LocalDateTime purchaseDate,
        ProductType productType
) {
}
