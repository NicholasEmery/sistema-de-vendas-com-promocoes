package com.supermercado.vendas.dto.product;

import com.supermercado.vendas.model.ProductType;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record BulkDiscountRequest(
        @NotNull(message = "Tipo do produto é obrigatório")
        ProductType type,

        @NotNull(message = "Percentual de desconto é obrigatório")
        @DecimalMin(value = "0.01", message = "Desconto deve ser maior que 0")
        @DecimalMax(value = "100.00", message = "Desconto deve ser no máximo 100")
        BigDecimal discountPercent
) {
}
