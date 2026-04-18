package com.supermercado.vendas.dto.purchase;

import com.supermercado.vendas.model.ProductType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PurchaseRequest(
        @NotBlank(message = "Cliente é obrigatório")
        String customerId,

        @NotBlank(message = "Nome do produto é obrigatório")
        String productName,

        @NotNull(message = "Preço é obrigatório")
        @DecimalMin(value = "0.01", message = "Preço deve ser maior que zero")
        BigDecimal price,

        @NotNull(message = "Data da compra é obrigatória")
        LocalDateTime purchaseDate,

        @NotNull(message = "Tipo de produto é obrigatório")
        ProductType productType
) {
}
